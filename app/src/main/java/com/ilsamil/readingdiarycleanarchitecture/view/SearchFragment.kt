package com.ilsamil.readingdiarycleanarchitecture.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilsamil.readingdiarycleanarchitecture.R
import com.ilsamil.readingdiarycleanarchitecture.adapter.SearchAdapter
import com.ilsamil.readingdiarycleanarchitecture.data.remote.model.Books
import com.ilsamil.readingdiarycleanarchitecture.databinding.FragmentSearchBinding
import com.ilsamil.readingdiarycleanarchitecture.viewmodel.SearchViewModel

class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private val searchViewModel by activityViewModels<SearchViewModel>()
    private lateinit var imm : InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.apply {
            searchBackBtn.setOnClickListener { findNavController().popBackStack() }
            searchRecyclerView.layoutManager = LinearLayoutManager(
                container?.context,
                RecyclerView.VERTICAL,
                false
            )

            // 키보드 엔터 혹은 검색버튼 클릭 시 searchViewModel 함수 호출
            searchEt.setOnKeyListener { view, i, keyEvent ->
                if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    val searchText = searchEt.text.toString()
                    if (searchText != "") {
                        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(searchEt.windowToken, 0)

                        searchViewModel.getSearchBook(searchText)
                    }
                    true
                } else false
            }

            searchClearBtn.setOnClickListener {
                searchEt.setText("")
                focusKy()
            }
        }

        val adapter = SearchAdapter().apply { onClickItem = this@SearchFragment::moveSearchResult }
        binding.searchRecyclerView.adapter = adapter
        searchViewModel.searchItem.observe(this, Observer {
            binding.searchGuideTv.visibility = View.INVISIBLE
            binding.searchRecyclerView.visibility = View.VISIBLE
            adapter.updateItem(it)
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        focusKy()
    }

    private fun focusKy() {
        imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.searchEt.requestFocus()
        imm.showSoftInput(binding.searchEt, InputMethodManager.SHOW_IMPLICIT)
    }

    // 클릭 이벤트
    private fun moveSearchResult(item : Books) {
        val action = SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(item)
        findNavController().navigate(action)
    }

}