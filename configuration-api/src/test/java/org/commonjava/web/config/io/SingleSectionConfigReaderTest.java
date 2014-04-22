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
package org.commonjava.web.config.io;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.fixture.TestChild;
import org.commonjava.web.config.fixture.TestRoot;
import org.commonjava.web.config.section.BeanSectionListener;
import org.junit.Test;

public class SingleSectionConfigReaderTest
{

    @Test
    public void simpleBeanConfiguration()
        throws ConfigurationException, IOException
    {
        final BeanSectionListener<TestRoot> listener = new BeanSectionListener<TestRoot>( TestRoot.class );
        final SingleSectionConfigReader reader = new SingleSectionConfigReader( listener );

        final Properties p = new Properties();
        p.setProperty( "key.one", "valueOne" );
        p.setProperty( "key.two", "valueTwo" );

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        p.store( baos, "" );

        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        final TestRoot result = listener.getConfiguration();

        assertThat( result.getKeyOne(), equalTo( "valueOne" ) );
        assertThat( result.getKeyTwo(), equalTo( "valueTwo" ) );
    }

    @Test
    public void inheritedBeanConfiguration()
        throws ConfigurationException, IOException
    {
        final BeanSectionListener<TestChild> listener = new BeanSectionListener<TestChild>( TestChild.class );

        final SingleSectionConfigReader reader = new SingleSectionConfigReader( listener );

        final Properties p = new Properties();
        p.setProperty( "key.one", "valueOne" );
        p.setProperty( "key.two", "valueTwo" );
        p.setProperty( "key.three", "valueThree" );

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        p.store( baos, "" );

        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        final TestChild result = listener.getConfiguration();

        assertThat( result.getKeyOne(), equalTo( "valueOne" ) );
        assertThat( result.getKeyTwo(), equalTo( "valueTwo" ) );
        assertThat( result.getKeyThree(), equalTo( "valueThree" ) );
    }

}
