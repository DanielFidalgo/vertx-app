package infrastructure.cdi

import infrastructure.config.EnvConfig
import infrastructure.config.StubConfig
import infrastructure.config.common.AppConfig
import infrastructure.config.common.Env
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [ApiModule::class, DomainModule::class, InfrastructureModule::class])
class AppModule {
    @Single
    fun appConfig(): AppConfig {
        val env = System.getenv("ENV")
        return when (Env.getByLabel(env)) {
            Env.PROD,
            Env.BETA,
            Env.STAGING,
            Env.DEV -> EnvConfig()

            else -> StubConfig()
        }
    }
}