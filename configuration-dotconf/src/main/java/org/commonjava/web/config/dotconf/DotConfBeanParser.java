package org.commonjava.web.config.dotconf;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.xbean.recipe.ObjectRecipe;
import org.commonjava.web.config.ConfigurationException;

public class DotConfBeanParser<T>
    extends AbstractDotConfParser<T>
{

    private final ObjectRecipe recipe;

    private final Class<T> type;

    public DotConfBeanParser( final Class<T> type )
    {
        this( type, false, null );
    }

    public DotConfBeanParser( final Class<T> type, final boolean saveRaw )
    {
        this( type, saveRaw, null );
    }

    public DotConfBeanParser( final Class<T> type, final LinkedHashMap<String, Class<?>> constructorArgs )
    {
        this( type, false, constructorArgs );
    }

    public DotConfBeanParser( final Class<T> type, final boolean saveRaw,
                              final LinkedHashMap<String, Class<?>> constructorArgs )
    {
        super( saveRaw );

        this.type = type;
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
    public T getConfiguration()
    {
        return type.cast( recipe.create() );
    }

    /**
     * By overriding in conjunction with {@link DotConfBeanParser#initialize()}, this method allows customization of the
     * {@link ObjectRecipe} instance prior to parsing...
     */
    protected final ObjectRecipe getRecipe()
    {
        return recipe;
    }

    @Override
    protected void initialize()
    {
        // done in ctor.
    }

    @Override
    protected void addParameter( final String key, final String value )
        throws ConfigurationException
    {
        recipe.setProperty( key, value );
    }

}
