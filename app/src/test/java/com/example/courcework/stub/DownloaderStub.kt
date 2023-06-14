package com.example.courcework.stub

import com.example.courcework.app.presentation.downloader.Downloader

class DownloaderStub : Downloader {
    override fun downloadFile(url: String): Long {
        return -1
    }
}