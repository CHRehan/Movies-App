package com.example.movies.data.entities.movies

import android.os.Parcel
import android.os.Parcelable

class Movie() : Parcelable, Comparable<Movie> {

    var title: String? = null
    var year: Int? = null
    var cast: List<String>? = null
    var genres: List<String>? = null
    var rating: Int? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        year = parcel.readValue(Int::class.java.classLoader) as? Int
        cast = parcel.createStringArrayList()
        genres = parcel.createStringArrayList()
        rating = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeValue(year)
        parcel.writeStringList(cast)
        parcel.writeStringList(genres)
        parcel.writeValue(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

    override fun compareTo(other: Movie): Int {
        return year?.let { other.year?.compareTo(other = it) }!!
    }


}