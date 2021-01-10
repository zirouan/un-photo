package com.zirouan.unphoto.screen.about

import android.os.Bundle
import android.view.LayoutInflater
import com.zirouan.unphoto.base.BaseFragment
import com.zirouan.unphoto.base.BaseViewModel
import com.zirouan.unphoto.databinding.FragmentAboutBinding
import org.koin.core.module.Module

class AboutFragment : BaseFragment<FragmentAboutBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentAboutBinding
        get() = FragmentAboutBinding::inflate

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
    }

    override fun onInitObserver() {}

    override fun onFetchInitial() {}

    override fun onLoading(loading: Boolean) {}

    override fun onError(message: String) {}
    //endregion BaseFragment
}