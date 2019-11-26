package com.example.srbus.presentation.search

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.srbus.R
import com.example.srbus.data.SearchBus
import kotlinx.android.synthetic.main.fragment_search_bus_rv_item.view.*

class SearchBusFragmentRvAdapter(
    private val view: SearchBusContract.View
) : RecyclerView.Adapter<SearchBusFragmentRvAdapter.ViewHolder>() {
    private val TAG = javaClass.simpleName

    private val list = arrayListOf<SearchBus>()
    private val context = (view as SearchBusFragment).context!!

    init {
        for (i in 0..30) {
            list.add(
                SearchBus(641, "서울", "간선버스", (i % 4 == 0))
            )
        }
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val bus = list[position]

            itemView.setOnClickListener { Log.d(TAG, "itemView.setOnClickListener") }

            tvBusNumber.text = bus.number.toString()
            tvBusCity.text = bus.city
            tvBusType.text = bus.type
            if (Build.VERSION.SDK_INT >= 21) {
                ivBusFavorite.imageTintList = bus.getFavoriteColorStateList(context)

                ivBusFavorite.setOnClickListener {
                    bus.isFavorite = !(bus.isFavorite)
                    ivBusFavorite.imageTintList = bus.getFavoriteColorStateList(context)
                }
            }
        }
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_search_bus_rv_item, parent, false)) {

        val tvBusNumber = itemView.tv_bus_number
        val tvBusCity = itemView.tv_bus_city
        val tvBusType = itemView.tv_bus_type
        val ivBusFavorite = itemView.iv_bus_favorite
    }
}