package com.ilsamil.readingdiarycleanarchitecture.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilsamil.readingdiarycleanarchitecture.R
import com.ilsamil.readingdiarycleanarchitecture.adapter.BooksAdapter
import com.ilsamil.readingdiarycleanarchitecture.data.db.entity.MyBook
import com.ilsamil.readingdiarycleanarchitecture.databinding.FragmentBooksReadingBinding
import com.ilsamil.readingdiarycleanarchitecture.utils.RecyclerDecoration
import com.ilsamil.readingdiarycleanarchitecture.viewmodel.BooksViewModel

class BooksReadingFragment : Fragment() {
    private val booksViewModel by activityViewModels<BooksViewModel>()
    private lateinit var binding : FragmentBooksReadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_books_reading, container, false)
        binding.booksReadingRecyclerView.layoutManager = LinearLayoutManager(container?.context,
            RecyclerView.VERTICAL,
            false)
        val adapter = BooksAdapter().apply { bookOnClickItem = this@BooksReadingFragment::moveReadingBookItem }
        binding.booksReadingRecyclerView.adapter = adapter

        val spaceDecoration = RecyclerDecoration(25)
        binding.booksReadingRecyclerView.addItemDecoration(spaceDecoration)


        booksViewModel.apply {
            setCategoryReading()
            bookReadingList.observe(this@BooksReadingFragment, Observer {
                adapter.updateItems(it)
            })
        }

        return binding.root
    }

    private fun moveReadingBookItem(item: MyBook) {
        val action = BooksFragmentDirections.actionBooksFragmentToSelBookFragment(item)
        findNavController().navigate(action)
    }

}