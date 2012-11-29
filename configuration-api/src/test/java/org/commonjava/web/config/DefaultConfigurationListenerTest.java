package org.commonjava.web.config;

import org.apache.log4j.Level;
import org.commonjava.util.logging.Log4jUtil;
import org.commonjava.web.config.annotation.SectionName;
import org.commonjava.web.config.fixture.TestRoot;
import org.commonjava.web.config.section.MapSectionListener;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultConfigurationListenerTest
{

    @BeforeClass
    public static void logging()
    {
        Log4jUtil.configure( Level.DEBUG );
    }

    @Test
    public void testAnnotationsUsedIfSectionNameIsNull()
        throws ConfigurationException
    {
        new DefaultConfigurationListener().with( null, TestRoot.class )
                                          .with( null, new TestMapListener() );
    }

    @SectionName( "test" )
    public static final class TestMapListener
        extends MapSectionListener
    {

    }

}
