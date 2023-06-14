package com.example.courcework.app.presentation.screens.profile

data class ProfileState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: ProfileItem? = null
)

sealed class ProfileEvent {

    sealed class UI : ProfileEvent() {
        object LoadProfile : ProfileEvent()
        object LogoutClick: ProfileEvent()
        object BackPressed : ProfileEvent()
    }

    sealed class Internal : ProfileEvent() {
        class ProfileLoaded(val data: ProfileItem) : ProfileEvent()
        class ErrorLoading(val msg: String) : ProfileEvent()
    }
}

sealed class ProfileEffect {
    class Error(val msg: String): ProfileEffect()
}

sealed class ProfileCommand {
    object LoadProfile : ProfileCommand()
    object Logout: ProfileCommand()
    object OnBackPressed : ProfileCommand()
}