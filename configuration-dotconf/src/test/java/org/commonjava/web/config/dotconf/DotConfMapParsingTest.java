/*******************************************************************************
 * Copyright (C) 2011  John Casey
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.  If not, see 
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.LINE_SEPARATOR;
import static org.apache.commons.io.IOUtils.writeLines;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.commonjava.web.config.ConfigurationRegistry;
import org.commonjava.web.config.DefaultConfigurationListener;
import org.commonjava.web.config.DefaultConfigurationRegistry;
import org.commonjava.web.config.dotconf.fixture.ListEx;
import org.commonjava.web.config.dotconf.fixture.StringMap;
import org.commonjava.web.config.section.ConfigurationSectionListener;
import org.commonjava.web.config.section.MapSectionListener;
import org.junit.Test;

public class DotConfMapParsingTest
{

    @Test
    public void parseMap_TwoParameters()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar" );
        final Map<String, String> check = new StringMap( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    @Test
    public void parseMap_ExactlyTwoParameters()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar" );
        final Map<String, String> check = new StringMap( "one", "foo", "two", "bar" );

        checkParse( lines, check, true );
    }

    @Test
    public void parseMap_TwoParametersWithLastHavingTrailingComment()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar # This is a comment" );
        final Map<String, String> check = new StringMap( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    @Test
    public void parseMap_TwoParametersWithFirstHavingTrailingComment()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo # This is a comment", "two=bar" );
        final Map<String, String> check = new StringMap( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    @Test
    public void parseMap_TwoParametersWithLeadingCommentLine()
        throws Exception
    {
        final List<String> lines = new ListEx( "# This is a comment header.", "one=foo", "two=bar" );
        final Map<String, String> check = new StringMap( "one", "foo", "two", "bar" );

        checkParse( lines, check, true );
    }

    @Test
    public void parseMap_TwoParametersWithLeadingEmptyLine()
        throws Exception
    {
        final List<String> lines = new ListEx( "    ", "one=foo", "two=bar" );
        final Map<String, String> check = new StringMap( "one", "foo", "two", "bar" );

        checkParse( lines, check, true );
    }

    @Test
    public void parseMap_TwoParametersWithPrefixSpacing()
        throws Exception
    {
        final List<String> lines = new ListEx( "  one=foo", "  two=bar" );
        final Map<String, String> check = new StringMap( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    @Test
    public void parseMap_TwoParametersWithColonSeparators()
        throws Exception
    {
        final List<String> lines = new ListEx( "one: foo", "two: bar" );
        final Map<String, String> check = new StringMap( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    private void checkParse( final List<String> lines, final Map<String, String> check, final boolean strict )
        throws Exception
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        writeLines( lines, LINE_SEPARATOR, baos );

        final MapSectionListener sectionListener = new MapSectionListener();

        final ConfigurationRegistry registry =
            new DefaultConfigurationRegistry(
                                              new DefaultConfigurationListener().with( ConfigurationSectionListener.DEFAULT_SECTION,
                                                                                       sectionListener ) );

        final DotConfConfigurationReader reader = new DotConfConfigurationReader( registry );
        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        final Map<String, String> result = sectionListener.getConfiguration();

        for ( final Map.Entry<String, String> entry : check.entrySet() )
        {
            assertThat( "Wrong value for: " + entry.getKey(), result.remove( entry.getKey() ),
                        equalTo( entry.getValue() ) );
        }

        if ( strict )
        {
            assertThat( "Parsed configuration has " + result.size() + " extra parameters: " + result, result.size(),
                        is( 0 ) );
        }
    }

}
