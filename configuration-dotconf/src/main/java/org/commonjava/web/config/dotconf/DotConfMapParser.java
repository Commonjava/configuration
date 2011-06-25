package org.commonjava.web.config.dotconf;

import java.util.HashMap;
import java.util.Map;

import org.commonjava.web.config.ConfigurationException;

public class DotConfMapParser
    extends AbstractDotConfParser<Map<String, String>>
{

    Map<String, String> result;

    public DotConfMapParser()
    {
    }

    public DotConfMapParser( final boolean saveRaw )
    {
        super( saveRaw );
    }

    @Override
    public Map<String, String> getConfiguration()
    {
        return result;
    }

    @Override
    protected void initialize()
    {
        result = new HashMap<String, String>();
    }

    @Override
    protected void addParameter( final String key, final String value )
        throws ConfigurationException
    {
        result.put( key, value );
    }

}
