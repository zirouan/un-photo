package com.zirouan.unphoto.base

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zirouan.unphoto.util.exception.ExceptionHandlerHelper
import com.zirouan.unphoto.util.exception.model.ErrorMessage
import kotlinx.coroutines.*
import java.net.HttpURLConnection

abstract class BaseViewModel(
    private val exceptionHandlerHelper: ExceptionHandlerHelper
) : ViewModel(), BaseContract.ViewModel {

    override val loading: LiveData<Boolean>
        get() = mLoading

    override val message: LiveData<String>
        get() = mMessage

    val redirect: LiveData<Int>
        get() = mRedirect

    private val mLoading = MutableLiveData<Boolean>()
    private val mMessage = MutableLiveData<String>()
    private val mRedirect = MutableLiveData<@IdRes Int>()

    protected fun defaultLaunch(
        validatorHelper: BaseValidatorHelper? = null,
        vararg any: Any?,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch {
            try {
                mLoading.postValue(true)

                validatorHelper?.let {
                    val validation = validatorHelper.validate(*any)
                    if (validation != null) {
                        mLoading.postValue(false)
                        mMessage.postValue(validation)
                        return@launch
                    }
                }

                block.invoke(this)
            } catch (exception: Exception) {
                handleException(exception)
            } finally {
                mLoading.postValue(false)
            }
        }
    }

    suspend fun doAtMainThread(doFunction: () -> Unit) {
        withContext(Dispatchers.Main) {
            doFunction.invoke()
        }
    }

    private fun handleException(exception: Exception) {
        val errorMessage = exceptionHandlerHelper.getErrorMessage(exception)
        callAction(errorMessage)
    }

    private fun callAction(errorMessage: ErrorMessage?) {
        // to force user to login
        errorMessage?.let {
            if (errorMessage.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                mRedirect.postValue(HttpURLConnection.HTTP_UNAUTHORIZED)
            } else {
                mMessage.postValue(errorMessage.message)
            }
        }?:run{
            mMessage.postValue("Error: Null")
        }
    }
}