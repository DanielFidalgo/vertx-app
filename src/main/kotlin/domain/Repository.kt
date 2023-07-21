package domain

interface Repository<T: Domain, in Q: Query> {
    fun insert(domains: Set<T>)
    fun insert(vararg domains: T) {
        insert(domains.toSet())
    }
    fun insert(domain: T) {
        insert(setOf(domain))
    }

    fun update(domains: Set<T>)
    fun update(vararg domains: T) {
        update(domains.toSet())
    }
    fun update(domain: T) {
        update(setOf(domain))
    }

    fun deleteById(ids: Set<String>)
    fun deleteById(vararg ids: String) {
        deleteById(ids.toSet())
    }
    fun deleteById(id: String) {
        deleteById(setOf(id))
    }

    fun delete(domains: Set<T>) {
        deleteById(domains.map { it.id!! }.toSet())
    }
    fun delete(vararg domains: T) {
        delete(domains.toSet())
    }
    fun delete(domain: T) {
        deleteById(setOf(domain.id!!))
    }

    fun find(query: Q): List<T>
    fun findFirst(query: Q): T? {
        return find(query).firstOrNull()
    }

    fun findByIds(ids: Set<String>): List<T>
    fun findById(id: String): T? {
        return findByIds(setOf(id)).firstOrNull()
    }

    fun conditions(query: Q): List<Any>
}