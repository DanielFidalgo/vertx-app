import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    kotlin("jvm") version "1.8.20"
    id("com.google.devtools.ksp") version "1.8.20-1.0.11"
    id("org.graalvm.buildtools.native") version "0.9.21"
    id("com.google.cloud.tools.jib") version "3.3.1"
    id("org.flywaydb.flyway") version "9.20.0"
    id("nu.studer.jooq") version "8.2"
    application
}

group = "io.fidalgotech"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.vertx.openapi)
    implementation(libs.vertx.kotlin)
    implementation(libs.vertx.opentelemetry)
    implementation(libs.vertx.micrometer)
    implementation(libs.micrometer.prometheus)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    implementation(libs.sql)
    implementation(libs.log.slf4j.jul)
    implementation(libs.log.logback)
    runtimeOnly(libs.log.logback.mdc)
    implementation("com.h2database:h2:2.1.214")
    jooqGenerator("com.h2database:h2:2.1.214")
    ksp(libs.koin.ksp)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
    if (project.hasProperty("graalvmAgent")) {
        applicationDefaultJvmArgs = listOf("-agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-config")
    }
}

graalvmNative {

    binaries {
        named("main") {
            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
                vendor.set(JvmVendorSpec.matching("GraalVM Community"))
            })
            mainClass.set("MainKt")
            fallback.set(false)
            buildArgs.add("--report-unsupported-elements-at-runtime")
            buildArgs.add("-H:+ReportExceptionStackTraces")
            configurationFileDirectories.setFrom("src/main/resources/META-INF/native-config")
            resources.autodetect()
        }
    }
    metadataRepository.enabled.set(true)
}

jib {
    if (project.hasProperty("buildNative")) {
        from{
            image = "istio/distroless:latest"
        }
        pluginExtensions {
            pluginExtension {
                implementation = "com.google.cloud.tools.jib.gradle.extension.nativeimage.JibNativeImageExtension"
                properties = mapOf("imageName" to "${rootProject.name}")
            }
        }
    } else {
        from{
            image = "istio/distroless:latest"
        }
    }

    to {
        image = "local/${rootProject.name}:latest"
    }
}

val h2Url by extra("jdbc:h2:file:${buildDir}/jooq;INIT=CREATE SCHEMA IF NOT EXISTS jooq\\;SET SCHEMA jooq;MODE=MySQL;")
val h2User by extra("sa")
val h2Password by extra("")
flyway {
    url = h2Url
    user = h2User
    password = h2Password
}

jooq {
    version.set(libs.jooq.get().versionConstraint.requiredVersion)
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    url = h2Url
                    user = h2User
                    password = h2Password
                    properties.add(org.jooq.meta.jaxb.Property().apply {
                        key = "ssl"
                        value = "true"
                    })
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.h2.H2Database"
                        inputSchema = "JOOQ"
                        excludes = "flyway_schema_history"
                    }
                    generate.apply {
                        isDeprecated = false
                        isPojosAsKotlinDataClasses = true
                        isFluentSetters = true
                        isDaos = true
                        isImplicitJoinPathsAsKotlinProperties = true
                        isKotlinSetterJvmNameAnnotationsOnIsPrefix = true
                        isKotlinNotNullPojoAttributes = false
                        isKotlinNotNullRecordAttributes = true
                        isKotlinNotNullInterfaceAttributes = false
                    }
                    target.apply {
                        packageName = "infrastructure"
                        directory = "${buildDir}/generated/main/kotlin/jooq/"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}
tasks.getByName("generateJooq").dependsOn(tasks.getByName("flywayMigrate"))