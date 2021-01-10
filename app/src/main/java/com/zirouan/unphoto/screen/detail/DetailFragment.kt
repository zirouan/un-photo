package com.zirouan.unphoto.screen.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.zirouan.unphoto.R
import com.zirouan.unphoto.base.BaseFragment
import com.zirouan.unphoto.databinding.FragmentDetailBinding
import com.zirouan.unphoto.screen.photo.model.Photo
import com.zirouan.unphoto.util.extension.TransitionAnimation
import com.zirouan.unphoto.util.extension.navigate
import com.zirouan.unphoto.util.extension.view.loadImage
import com.zirouan.unphoto.util.extension.view.toastLong
import com.zirouan.unphoto.util.extension.view.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    override val module = detailModule
    private val mArgs: DetailFragmentArgs by navArgs()

    override val viewModel: DetailViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentDetailBinding =
        FragmentDetailBinding::inflate

    private var mPhoto: Photo? = null

    //region BaseFragment
    override fun onInitView() {
        this.setToolbar(mBinding.toolbar)
            .title(R.string.title_detail)
            .displayHome(true)
            .icon(R.drawable.ic_back)
            .builder()

        activity?.let {
            mBinding.ctLayout.isTitleEnabled = false
        }

        mBinding.fab.setOnClickListener {
            mPhoto?.id?.let { id ->
                viewModel.fetchTrackPhoto(id)
            }
        }

        hideNavigationBottom()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            changeStatusBarColor(ContextCompat.getColor(it, android.R.color.transparent))
        }
    }

    override fun onInitObserver() {
        viewModel.photo.observe(this, { url ->
            val like = mPhoto?.likes ?: run { 0 }
            val direction = DetailFragmentDirections.actionDetailPreviewFragment(like, url)
            navigate(directions = direction, animation = TransitionAnimation.TRANSLATE_FROM_UP)
        })
    }

    override fun onFetchInitial() {
        mPhoto = mArgs.argPhoto
        mPhoto?.let {
            it.user?.bio?.let { bio ->
                mBinding.icBio.txtBio.text = bio
            }

            it.urls?.full?.let { full ->
                mBinding.imgPhoto.loadImage(full, onFinished = { loading ->
                    mBinding.pbPhoto.visible(loading)
                })
            }
        }
    }

    override fun onLoading(loading: Boolean) {
    }

    override fun onError(message: String) {
        activity?.toastLong(message)
    }
    //endregion BaseFragment
}