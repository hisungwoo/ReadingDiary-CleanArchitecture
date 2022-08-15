package com.ilsamil.readingdiarycleanarchitecture.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilsamil.readingdiarycleanarchitecture.data.remote.model.Books
import com.ilsamil.readingdiarycleanarchitecture.repository.KakaoBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: KakaoBookRepository
) : ViewModel() {
    var searchItem = MutableLiveData<List<Books>>()

    fun getSearchBook(SearchText : String) {
        viewModelScope.launch {
            val searchBooks = repository.getBookInfo(SearchText)
            if (searchBooks != null) {
                searchItem.value = searchBooks.bookInfo
            }
        }
    }
}