package org.commonjava.web.config.dotconf.fixture;

public class Simpleton
{

    private String one;

    private String two;

    public Simpleton()
    {

    }

    public Simpleton( final String one, final String two )
    {
        this.one = one;
        this.two = two;
    }

    public String getOne()
    {
        return one;
    }

    public void setOne( final String one )
    {
        this.one = one;
    }

    public String getTwo()
    {
        return two;
    }

    public void setTwo( final String two )
    {
        this.two = two;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( one == null ) ? 0 : one.hashCode() );
        result = prime * result + ( ( two == null ) ? 0 : two.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final Simpleton other = (Simpleton) obj;
        if ( one == null )
        {
            if ( other.one != null )
            {
                return false;
            }
        }
        else if ( !one.equals( other.one ) )
        {
            return false;
        }
        if ( two == null )
        {
            if ( other.two != null )
            {
                return false;
            }
        }
        else if ( !two.equals( other.two ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format( "Simpleton [one=%s, two=%s]", one, two );
    }

}
