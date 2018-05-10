# krail-config-api

The API part of a Guice enabled wrapper for Apache Commons Configuration.  When used with the [default implementation](https://github.com/KrailOrg/krail-config), it enables the loading of configuration data from multiple sources, with the priority of those sources set by Guice.

All the features of [CombinedConfiguration](https://commons.apache.org/proper/commons-configuration/apidocs/org/apache/commons/configuration2/CombinedConfiguration.html) are available

## Serialisation

The Apache Commons implementation does not support serialisation. See the [default implementation](https://github.com/KrailOrg/krail-config) for its method for overcoming this.