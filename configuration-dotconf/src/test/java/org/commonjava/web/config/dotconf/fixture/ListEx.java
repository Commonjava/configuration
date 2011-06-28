package org.commonjava.web.config.dotconf.fixture;

import java.util.ArrayList;

public class ListEx
    extends ArrayList<String>
{

    private static final long serialVersionUID = 1L;

    public ListEx( final String... lines )
    {
        for ( final String line : lines )
        {
            add( line );
        }
    }
}
