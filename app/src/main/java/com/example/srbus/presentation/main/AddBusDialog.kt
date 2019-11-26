package com.example.srbus.presentation.main

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.srbus.R
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.DisplayMetrics
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.local.favorite.FavoriteStationInBusDB
import com.example.srbus.data.local.favorite.FavoriteStationInBusDao
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.utils.Constants
import com.example.srbus.utils.Util
import kotlinx.android.synthetic.main.activity_main_add_bus_dialog.*

class AddBusDialog : BottomSheetDialogFragment() {
    private val TAG = javaClass.simpleName

    private var favoriteStation: FavoriteStation? = null
    private var arrBus: ArrBus? = null
    private val originalFavoriteValues = arrayListOf<Boolean>()

    private lateinit var rvAdapter: AddBusDialogRvAdapter
    private lateinit var favoriteStationInBusDao: FavoriteStationInBusDao

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setWhiteNavigationBar(dialog)
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(context).inflate(R.layout.activity_main_add_bus_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteStationInBusDao = FavoriteStationInBusDB
            .getInstance(context!!)
            .favoriteStationInBusDao()

        arguments?.let {
            favoriteStation = it.getSerializable(Constants.INTENT_KEY_FAVORITE_STATION) as? FavoriteStation
            arrBus = it.getSerializable(Constants.INTENT_KEY_ARRBUS) as? ArrBus

            if (favoriteStation != null) {
                val favoriteBuses = favoriteStationInBusDao.getItemsByArsId(favoriteStation!!.arsId)

                arrBus?.msgBody?.itemList?.forEach { bus ->
                    favoriteBuses.forEach { favoriteBus ->
                        if (bus.busRouteId == favoriteBus.busRouteId) {
                            bus.isFavorite = true
                        }
                    }
                    originalFavoriteValues.add(bus.isFavorite)
                }
            }

            rvAdapter = AddBusDialogRvAdapter(this, arrBus)
        }
        recycler_view.adapter = rvAdapter

        tv_station_name.text = favoriteStation?.stNm ?: "No Data"
        tv_next_station.text = favoriteStation?.nextStation ?: "No Data"
        iv_close.setOnClickListener {
            (activity as MainActivity).onDialogDismiss(
                favoriteStation?.arsId, isChangedFavoriteValue()
            )
            dismiss()
        }
    }

    override fun onDetach() {
        (activity as MainActivity).onDialogDismiss(
            favoriteStation?.arsId, isChangedFavoriteValue()
        )
        super.onDetach()
    }

    fun addFavoriteBus(bus: FavoriteStationInBus) {
        favoriteStationInBusDao.insert(bus)
        Toast.makeText(context, R.string.toast_message_add_favorite, Toast.LENGTH_SHORT).show()
    }

    fun removeFavoriteBus(busRouteId: String) {
        favoriteStationInBusDao.deleteByBusRouteId(busRouteId)
        Toast.makeText(context, R.string.toast_message_remove_favorite, Toast.LENGTH_SHORT).show()
    }
    
    private fun isChangedFavoriteValue(): Boolean {
        arrBus?.msgBody?.itemList?.forEachIndexed { index, arrBusItem ->
            if (arrBusItem.isFavorite != originalFavoriteValues[index]) {
                return true
            }
        }
        return false
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setWhiteNavigationBar(dialog: Dialog) {
        val window = dialog.window
        if (window != null) {
            val metrics = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(metrics)

            val dimDrawable = GradientDrawable()
            // ...customize your dim effect here

            val navigationBarDrawable = GradientDrawable()
            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
            navigationBarDrawable.setColor(Util.getColor(context!!, R.color.gray300))

            val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)

            val windowBackground = LayerDrawable(layers)
            windowBackground.setLayerInsetTop(1, metrics.heightPixels)

            window.setBackgroundDrawable(windowBackground)
        }
    }
}