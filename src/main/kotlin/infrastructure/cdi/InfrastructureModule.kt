package infrastructure.cdi

import com.github.fidalgotech.datasources.DatasourceFactory
import com.github.fidalgotech.jooq.DslContext
import infrastructure.config.common.AppConfig
import infrastructure.resources.Resource
import infrastructure.resources.RootResource
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import javax.sql.DataSource

@Module
@ComponentScan("Infrastructure")
class InfrastructureModule {

    @Single
    fun vertx(): Vertx {
        val options = VertxOptions();
        options.preferNativeTransport = true
        return Vertx.vertx(options)
    }

    @Single
    fun router(vertx: Vertx): Router {
        return Router.router(vertx)
    }

    @Single
    fun server(appConfig: AppConfig, vertx: Vertx): HttpServer {
        val options = HttpServerOptions()
        options.isTcpFastOpen = true
        options.isTcpNoDelay = true
        options.isTcpQuickAck = true
        options.port = appConfig.port()
        return vertx.createHttpServer(options)
    }

    @Single
    fun resources(rootResource: RootResource): List<Resource> {
        return listOf(rootResource)
    }

    @Single
    @Named("writer-ds")
    fun writer(appConfig: AppConfig): DataSource {
        return DatasourceFactory.AGROAL.create(appConfig.writerConfig())
    }

    @Single
    @Named("reader-ds")
    fun reader(appConfig: AppConfig): DataSource {
        return DatasourceFactory.AGROAL.create(appConfig.readerConfig())
    }

    @Single
    fun dslContext(@Named("writer-ds") writer: DataSource,
                   @Named("reader-ds") reader: DataSource,
                   appConfig: AppConfig) :DslContext {
        return DslContext(writer, reader, appConfig.sqlDialect())
    }
}