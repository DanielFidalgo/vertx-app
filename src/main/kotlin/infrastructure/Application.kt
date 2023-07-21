package infrastructure

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.fidalgotech.flyway.FlywayMigrate
import infrastructure.cdi.AppModule
import infrastructure.resources.Resource
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.web.Router
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.ksp.generated.module
import org.slf4j.bridge.SLF4JBridgeHandler
import javax.sql.DataSource

class Application : KoinComponent {
    private val vertx: Vertx by inject()
    private val server: HttpServer by inject()
    private val router: Router by inject()
    private val resources: List<Resource> by inject()
    private val dataSource: DataSource by inject(named("writer-ds"))
    init {
        SLF4JBridgeHandler.removeHandlersForRootLogger()
        SLF4JBridgeHandler.install()

        startKoin { modules(AppModule().module) }

        registerCodecModules()

        FlywayMigrate.migrate(dataSource)

        resources.forEach(Resource::configure)
        server.requestHandler(router).listen()
    }

    private fun registerCodecModules() {
        DatabindCodec.mapper().registerKotlinModule()
        DatabindCodec.prettyMapper().registerKotlinModule()
        DatabindCodec.mapper().registerModule(JavaTimeModule())
        DatabindCodec.prettyMapper().registerModule(JavaTimeModule())
    }
}