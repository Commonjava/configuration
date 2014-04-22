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

import java.util.ArrayList;

public class ListEx
    extends ArrayList<String>
{

    private static final long serialVersionUID = 1L;

    public ListEx( final String... lines )
    {
        for ( final String line : lines )
        {
            add( line );
        }
    }
}
