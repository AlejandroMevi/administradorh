package com.venturessoft.human.views.ui.fragments.createAccount

import androidx.fragment.app.Fragment

interface CreateAccountInterface {
    fun showLoading(isShowing: Boolean)
    fun loadFragment(fragment: Fragment, tag: String)
}