package infrastructure.resources

import api.UserApi
import api.dto.`in`.UserCreate
import api.dto.`in`.UserUpdate
import io.vertx.core.Handler
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.koin.core.annotation.Single

@Single
class UserResource(router: Router, private val userApi: UserApi) : Resource(router) {
    override fun configure() {
        post("/users/create", createUser())
        patch("/users/update", updateUser())
        get("/users/:userId", getUser())
    }

    private fun createUser() = Handler<RoutingContext> {
        val body = it.body().asPojo(UserCreate::class.java)
        val userId = userApi.createUser(body)
        it.response().statusCode = 200
        it.end(userId)
    }

    private fun updateUser() = Handler<RoutingContext> {
        val body = it.body().asPojo(UserUpdate::class.java)
        userApi.updateUser(body)
        it.response().statusCode = 204
        it.end()
    }

    private fun getUser() = Handler<RoutingContext> {
        val userId = it.pathParam("userId")
        val user = userApi.getUser(userId)
        it.response().statusCode = 200
        it.json(user)
    }
}