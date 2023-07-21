package api.impl

import api.UserApi
import api.dto.`in`.UserCreate
import api.dto.`in`.UserUpdate
import api.dto.out.User
import domain.users.UserService
import org.koin.core.annotation.Single

@Single
class ImplUserApi(private val userService: UserService): UserApi {
    override fun createUser(dto: UserCreate): String {
       return userService.insertUser(dto.toDomain())
    }

    override fun updateUser(dto: UserUpdate) {
        userService.updateUser(dto.toDomain())
    }

    override fun getUser(id: String): User {
        return  User.fromDomain(userService.getUserById(id))
    }
}