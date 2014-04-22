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

import org.commonjava.web.config.ConfigurationException;

public interface ConfigurationSectionListener<T>
{

    static String DEFAULT_SECTION = "default";

    void sectionStarted( String name )
        throws ConfigurationException;

    void parameter( String name, String value )
        throws ConfigurationException;

    void sectionComplete( String name )
        throws ConfigurationException;

    T getConfiguration();

}
