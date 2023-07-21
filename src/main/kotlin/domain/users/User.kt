package domain.users

import domain.Domain
import java.time.LocalDateTime
import java.util.UUID

data class User(override val id: String? = null,
                val name: String? = null,
                val email: String? = null,
                val created: LocalDateTime? = null): Domain(id) {
    companion object {
        fun create(name: String, email: String): User {
            return User(UUID.randomUUID().toString(), name, email, LocalDateTime.now())
        }
    }
}
