package com.example.courcework.app.presentation.screens.bottom

import com.example.courcework.app.di.bottom.Bottom
import com.github.terrakok.cicerone.Router
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject

class BottomStoreFactory @Inject constructor(
    @Bottom private val router: Router
) {

    @Suppress("UNCHECKED_CAST")
    fun create() = ElmStoreCompat(
        initialState = BottomState.Channels,
        reducer = BottomReducer,
        actor = BottomActor(router)
    ) as ElmStoreCompat<BottomEvent, BottomState, BottomEffect, BottomCommand>
}