package com.example.movies.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.BuildConfig
import com.example.movies.R
import com.example.movies.data.entities.flickrPhoto.Photo
import com.example.movies.data.entities.movies.Movie
import com.example.movies.databinding.MovieDetailFragmentBinding
import com.example.movies.utils.MarginItemDecoration
import com.example.movies.utils.Resource
import com.example.movies.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.HashMap

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var binding: MovieDetailFragmentBinding by autoCleared()
    private val viewModel: MovieDetailViewModel by viewModels()
    private var noPictureToast: Toast? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(false)
        binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie =
            arguments?.getParcelable<Movie>("selected_movie")
        movie?.let {
            setupObservers(it)
        }
    }


    private fun setupObservers(movie: Movie) {
        val params = HashMap<String, String>()
        params["api_key"] = BuildConfig.FLICKER_KEY
        params["text"] = movie.title!!
        params["page"] = 1.toString()
        params["per_page"] = 10.toString()


        viewModel.start(params)
        viewModel.movieLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {

                    bindMovieDetails(movie)
                    if (it.data?.photos?.photo?.size!! > 0) {
                        setupImageRecyclerView(it.data.photos?.photo)
                    } else {
                        noPictureToast =
                            Toast.makeText(context, "No Picture found on Flickr", LENGTH_LONG)
                        noPictureToast?.show()
                    }

                    binding.progressBar.visibility = GONE
                    binding.movieCl.visibility = VISIBLE


                }

                Resource.Status.ERROR -> {
                    Toast.makeText(activity, it.message, LENGTH_LONG).show()
                    binding.progressBar.visibility = GONE
                    binding.movieCl.visibility = GONE
                }

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = VISIBLE
                    binding.movieCl.visibility = GONE
                }
            }
        })
    }

    private fun bindMovieDetails(movie: Movie) {

        binding.title.text = getString(R.string.movie_name, movie.title)
        binding.ratingAndYear.text = getString(R.string.rating_year, movie.rating, movie.year)

        if (movie.genres?.size!! > 0) {
            binding.genresHeading.visibility = VISIBLE
            binding.genres.visibility = VISIBLE
            movie.genres?.forEach {
                binding.genres.append(it.plus("\n"))
            }

        } else {
            binding.genresHeading.visibility = GONE
            binding.genres.visibility = GONE
        }

        if (movie.cast?.size!! > 0) {
            binding.castHeading.visibility = VISIBLE
            binding.casts.visibility = VISIBLE
            movie.cast?.forEach {
                binding.casts.append(it.plus("\n"))
            }

        } else {
            binding.castHeading.visibility = GONE
            binding.casts.visibility = GONE
        }


    }


    private fun setupImageRecyclerView(photos: List<Photo>?) {
        val adapter = MoviePicturesAdapter()
        binding.moviePictures.layoutManager = GridLayoutManager(context, 2)
        binding.moviePictures.adapter = adapter
        binding.moviePictures.addItemDecoration(
            MarginItemDecoration(
                R.dimen._8sdp,
                R.dimen._4sdp,
                R.dimen._8sdp,
                R.dimen._4sdp,
                R.dimen._8sdp
            )
        )

        adapter.setItems(photos)


    }

    override fun onDestroy() {
        noPictureToast?.cancel()
        super.onDestroy()

    }

}
