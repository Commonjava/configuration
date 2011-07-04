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

import java.util.LinkedHashMap;
import java.util.Map;

import org.commonjava.web.config.ConfigurationException;

public class MapSectionListener
    implements ConfigurationSectionListener<Map<String, String>>
{

    private Map<String, String> parameters;

    @Override
    public void sectionStarted( final String name )
        throws ConfigurationException
    {
        parameters = new LinkedHashMap<String, String>();
    }

    @Override
    public void parameter( final String name, final String value )
        throws ConfigurationException
    {
        parameters.put( name, value );
    }

    @Override
    public void sectionComplete( final String name )
        throws ConfigurationException
    {
        // NOP.
    }

    @Override
    public Map<String, String> getConfiguration()
    {
        return parameters;
    }

}
