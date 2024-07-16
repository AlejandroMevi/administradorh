package com.venturessoft.human.views.ui.fragments

import rx.subscriptions.CompositeSubscription

open class BaseFragment : androidx.fragment.app.Fragment() {
    protected var mCompositeSubscription: CompositeSubscription? = CompositeSubscription()
    override fun onStop() {
        super.onStop()
        mCompositeSubscription?.clear()
    }
}