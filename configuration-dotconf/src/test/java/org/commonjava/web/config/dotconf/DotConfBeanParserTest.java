package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.LINE_SEPARATOR;
import static org.apache.commons.io.IOUtils.writeLines;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.commonjava.web.config.dotconf.fixture.Simpleton;
import org.commonjava.web.config.dotconf.fixture.SimpletonInt;
import org.junit.Test;

public class DotConfBeanParserTest
{

    @Test
    public void parseTwoParameters()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new DotConfBeanParser<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseTwoParametersCoerceSecondToInteger()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=1" );
        final SimpletonInt check = new SimpletonInt( "foo", 1 );

        checkParse( lines,
                    new DotConfBeanParser<SimpletonInt>( SimpletonInt.class,
                                                         new MapEx<String, Class<?>>().with( "one", String.class )
                                                                                      .with( "two", Integer.class ) ),
                    check );
    }

    @Test
    public void parseTwoParametersWithLastHavingTrailingComment()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo", "two=bar # This is a comment" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new DotConfBeanParser<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseTwoParametersWithFirstHavingTrailingComment()
        throws Exception
    {
        final List<String> lines = new ListEx( "one=foo # This is a comment", "two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new DotConfBeanParser<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseTwoParametersWithLeadingCommentLine()
        throws Exception
    {
        final List<String> lines = new ListEx( "# This is a comment header.", "one=foo", "two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new DotConfBeanParser<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseTwoParametersWithLeadingEmptyLine()
        throws Exception
    {
        final List<String> lines = new ListEx( "    ", "one=foo", "two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new DotConfBeanParser<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseTwoParametersWithPrefixSpacing()
        throws Exception
    {
        final List<String> lines = new ListEx( "  one=foo", "  two=bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new DotConfBeanParser<Simpleton>( Simpleton.class ), check );
    }

    @Test
    public void parseTwoParametersWithColonSeparators()
        throws Exception
    {
        final List<String> lines = new ListEx( "one: foo", "two: bar" );
        final Simpleton check = new Simpleton( "foo", "bar" );

        checkParse( lines, new DotConfBeanParser<Simpleton>( Simpleton.class ), check );
    }

    private void checkParse( final List<String> lines, final DotConfBeanParser<?> parser, final Object check )
        throws Exception
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        writeLines( lines, LINE_SEPARATOR, baos );

        parser.parse( new ByteArrayInputStream( baos.toByteArray() ) );

        final Object result = parser.getConfiguration();

        assertThat( result, equalTo( check ) );
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

    private static final class MapEx<K, V>
        extends LinkedHashMap<K, V>
    {
        private static final long serialVersionUID = 1L;

        MapEx<K, V> with( final K key, final V value )
        {
            put( key, value );
            return this;
        }
    }

}
