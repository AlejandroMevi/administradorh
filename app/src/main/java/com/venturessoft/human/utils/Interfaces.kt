package com.venturessoft.human.utils

interface WelcomeFragmentCallback {
    fun goToWelcomeFragmentAndClearStack()
}

interface LoadingAnimationCallback {
    fun showLoadingAnimation()
    fun hideLoadingAnimation()
    fun showLoadingMessage(message: String)
    fun hideLoadingMessage()
}
interface MainInterface {
    fun showLanding(isShowing: Boolean = false)
    fun setTextToolbar(text: String)

}