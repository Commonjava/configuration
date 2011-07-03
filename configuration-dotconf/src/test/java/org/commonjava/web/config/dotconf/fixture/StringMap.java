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

import java.util.HashMap;

public class StringMap
    extends HashMap<String, String>
{
    private static final long serialVersionUID = 1L;

    public StringMap( final String... parts )
    {
        if ( parts.length % 2 != 0 )
        {
            throw new IllegalArgumentException( "Must have an even number of arguments to form key = value pairs!" );
        }

        String last = null;
        for ( final String part : parts )
        {
            if ( last == null )
            {
                last = part;
            }
            else
            {
                put( last, part );
                last = null;
            }
        }
    }
}
