package infrastructure.cdi

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("domain")
class DomainModule {}