package org.commonjava.web.config;

import org.commonjava.web.config.annotation.SectionName;
import org.commonjava.web.config.section.ConfigurationSectionListener;

public final class ConfigUtils
{

    private ConfigUtils()
    {
    }

    public static String getSectionName( final Class<?> cls )
    {
        final SectionName anno = cls.getAnnotation( SectionName.class );

        return anno == null ? ConfigurationSectionListener.DEFAULT_SECTION : anno.value();
    }

}
