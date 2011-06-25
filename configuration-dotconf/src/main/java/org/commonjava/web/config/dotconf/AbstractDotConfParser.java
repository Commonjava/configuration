package org.commonjava.web.config.dotconf;

import static org.apache.commons.io.IOUtils.readLines;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.ConfigurationParser;

public abstract class AbstractDotConfParser<T>
    implements ConfigurationParser<T>
{

    private final Pattern parameter;

    private List<String> rawLines;

    private final boolean saveRaw;

    protected AbstractDotConfParser()
    {
        this( false );
    }

    protected AbstractDotConfParser( final boolean saveRaw )
    {
        this.saveRaw = saveRaw;
        parameter = Pattern.compile( "\\s*([-._a-zA-Z0-9]+)\\s*[:=]\\s*([^\\s#]+)(\\s*#.*)?" );
    }

    @Override
    public final String getContentType()
    {
        return "properties";
    }

    @Override
    public final void parse( final InputStream stream )
        throws ConfigurationException
    {
        initialize();

        List<String> lines;
        try
        {
            lines = readLines( stream );
        }
        catch ( final IOException e )
        {
            throw new ConfigurationException( "Failed to read configuration section. Error: %s", e, e.getMessage() );
        }

        if ( saveRaw )
        {
            rawLines = lines;
        }

        for ( final String line : lines )
        {
            final Matcher matcher = parameter.matcher( line );
            if ( matcher.matches() )
            {
                final String key = matcher.group( 1 );
                final String value = matcher.group( 2 );
                addParameter( key, value );
            }
        }
    }

    public List<String> getRawLines()
    {
        return rawLines;
    }

    protected abstract void initialize();

    protected abstract void addParameter( String key, String value )
        throws ConfigurationException;

}