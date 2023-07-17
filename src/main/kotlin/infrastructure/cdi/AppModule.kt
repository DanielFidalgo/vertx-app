package infrastructure.cdi

import infrastructure.config.BetaConfig
import infrastructure.config.DevConfig
import infrastructure.config.ProdConfig
import infrastructure.config.StagingConfig
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
            Env.PROD -> ProdConfig()
            Env.BETA -> BetaConfig()
            Env.STAGING -> StagingConfig()
            Env.DEV -> DevConfig()
            else -> StubConfig()
        }
    }
}