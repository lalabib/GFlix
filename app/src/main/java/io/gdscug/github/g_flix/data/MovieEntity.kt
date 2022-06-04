package io.gdscug.github.g_flix.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieEntity(
    var movieId: String,
    var movieTitle: String,
    var moviePoster: String,
    var movieDescription: String,
) : Parcelable
