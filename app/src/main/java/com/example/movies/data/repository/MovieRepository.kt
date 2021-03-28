package com.example.movies.data.repository

import com.example.movies.data.remote.MovieRemoteDataSource
import com.example.movies.utils.performGetOperation
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource) {


    fun getFlickrPhotoData(params: Map<String, String>) =
        performGetOperation(networkCall = { remoteDataSource.getFlickrPhotoData(params) })


}