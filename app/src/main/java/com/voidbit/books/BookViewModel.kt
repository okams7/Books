package com.voidbit.books

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voidbit.books.models.Book
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookViewModel(private val stateHandle: SavedStateHandle): ViewModel() {
    private var api: BooksApi
    val state = mutableStateOf(emptyList<Book>())
    private val errorHandler = CoroutineExceptionHandler{_, e ->
        e.printStackTrace()
    }
    init{
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl("https://books-34b46-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()
        api = retrofit.create(BooksApi::class.java)
        getBooks()
    }

    private fun getBooks() {
        viewModelScope.launch(errorHandler) {
            val books = getRemoteBooks()
            state.value = books.restoreFinishedField()
        }
    }

    private suspend fun getRemoteBooks(): List<Book>{
        return withContext(Dispatchers.IO){
            api.getBooks()
        }
    }

    private fun List<Book>.restoreFinishedField(): List<Book> {
        stateHandle.get<List<Int>?>("finished")?.let {selectedIds ->
            val booksMap = this.associateBy { it.id }
            selectedIds.forEach {id ->
                booksMap[id]?.finished = true
            }
            return booksMap.values.toList()
        }
        return this
    }

    fun toggleFinished(id: Int){
        val books = state.value.toMutableList()
        val bookIndex = books.indexOfFirst { it.id == id }
        val book = books[bookIndex]
        books[bookIndex] = book.copy(finished = !book.finished)
        state.value = books
    }
}