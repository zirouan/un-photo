package com.zirouan.unphoto.util.listener

abstract class OnScrollCallback {
    open fun onHide() {}
    open fun onShow() {}
    open fun onScrolledToLastItem() {}
    open fun onScrolledToFirstItem() {}
    open fun onScrolled(visibleItem: Int) {}
    open fun onScrollPage(page: Int, totalItemsCount: Int) {}
}