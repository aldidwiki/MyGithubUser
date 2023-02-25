package com.aldidwikip.mygithubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseVBActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB

    protected abstract fun getViewBinding(): VB
    protected abstract fun init(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        init(savedInstanceState)
    }
}