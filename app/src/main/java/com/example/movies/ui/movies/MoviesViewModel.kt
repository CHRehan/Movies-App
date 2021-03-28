package com.example.movies.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// will inject via extension functions

class MoviesViewModel() : ViewModel() {
    val searchQuery = MutableLiveData<String>()
    fun setSearchQuery(query: String) {
        this.searchQuery.value = query
    }
}
