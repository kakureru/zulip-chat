package com.example.courcework.domain

sealed class RequestStatus{
    object Failure : RequestStatus()
    object Success : RequestStatus()
}
