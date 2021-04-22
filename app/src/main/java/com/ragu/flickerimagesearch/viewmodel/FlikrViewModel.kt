package com.ragu.flickerimagesearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ragu.flickerimagesearch.repository.MainRepository
import com.ragu.flickerimagesearch.utils.Resource
import kotlinx.coroutines.Dispatchers

class FlikrViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun searchImage(searchText:String,pageSize:Int) = liveData(Dispatchers.IO) {
       // emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.searchPhoto(searchText,pageSize)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}