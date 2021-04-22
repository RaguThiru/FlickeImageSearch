package com.ragu.flickerimagesearch.model

data class Photos(val page:Int,val pages:Int,val perpage:Int,val total:Int,val photo:List<PhotoResponse>)