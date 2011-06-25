package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.LINE_SEPARATOR;
import static org.apache.commons.io.IOUtils.writeLines;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DotConfMapParserTest
{

    @Test
    public void parseTwoParameters()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar" );
        final Map<String, String> check = new MapEx( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    @Test
    public void parseExactlyTwoParameters()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar" );
        final Map<String, String> check = new MapEx( "one", "foo", "two", "bar" );

        checkParse( lines, check, true );
    }

    @Test
    public void parseTwoParametersWithLastHavingTrailingComment()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar # This is a comment" );
        final Map<String, String> check = new MapEx( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    @Test
    public void parseTwoParametersWithFirstHavingTrailingComment()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo # This is a comment", "two=bar" );
        final Map<String, String> check = new MapEx( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    @Test
    public void parseTwoParametersWithLeadingCommentLine()
        throws Exception
    {
        final List<String> lines = new ListEx( "# This is a comment header.", "one=foo", "two=bar" );
        final Map<String, String> check = new MapEx( "one", "foo", "two", "bar" );

        checkParse( lines, check, true );
    }

    @Test
    public void parseTwoParametersWithLeadingEmptyLine()
        throws Exception
    {
        final List<String> lines = new ListEx( "    ", "one=foo", "two=bar" );
        final Map<String, String> check = new MapEx( "one", "foo", "two", "bar" );

        checkParse( lines, check, true );
    }

    @Test
    public void parseTwoParametersWithPrefixSpacing()
        throws Exception
    {
        final List<String> lines = new ListEx( "  one=foo", "  two=bar" );
        final Map<String, String> check = new MapEx( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    @Test
    public void parseTwoParametersWithColonSeparators()
        throws Exception
    {
        final List<String> lines = new ListEx( "one: foo", "two: bar" );
        final Map<String, String> check = new MapEx( "one", "foo", "two", "bar" );

        checkParse( lines, check, false );
    }

    private void checkParse( final List<String> lines, final Map<String, String> check, final boolean strict )
        throws Exception
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        writeLines( lines, LINE_SEPARATOR, baos );

        final DotConfMapParser parser = new DotConfMapParser();
        parser.parse( new ByteArrayInputStream( baos.toByteArray() ) );

        final Map<String, String> result = parser.getConfiguration();

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

    private static final class ListEx
        extends ArrayList<String>
    {
        private static final long serialVersionUID = 1L;

        ListEx( final String... lines )
        {
            for ( final String line : lines )
            {
                add( line );
            }
        }
    }

    private static final class MapEx
        extends HashMap<String, String>
    {
        private static final long serialVersionUID = 1L;

        MapEx( final String... parts )
        {
            if ( parts.length % 2 != 0 )
            {
                throw new IllegalArgumentException( "Must have an even number of arguments to form key = value pairs!" );
            }

            String last = null;
            for ( final String part : parts )
            {
                if ( last == null )
                {
                    last = part;
                }
                else
                {
                    put( last, part );
                    last = null;
                }
            }
        }
    }

}
