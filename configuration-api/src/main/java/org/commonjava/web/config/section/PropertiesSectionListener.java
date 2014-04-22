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
package org.commonjava.web.config.section;

import java.util.Properties;

import org.commonjava.web.config.ConfigurationException;

public class PropertiesSectionListener
    implements ConfigurationSectionListener<Properties>
{

    private Properties parameters;

    @Override
    public void sectionStarted( final String name )
        throws ConfigurationException
    {
        parameters = new Properties();
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
    public Properties getConfiguration()
    {
        return parameters;
    }

}
