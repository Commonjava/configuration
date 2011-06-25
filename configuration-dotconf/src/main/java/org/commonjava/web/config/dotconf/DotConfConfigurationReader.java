package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.readLines;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.commonjava.web.config.ConfigurationDispatcher;
import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationReader;

@Named( ".conf" )
public class DotConfConfigurationReader
    implements ConfigurationReader
{

    private static final String DEFAULT_SECTION = "default";

    private final ConfigurationDispatcher dispatch;

    @Inject
    public DotConfConfigurationReader( final ConfigurationDispatcher dispatch )
    {
        this.dispatch = dispatch;
    }

    @Override
    public void loadConfiguration( final InputStream stream )
        throws ConfigurationException
    {
        List<String> lines;
        try
        {
            lines = readLines( stream );
        }
        catch ( final IOException e )
        {
            throw new ConfigurationException( "Failed to read configuration. Error: %s", e, e.getMessage() );
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream out = new PrintStream( baos );

        String sectionName = DEFAULT_SECTION;
        for ( final String line : lines )
        {
            final String trimmed = line.trim();
            if ( trimmed.startsWith( "[" ) && trimmed.endsWith( "]" ) )
            {
                if ( trimmed.length() == 2 )
                {
                    continue;
                }

                out.flush();

                if ( baos.size() > 0 )
                {
                    dispatch.parseSection( sectionName, new ByteArrayInputStream( baos.toByteArray() ) );
                }

                sectionName = trimmed.substring( 1, trimmed.length() - 1 );
                baos.reset();
            }
            else
            {
                out.println( line );
            }
        }

        out.flush();
        if ( baos.size() > 0 )
        {
            dispatch.parseSection( sectionName, stream );
        }

        dispatch.configurationParsed();
    }

}
