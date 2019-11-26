package com.example.srbus.presentation.search

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.srbus.R
import com.example.srbus.presentation.BaseActivity
import com.example.srbus.utils.SharedPref
import com.example.srbus.utils.Util
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search_tab_item_bus.*
import kotlinx.android.synthetic.main.activity_search_tab_item_station.*

class SearchActivity : BaseActivity(), SearchActivityContract.View {
    private val TAG = javaClass.simpleName

    private lateinit var presenter: SearchActivityContract.Presenter
    private var listenerSearchBus: OnSearchKeywordListener? = null
    private var listenerSearchStation: OnSearchKeywordListener? = null

    private var textEdtBus = ""
    private var textEdtStation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        presenter = SearchActivityPresenter(this)

        initSoftKeyboardInputTypeButton()
        initTabLayoutAndViewPager()
        initEditText()

        iv_back.setOnClickListener { finish() }

        iv_delete.setOnClickListener {
            edt_search.editableText.clear()
        }
    }

    private fun initSoftKeyboardInputTypeButton() {
        var heightContainer = 0
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
            if (heightContainer == 0) {
                heightContainer = container.height
            }

            Rect().also {
                window.decorView.getWindowVisibleDisplayFrame(it)
                val heightRect = it.bottom - it.top
                Log.i(TAG, "addOnGlobalLayoutListener(), heightContainer=$heightContainer, heightRect=$heightRect")
                if (heightContainer - heightRect > 0) {
                    Log.i(TAG, "addOnGlobalLayoutListener(), SoftKeyboard Up")
                    cl_soft_keyboard_input_type_choice_bar.visibility = View.VISIBLE
                } else {
                    Log.i(TAG, "addOnGlobalLayoutListener(), SoftKeyboard Down")
                    cl_soft_keyboard_input_type_choice_bar.visibility = View.GONE
                }
            }
        }
        tv_input_type_number.setOnClickListener {
            edt_search.inputType = InputType.TYPE_CLASS_PHONE
            tv_input_type_number.background = resources.getDrawable(R.drawable.activity_search_soft_keyboard_input_type_button_background_on)
            tv_input_type_number.setTextColor(resources.getColor(R.color.white))
            tv_input_type_normal.background = resources.getDrawable(R.drawable.activity_search_soft_keyboard_input_type_button_background_off)
            tv_input_type_normal.setTextColor(resources.getColor(R.color.gray600))
        }
        tv_input_type_normal.setOnClickListener {
            edt_search.inputType = InputType.TYPE_CLASS_TEXT
            tv_input_type_normal.background = resources.getDrawable(R.drawable.activity_search_soft_keyboard_input_type_button_background_on)
            tv_input_type_normal.setTextColor(resources.getColor(R.color.white))
            tv_input_type_number.background = resources.getDrawable(R.drawable.activity_search_soft_keyboard_input_type_button_background_off)
            tv_input_type_number.setTextColor(resources.getColor(R.color.gray600))
        }
        iv_soft_keyboard_down.setOnClickListener {
            Util.hideSoftInput(this, edt_search)
        }
    }

    private fun initTabLayoutAndViewPager() {
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(
                LayoutInflater.from(this).inflate(R.layout.activity_search_tab_item_bus, null)
            ))
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(
                LayoutInflater.from(this).inflate(R.layout.activity_search_tab_item_station, null)
            ))

        if (Build.VERSION.SDK_INT >= 23) {
            tabLayout.tabIconTint = getColorStateList(R.color.selector_search_activity_tab_item)
        }

        with(view_pager) {
            adapter = SearchActivityPagerAdapter(supportFragmentManager, tabLayout.tabCount)
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    view_pager.currentItem = tab!!.position
                    when (tab.position) {
                        SearchActivityPagerAdapter.POSITION_SEARCH_BUS -> {
                            edt_search.hint = resources.getString(R.string.edit_text_search_hint_bus)
                            textEdtStation = edt_search.text.toString()
                            edt_search.text.clear()
                            edt_search.text.append(textEdtBus)

                            iv_tab_item_bus_icon.setColorFilter(Util.getColor(context, R.color.red500))
                            tv_tab_item_bus_text.setTextColor(Util.getColor(context, R.color.red500))
                            tv_tab_item_station_icon.setColorFilter(Util.getColor(context, R.color.gray400))
                            tv_tab_item_station_text.setTextColor(Util.getColor(context, R.color.gray400))
                        }
                        SearchActivityPagerAdapter.POSITION_SEARCH_STATION -> {
                            edt_search.hint = resources.getString(R.string.edit_text_search_hint_station)
                            textEdtBus = edt_search.text.toString()
                            edt_search.text.clear()
                            edt_search.text.append(textEdtStation)

                            iv_tab_item_bus_icon.setColorFilter(Util.getColor(context, R.color.gray400))
                            tv_tab_item_bus_text.setTextColor(Util.getColor(context, R.color.gray400))
                            tv_tab_item_station_icon.setColorFilter(Util.getColor(context, R.color.red500))
                            tv_tab_item_station_text.setTextColor(Util.getColor(context, R.color.red500))
                        }
                    }

                    SharedPref.saveStringPreferences(this@SearchActivity, SharedPref.Key.RECENT_SEARCH_MODE, when (tab.position) {
                        SearchActivityPagerAdapter.POSITION_SEARCH_BUS -> SharedPref.RecentSearchMode.BUS.name
                        SearchActivityPagerAdapter.POSITION_SEARCH_STATION -> SharedPref.RecentSearchMode.STATION.name
                        else -> SharedPref.RecentSearchMode.BUS.name
                    })
                }
                override fun onTabReselected(p0: TabLayout.Tab?) {}
                override fun onTabUnselected(p0: TabLayout.Tab?) {}
            })
        }

        view_pager.currentItem = when (SharedPref.getStringPreferences(this, SharedPref.Key.RECENT_SEARCH_MODE)) {
            SharedPref.RecentSearchMode.BUS.name -> SearchActivityPagerAdapter.POSITION_SEARCH_BUS
            SharedPref.RecentSearchMode.STATION.name -> SearchActivityPagerAdapter.POSITION_SEARCH_STATION
            else -> SearchActivityPagerAdapter.POSITION_SEARCH_BUS
        }
    }

    private fun initEditText() {
        edt_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                when (view_pager.currentItem) {
                    SearchActivityPagerAdapter.POSITION_SEARCH_BUS -> {
                        listenerSearchBus?.onSearch(v.text.toString())
                    }
                    SearchActivityPagerAdapter.POSITION_SEARCH_STATION -> {
                        listenerSearchStation?.onSearch(v.text.toString())
                    }
                }
                Util.hideSoftInput(this, edt_search)
                return@setOnEditorActionListener true
            }
            false
        }
        edt_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                iv_delete.visibility = if (s!!.isEmpty())
                    View.GONE
                else View.VISIBLE

                when (view_pager.currentItem) {
                    SearchActivityPagerAdapter.POSITION_SEARCH_BUS -> {
                        if (textEdtBus != s.toString()) { // view_pager 의 page 이동시 바뀌는 text change 는 무시하기 위함.
                            listenerSearchBus?.onSearch(s.toString())
                        }
                        textEdtBus = s.toString()
                    }
                    SearchActivityPagerAdapter.POSITION_SEARCH_STATION -> {
                        if (textEdtStation != s.toString()) { // view_pager 의 page 이동시 바뀌는 text change 는 무시하기 위함.
                            listenerSearchStation?.onSearch(s.toString())
                        }
                        textEdtStation = s.toString()
                    }
                }
            }
        })
    }

    interface OnSearchKeywordListener {
        fun onSearch(keyword: String)
    }

    fun setOnSearchKeywordListener(listener: OnSearchKeywordListener) {
        when (listener) {
            is SearchBusFragment -> listenerSearchBus = listener
            is SearchStationFragment -> listenerSearchStation = listener
        }
    }

    fun removeOnSearchKeywordListener(listener: OnSearchKeywordListener) {
        when (listener) {
            is SearchBusFragment -> listenerSearchBus = null
            is SearchStationFragment -> listenerSearchStation = null
        }
    }
}