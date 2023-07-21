package infrastructure.sql.mappers

import domain.users.User
import infrastructure.tables.records.UsersRecord
import infrastructure.tables.references.USERS
import org.jooq.DSLContext

object UserMapper {
    fun User.toInsert(context: DSLContext): UsersRecord {
        var record = context.newRecord(USERS)
        this.id?.let { record.id = it } ?: throw IllegalArgumentException("Id cannot be null!")
        this.name?.let { record.name = it } ?: throw IllegalArgumentException("Name cannot be null!")
        this.email?.let { record.email = it } ?: throw IllegalArgumentException("Email cannot be null!")
        this.created?.let { record.created = it } ?: throw IllegalArgumentException("Created cannot be null!")
        return record;
    }

    fun User.toUpdate(context: DSLContext): UsersRecord {
        var record = context.newRecord(USERS)
        this.name?.let { record.name = it }
        this.email?.let { record.email = it }
        return record;
    }

    fun UsersRecord.toDomain(): User {
        return User(this.id, this.name, this.email, this.created)
    }
}