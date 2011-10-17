package org.commonjava.web.config.io;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.fixture.TestChild;
import org.commonjava.web.config.fixture.TestRoot;
import org.commonjava.web.config.section.BeanSectionListener;
import org.junit.Test;

public class SingleSectionConfigReaderTest
{

    @Test
    public void simpleBeanConfiguration()
        throws ConfigurationException, IOException
    {
        BeanSectionListener<TestRoot> listener = new BeanSectionListener<TestRoot>( TestRoot.class );
        SingleSectionConfigReader reader = new SingleSectionConfigReader( listener );

        Properties p = new Properties();
        p.setProperty( "key.one", "valueOne" );
        p.setProperty( "key.two", "valueTwo" );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        p.store( baos, "" );

        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        TestRoot result = listener.getConfiguration();

        assertThat( result.getKeyOne(), equalTo( "valueOne" ) );
        assertThat( result.getKeyTwo(), equalTo( "valueTwo" ) );
    }

    @Test
    public void inheritedBeanConfiguration()
        throws ConfigurationException, IOException
    {
        BeanSectionListener<TestChild> listener =
            new BeanSectionListener<TestChild>( TestChild.class );

        SingleSectionConfigReader reader = new SingleSectionConfigReader( listener );

        Properties p = new Properties();
        p.setProperty( "key.one", "valueOne" );
        p.setProperty( "key.two", "valueTwo" );
        p.setProperty( "key.three", "valueThree" );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        p.store( baos, "" );

        reader.loadConfiguration( new ByteArrayInputStream( baos.toByteArray() ) );

        TestChild result = listener.getConfiguration();

        assertThat( result.getKeyOne(), equalTo( "valueOne" ) );
        assertThat( result.getKeyTwo(), equalTo( "valueTwo" ) );
        assertThat( result.getKeyThree(), equalTo( "valueThree" ) );
    }

}
