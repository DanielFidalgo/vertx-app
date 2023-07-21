package api.dto.out

import java.time.LocalDateTime
import domain.users.User as Domain

data class User(val id: String,
                val name: String,
                val email: String,
                val created: LocalDateTime) {
    companion object{
        fun fromDomain(domain: Domain): User {
            return User(domain.id!!, domain.name!!, domain.email!!, domain.created!!);
        }
    }
}
