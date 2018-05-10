/*
 *
 *  * Copyright (c) 2016. David Sowerby
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations under the License.
 *
 */

package uk.q3c.krail.config

import org.apache.commons.configuration2.CombinedConfiguration
import java.io.Serializable

/**
 * Holds a full set of configuration properties, possibly from multiple sources (see [ApplicationConfigurationService]), using a "load on demand" approach.
 *
 * This interface follows the Kotlin principle of reducing the use of nulls.  [getPropertyValue] will either throw an exception
 * or return a default value, depending on the signature used.  Both of these methods also invoke [checkLoaded] to make sure the
 * configuration is loaded before attempting to retrieve values - this is particularly relevant after deserialisation, as
 * [CombinedConfiguration] is not Serializable, and has to be reconstructed.
 *
 * Property names can be in a hierarchical (FQN) form, for example "database.tables.minky", see the
 * [Apache Commons Configuration docs](https://commons.apache.org/proper/commons-configuration/userguide/howto_hierarchical.html#Accessing_structured_properties) for more detail.
 *
 * **NOTE:** In order to keep a simple lookup structure, the property name is always a String even if it contains an index to a list.
 * So for example, "tables.table(0).name" is exactly as it looks, a String literal
 *
 *
 * Created by David Sowerby on 15 Jan 2016
 */
interface ApplicationConfiguration : Serializable {
    /**
     * Read-only property indicating whether configuration has been loaded from its sources
     */
    val loaded: Boolean

    /**
     * A composite of all the configuration settings applied
     */
    val combinedConfiguration: CombinedConfiguration

    /**
     * Returns the value for [propertyName], or the [defaultValue] if not found.  Invokes [checkLoaded] to ensure configuration
     * has been loaded before accessing its values
     *
     * */
    fun <T : Any> getPropertyValue(propertyName: String, defaultValue: T): T

    /**
     * Returns the value for [propertyName], or throws a [ConfigurationPropertyNotFoundException] if not found.
     * Invokes [checkLoaded] to ensure configuration has been loaded before accessing its values
     *
     * @throws ConfigurationPropertyNotFoundException if [propertyName] not found
     */
    fun <T : Any> getPropertyValue(valueClass: Class<T>, propertyName: String): T


    /**
     * Clears all properties from the configuration, and marks it as not loaded
     */
    fun clear()

    /**
     * Call this first if you want to use [combinedConfiguration] to retrieve property values
     */
    fun checkLoaded()
}


class ConfigurationPropertyNotFoundException @JvmOverloads constructor(propertyName: String, cause: Exception? = null) : RuntimeException("Configuration property '$propertyName' not found", cause)

class ConfigurationPropertyTypeNotKnown(propertyName: String, propertyType: String) : RuntimeException("Configuration property type '$propertyType' not recognised for '$propertyName'")
