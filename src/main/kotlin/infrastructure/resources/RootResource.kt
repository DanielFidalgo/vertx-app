package infrastructure.resources

import io.vertx.core.Handler
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single

@Single
class RootResource(router: Router): Resource(router) {

    override fun configure() {
        get("/", getRoot())
    }

    fun getRoot() = Handler<RoutingContext> {
        it.json("asdasdasd")
    }
}