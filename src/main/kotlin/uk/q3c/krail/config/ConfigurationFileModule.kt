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

import com.google.common.base.Preconditions.checkNotNull
import com.google.inject.AbstractModule
import com.google.inject.multibindings.MapBinder
import java.util.*

/**
 * A Guice module to define configuration files to be loaded into an [ApplicationConfiguration].
 *
 *
 * An [Index] is used to specify the order in which the files are assessed.  A config file will override properties
 * with the same key from a config at a higher index.
 *
 *
 * You can use multiple modules based on this class (or create your own to populate an equivalent MapBinder) and Guice
 * will merge the map binders together. It is up to the developer to ensure that indexes are unique (but do not need to
 * be contiguous).
 *
 *
 * Alternatively, it may be easier to use just one module and specify the files all in one place.
 *
 * @author David Sowerby
 */
open class ConfigurationFileModule : AbstractModule() {
    private lateinit var iniFileConfigs: MapBinder<Index, ConfigFile>
    private val prepIniFileConfigs = HashMap<Index, ConfigFile>()

    override fun configure() {
        iniFileConfigs = MapBinder.newMapBinder(binder(), Index::class.java, ConfigFile::class.java)
        define()
        bindConfigs()
    }

    protected open fun define() {

    }

    /**
     * Override this with calls to [addConfig] to specify the configuration files to use.
     */
    private fun bindConfigs() {
        prepIniFileConfigs.forEach { pty, cfg ->
            iniFileConfigs.addBinding(pty)
                    .toInstance(cfg)
        }
    }

    /**
     * Adds an ini file configuration at the specified index. A config will override properties with the same key from
     * a config at a higher index.  It is up to the developer to ensure that the index is unique.  Indexes do not
     * need to be contiguous
     *
     * @param filename the filename for the config file.  This is relative to the application root unless prepended with "/"
     * @param index the priority of this file (level 0 is at the 'top' - meaning it will override any properties of the same name which exist at index >= 1
     * @param optional if false, a failure will occur if the file is not available / readable
     * @return this for fluency
     * @see InheritingConfiguration
     */
    @Suppress("MemberVisibilityCanBePrivate")
    @JvmOverloads
    fun addConfig(filename: String, index: Int, optional: Boolean = false, fileType: ConfigFileType = ConfigFileType.AUTO): ConfigurationFileModule {
        checkNotNull(filename)
        prepIniFileConfigs[Index(index)] = ConfigFile(filename = filename, isOptional = optional, fileType = fileType)
        return this
    }

}

/**
 * We simply wrap an Int - there were a Kotlin-Java issue with using Kotlin Int directly in [MapBinder],
 * because it translates to a primitive - which cannot be used in a map
 */
data class Index(@Suppress("MemberVisibilityCanBePrivate") val level: Int) : Comparable<Index> {
    override fun compareTo(other: Index): Int {
        return level.compareTo(other.level)
    }
}
