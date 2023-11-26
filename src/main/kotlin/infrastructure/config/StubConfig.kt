package infrastructure.config

import com.github.fidalgotech.JdbcConfig
import infrastructure.config.common.AppConfig
import java.time.Duration

class StubConfig: AppConfig() {

    override fun writerConfig(): JdbcConfig {
        return JdbcConfig(
            "org.h2.Driver",
            "jdbc:h2:mem:jooq;INIT=CREATE SCHEMA IF NOT EXISTS jooq\\;SET SCHEMA jooq;MODE=MySQL;",
            "sa",
            "",
            false,
            defaultMinPool,
            defaultMaxPool,
            defaultAcquireIncrement,
            Duration.ofSeconds(defaultIdleTestDuration))
    }

    override fun readerConfig(): JdbcConfig {
        return writerConfig()
    }
}