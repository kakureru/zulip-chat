package com.example.courcework.data.network.model.users

import com.google.gson.annotations.SerializedName

class AllUsersResponse(
    @SerializedName("members") val users: List<UserDto>
)