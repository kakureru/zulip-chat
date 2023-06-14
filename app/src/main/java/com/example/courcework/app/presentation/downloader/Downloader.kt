package com.example.courcework.app.presentation.downloader

interface Downloader {
    fun downloadFile(url: String): Long
}