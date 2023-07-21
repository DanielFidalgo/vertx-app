package domain.users

interface UserService {
    fun insertUser(user: User): String
    fun updateUser(user: User)
    fun getUserById(id: String): User
}