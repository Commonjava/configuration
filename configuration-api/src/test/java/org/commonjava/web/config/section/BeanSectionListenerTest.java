package org.commonjava.web.config.section;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.fixture.TestChild;
import org.commonjava.web.config.fixture.TestRoot;
import org.junit.Test;

public class BeanSectionListenerTest
{

    @Test
    public void simpleBeanConfiguration()
        throws ConfigurationException
    {
        BeanSectionListener<TestRoot> listener = new BeanSectionListener<TestRoot>( TestRoot.class );
        listener.sectionStarted( ConfigurationSectionListener.DEFAULT_SECTION );

        listener.parameter( "key.one", "valueOne" );
        listener.parameter( "key.two", "valueTwo" );

        listener.sectionComplete( ConfigurationSectionListener.DEFAULT_SECTION );

        TestRoot result = listener.getConfiguration();

        assertThat( result.getKeyOne(), equalTo( "valueOne" ) );
        assertThat( result.getKeyTwo(), equalTo( "valueTwo" ) );
    }

    @Test
    public void inheritedBeanConfiguration()
        throws ConfigurationException
    {
        BeanSectionListener<TestChild> listener =
            new BeanSectionListener<TestChild>( TestChild.class );
        listener.sectionStarted( ConfigurationSectionListener.DEFAULT_SECTION );

        listener.parameter( "key.one", "valueOne" );
        listener.parameter( "key.two", "valueTwo" );
        listener.parameter( "key.three", "valueThree" );

        listener.sectionComplete( ConfigurationSectionListener.DEFAULT_SECTION );

        TestChild result = listener.getConfiguration();

        assertThat( result.getKeyOne(), equalTo( "valueOne" ) );
        assertThat( result.getKeyTwo(), equalTo( "valueTwo" ) );
        assertThat( result.getKeyThree(), equalTo( "valueThree" ) );
    }

}
