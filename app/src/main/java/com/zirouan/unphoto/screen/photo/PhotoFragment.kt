package com.zirouan.unphoto.screen.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.zirouan.unphoto.R
import com.zirouan.unphoto.base.BaseFragment
import com.zirouan.unphoto.databinding.FragmentPhotoBinding
import com.zirouan.unphoto.screen.photo.decoration.PhotoDecoration
import com.zirouan.unphoto.util.extension.navigate
import com.zirouan.unphoto.util.extension.view.toastLong
import com.zirouan.unphoto.util.listener.OnRecyclerViewScrollListener
import com.zirouan.unphoto.util.widget.ToolbarBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoFragment : BaseFragment<FragmentPhotoBinding>() {

    override val module = photoModule
    override val viewModel: PhotoViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentPhotoBinding =
        FragmentPhotoBinding::inflate

    //region RecyclerView
    private val mAdapter: PhotoAdapter by lazy { PhotoAdapter() }
    private val mLayoutManager: GridLayoutManager by lazy { GridLayoutManager(context, 2) }
    private val onRecyclerScrollListener = object :
        OnRecyclerViewScrollListener(mLayoutManager, DIRECTION_END) {
        override fun loadMore(page: Int) {
            viewModel.fetchPhotos(page)
        }
    }
    //endregion RecyclerView

    //region BaseFragment
    override fun onInitView() {
        (mBinding.appbar.layoutParams as CoordinatorLayout.LayoutParams).behavior =
            ToolbarBehavior(mBinding.toolbar, mBinding.txtTitle, mBinding.imgIcon)

        mBinding.rvPhoto.let { rvView ->
            mAdapter.onItemClickListener = { _, _, photo ->
                val direction = PhotoFragmentDirections.actionPhotoDetailFragment(photo)
                navigate(direction)
            }

            rvView.adapter = mAdapter
            rvView.layoutManager = mLayoutManager
            rvView.addItemDecoration(PhotoDecoration(resources.getDimensionPixelSize(R.dimen.grid_space)))
            rvView.addOnScrollListener(onRecyclerScrollListener)
        }

        activity?.let {
            mBinding.swPhoto.setOnRefreshListener {
                viewModel.fetchPhotos(isRefresh = true, isInitData = false)
            }
            mBinding.swPhoto.setColorSchemeColors(
                ContextCompat.getColor(it, android.R.color.holo_red_dark),
                ContextCompat.getColor(it, android.R.color.holo_blue_dark),
                ContextCompat.getColor(it, android.R.color.holo_green_dark),
                ContextCompat.getColor(it, android.R.color.holo_orange_dark)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.showNavigationBottom()
    }

    override fun onInitObserver() {
        viewModel.photos.observe(this, {
            it?.let {
                mAdapter.addData(it)
            }
        })

        viewModel.page.observe(this, {
            onRecyclerScrollListener.page(it)
            mBinding.rvPhoto.smoothScrollToPosition(mAdapter.lastIndex)
        })

        viewModel.reset.observe(this, {
            mAdapter.clearData()
            onRecyclerScrollListener.reset()
        })
    }

    override fun onFetchInitial() {
        viewModel.fetchPhotos(isRefresh = true)
    }

    override fun onLoading(loading: Boolean) {
        mBinding.swPhoto.isRefreshing = loading
    }

    override fun onError(message: String) {
        activity?.toastLong(message)
    }
    //endregion BaseFragment
}