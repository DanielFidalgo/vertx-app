package infrastructure.config

import com.github.fidalgotech.JdbcConfig
import infrastructure.config.common.AppConfig

class EnvConfig : AppConfig() {
    override fun writerConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }

    override fun readerConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }
}