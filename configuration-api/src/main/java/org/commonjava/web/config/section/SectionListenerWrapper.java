package org.commonjava.web.config.section;

import org.commonjava.web.config.ConfigurationException;

/**
 * Created by jdcasey on 3/8/16.
 */
public class SectionListenerWrapper<T>
    implements ConfigurationSectionListener<T>
{
    private ConfigurationSectionListener<T> delegate;

    public SectionListenerWrapper(ConfigurationSectionListener<T> delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public void sectionStarted( String name )
            throws ConfigurationException
    {
        beforeSectionStarted( name );
        delegate.sectionStarted( name );
        afterSectionStarted( name );
    }

    protected void beforeSectionStarted( String name )
    {
    }

    protected void afterSectionStarted( String name )
    {
    }

    @Override
    public void parameter( String name, String value )
            throws ConfigurationException
    {
        beforeParameter( name, value );
        delegate.parameter( name, value );
        afterParameter( name, value );
    }

    protected void afterParameter( String name, String value )
    {
    }

    protected void beforeParameter( String name, String value )
    {
    }

    @Override
    public void sectionComplete( String name )
            throws ConfigurationException
    {
        beforeSectionComplete( name );
        delegate.sectionComplete( name );
        afterSectionComplete( name );
    }

    protected void afterSectionComplete( String name )
    {
    }

    protected void beforeSectionComplete( String name )
    {
    }

    @Override
    public T getConfiguration()
    {
        beforeGetConfiguration();
        T config = delegate.getConfiguration();
        afterGetConfiguration( config );

        return config;
    }

    protected void afterGetConfiguration( T config )
    {
    }

    protected void beforeGetConfiguration()
    {
    }
}
