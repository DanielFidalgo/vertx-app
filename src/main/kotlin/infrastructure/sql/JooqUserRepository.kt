package infrastructure.sql

import com.github.fidalgotech.jooq.DslContext
import domain.users.User
import domain.users.UserQuery
import domain.users.UserRepository
import infrastructure.sql.mappers.UserMapper.toDomain
import infrastructure.sql.mappers.UserMapper.toInsert
import infrastructure.sql.mappers.UserMapper.toUpdate
import infrastructure.tables.daos.UsersDao
import infrastructure.tables.records.UsersRecord
import infrastructure.tables.references.USERS
import org.jooq.Condition
import org.jooq.impl.DSL
import org.koin.core.annotation.Single

@Single
class JooqUserRepository(private val context: DslContext) : UserRepository {
    val dao = UsersDao(context.writer.configuration())
    override fun insert(domains: Set<User>) {
        context.transactional {
            val current = DSL.using(it)
            val queries = domains.map { d -> d.toInsert(current) }
                                 .map { r ->
                                     current.insertInto(USERS)
                                            .set(r)
                                 }
            current.batch(queries)
                   .execute()
        }
    }

    override fun update(domains: Set<User>) {
        context.transactional {
            val current = DSL.using(it)
            val queries = domains
                .map { d ->
                    val query = current.update(USERS)
                                       .set(d.toUpdate(current))
                    d.id?.let { id -> query.where(USERS.ID.eq(id)) } ?: query
                }
            current.batch(queries)
                   .execute()
        }
    }

    override fun deleteById(ids: Set<String>) {
        context.transactional {
            val current = DSL.using(it)
            current.delete(USERS)
                   .where(USERS.ID.`in`(ids))
                   .execute()
        }
    }

    override fun find(query: UserQuery): List<User> {
        return context.reader
                      .select()
                      .from(USERS)
                      .where(conditions(query))
                      .fetchInto(UsersRecord::class.java)
                      .map { it.toDomain() }
    }

    override fun findByIds(ids: Set<String>): List<User> {
        return find(UserQuery(ids = ids))
    }

    override fun conditions(query: UserQuery): List<Condition> {
        val conditions = mutableListOf<Condition>()
        query.ids?.let { conditions.add(USERS.ID.`in`(it)) }
        return conditions
    }


}