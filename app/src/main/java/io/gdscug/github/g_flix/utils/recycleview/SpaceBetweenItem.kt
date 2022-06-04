package io.gdscug.github.g_flix.utils.recycleview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceBetweenItem(private val Spacing: Int, private val grid: Boolean? = false) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        outRect.left = if (itemPosition == 0) 0 else Spacing
        outRect.right = if (itemPosition == state.itemCount - 1) 2 * Spacing else Spacing

        if (grid == true) {
            outRect.left = 0
            outRect.right = 0
            outRect.bottom = if (itemPosition == state.itemCount - 1) 0 else Spacing
        }
    }
}
