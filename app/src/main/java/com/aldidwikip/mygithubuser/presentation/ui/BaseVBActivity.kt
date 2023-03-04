package com.aldidwikip.mygithubuser.presentation.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseVBActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB

    protected abstract fun getViewBinding(): VB
    protected abstract fun init(savedInstanceState: Bundle?)
    protected open fun onBackPressCallback() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressCallback()
            }
        })

        init(savedInstanceState)
    }
}