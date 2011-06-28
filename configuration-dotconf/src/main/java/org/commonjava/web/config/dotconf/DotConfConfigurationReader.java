package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.readLines;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationReader;
import org.commonjava.web.config.ConfigurationRegistry;

@Named( ".conf" )
public class DotConfConfigurationReader
    implements ConfigurationReader
{

    public static final String DEFAULT_SECTION = "default";

    private final ConfigurationRegistry dispatch;

    private final Pattern parameter;

    @Inject
    public DotConfConfigurationReader( final ConfigurationRegistry dispatch )
    {
        this.dispatch = dispatch;
        parameter = Pattern.compile( "\\s*([-._a-zA-Z0-9]+)\\s*[:=]\\s*([^\\s#]+)(\\s*#.*)?" );
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

        String sectionName = DEFAULT_SECTION;
        boolean processSection = dispatch.sectionStarted( sectionName );
        for ( final String line : lines )
        {
            final String trimmed = line.trim();
            if ( trimmed.startsWith( "[" ) && trimmed.endsWith( "]" ) )
            {
                if ( trimmed.length() == 2 )
                {
                    continue;
                }

                dispatch.sectionComplete( sectionName );
                sectionName = trimmed.substring( 1, trimmed.length() - 1 );
                processSection = dispatch.sectionStarted( sectionName );
            }
            else if ( processSection )
            {
                final Matcher matcher = parameter.matcher( line );
                if ( matcher.matches() )
                {
                    final String key = matcher.group( 1 );
                    final String value = matcher.group( 2 );
                    dispatch.parameter( sectionName, key, value );
                }
            }
        }

        dispatch.sectionComplete( sectionName );
        dispatch.configurationParsed();
    }

}
