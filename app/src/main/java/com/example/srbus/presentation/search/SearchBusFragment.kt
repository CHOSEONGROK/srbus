package com.example.srbus.presentation.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.srbus.R
import kotlinx.android.synthetic.main.fragment_search_bus.*

class SearchBusFragment : Fragment(), SearchBusContract.View, SearchActivity.OnSearchKeywordListener {
    private val TAG = javaClass.simpleName

    private lateinit var rvAdapter: SearchBusFragmentRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rvAdapter = SearchBusFragmentRvAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(context).inflate(R.layout.fragment_search_bus, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.adapter = rvAdapter

        (activity as SearchActivity).setOnSearchKeywordListener(this)
    }

    override fun onDetach() {
        (activity as SearchActivity).removeOnSearchKeywordListener(this)
        super.onDetach()
    }

    override fun initView() {

    }

    override fun showBusData() {

    }

    override fun onSearch(keyword: String) {
        Log.i(TAG, "onSearch($keyword)")
    }
}