package com.ilsamil.readingdiarycleanarchitecture.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.ilsamil.readingdiarycleanarchitecture.R
import com.ilsamil.readingdiarycleanarchitecture.adapter.ViewPagerAdapter
import com.ilsamil.readingdiarycleanarchitecture.databinding.FragmentBooksBinding
import com.ilsamil.readingdiarycleanarchitecture.viewmodel.BooksViewModel


class BooksFragment : Fragment() {
    private val booksViewModel by activityViewModels<BooksViewModel>()
    private lateinit var binding : FragmentBooksBinding

    private val tabTitleArray = arrayOf(
        "전체",
        "읽고 있는 책",
        "다 읽은 책"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_books, container, false)
        binding.booksViewPager.adapter = ViewPagerAdapter(activity?.supportFragmentManager!!, lifecycle)

        TabLayoutMediator(binding.booksTabLayoutTl, binding.booksViewPager) { tab, position ->
            tab.text = tabTitleArray[position]
            booksViewModel.setCategoryAll()
            booksViewModel.setCategoryReading()
            booksViewModel.setCategoryFinish()
        }.attach()

        binding.booksAddBtn.setOnClickListener {
            findNavController().navigate(R.id.action_booksFragment_to_searchFragment)
        }

        return binding.root
    }
}