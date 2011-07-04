package org.commonjava.web.config.section;

public interface TypedConfigurationSectionListener<T>
    extends ConfigurationSectionListener<T>
{

    Class<T> getConfigurationType();

}
