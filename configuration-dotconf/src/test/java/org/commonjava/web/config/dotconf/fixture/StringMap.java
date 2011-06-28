package org.commonjava.web.config.dotconf.fixture;

import java.util.HashMap;

public class StringMap
    extends HashMap<String, String>
{
    private static final long serialVersionUID = 1L;

    public StringMap( final String... parts )
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
