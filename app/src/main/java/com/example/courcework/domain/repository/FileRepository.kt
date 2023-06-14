package com.example.courcework.domain.repository

interface FileRepository {
    suspend fun uploadFile(uri: String, fileName: String): String
    fun downloadFile(url: String)
}