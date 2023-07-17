package infrastructure.config

import com.github.fidalgotech.JdbcConfig
import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env

class BetaConfig: AppConfig() {
    override fun env(): Env {
        return Env.BETA
    }

    override fun writerConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }

    override fun readerConfig(): JdbcConfig {
        TODO("Not yet implemented")
    }
}