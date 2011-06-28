package org.commonjava.web.config;

public interface ConfigurationRegistry
{

    void configurationParsed()
        throws ConfigurationException;

    boolean sectionStarted( final String name )
        throws ConfigurationException;

    void sectionComplete( final String name )
        throws ConfigurationException;

    void parameter( final String section, final String name, final String value )
        throws ConfigurationException;

}