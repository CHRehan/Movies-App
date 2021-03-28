package com.example.movies.ui.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.entities.movies.Movie
import com.example.movies.databinding.ItemMovieBinding

class MoviesAdapter(private val listener: MovieItemListener) :
    RecyclerView.Adapter<MovieViewHolder>(), Filterable {


    private val searchableList = ArrayList<Movie>()
    private val originalList = ArrayList(searchableList)
    private var onNothingFound: (() -> Unit)? = null

    fun setItems(items: List<Movie>?) {
        this.searchableList.clear()
        items?.let {
            this.searchableList.addAll(it)
            originalList.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding: ItemMovieBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = searchableList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(searchableList[position])

        holder.itemView.setOnClickListener {
            listener.onClickedMovie(searchableList[position])

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charString = constraint.toString()
                searchableList.clear()
                if (constraint.isNullOrBlank()) {
                    searchableList.addAll(originalList)
                } else {
                    val searchResults =
                        originalList.filter {
                            it.title?.toLowerCase()?.contains(charString.toLowerCase())!!
                        }
                    searchableList.addAll(searchResults)
                }
                return filterResults.also {
                    it.values = searchableList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // no need to use "results" filtered list provided by this method.
                if (searchableList.isNullOrEmpty())
                    onNothingFound?.invoke()
                notifyDataSetChanged()

            }
        }
    }


    interface MovieItemListener {
        fun onClickedMovie(movie: Movie)
    }

    fun search(s: String?, onNothingFound: (() -> Unit)?) {
        this.onNothingFound = onNothingFound
        filter.filter(s)
    }


}

class MovieViewHolder(
    private val itemBinding: ItemMovieBinding,
    private val context: Context
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var movie: Movie


    fun bind(item: Movie) {
        this.movie = item
        itemBinding.title.text = item.title
        itemBinding.ratingAndYear.text =
            context.getString(R.string.rating_year, item.rating, item.year)

    }


}

