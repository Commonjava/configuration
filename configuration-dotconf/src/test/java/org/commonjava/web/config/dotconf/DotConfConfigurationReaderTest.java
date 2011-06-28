package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.LINE_SEPARATOR;
import static org.apache.commons.io.IOUtils.writeLines;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationListener;
import org.commonjava.web.config.ConfigurationRegistry;
import org.commonjava.web.config.ConfigurationSectionListener;
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
            new ListEx( "[mappings]", "newUser: templates/custom-newUser", "changePassword: templates/change-password",
                        "", "", "[object]", "one=foo", "two: 2" );

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeLines( lines, LINE_SEPARATOR, baos );

        final TestConfigListener configListener =
            new TestConfigListener().add( "mappings", new MapSectionListener() )
                                    .add( "object", new BeanSectionListener<SimpletonInt>( SimpletonInt.class ) );

        final ConfigurationRegistry dispatcher = new DefaultConfigurationRegistry( configListener );
        final DotConfConfigurationReader reader = new DotConfConfigurationReader( dispatcher );

        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        assertThat( (String) configListener.getSection( "mappings", Map.class )
                                           .get( "newUser" ), equalTo( "templates/custom-newUser" ) );
        assertThat( (String) configListener.getSection( "mappings", Map.class )
                                           .get( "changePassword" ), equalTo( "templates/change-password" ) );
        assertThat( configListener.getSection( "object", SimpletonInt.class ), equalTo( new SimpletonInt( "foo", 2 ) ) );
    }

    private static final class TestConfigListener
        implements ConfigurationListener
    {

        private final Map<String, ConfigurationSectionListener<?>> parsers =
            new HashMap<String, ConfigurationSectionListener<?>>();

        private final Map<String, Object> sections = new HashMap<String, Object>();

        TestConfigListener add( final String section, final ConfigurationSectionListener<?> parser )
        {
            parsers.put( section, parser );
            return this;
        }

        @Override
        public Map<String, ConfigurationSectionListener<?>> getSectionListeners()
        {
            return parsers;
        }

        @Override
        public void configurationComplete()
            throws ConfigurationException
        {
            for ( final String section : parsers.keySet() )
            {
                final ConfigurationSectionListener<?> parser = parsers.get( section );
                sections.put( section, parser.getConfiguration() );
            }
        }

        <T> T getSection( final String section, final Class<T> type )
        {
            return type.cast( sections.get( section ) );
        }

    }
}
