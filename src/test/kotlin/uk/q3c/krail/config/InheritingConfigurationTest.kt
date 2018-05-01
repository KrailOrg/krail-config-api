package uk.q3c.krail.config

import com.google.inject.Guice
import org.amshove.kluent.shouldEqual
import org.apache.commons.configuration.ConfigurationException
import org.apache.commons.configuration.HierarchicalINIConfiguration
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
        val ref = uk.q3c.krail.config.ConfigurationException("") // to get classloader
        val config1: HierarchicalINIConfiguration = config(ref, "config1.ini")
        val config2: HierarchicalINIConfiguration = config(ref, "config2.ini")
        val config3: HierarchicalINIConfiguration = config(ref, "config3.ini")
        val injector = Guice.createInjector()

        lateinit var configuration: InheritingConfiguration

        beforeEachTest {
            configuration = injector.getInstance(InheritingConfiguration::class.java)
        }

        on("loading 3 configs with overlapping properties") {

            configuration.addConfiguration(config1)
            configuration.addConfiguration(config2)
            configuration.addConfiguration(config3)


            it("later configs override earlier") {
                configuration.getProperty("a.k1").shouldEqual("1-1")
                configuration.getProperty("a.k2").shouldEqual("2-2")
                configuration.getProperty("a.k3").shouldEqual("3-3")

                configuration.getProperty("a.a").shouldEqual("1")
                configuration.getProperty("a.b").shouldEqual("1")
                configuration.getProperty("a.c").shouldEqual("1")
            }

        }
    }


})


@Throws(ConfigurationException::class, URISyntaxException::class)
private fun config(ref: Any, filename: String): HierarchicalINIConfiguration {

    val file = TestResource.resource(ref, filename)
    println(file!!.absolutePath)
    return HierarchicalINIConfiguration(file)
}