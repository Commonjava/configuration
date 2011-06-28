package org.commonjava.web.config;


public interface ConfigurationSectionListener<T>
{

    void sectionStarted( String name )
        throws ConfigurationException;

    void parameter( String name, String value )
        throws ConfigurationException;

    void sectionComplete( String name )
        throws ConfigurationException;

    T getConfiguration();

}
