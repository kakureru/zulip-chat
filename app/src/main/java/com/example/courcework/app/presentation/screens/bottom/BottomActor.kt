package com.example.courcework.app.presentation.screens.bottom

import android.view.MenuItem
import com.example.courcework.R
import com.example.courcework.app.presentation.navigation.Screens
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor

class BottomActor(
    private val router: Router
) : Actor<BottomCommand, BottomEvent> {

    override fun execute(command: BottomCommand): Flow<BottomEvent> {
        return when(command) {
            is BottomCommand.SwitchTab -> switchTab(command.menuItem)
            BottomCommand.OpenDefaultTab -> openDefaultTab()
        }
    }

    private fun openDefaultTab(): Flow<BottomEvent> = flow {
        router.replaceScreen(Screens.Channels())
        emit(BottomEvent.Internal.SwitchedToChannels)
    }

    private fun switchTab(menuItem: MenuItem): Flow<BottomEvent> = flow {
        if (menuItem.isChecked.not()) {
            when(menuItem.itemId) {
                R.id.miChannels -> {
                    router.replaceScreen(Screens.Channels())
                    emit(BottomEvent.Internal.SwitchedToChannels)
                }
                R.id.miPeople -> {
                    router.replaceScreen(Screens.People())
                    emit(BottomEvent.Internal.SwitchedToPeople)
                }
                R.id.miProfile -> {
                    router.replaceScreen(Screens.OwnProfile())
                    emit(BottomEvent.Internal.SwitchedToProfile)
                }
            }
        }
    }
}