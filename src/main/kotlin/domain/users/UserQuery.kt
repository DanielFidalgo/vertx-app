package domain.users

import domain.Query

data class UserQuery(override val ids: Set<String>? = null): Query(ids) {}