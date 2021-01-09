package com.zirouan.unphoto.screen.photo.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class PhotoDecoration(
    private val space: Int,
    private val spanCount: Int = 2
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column: Int = position % spanCount

        outRect.left = space - column * space / spanCount
        outRect.right = (column + 1) * space / spanCount

        if (position < spanCount) {
            outRect.top = space;
        }
        outRect.bottom = space
    }
}