/*
 * Copyright (C) 2013 David Sowerby
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.q3c.krail.config

import org.apache.commons.configuration2.Configuration
import uk.q3c.krail.service.Service
import java.util.*

/**
 * Building a full set of configuration properties may require combining inputs from different sources, and then merging them
 * according to some form of "overriding" principle.  An implementation of this interface is responsible for
 * combining these inputs and providing [ApplicationConfiguration] with a single source from which to look up the result
 *
 * Zero or more configuration files are specified via Guice modules (usually sub-classes of [ConfigurationFileModule])
 *
 * 'Overriding' only occurs if both files have a property of the same key
 *
 * Property names can be in a hierarchical form, for example "database.tables.minky"
 */
interface ApplicationConfigurationService : Service {

    /**
     * Loads an [ApplicationConfiguration] instance with data
     */
    fun load(): SortedMap<Index, Configuration>
}
