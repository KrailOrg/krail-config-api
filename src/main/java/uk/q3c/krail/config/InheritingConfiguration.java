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
package uk.q3c.krail.config;

import com.google.inject.Inject;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Changes the standard behaviour of {@link CombinedConfiguration}, so that the configurations can be overridden by a
 * configuration added later. So for example, config1 is added first and contains a property "a.k1=1" and config2 is
 * then added with a property "a.k1=2", then a value of 2 will be returned by a call to <i>getProperty("a.k1")</i>
 * <p/>
 *
 * The behaviour of this class has changed in order to make it Serializable - originally it was sub-classed
 * from {@link CompositeConfiguration}, but that was not Serializable.  {@link CombinedConfiguration}, is Serializable,
 * but does not use a memory layer

 *
 * @author David Sowerby 02 May 2018
 * @author David Sowerby
 */

public class InheritingConfiguration extends CombinedConfiguration {

    @Inject
    protected InheritingConfiguration() {
        super();
    }

    /**
     * Read property from underlying composite
     *
     * @param key key to use for mapping
     * @return object associated with the given configuration key.
     */
    @Override
    public Object getProperty(String key) {
        checkNotNull(key);
        Configuration firstMatchingConfiguration = getSourceUsed(key);

        if (firstMatchingConfiguration != null) {
            return firstMatchingConfiguration.getProperty(key);
        } else {
            return null;
        }
    }

    /**
     * Returns the source (the configuration object) actually used to fulfil the value of {@code key}, or null if there
     * is no matching key. This is different to the {@link CombinedConfiguration#getSource(String)} behaviour.
     *
     * @param key the property key to look for
     * @return the Configuration object used to provide the value for the key, or null if the key is not found
     */
    public Configuration getSourceUsed(String key) {
        checkNotNull(key);
        Configuration firstMatchingConfiguration = null;
        int c = getNumberOfConfigurations();
        for (int i = c - 1; i >= 0; i--) {
            Configuration config = getConfiguration(i);
            if (config.containsKey(key)) {
                firstMatchingConfiguration = config;
                break;
            }
        }
        return firstMatchingConfiguration;
    }

    /**
     * Returns a section specified by {@code sectionName}, or null if none exists. Sections are recognised only be
     * {@link HierarchicalINIConfiguration}, and any other type of configuration contained within this composite will
     * be ignored.
     *
     * There is a bug in this - it returns the first section it finds that matches {@code name}.  It should return a merge
     * of all appropriate configs, see https://github.com/KrailOrg/krail-config-api/issues/3
     *
     * @param sectionName the sectionName to return
     * @return the section if found, null if not
     */
    public SubnodeConfiguration getSection(String sectionName) {
        checkNotNull(sectionName);
        int c = getNumberOfConfigurations();
        for (int i = c - 1; i >= 0; i--) {
            Configuration cfg = getConfiguration(i);
            if (cfg instanceof HierarchicalINIConfiguration) {
                HierarchicalINIConfiguration config = (HierarchicalINIConfiguration) cfg;
                if (config.getSections()
                        .contains(sectionName)) {
                    return config.getSection(sectionName);
                }
            }
        }
        return null;
    }

}
