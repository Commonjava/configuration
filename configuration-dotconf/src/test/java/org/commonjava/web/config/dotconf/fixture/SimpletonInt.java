/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.commonjava.web.config.dotconf.fixture;

import org.commonjava.web.config.annotation.ConfigNames;
import org.commonjava.web.config.annotation.SectionName;

@SectionName( "object" )
public class SimpletonInt
{

    private String one;

    private Integer two;

    public SimpletonInt()
    {

    }

    @ConfigNames( { "one", "two" } )
    public SimpletonInt( final String one, final Integer two )
    {
        this.one = one;
        this.two = two;
    }

    public String getOne()
    {
        return one;
    }

    public void setOne( final String one )
    {
        this.one = one;
    }

    public Integer getTwo()
    {
        return two;
    }

    public void setTwo( final Integer two )
    {
        this.two = two;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( one == null ) ? 0 : one.hashCode() );
        result = prime * result + ( ( two == null ) ? 0 : two.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final SimpletonInt other = (SimpletonInt) obj;
        if ( one == null )
        {
            if ( other.one != null )
            {
                return false;
            }
        }
        else if ( !one.equals( other.one ) )
        {
            return false;
        }
        if ( two == null )
        {
            if ( other.two != null )
            {
                return false;
            }
        }
        else if ( !two.equals( other.two ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format( "Simpleton [one=%s, two=%s]", one, two );
    }

}
