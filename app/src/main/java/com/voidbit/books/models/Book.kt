package com.voidbit.books.models

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("r_id")
    val id: Int,
    @SerializedName("r_title")
    val title: String,
    @SerializedName("r_author")
    val author: String,
    @SerializedName("r_genre")
    val genre: String,
    @SerializedName("r_series")
    val series: String?,
    @SerializedName("r_finished")
    var finished: Boolean = false
)
