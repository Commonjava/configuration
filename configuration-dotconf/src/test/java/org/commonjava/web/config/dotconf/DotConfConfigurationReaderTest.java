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
package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.LINE_SEPARATOR;
import static org.apache.commons.io.IOUtils.writeLines;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.commonjava.web.config.ConfigurationRegistry;
import org.commonjava.web.config.DefaultConfigurationListener;
import org.commonjava.web.config.DefaultConfigurationRegistry;
import org.commonjava.web.config.dotconf.fixture.ListEx;
import org.commonjava.web.config.dotconf.fixture.SimpletonInt;
import org.commonjava.web.config.section.BeanSectionListener;
import org.commonjava.web.config.section.MapSectionListener;
import org.junit.Test;

public class DotConfConfigurationReaderTest
{

    @Test
    public void readTwoSectionsOneWithMapParserTheOtherWithBeanParserUsingCoercion()
        throws Exception
    {
        final List<String> lines =
            new ListEx( "[mappings]", "newUser: templates/custom-newUser", "changePassword: templates/change-password", "", "", "[object]",
                        "one=foo", "two: 2" );

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeLines( lines, LINE_SEPARATOR, baos );

        final DefaultConfigurationListener configListener =
            new DefaultConfigurationListener( new BeanSectionListener<SimpletonInt>( SimpletonInt.class ) ).with( "mappings",
                                                                                                                  new MapSectionListener() );

        final ConfigurationRegistry dispatcher = new DefaultConfigurationRegistry( configListener );
        final DotConfConfigurationReader reader = new DotConfConfigurationReader( dispatcher );

        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        assertThat( (String) configListener.getConfiguration( "mappings", Map.class )
                                           .get( "newUser" ), equalTo( "templates/custom-newUser" ) );
        assertThat( (String) configListener.getConfiguration( "mappings", Map.class )
                                           .get( "changePassword" ), equalTo( "templates/change-password" ) );
        assertThat( configListener.getConfiguration( "object", SimpletonInt.class ), equalTo( new SimpletonInt( "foo", 2 ) ) );
    }

    @Test
    public void readTwoSectionsOneWithMapParserTheOtherWithAnnotatedBeanParser()
        throws Exception
    {
        final List<String> lines =
            new ListEx( "[mappings]", "newUser: templates/custom-newUser", "changePassword: templates/change-password", "", "", "[object]",
                        "one=foo", "two: 2" );

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeLines( lines, LINE_SEPARATOR, baos );

        final DefaultConfigurationListener configListener =
            new DefaultConfigurationListener( new BeanSectionListener<SimpletonInt>( SimpletonInt.class ) ).with( "mappings",
                                                                                                                  new MapSectionListener() );

        final ConfigurationRegistry dispatcher = new DefaultConfigurationRegistry( configListener );
        final DotConfConfigurationReader reader = new DotConfConfigurationReader( dispatcher );

        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        assertThat( (String) configListener.getConfiguration( "mappings", Map.class )
                                           .get( "newUser" ), equalTo( "templates/custom-newUser" ) );
        assertThat( (String) configListener.getConfiguration( "mappings", Map.class )
                                           .get( "changePassword" ), equalTo( "templates/change-password" ) );
        assertThat( configListener.getConfiguration( "object", SimpletonInt.class ), equalTo( new SimpletonInt( "foo", 2 ) ) );
    }

    @Test
    public void readOneSectionWithMapParserAndLineContinuation()
        throws Exception
    {
        final List<String> lines = new ListEx( "[mappings]", "newUser: templates/custom-newUser \\" + "\n        Testing" );

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeLines( lines, LINE_SEPARATOR, baos );

        final DefaultConfigurationListener configListener = new DefaultConfigurationListener().with( "mappings", new MapSectionListener() );

        final ConfigurationRegistry dispatcher = new DefaultConfigurationRegistry( configListener );
        final DotConfConfigurationReader reader = new DotConfConfigurationReader( dispatcher );

        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        assertThat( (String) configListener.getConfiguration( "mappings", Map.class )
                                           .get( "newUser" ), equalTo( "templates/custom-newUser Testing" ) );
    }

    @Test
    public void readOneSectionWithMapParserAndCompoundValue()
        throws Exception
    {
        final String testValue = "templates/custom-newUser;test=true";
        final List<String> lines = new ListEx( "[mappings]", "newUser=" + testValue );

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeLines( lines, LINE_SEPARATOR, baos );

        final DefaultConfigurationListener configListener =
            new DefaultConfigurationListener().with( "mappings", new MapSectionListener() );

        final ConfigurationRegistry dispatcher = new DefaultConfigurationRegistry( configListener );
        final DotConfConfigurationReader reader = new DotConfConfigurationReader( dispatcher );

        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        assertThat( (String) configListener.getConfiguration( "mappings", Map.class )
                                           .get( "newUser" ), equalTo( testValue ) );
    }

}
