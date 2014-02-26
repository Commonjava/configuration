package org.commonjava.web.config;

import org.commonjava.web.config.annotation.SectionName;
import org.commonjava.web.config.fixture.TestRoot;
import org.commonjava.web.config.section.MapSectionListener;
import org.junit.Test;

public class DefaultConfigurationListenerTest
{

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
