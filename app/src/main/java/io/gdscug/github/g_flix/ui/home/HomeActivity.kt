package io.gdscug.github.g_flix.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import io.gdscug.github.g_flix.data.MovieEntity
import io.gdscug.github.g_flix.databinding.ActivityHomeBinding
import io.gdscug.github.g_flix.databinding.ContentHomeBinding
import io.gdscug.github.g_flix.ui.detail.DetailActivity
import io.gdscug.github.g_flix.utils.recycleview.LinearPageIndicatorDecoration
import io.gdscug.github.g_flix.utils.recycleview.SpaceBetweenItem

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var contentHomeBinding: ContentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentHomeBinding = binding.contentHome
        setSupportActionBar(binding.toolbar)

        // viewModel
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val movies = viewModel.getMovies()

        // Caraousel adapter
        val homeCaraouselAdapter = HomeCaraouselAdapter()

        // Limit caraousel to 5 items
        homeCaraouselAdapter.setMovies(movies.subList(0, 5))

        // Carousel move to detail
        homeCaraouselAdapter.onItemClickCallback = object : HomeCaraouselAdapter.OnItemClickCallback {
            override fun onItemClicked(movie: MovieEntity) {
                Intent(this@HomeActivity, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_MOVIE, movie)
                    startActivity(this)
                }
            }
        }

        with(contentHomeBinding.rvCarouselHome) {
            // Horizontal Scroll
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            // Snap it!
            PagerSnapHelper().attachToRecyclerView(this)
            setHasFixedSize(true)

            // add page indicator
            addItemDecoration(LinearPageIndicatorDecoration())

            adapter = homeCaraouselAdapter
        }

        // ForYou adapter
        val homeForYouAdapter = HomePosterAdapter()
        homeForYouAdapter.setMovies(movies.subList(0, 8))

        // ForYou move to detail
        homeForYouAdapter.onItemClickCallback = object : HomePosterAdapter.OnItemClickCallback {
            override fun onItemClicked(movie: MovieEntity) {
                Intent(this@HomeActivity, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_MOVIE, movie)
                    startActivity(this)
                }
            }
        }

        with(contentHomeBinding.rvForYouHome) {
            // Horizontal Scroll
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            // Snap it!
            PagerSnapHelper().attachToRecyclerView(this)
            setHasFixedSize(true)

            // space between item
            addItemDecoration(SpaceBetweenItem(16))

            adapter = homeForYouAdapter
        }

        // AllMovies adapter
        val allMoviesAdapter = HomePosterAdapter()
        allMoviesAdapter.setMovies(movies)

        // AllMovies move to detail
        allMoviesAdapter.onItemClickCallback = object : HomePosterAdapter.OnItemClickCallback {
            override fun onItemClicked(movie: MovieEntity) {
                Intent(this@HomeActivity, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_MOVIE, movie)
                    startActivity(this)
                }
            }
        }

        with(contentHomeBinding.rvAllMovies) {
            // snap it!
            PagerSnapHelper().attachToRecyclerView(this)
            setHasFixedSize(true)

            // space between item
            addItemDecoration(SpaceBetweenItem(16, true))

            adapter = allMoviesAdapter
        }
    }
}