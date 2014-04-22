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

public class TestChild
    extends TestRoot
{
    private String keyThree;

    public String getKeyThree()
    {
        return keyThree;
    }

    @ConfigName( "key.three" )
    public void setKeyThree( final String keyThree )
    {
        this.keyThree = keyThree;
    }

}
