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
package org.commonjava.web.config.fixture;

import org.commonjava.web.config.annotation.ConfigName;

public class TestRoot
{
    private String keyOne;

    private String keyTwo;

    public String getKeyOne()
    {
        return keyOne;
    }

    @ConfigName( "key.one" )
    public void setKeyOne( final String keyOne )
    {
        this.keyOne = keyOne;
    }

    public String getKeyTwo()
    {
        return keyTwo;
    }

    @ConfigName( "key.two" )
    public void setKeyTwo( final String keyTwo )
    {
        this.keyTwo = keyTwo;
    }

}
