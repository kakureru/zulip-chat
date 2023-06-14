package com.example.courcework.app.presentation.screens.bottom

import android.view.MenuItem

sealed class BottomState() {
    object Channels : BottomState()
    object People : BottomState()
    object Profile : BottomState()
}

sealed class BottomEvent {

    sealed class UI {
        object OpenDefaultTab : BottomEvent()
        class BottomNavClick(val menuItem: MenuItem) : BottomEvent()
    }

    sealed class Internal {
        object SwitchedToChannels : BottomEvent()
        object SwitchedToPeople : BottomEvent()
        object SwitchedToProfile : BottomEvent()
    }
}

sealed class BottomEffect {

}

sealed class BottomCommand {
    object OpenDefaultTab : BottomCommand()
    class SwitchTab(val menuItem: MenuItem) : BottomCommand()
}