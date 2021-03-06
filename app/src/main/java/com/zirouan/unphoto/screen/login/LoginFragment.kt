package com.zirouan.unphoto.screen.login

import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import com.zirouan.unphoto.R
import com.zirouan.unphoto.base.BaseFragment
import com.zirouan.unphoto.databinding.FragmentLoginBinding
import com.zirouan.unphoto.util.AnimationUtil
import com.zirouan.unphoto.util.extension.TransitionAnimation
import com.zirouan.unphoto.util.extension.hideKeyboard
import com.zirouan.unphoto.util.extension.navigate
import com.zirouan.unphoto.util.extension.showToast
import com.zirouan.unphoto.util.extension.view.color
import com.zirouan.unphoto.util.extension.view.tint
import com.zirouan.unphoto.util.extension.view.visible
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val module = loginModule
    override val viewModel: LoginViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentLoginBinding =
            FragmentLoginBinding::inflate

    //region BaseFragment
    override fun onInitObserver() {
        viewModel.login.observe(this, {
            showScreenPhoto()
        })
    }

    override fun onInitView() {
        mBinding.includeField.btnLogin.setOnClickListener { doLogin() }
        mBinding.includeField.edtPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                doLogin()
            }

            true
        }

        activity?.let {
            KeyboardVisibilityEvent.setEventListener(it) { isOpen ->
                AnimationUtil.incrementPercent(
                        mBinding.guideline,
                        if (isOpen) 0.5f else 0.15f,
                        if (isOpen) 0.15f else 0.5f,
                        800
                )
            }
        }
    }

    override fun onFetchInitial() {
        AnimationUtil.showView(
                mBinding.includeField.clField,
                R.anim.translate_fade_in, 1000
        )
        AnimationUtil.incrementPercent(
                mBinding.guideline,
                0.55f, 0.5f,
                600
        )
    }

    private fun doLogin() {
        hideKeyboard()
        viewModel.doLogin(mBinding.includeField.edtEmail.text.toString(),
                mBinding.includeField.edtPassword.text.toString())
    }

    override fun onLoading(loading: Boolean) {
        mBinding.includeField.pbLogin.visible(loading)

        mBinding.includeField.btnLogin.isEnabled = !loading
        mBinding.includeField.edtEmail.isEnabled = !loading
        mBinding.includeField.edtPassword.isEnabled = !loading
    }

    override fun onError(message: String) {
        showToast(message)

    }    //endregion BaseFragment

    //region Local
    private fun showScreenPhoto() {
        mBinding.includeField.btnLogin.postDelayed({
            val direction = LoginFragmentDirections.actionLoginHomeFragment()
            navigate(
                    directions = direction,
                    clearBackStack = true,
                    animation = TransitionAnimation.TRANSLATE_FROM_DOWN
            )
        }, 1000)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.hideStatusBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.showStatusBar()
    }
    //region Local
}