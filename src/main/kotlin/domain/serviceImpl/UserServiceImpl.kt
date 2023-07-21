package domain.serviceImpl

import domain.users.User
import domain.users.UserRepository
import domain.users.UserService
import org.koin.core.annotation.Single

@Single
class UserServiceImpl(private val userRepository: UserRepository): UserService {

    override fun insertUser(user: User): String {
        userRepository.insert(user)
        return user.id!!
    }

    override fun updateUser(user: User) {
        userRepository.update(user)
    }

    override fun getUserById(id: String): User {
        return userRepository.findById(id)!!
    }

}