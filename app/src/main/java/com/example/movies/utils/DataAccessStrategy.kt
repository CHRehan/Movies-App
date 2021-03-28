package com.example.movies.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.movies.utils.Resource.Status.*
import kotlinx.coroutines.Dispatchers

fun <A> performGetOperation(networkCall: suspend () -> Resource<A>): LiveData<Resource<A>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == SUCCESS) {
            emit(Resource.success(responseStatus.data!!))
        } else if (responseStatus.status == ERROR) {
            emit(Resource.error(responseStatus.message!!))

        }
    }