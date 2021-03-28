package com.example.movies.data.remote

import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(private val movieService: MovieService): BaseDataSource() {

    suspend fun getFlickrPhotoData(params: Map<String, String>) = getResult { movieService.getFlickrPhotoData(params) }
}