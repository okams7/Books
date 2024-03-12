package com.voidbit.books

import com.voidbit.books.models.Book
import retrofit2.http.GET

interface BooksApi {

    @GET("books.json")
    suspend fun getBooks(): List<Book>
}