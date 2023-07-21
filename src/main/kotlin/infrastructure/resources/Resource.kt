package infrastructure.resources

import io.vertx.core.Handler
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler

abstract class Resource(private val router: Router) {

    abstract fun configure()

    fun get(path:String, handler:Handler<RoutingContext>): Route {
        return router.get(path).handler(handler)
    }

    fun post(path:String, handler:Handler<RoutingContext>): Route {
        return router.post(path).handler(BodyHandler.create()).handler(handler)
    }

    fun put(path:String, handler:Handler<RoutingContext>): Route {
        return router.put(path).handler(BodyHandler.create()).handler(handler)
    }

    fun patch(path:String, handler:Handler<RoutingContext>): Route {
        return router.patch(path).handler(BodyHandler.create()).handler(handler)
    }

    fun delete(path:String, handler:Handler<RoutingContext>): Route {
        return router.delete(path).handler(BodyHandler.create()).handler(handler)
    }
}