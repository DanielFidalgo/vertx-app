
rootProject.name = "vertx-app"
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("vertx.openapi", "io.vertx:vertx-web-openapi:4.4.4")
            library("vertx.kotlin", "io.vertx:vertx-lang-kotlin:4.4.4")
            library("koin.core", "io.insert-koin:koin-core:3.4.2")
            library("koin.annotations", "io.insert-koin:koin-annotations-jvm:1.2.2")
            library("koin.ksp", "io.insert-koin:koin-ksp-compiler:1.2.2")
            library("micrometer", "io.micrometer:micrometer-core:1.11.1")
            library("micrometer.prometheus", "io.micrometer:micrometer-registry-prometheus:1.11.1")
            library("slf4j", "org.slf4j:slf4j-simple:2.0.5")
            library("kotlinx.serialization.json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            library("kotlinx.serialization.protobuf", "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.5.1")
            library("kotlinx.coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
            library("jooq", "org.jooq:jooq:3.18.5")
            library("agroal", "io.agroal:agroal-pool:2.2")
            library("flyway", "org.flywaydb:flyway-core:9.20.0")
            library("sql", "com.github.fidalgotech:sql:230717-031827")
        }

        create("testLibs") {
            library("jupiter", "org.junit.jupiter:junit-jupiter:5.9.2")
            library("jupiter.api", "org.junit.jupiter:junit-jupiter-api:5.9.2")
            library("jupiter.engine", "org.junit.jupiter:junit-jupiter-engine:5.9.2")
        }
    }
}
