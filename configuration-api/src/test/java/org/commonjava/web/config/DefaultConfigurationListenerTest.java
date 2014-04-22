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

import org.commonjava.web.config.annotation.SectionName;
import org.commonjava.web.config.fixture.TestRoot;
import org.commonjava.web.config.section.MapSectionListener;
import org.junit.Test;

public class DefaultConfigurationListenerTest
{

    @Test
    public void testAnnotationsUsedIfSectionNameIsNull()
        throws ConfigurationException
    {
        new DefaultConfigurationListener().with( null, TestRoot.class )
                                          .with( null, new TestMapListener() );
    }

    @SectionName( "test" )
    public static final class TestMapListener
        extends MapSectionListener
    {

    }

}
