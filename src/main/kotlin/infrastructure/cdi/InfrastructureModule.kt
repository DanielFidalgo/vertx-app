package infrastructure.cdi

import com.github.fidalgotech.datasources.DatasourceFactory
import com.github.fidalgotech.jooq.DslContext
import infrastructure.config.common.AppConfig
import infrastructure.resources.Resource
import infrastructure.resources.RootResource
import infrastructure.resources.UserResource
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.web.Router
import io.vertx.micrometer.MicrometerMetricsOptions
import io.vertx.tracing.opentelemetry.OpenTelemetryOptions
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.io.File
import javax.sql.DataSource


@Module
@ComponentScan("Infrastructure")
class InfrastructureModule {

    @Single
    fun openTelemetry(): OpenTelemetry {
        val sdkTracerProvider = SdkTracerProvider.builder()
            .build()
        return OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .buildAndRegisterGlobal()
    }

    @Single
    fun micrometer(appConfig: AppConfig): MeterRegistry {
        val registry = CompositeMeterRegistry()
        registry.add(PrometheusMeterRegistry(PrometheusConfig.DEFAULT))
        val tags = Tags.of("service", appConfig.name(), "env", appConfig.env().label)
        ClassLoaderMetrics(tags).bindTo(registry)
        JvmMemoryMetrics(tags).bindTo(registry)
        JvmGcMetrics(tags).bindTo(registry)
        JvmThreadMetrics(tags).bindTo(registry)
        UptimeMetrics(tags).bindTo(registry)
        ProcessorMetrics(tags).bindTo(registry)
        DiskSpaceMetrics(File(System.getProperty("user.dir")), tags).bindTo(registry)
        return registry
    }

    @Single
    fun vertx(openTelemetry: OpenTelemetry, meterRegistry: MeterRegistry): Vertx {
        val options = VertxOptions()
        options.preferNativeTransport = true
        options.tracingOptions = OpenTelemetryOptions(openTelemetry)
        options.metricsOptions = MicrometerMetricsOptions().setMicrometerRegistry(meterRegistry).setEnabled(true)
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
    fun resources(rootResource: RootResource, userResource: UserResource): List<Resource> {
        return listOf(rootResource, userResource)
    }

    @Single
    @Named("writer-ds")
    fun writer(appConfig: AppConfig): DataSource {
        return DatasourceFactory.AGROAL
            .create(appConfig.writerConfig())
    }

    @Single
    @Named("reader-ds")
    fun reader(appConfig: AppConfig): DataSource {
        return DatasourceFactory.AGROAL
            .create(appConfig.readerConfig())
    }

    @Single
    fun dslContext(@Named("writer-ds") writer: DataSource,
                   @Named("reader-ds") reader: DataSource,
                   appConfig: AppConfig) :DslContext {
        return DslContext(writer, reader, appConfig.sqlDialect())
    }
}