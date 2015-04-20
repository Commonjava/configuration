---
---

### Stretch Out on Our Sectional

The Configuration API provides a means of reading single- and multi-section configuration files and injecting their values into Java configuration objects. This allows your application to include add-ons or plugins flexibly, and configure them as part of the main application configuration. Each part of your application will receive its configuration and not have to know anything about any other part.

### Annotations

* `@SectionName("somepart")` denotes a configuration class that is responsible for managing the `somepart` section of your application's configuration file.
* `@ConfigName("my.email")` denotes the setter method that should be used to inject the `my.email` configuration field into your configuration class.
* `@ConfigNames({"field.one", "field.two"})` denotes a constructor in your configuration class which will have two configuration fields (`field.one` and `field.two`) injected as parameters (in order, of course).

### Simple is Good, Too

Don't need multiple sections in your configuration? That's okay, too. Simply leave off the `@SectionName` annotation for your configuration class, and the fields in the configuration file without a declared section will be injected. If you want to make sure the configuration reader doesn't even try to load multiple sections, use something like `org.commonjava.web.config.io.SingleSectionConfigReader` to read your config file.

### `.conf` Goodness

The Configuration project also contains a module - `configuration-dotconf` - which knows how to read .ini-style configurations. These are often named `something.conf` in linux, hence the module name. The configuration reader in this module is `org.commonjava.web.config.dotconf.DotConfConfigurationReader`, and it works with files like this:

    [main]
    port=8080
    bind=0.0.0.0
    
    [auth]
    mutual.ssl=enabled

### All-Inclusive

Many management systems like Puppet and Chef work much better when each recipe has an associated file, rather than having multiple recipes that all modify a single file. This is what makes applications like Apache HTTPd simpler to manage; it's use of `/etc/httpd/conf.d` means each virtual host or module activation can have its own file.

Your application's configuration can be managed in the same way with `org.commonjava.web.config.io.ConfigFileUtils`. Simply add a line like `Include conf.d/*.conf` to your application's configuration, and then use the method `ConfigFileUtils.readFileWithIncludes(File)` to get an `InputStream` that contains the main file's content along with all matching files in the `conf.d/` directory matching the given pattern. The merged contents will replace the original `Include ...` line.

### Existing Beans, New Beans, Maps and Even Properties Sections

Sometimes it's easier to populate an existing object, other times it's simpler to let the configuration API create the configuration object instances for you. The Configuration API contains section listeners for both cases.

And at other times, you need something more free-form. Sometimes it's just simpler to create a map of options and let your application deal with the flexibility of an arbitrary key-value map for a particular configuration section. Or a Properties instance. Configuration API can handle these cases, too.

The Configuration API includes the following section listener implementations:

* `org.commonjava.web.config.section.BeanSectionListener<T>` - Populate the fields of an existing bean, or creates a new bean from the provided configuration parameters.
* `org.commonjava.web.config.section.MapSectionListener` - Populates a new `java.util.Map` instance from the provided configuration parameters.
* `org.commonjava.web.config.section.PropertiesSectionListener` - Populates a new `java.util.Properties` instance from the provided configuration parameters.

Or, you can provide your own implementation of `org.commonjava.web.config.section.ConfigurationSectionListener<T>`.

### Example

Here's an example that reads a multi-section configuration which can be spread into multiple, included configuration files:

    public class ConfigFactory
        extends DefaultConfigurationListener
    {
        private Set<TypedConfigurationSectionListener<?>> sections;
        public ConfigFactory(
                  Set<TypedConfigurationSectionListener<?>> sections )
        {
            this.sections = sections;
        }
        
        public void load( final String configPath )
            throws ConfigurationException
        {
            for ( TypedConfigurationSectionListener<?> section : sections )
            {
                with( section.getSectionName(), 
                      section.getConfigurationClass() );
            }
            
            File configFile = new File( configPath );
            if ( configFile.isDirectory() )
            {
                configFile = new File( configFile, "main.conf" );
            }
            
            InputStream stream = null;
            try
            {
                stream = ConfigFileUtils.readFileWithIncludes( config );
                new DotConfConfigurationReader( this )
                        .loadConfiguration( stream );
            }
            catch ( final IOException e )
            {
                throw new ConfigurationException( 
                    "Cannot open configuration file: %s. Reason: %s", 
                    e, configPath, e.getMessage() );
            }
            finally
            {
                closeQuietly( stream );
            }
        }
    }
