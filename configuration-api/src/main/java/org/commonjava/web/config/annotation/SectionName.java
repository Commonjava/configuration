package org.commonjava.web.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.commonjava.web.config.section.ConfigurationSectionListener;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.TYPE } )
public @interface SectionName
{

    String value() default ConfigurationSectionListener.DEFAULT_SECTION;

}
