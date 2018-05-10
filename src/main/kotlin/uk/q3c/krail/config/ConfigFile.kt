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

import uk.q3c.krail.config.ConfigFileType.AUTO
import java.io.Serializable

/**
 * Defines a configuration file to be used with [ApplicationConfiguration]
 *
 * The filename is considered to be relative to the application root, unless prepended with "/"
 *
 * @author David Sowerby
 */
data class ConfigFile @JvmOverloads constructor(val filename: String, val fileType: ConfigFileType = AUTO, val isOptional: Boolean = false) : Serializable

/**
 * The type of configuration file to be used.  [AUTO] expects the application to detect the file type automatically
 */
enum class ConfigFileType { AUTO, INI, YAML, XML, JSON }
