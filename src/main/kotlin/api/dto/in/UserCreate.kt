package api.dto.`in`

import domain.users.User

data class UserCreate(val name:String, val email: String) {

    fun toDomain(): User{
        return User.create(name, email)
    }
}
