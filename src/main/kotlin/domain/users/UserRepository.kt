package domain.users

import domain.Repository

interface UserRepository: Repository<User, UserQuery>