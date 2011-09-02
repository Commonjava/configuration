/*******************************************************************************
 * Copyright (C) 2011  John Casey
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.  If not, see 
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.commonjava.web.config.section;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xbean.recipe.ObjectRecipe;
import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.annotation.ConfigName;
import org.commonjava.web.config.annotation.ConfigNames;

public class BeanSectionListener<T>
    implements TypedConfigurationSectionListener<T>
{

    private ObjectRecipe recipe;

    private final Class<T> type;

    private final List<String> constructorArgs;

    private final Map<String, String> propertyMap = new HashMap<String, String>();

    public BeanSectionListener( final Class<T> type )
    {
        this.type = type;

        List<String> ctorArgs = null;
        for ( final Constructor<?> ctor : type.getConstructors() )
        {
            final ConfigNames names = ctor.getAnnotation( ConfigNames.class );
            if ( names != null )
            {
                if ( ctorArgs != null )
                {
                    throw new IllegalArgumentException( "Only one constructor can be annotated with @ConfigNames!" );
                }
                else if ( names.value().length != ctor.getParameterTypes().length )
                {
                    throw new IllegalArgumentException(
                                                        "Invalid number of configuration names in @ConfigNames annotation. Expected: "
                                                            + ctor.getParameterTypes().length + ", got: "
                                                            + names.value().length );
                }

                ctorArgs = new ArrayList<String>( Arrays.asList( names.value() ) );
            }
        }

        for ( final Method meth : type.getMethods() )
        {
            final ConfigName cn = meth.getAnnotation( ConfigName.class );

            if ( cn == null )
            {
                continue;
            }

            final String name = meth.getName();

            if ( !( Modifier.isPublic( meth.getModifiers() ) && name.length() > 3 && name.startsWith( "set" ) ) )
            {
                throw new IllegalArgumentException(
                                                    "Invalid configuration method; not accessible, not a setter, or not a valid property name: "
                                                        + type.getClass()
                                                              .getName() + "." + name );
            }

            String propertyName = name.substring( 3 );
            if ( propertyName.length() > 1 )
            {
                propertyName = Character.toLowerCase( propertyName.charAt( 0 ) ) + propertyName.substring( 1 );
            }
            else
            {
                propertyName = propertyName.toLowerCase();
            }

            propertyMap.put( cn.value(), propertyName );
        }

        constructorArgs = ctorArgs;
    }

    @Override
    public void sectionStarted( final String name )
        throws ConfigurationException
    {
        recipe = new ObjectRecipe( type );

        if ( constructorArgs != null && !constructorArgs.isEmpty() )
        {
            recipe.setConstructorArgNames( constructorArgs );
        }
    }

    @Override
    public void parameter( final String name, final String value )
        throws ConfigurationException
    {
        String realName = name;
        if ( ( constructorArgs == null || !constructorArgs.contains( name ) ) && propertyMap.containsKey( name ) )
        {
            realName = propertyMap.get( name );
        }

        recipe.setProperty( realName, value );
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

    @Override
    public String toString()
    {
        return String.format( "BeanSectionListener [%s]", type.getName() );
    }

    @Override
    public Class<T> getConfigurationType()
    {
        return type;
    }

}
