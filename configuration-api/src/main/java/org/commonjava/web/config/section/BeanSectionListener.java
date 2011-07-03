/*******************************************************************************
 * Copyright (C) 2011 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
