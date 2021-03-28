package com.example.movies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.movies.data.entities.flickrPhoto.FlickrPhoto
import com.example.movies.data.repository.MovieRepository
import com.example.movies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val params = MutableLiveData<Map<String, String>>()

    private val movie = params.switchMap { id ->
        repository.getFlickrPhotoData(id)
    }
    val movieLiveData: LiveData<Resource<FlickrPhoto>> = movie

    fun start(params: Map<String, String>) {
        this.params.value = params
    }

}
