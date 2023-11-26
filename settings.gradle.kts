
rootProject.name = "vertx-app"
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("vertx.openapi", "io.vertx:vertx-web-openapi:4.4.6")
            library("vertx.kotlin", "io.vertx:vertx-lang-kotlin:4.4.6")
            library("vertx.micrometer", "io.vertx:vertx-micrometer-metrics:4.4.6")
            library("vertx.opentelemetry", "io.vertx:vertx-opentelemetry:4.4.6")
            library("koin.core", "io.insert-koin:koin-core:3.5.0")
            library("koin.annotations", "io.insert-koin:koin-annotations-jvm:1.3.0")
            library("koin.ksp", "io.insert-koin:koin-ksp-compiler:1.3.0")
            library("micrometer", "io.micrometer:micrometer-core:1.12.0")
            library("micrometer.prometheus", "io.micrometer:micrometer-registry-prometheus:1.12.0")
            library("log.slf4j", "org.slf4j:slf4j-simple:2.0.5")
            library("log.slf4j.jul", "org.slf4j:jul-to-slf4j:2.0.5")
            library("log.logback", "ch.qos.logback:logback-classic:1.4.8")
            library("log.logback.mdc", "io.opentelemetry.instrumentation:opentelemetry-logback-mdc-1.0:1.18.0-alpha")
            library("kotlinx.serialization.json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            library("kotlinx.serialization.protobuf", "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.6.0")
            library("kotlinx.coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            library("jooq", "org.jooq:jooq:3.18.7")
            library("agroal", "io.agroal:agroal-pool:2.2")
            library("flyway", "org.flywaydb:flyway-core:10.0.1")
            library("sql", "com.github.fidalgotech:sql:231116-023208")
            library("jackson.databind", "com.fasterxml.jackson.core:jackson-databind:2.15.3")
            library("jackson.module.kotlin", "com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
            library("jackson.module.jsr310", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3")
        }

        create("testLibs") {
            library("jupiter", "org.junit.jupiter:junit-jupiter:5.10.1")
            library("jupiter.api", "org.junit.jupiter:junit-jupiter-api:5.10.1")
            library("jupiter.engine", "org.junit.jupiter:junit-jupiter-engine:5.10.1")
        }
    }
}
