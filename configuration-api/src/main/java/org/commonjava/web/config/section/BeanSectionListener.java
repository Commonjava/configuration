package org.commonjava.web.config.section;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.xbean.recipe.ObjectRecipe;
import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationSectionListener;

public class BeanSectionListener<T>
    implements ConfigurationSectionListener<T>
{

    private ObjectRecipe recipe;

    private final Class<T> type;

    private final LinkedHashMap<String, Class<?>> constructorArgs;

    public BeanSectionListener( final Class<T> type )
    {
        this( type, null );
    }

    public BeanSectionListener( final Class<T> type, final LinkedHashMap<String, Class<?>> constructorArgs )
    {
        this.type = type;
        this.constructorArgs = constructorArgs;
    }

    @Override
    public void sectionStarted( final String name )
        throws ConfigurationException
    {
        recipe = new ObjectRecipe( type );

        if ( constructorArgs != null && !constructorArgs.isEmpty() )
        {
            final List<String> names = new ArrayList<String>();
            final List<Class<?>> types = new ArrayList<Class<?>>();
            for ( final Map.Entry<String, Class<?>> entry : constructorArgs.entrySet() )
            {
                names.add( entry.getKey() );
                types.add( entry.getValue() );
            }

            recipe.setConstructorArgNames( names );
            recipe.setConstructorArgTypes( types );
        }
    }

    @Override
    public void parameter( final String name, final String value )
        throws ConfigurationException
    {
        recipe.setProperty( name, value );
    }

    @Override
    public void sectionComplete( final String name )
        throws ConfigurationException
    {
        // NOP (?)
    }

    @Override
    public T getConfiguration()
    {
        return type.cast( recipe.create() );
    }

}
