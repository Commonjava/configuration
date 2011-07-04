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
package org.commonjava.web.config.dotconf.fixture;

import org.commonjava.web.config.annotation.SectionName;

@SectionName( "object" )
public class Simpleton
{

    private String one;

    private String two;

    public Simpleton()
    {

    }

    public Simpleton( final String one, final String two )
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

    public String getTwo()
    {
        return two;
    }

    public void setTwo( final String two )
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
        final Simpleton other = (Simpleton) obj;
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
