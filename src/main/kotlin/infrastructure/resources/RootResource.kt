package infrastructure.resources

import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.vertx.core.Handler
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.Json
import io.vertx.kotlin.core.json.obj
import io.vertx.micrometer.backends.BackendRegistries
import org.koin.core.annotation.Single
import java.time.Instant
import java.util.logging.Logger

@Single
class RootResource(router: Router) : Resource(router) {

    override fun configure() {
        get("/", getRoot())
        get("/ping", pong())
        get("/health-check", metrics())
    }

    private fun getRoot() = Handler<RoutingContext> {
        Logger.getGlobal()
            .info("this is the root")
        
    }

    private fun pong() = Handler<RoutingContext> {
        it.json(
            Json.obj(
                "date" to Instant.now(),
                "response" to "pong"
            )
        )
    }

    private fun metrics() = Handler<RoutingContext> {
        val registry = BackendRegistries.getDefaultNow() as CompositeMeterRegistry
        val prometheus = registry.registries
            .first(PrometheusMeterRegistry::class::isInstance) as PrometheusMeterRegistry
        it.end(prometheus.scrape())
    }
}