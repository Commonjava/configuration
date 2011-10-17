package org.commonjava.web.config.fixture;

import org.commonjava.web.config.annotation.ConfigName;

public class TestChild
    extends TestRoot
{
    private String keyThree;

    public String getKeyThree()
    {
        return keyThree;
    }

    @ConfigName( "key.three" )
    public void setKeyThree( final String keyThree )
    {
        this.keyThree = keyThree;
    }

}
