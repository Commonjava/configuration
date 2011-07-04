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
package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.LINE_SEPARATOR;
import static org.apache.commons.io.IOUtils.writeLines;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.commonjava.web.config.ConfigurationRegistry;
import org.commonjava.web.config.DefaultConfigurationListener;
import org.commonjava.web.config.DefaultConfigurationRegistry;
import org.commonjava.web.config.dotconf.fixture.ListEx;
import org.commonjava.web.config.dotconf.fixture.Simpleton;
import org.commonjava.web.config.dotconf.fixture.SimpletonInt;
import org.commonjava.web.config.section.BeanSectionListener;
import org.junit.Test;

public class DotConfBeanParsingTest
{

    @Test
    public void parseBean_TwoParameters()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new BeanSectionListener<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseBean_TwoParametersCoerceSecondToInteger()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=1" );
        final SimpletonInt check = new SimpletonInt( "foo", 1 );

        checkParse( lines, new BeanSectionListener<SimpletonInt>( SimpletonInt.class ), check );
    }

    @Test
    public void parseBean_TwoParametersWithLastHavingTrailingComment()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar # This is a comment" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new BeanSectionListener<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseBean_TwoParametersWithFirstHavingTrailingComment()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo # This is a comment", "two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new BeanSectionListener<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseBean_TwoParametersWithLeadingCommentLine()
        throws Exception
    {
        final List<String> lines = new ListEx( "# This is a comment header.", "one=foo", "two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new BeanSectionListener<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseBean_TwoParametersWithLeadingEmptyLine()
        throws Exception
    {
        final List<String> lines = new ListEx( "    ", "one=foo", "two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new BeanSectionListener<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseBean_TwoParametersWithPrefixSpacing()
        throws Exception
    {
        final List<String> lines = new ListEx( "  one=foo", "  two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new BeanSectionListener<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseBean_TwoParametersWithColonSeparators()
        throws Exception
    {
        final List<String> lines = new ListEx( "one: foo", "two: bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new BeanSectionListener<Simpleton>( Simpleton.class ), check );
    }

    private void checkParse( final List<String> lines, final BeanSectionListener<?> sectionListener, final Object check )
        throws Exception
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        writeLines( lines, LINE_SEPARATOR, baos );

        final ConfigurationRegistry registry =
            new DefaultConfigurationRegistry(
                                              new DefaultConfigurationListener().with( DotConfConfigurationReader.DEFAULT_SECTION,
                                                                                       sectionListener ) );

        final DotConfConfigurationReader reader = new DotConfConfigurationReader( registry );
        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        final Object result = sectionListener.getConfiguration();

        assertThat( result, equalTo( check ) );
    }

}
