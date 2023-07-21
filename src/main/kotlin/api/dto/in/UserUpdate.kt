package api.dto.`in`

import domain.users.User

data class UserUpdate(val id: String, val name: String? = null, val email: String? = null) {
    fun toDomain(): User {
        return User(id=id ,name=name, email=email)
    }
}
