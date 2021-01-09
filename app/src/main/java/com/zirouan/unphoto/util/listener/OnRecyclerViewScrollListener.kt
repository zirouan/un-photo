package com.zirouan.unphoto.util.listener

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * This class is based on RecyclerViewPaginator.java {@link #https://gist.github.com/anitaa1990/0e8911211faef2dd374cd8f750053704}
 */

abstract class OnRecyclerViewScrollListener(
        private val layoutManager: RecyclerView.LayoutManager,
        private val scrollDirectionListening: Int
) : RecyclerView.OnScrollListener() {

    companion object {
        const val CURRENT_PAGE = 1
        const val DIRECTION_END = 0
        const val DIRECTION_START = 1
        const val DIRECTION_START_END = 2
    }

    private var mOldScrollD = 0
    private var mCurrentPage = CURRENT_PAGE
    private var mIsDirectionScrolled = false

    /*
     * This variable is used to set
     * the threshold. For instance, if I have
     * set the page limit to 20, this will notify
     * the app to fetch more transactions when the
     * user scrolls to the 18th item of the list.
     * */
    private val mThreshold = 2

    var isLoading = false
    var isLastPage = false

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    private fun getFirstVisibleItem(firstVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in firstVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = firstVisibleItemPositions[i]
            } else if (firstVisibleItemPositions[i] > maxSize) {
                maxSize = firstVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    private fun findFirstVisibleItemPosition(): Int {
        layoutManager.let { layoutManager ->
            return when (layoutManager) {
                is LinearLayoutManager -> {
                    layoutManager.findFirstVisibleItemPosition()
                }
                is GridLayoutManager -> {
                    layoutManager.findFirstVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    val firstVisibleItemPositions =
                            layoutManager.findFirstVisibleItemPositions(null)
                    getFirstVisibleItem(firstVisibleItemPositions)
                }
                else -> 0
            }
        }
    }

    private fun findLastVisibleItemPosition(): Int {
        layoutManager.let { layoutManager ->
            return when (layoutManager) {
                is LinearLayoutManager -> {
                    layoutManager.findLastVisibleItemPosition()
                }
                is GridLayoutManager -> {
                    layoutManager.findLastVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    val lastVisibleItemPositions =
                            layoutManager.findLastVisibleItemPositions(null)
                    getLastVisibleItem(lastVisibleItemPositions)
                }
                else -> 0
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE && mIsDirectionScrolled) {
            val totalItemCount: Int = layoutManager.itemCount
            val firstVisibleItemPosition = findLastVisibleItemPosition()

            if (isLoading) return
            if (isLastPage) return

            if (firstVisibleItemPosition + mThreshold >= totalItemCount) {
                loadMore(++mCurrentPage)
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val orientation = when (layoutManager) {
            is GridLayoutManager -> layoutManager.orientation
            is LinearLayoutManager -> layoutManager.orientation
            is StaggeredGridLayoutManager -> layoutManager.orientation
            else -> RecyclerView.VERTICAL
        }

        val d = if (orientation == RecyclerView.VERTICAL) dy else dx

        mIsDirectionScrolled = when (scrollDirectionListening) {
            DIRECTION_END -> d > mOldScrollD
            DIRECTION_START -> d < mOldScrollD
            else -> true
        }
    }

    fun reset() {
        mCurrentPage = CURRENT_PAGE
    }

    fun page(page: Int) {
        mCurrentPage = page
    }

    abstract fun loadMore(page: Int)
}
