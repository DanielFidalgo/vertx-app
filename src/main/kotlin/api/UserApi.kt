package api

import api.dto.`in`.UserCreate
import api.dto.`in`.UserUpdate
import api.dto.out.User

interface UserApi {
    fun createUser(dto: UserCreate): String
    fun updateUser(dto: UserUpdate)
    fun getUser(id: String): User
}