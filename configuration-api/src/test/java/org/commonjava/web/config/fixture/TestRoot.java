package org.commonjava.web.config.fixture;

import org.commonjava.web.config.annotation.ConfigName;

public class TestRoot
{
    private String keyOne;

    private String keyTwo;

    public String getKeyOne()
    {
        return keyOne;
    }

    @ConfigName( "key.one" )
    public void setKeyOne( final String keyOne )
    {
        this.keyOne = keyOne;
    }

    public String getKeyTwo()
    {
        return keyTwo;
    }

    @ConfigName( "key.two" )
    public void setKeyTwo( final String keyTwo )
    {
        this.keyTwo = keyTwo;
    }

}
