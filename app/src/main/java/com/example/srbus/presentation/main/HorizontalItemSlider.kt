package com.example.srbus.presentation.main

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.constraint.ConstraintSet.WRAP_CONTENT
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.srbus.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class HorizontalItemSlider<T : ViewGroup.LayoutParams> : ConstraintLayout {
    private val TAG = javaClass.simpleName

    companion object {
        val SWIPE_OFFSET = 10
    }

    enum class LayoutParamsFlag { LEFT, CENTER, RIGHT }

    private val itemViews = arrayListOf<ItemView>()
    private val horizontalScrollDots = arrayListOf<ImageView>()
    private var currentPosition = 0
    private var containerWidth = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, activity: Activity, layoutManager: MainActivity.CustomLinearLayoutManager?) : super(context) {
        containerWidth = getScreenSize(activity).x - convertDpToPixel(16 * 4, activity)

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        id = generateViewId2()

        setOnTouchListener(MyOnTouchListener(activity, layoutManager))
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, layoutParams: T) : super(context) {
        this.layoutParams = layoutParams
    }

    fun addView(itemView: ItemView): ConstraintLayout {
        this.itemViews.add(itemView)

        val container = createNewContainer(itemView)
        itemViews[itemView.ordinal].item = container
        addView(container, 0)

        val horizontalScrollDot = createNewHorizontalScrollDot(itemView.ordinal)
        horizontalScrollDots.add(horizontalScrollDot)
        addView(horizontalScrollDot)

        return container
    }

    private fun createNewContainer(itemView: ItemView): ConstraintLayout {
        return ConstraintLayout(context).also { container ->
            container.id = generateViewId2()


            if (itemView.ordinal == 0) {
                container.layoutParams = createContainerLayoutParams(itemView.ordinal, LayoutParamsFlag.CENTER)
            } else {
                if (itemView.ordinal - 1 != 0) {
                    itemViews[itemView.ordinal - 1].item!!.layoutParams =
                        createContainerLayoutParams(itemView.ordinal - 1, LayoutParamsFlag.RIGHT)
                }
                container.layoutParams = createContainerLayoutParams(itemView.ordinal, LayoutParamsFlag.LEFT)
            }

//            container.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            container.background = ContextCompat.getDrawable(context, R.drawable.all_round_corner_background_white)
            container.addRipple()

            // ImageView(Icon)
            val ivIcon = ImageView(context).also {
                it.id = generateViewId2()
                it.layoutParams = LayoutParams(convertDpToPixel(30, context), convertDpToPixel(30, context)).also { params ->
                    params.topToTop = container.id
                    params.bottomToBottom = container.id
                    params.leftToLeft = container.id
                    params.leftMargin = convertDpToPixel(24, context)
                }
                it.setImageResource(itemView.icon)
            }
            container.addView(ivIcon)

            // TextView(SubTitle)
            val subTitle = TextView(context).also {
                it.id = generateViewId2()
                it.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also { params ->
                    params.bottomToBottom = ivIcon.id
                    params.leftToRight = ivIcon.id
                    params.leftMargin = convertDpToPixel(8, context)
                }
                it.text = "${itemView.subTitle} (${itemView.ordinal})"
            }
            container.addView(subTitle)

            // TextView(Title)
            val tvTitle = TextView(context).also {
                it.id = generateViewId2()
                it.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also { params ->
                    params.bottomToTop = subTitle.id
                    params.leftToLeft = subTitle.id
                    params.bottomMargin = convertDpToPixel(2, context)
                }
                it.text = itemView.title
            }
            container.addView(tvTitle)
        }
    }

    private fun createContainerLayoutParams(ordinal: Int, flag: LayoutParamsFlag): LayoutParams {
        return LayoutParams(containerWidth, convertDpToPixel(80, context)).also {
            it.topToTop = this@HorizontalItemSlider.id
            it.bottomToBottom = this@HorizontalItemSlider.id

            when (flag) {
                LayoutParamsFlag.LEFT -> {
                    val index = (ordinal + 1) % itemViews.size
                    it.rightToLeft = itemViews[index].item!!.id
                }
                LayoutParamsFlag.CENTER -> {
//                    it.leftToLeft = this@HorizontalItemSlider.id
                    it.rightToRight = this@HorizontalItemSlider.id
                }
                LayoutParamsFlag.RIGHT -> {
                    val index = when (ordinal - 1 >= 0) {
                        true -> ordinal - 1
                        false -> itemViews.size - 1
                    }
                    it.leftToRight = itemViews[index].item!!.id
                }
            }

            it.topMargin = convertDpToPixel(16, context)
            it.bottomMargin = convertDpToPixel(0, context)
            if (flag != LayoutParamsFlag.CENTER)
                it.leftMargin = convertDpToPixel(16, context)
            if (flag == LayoutParamsFlag.CENTER) {
                it.rightMargin = convertDpToPixel(16 * 3, context)
            } else if (flag == LayoutParamsFlag.LEFT) {
                it.rightMargin = convertDpToPixel(16, context)
            }
        }
    }

    private fun createNewHorizontalScrollDot(ordinal: Int): ImageView {
        return ImageView(context).also {
            it.id = generateViewId2()
            it.layoutParams = LayoutParams(convertDpToPixel(5, context), convertDpToPixel(5, context)).also { params ->
                params.bottomToBottom = itemViews[0].item!!.id
                if (horizontalScrollDots.size == 0) {
                    params.leftToLeft = this@HorizontalItemSlider.id
                    params.rightToRight = this@HorizontalItemSlider.id
                } else {
                    (horizontalScrollDots[ordinal - 1].layoutParams as LayoutParams).rightToLeft = it.id
                    params.leftToRight = horizontalScrollDots[ordinal - 1].id
                    params.rightToRight = this@HorizontalItemSlider.id
                }
                params.horizontalChainStyle = ConstraintSet.CHAIN_PACKED
                params.bottomMargin = convertDpToPixel(6, context)
                params.leftMargin = convertDpToPixel(2, context)
                params.rightMargin = convertDpToPixel(2, context)
            }
            it.setImageResource(R.drawable.circle)
            if (ordinal == currentPosition) {
                it.setColorFilter(ContextCompat.getColor(context, R.color.gray700))
            } else {
                it.setColorFilter(ContextCompat.getColor(context, R.color.gray300))
            }
        }
    }

    private fun convertDpToPixel(dp: Int, context: Context): Int =
        (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()

    private fun generateViewId2(): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return generateViewIdSdk17Under()
        } else {
            return View.generateViewId()
        }
    }
    private val sNextGeneratedId = AtomicInteger(1)
    private fun generateViewIdSdk17Under(): Int {
        while (true) {
            val result = sNextGeneratedId.get()
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun View.addRipple() {
        TypedValue().run {
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
            foreground = ContextCompat.getDrawable(context, resourceId)
        }
    }

    private fun getScreenSize(activity: Activity): Point {
        Point().let {
            activity.windowManager.defaultDisplay.getSize(it)
            return it
        }
    }

    data class ItemView(
        @DrawableRes val icon: Int, val title: String, val subTitle: String,
        val ordinal: Int, var item: ConstraintLayout? = null
    )

    inner class MyOnTouchListener(
        private val activity: Activity,
        private val layoutManager: MainActivity.CustomLinearLayoutManager?
    ) : OnTouchListener {

        private val screenWidth = getScreenSize(activity).x
        private var actionDownPointX = 0f
        private var actionDownRightMargin = 0
        private var actionDownLeftMargin = 0

        var test = 0

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//            Log.i(TAG, "v.width=${view.width}, event.x=${event.x}, setOnTouchListener, event=$event")
            if (itemViews.size == 0) {
                return false
            }

            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    layoutManager?.setScrollEnabled(false)
                    actionDownPointX = event.x
                    actionDownRightMargin = (itemViews[currentPosition].item!!.layoutParams as LayoutParams).rightMargin
                    actionDownLeftMargin = (itemViews[currentPosition].item!!.layoutParams as LayoutParams).leftMargin
//                    return false
                }
                MotionEvent.ACTION_MOVE -> {
                    itemViews[currentPosition].item!!.let {
                        val params = it.layoutParams as LayoutParams

                        if (params.rightMargin > 0) {
                            params.leftToLeft = -1
                            params.rightToRight = this@HorizontalItemSlider.id
                            params.rightMargin = actionDownRightMargin + Math.round(actionDownPointX - event.x)
                        } else {
                            params.leftToLeft = this@HorizontalItemSlider.id
                            params.rightToRight = -1
                            params.leftMargin = actionDownLeftMargin + Math.round(event.x - actionDownPointX)
                        }

                        it.layoutParams = params
                        test++
                    }

//                    return false
                }
                MotionEvent.ACTION_UP -> {
                    layoutManager?.setScrollEnabled(true)

                    val item = itemViews[currentPosition].item!!

                    if (item.x + item.width < screenWidth * (2 / 3f)) {
                        val smoothScrollingX = Math.round(screenWidth - (actionDownPointX - event.x)) - actionDownRightMargin - 10

                        GlobalScope.launch(Dispatchers.Default) {
                            for (i in 0 until (smoothScrollingX / 5f).toInt()) {
                                activity.runOnUiThread {
                                    val params = item.layoutParams as LayoutParams
                                    params.rightMargin += 5
                                    item.layoutParams = params
                                }
                                Thread.sleep(1)
                            }

                            activity.runOnUiThread {
                                var index = currentPosition - 1
                                if (index < 0)  {
                                    index = itemViews.size - 1
                                }

                                this@HorizontalItemSlider.removeView(itemViews[index].item)

                                index = (index + 1) % itemViews.size
                                itemViews[index].item!!.layoutParams = createContainerLayoutParams(itemViews[index].ordinal, LayoutParamsFlag.LEFT)
                                index = (index + 1) % itemViews.size
                                itemViews[index].item!!.layoutParams = createContainerLayoutParams(itemViews[index].ordinal, LayoutParamsFlag.CENTER)
                                index = (index + 1) % itemViews.size
                                itemViews[index].item!!.layoutParams = createContainerLayoutParams(itemViews[index].ordinal, LayoutParamsFlag.RIGHT)
                                index = (index + 1) % itemViews.size
                                itemViews[index].item!!.layoutParams = createContainerLayoutParams(itemViews[index].ordinal, LayoutParamsFlag.RIGHT)

                                this@HorizontalItemSlider.addView(itemViews[index].item, 0)

                                horizontalScrollDots[currentPosition].setColorFilter(ContextCompat.getColor(context!!, R.color.gray300))
                                currentPosition = (currentPosition + 1) % itemViews.size
                                horizontalScrollDots[currentPosition].setColorFilter(ContextCompat.getColor(context, R.color.gray700))
                            }
                        }
                    } else {
                        val item = itemViews[currentPosition].item!!

                        GlobalScope.launch(Dispatchers.Default) {
                            val smoothScrollingX = Math.round(actionDownPointX - event.x)

                            for (i in 0 until (smoothScrollingX / 5f).toInt()) {
                                activity.runOnUiThread {
                                    val params = item.layoutParams as LayoutParams
                                    params.rightMargin -= 5
                                    item.layoutParams = params
                                }
                                Thread.sleep(1)
                            }
                        }
                    }

                    if (item.x > screenWidth * (1 / 3f)) {
                        val smoothScrollingX = Math.round(screenWidth - item.x - 30)

                        GlobalScope.launch(Dispatchers.Default) {
                            for (i in 0 until (smoothScrollingX / 10f).toInt()) {
                                activity.runOnUiThread {
                                    val params = item.layoutParams as LayoutParams
                                    params.leftMargin += 10
                                    item.layoutParams = params
                                }
                                Thread.sleep(1)
                            }

                            var index = (currentPosition + 2) % itemViews.size
                            activity.runOnUiThread {
                                this@HorizontalItemSlider.removeView(itemViews[index].item)

                                index -= 4
                                if (index < 0) {
                                    index += itemViews.size
                                }
                                itemViews[index].item!!.layoutParams = createContainerLayoutParams(itemViews[index].ordinal, LayoutParamsFlag.LEFT)
                                this@HorizontalItemSlider.addView(itemViews[index].item, 0)

                                index = (index + 1) % itemViews.size
                                itemViews[index].item!!.layoutParams = createContainerLayoutParams(itemViews[index].ordinal, LayoutParamsFlag.CENTER)
                                index = (index + 1) % itemViews.size
                                itemViews[index].item!!.layoutParams = createContainerLayoutParams(itemViews[index].ordinal, LayoutParamsFlag.RIGHT)
                                index = (index + 1) % itemViews.size
                                itemViews[index].item!!.layoutParams = createContainerLayoutParams(itemViews[index].ordinal, LayoutParamsFlag.RIGHT)

                                horizontalScrollDots[currentPosition].setColorFilter(ContextCompat.getColor(context!!, R.color.gray300))
                                currentPosition -= 1
                                if (currentPosition < 0) {
                                    currentPosition = itemViews.size - 1
                                }
                                horizontalScrollDots[currentPosition].setColorFilter(ContextCompat.getColor(context, R.color.gray700))
                            }
                        }
                    } else {
                        val item = itemViews[currentPosition].item!!

                        GlobalScope.launch(Dispatchers.Default) {
                            val smoothScrollingX = Math.round(event.x - actionDownPointX)

                            for (i in 0 until (smoothScrollingX / 5f).toInt()) {
                                activity.runOnUiThread {
                                    val params = item.layoutParams as LayoutParams
                                    params.leftMargin -= 5
                                    item.layoutParams = params
                                }
                                Thread.sleep(1)
                            }
                        }
                    }

                    v?.performClick()
//                    return true
                }
            }
            return true
        }
    }
}



