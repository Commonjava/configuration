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
package org.commonjava.web.config;

public interface ConfigurationRegistry
{

    void configurationParsed()
        throws ConfigurationException;

    boolean sectionStarted( final String name )
        throws ConfigurationException;

    void sectionComplete( final String name )
        throws ConfigurationException;

    void parameter( final String section, final String name, final String value )
        throws ConfigurationException;

}
