package io.gdscug.github.g_flix.utils.recycleview

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

class LinearPageIndicatorDecoration : RecyclerView.ItemDecoration() {
    // source https://blog.davidmedenjak.com/android/2017/06/24/viewpager-recyclerview.html

    private val colorActive: Long = 0xFFFFFFFF
    private val colorInactive: Long = 0x66FFFFFF

    private val displayMetrics: Float = Resources.getSystem().displayMetrics.density

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private val mIndicatorHeight: Int = (displayMetrics * -64).toInt()

    /**
     * Indicator stroke width.
     */
    private val mIndicatorStrokeWidth: Float = displayMetrics * 2

    /**
     * Indicator width.
     */
    private val mIndicatorItemLength: Float = displayMetrics * 16

    /**
     * Padding between indicators.
     */
    private val mIndicatorItemPadding: Float = displayMetrics * 4

    /**
     * Some more natural animation interpolation
     */
    private var mInterpolator: AccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()

    private var mPaint: Paint = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = mIndicatorStrokeWidth
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val itemCount = state.itemCount

        // center horizontally, calculate width and subtract half from center
        val totalLength: Float = mIndicatorItemLength * itemCount
        val paddingBetweenItems: Float = max(0, itemCount - 1) * mIndicatorItemPadding
        val indicatorTotalWidth: Float = totalLength + paddingBetweenItems
        val indicatorStartX: Float = (parent.width - indicatorTotalWidth) / 2F

        // center vertically in the allotted space
        val indicatorPosY: Float = (parent.width - mIndicatorHeight) / 2F

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)

        // find active page (which should be highlighted)
        val layoutManager = (parent.layoutManager as LinearLayoutManager)
        val activePosition: Int = layoutManager.findFirstVisibleItemPosition()
        if (activePosition == RecyclerView.NO_POSITION) return

        // find offset of active page (if the user is scrolling)
        val activeChild = layoutManager.findViewByPosition(activePosition)
        val left = activeChild?.left
        val width = activeChild?.width

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        if (left != null && width != null) {
            val progress: Float = mInterpolator.getInterpolation(left * -1 / width.toFloat())
            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
        }
    }

    private fun drawInactiveIndicators(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        itemCount: Int
    ) {
        mPaint.apply {
            color = colorInactive.toInt()
        }

        // width of item indicator including padding
        val itemWidth: Float = mIndicatorItemLength + mIndicatorItemPadding

        var start: Float = indicatorStartX

        for (item in itemCount.downTo(1)) {
            // draw the line for every time
            c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint)
            start += itemWidth
        }
    }

    private fun drawHighlights(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        activePosition: Int,
        progress: Float,
        itemCount: Int
    ) {
        mPaint.apply {
            color = colorActive.toInt()
        }

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding

        var highlightStart: Float = indicatorStartX + itemWidth * activePosition
        if (progress == 0F) {
            // no swipe, draw a normal indicator
            c.drawLine(
                highlightStart,
                indicatorPosY,
                highlightStart + mIndicatorItemLength,
                indicatorPosY,
                mPaint
            )
        } else {
            // calculate partial highlight
            val partialLength = mIndicatorItemLength * progress

            // draw the cut off highlight
            c.drawLine(
                highlightStart + partialLength,
                indicatorPosY,
                highlightStart + mIndicatorItemLength,
                indicatorPosY,
                mPaint
            )

            // draw the highlight overlapping to the next item as well
            if (activePosition < itemCount - 1) {
                highlightStart += itemWidth
                c.drawLine(
                    highlightStart,
                    indicatorPosY,
                    highlightStart + partialLength,
                    indicatorPosY,
                    mPaint
                )
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = mIndicatorHeight
    }
}
