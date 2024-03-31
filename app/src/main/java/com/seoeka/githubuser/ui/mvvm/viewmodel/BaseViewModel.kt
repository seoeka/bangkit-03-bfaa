package com.seoeka.githubuser.ui.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel : ViewModel() {
    protected val isLoading = MutableLiveData<Boolean>()
    val isLoadingState: LiveData<Boolean> = isLoading

    protected val isError = MutableLiveData<String>()
    val isErrorState: LiveData<String> = isError

    protected val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        isError.value = "Error: ${exception.message}"
    }
}