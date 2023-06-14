package com.example.courcework.data.network

interface ApiUrlProvider {
    var apiUrl: String
    var baseUrl: String

    class Impl : ApiUrlProvider {
        override var apiUrl: String = "https://tinkoff-android-spring-2023.zulipchat.com/api/v1/"
        override var baseUrl: String = "https://tinkoff-android-spring-2023.zulipchat.com"
    }
}
