package com.ragu.flickerimagesearch

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ragu.flickerimagesearch.model.PhotoResponse
import com.ragu.flickerimagesearch.network.RetrofitService
import com.ragu.flickerimagesearch.service.ApiHelper
import com.ragu.flickerimagesearch.ui.MainAdapter
import com.ragu.flickerimagesearch.utils.Status
import com.ragu.flickerimagesearch.viewmodel.FlikrViewModel
import com.ragu.flickerimagesearch.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var page: Int = 0
    private var imageList: MutableList<PhotoResponse>? = null
    private var pages: Int = 0
    private lateinit var viewModel: FlikrViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageList = ArrayList<PhotoResponse>()
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                imageList?.let {
                    it.clear()
                    retrieveList(ArrayList())
                }
                if (p0?.length!! <= 0) adapter?.apply { addUsers(ArrayList()) }

                setupObservers(p0!!.toString().trim(), 1)
            }
        })

        setupViewModel()
        setupUI()

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitService.apiService(this)))
        ).get(FlikrViewModel::class.java)
    }

    private fun setupUI() {
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as GridLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var previousTotal = 0
            var loading = true
            val visibleThreshold = 10
            var firstVisibleItem = 0
            var visibleItemCount = 0
            var totalItemCount = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = recyclerView?.layoutManager!!.itemCount
                    firstVisibleItem =
                        (recyclerView?.layoutManager as GridLayoutManager)!!.findFirstVisibleItemPosition()

                    if (loading) {
                        if (totalItemCount >= previousTotal) {
                            loading = false
                            previousTotal = totalItemCount
                        }
                    }

                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        loading = true
                        var temppage = page
                        temppage += 1
                        if (temppage <= pages)
                            setupObservers(etSearch?.text!!.toString().trim(), temppage)

                    }
                }
            }
        })

    }


    private fun setupObservers(searchText: String, value: Int) {

        viewModel.searchImage(searchText, value).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        resource.data?.let { users ->
                            Log.e("error", users.toString())
                            if (users?.stat?.equals("ok")) {
                                this.page = users?.photos?.page;
                                this.pages = users?.photos?.pages;
                                retrieveList(users?.photos?.photo)
                            } else {
                                retrieveList(ArrayList())
                            }
                        }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        Log.e("error", it.message.toString())
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: List<PhotoResponse>) {
        imageList?.addAll(users)
        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}