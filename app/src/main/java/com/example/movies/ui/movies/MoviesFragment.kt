package com.example.movies.ui.movies

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.data.entities.movies.Movie
import com.example.movies.data.entities.movies.MoviesDetail
import com.example.movies.databinding.MoviesFragmentBinding
import com.example.movies.utils.Helpers
import com.example.movies.utils.MarginItemDecoration
import com.example.movies.utils.autoCleared
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MoviesFragment : Fragment(), MoviesAdapter.MovieItemListener {

    private var binding: MoviesFragmentBinding by autoCleared()
    private val viewModel: MoviesViewModel by lazy {
        obtainViewModel(
            requireActivity(),
            MoviesViewModel::class.java,
            defaultViewModelProviderFactory
        )
    }
    private lateinit var adapter: MoviesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MoviesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadLocalData()
        setUpSearchResult()
    }


    private fun setupRecyclerView() {
        adapter = MoviesAdapter(this)
        binding.moviesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRv.adapter = adapter
        binding.moviesRv.addItemDecoration(
            MarginItemDecoration(
                R.dimen._16sdp,
                R.dimen._4sdp,
                R.dimen._16sdp,
                R.dimen._4sdp,
                R.dimen._6sdp
            )
        )


    }

    private fun setUpSearchResult() {
        viewModel.searchQuery.observe(viewLifecycleOwner, {
            search(it)
        })

    }


    private fun loadLocalData() {
        val dataString = context?.let { Helpers.loadJSONFromAsset(it, "movies.json") }
        val data: MoviesDetail = Gson().fromJson(dataString, MoviesDetail::class.java)
        Collections.sort(data.movies)
        adapter.setItems(data.movies)
    }

    override fun onClickedMovie(movie: Movie) {
        findNavController().navigate(
            R.id.action_moviesFragment_to_movieDetailFragment,
            bundleOf("selected_movie" to movie)
        )
    }

    /**
     * Extension Function
     */
    private fun <T : ViewModel> obtainViewModel(
        owner: ViewModelStoreOwner,
        viewModelClass: Class<T>,
        viewModelFactory: ViewModelProvider.Factory
    ) = ViewModelProvider(owner, viewModelFactory).get(viewModelClass)


    private fun search(s: String?) {
        adapter.search(s) {
            // update UI on nothing found
//            Toast.makeText(context, "Nothing Found", Toast.LENGTH_SHORT)
//            .show()
//

        }
    }
}
