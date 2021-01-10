package com.zirouan.unphoto.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import com.zirouan.unphoto.R
import com.zirouan.unphoto.base.BaseFragment
import com.zirouan.unphoto.base.BaseViewModel
import com.zirouan.unphoto.databinding.FragmentHomeBinding
import com.zirouan.unphoto.util.extension.baseActivity
import com.zirouan.unphoto.util.extension.setupWithNavController
import com.zirouan.unphoto.util.extension.view.visible
import org.koin.core.module.Module

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override val module: Module? = null
    override val viewModel: BaseViewModel? = null

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }
    //endregion Fragment

    //region BaseFragment
    override fun onInitView() {
        val navGraphs = listOf(
            R.navigation.photo_nav_graph,
            R.navigation.about_nav_graph
        )

        mBinding.navBottom.setupWithNavController(
            navGraphIds = navGraphs,
            fragmentManager = childFragmentManager,
            containerId = R.id.navHostContainer
        )
        baseActivity?.changeNavigationVisibilityListener = { isVisible ->
            mBinding.navBottom.visible(isVisible)
            mBinding.vwDividerBottom.visible(isVisible)
        }
    }

    override fun onInitObserver() {}

    override fun onFetchInitial() {}

    override fun onLoading(loading: Boolean) {}

    override fun onError(message: String) {}
    //endregion BaseFragment
}