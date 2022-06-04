package io.gdscug.github.g_flix.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import io.gdscug.github.g_flix.R
import io.gdscug.github.g_flix.data.MovieEntity
import io.gdscug.github.g_flix.databinding.ActivityDetailBinding
import io.gdscug.github.g_flix.databinding.ContentDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var contentBinding: ContentDetailBinding

    private var data: MovieEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        contentBinding = binding.contentDetail

        val movies = intent.extras
        if (movies != null) {
            data = intent.getParcelableExtra(EXTRA_MOVIE)
            data?.let { populatedMovie(it) }
        }
    }

    private fun populatedMovie(movieEntity: MovieEntity) {
        supportActionBar?.title = movieEntity.movieTitle

        contentBinding.tvRate.text = resources.getString(R.string.rate)
        contentBinding.tvRateAge.text = resources.getString(R.string.rate_age)
        contentBinding.tvGenre.text = resources.getString(R.string.genre)
        contentBinding.tvDesc.text = movieEntity.movieDescription

        Glide.with(this)
            .load(movieEntity.moviePoster)
            .into(binding.ivPosterDetail)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }
}