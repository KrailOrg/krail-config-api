package uk.q3c.krail.config

import com.google.inject.Guice
import org.amshove.kluent.shouldEqual
import org.apache.commons.configuration2.INIConfiguration
import org.apache.commons.lang3.SerializationUtils
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.util.testutil.TestResource
import java.net.URISyntaxException

/**
 * Created by David Sowerby on 01 May 2018
 */

object InheritingConfigurationTest : Spek({

    given("we want second and subsequent configuration layers to override earlier layers") {
        val ref = uk.q3c.krail.config.ApplicationConfigurationException("") // to get classloader
        val config1: INIConfiguration = config(ref, "config1.ini")
        val config2: INIConfiguration = config(ref, "config2.ini")
        val config3: INIConfiguration = config(ref, "config3.ini")
        val injector = Guice.createInjector()

        lateinit var configuration: InheritingConfiguration

        beforeEachTest {
            configuration = injector.getInstance(InheritingConfiguration::class.java)
            configuration.addConfiguration(config1)
            configuration.addConfiguration(config2)
            configuration.addConfiguration(config3)
        }

        on("loading 3 configs with overlapping properties") {


            it("later configs override earlier") {
                configuration.getProperty("a.k1").shouldEqual("1-1")
                configuration.getProperty("a.k2").shouldEqual("2-2")
                configuration.getProperty("a.k3").shouldEqual("3-3")

                configuration.getProperty("a.a").shouldEqual("1")
                configuration.getProperty("a.b").shouldEqual("1")
                configuration.getProperty("a.c").shouldEqual("1")
            }

        }

        on("serialisation") {
            SerializationUtils.serialize(configuration)

            it("does not throw exception") {

            }

        }
    }


})


@Throws(ApplicationConfigurationException::class, URISyntaxException::class)
private fun config(ref: Any, filename: String): INIConfiguration {

    val file = TestResource.resource(ref, filename)
    println(file!!.absolutePath)
    return INIConfiguration(file)
}