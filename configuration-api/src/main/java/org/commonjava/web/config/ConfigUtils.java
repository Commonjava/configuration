/**
 * Copyright (C) 2011 John Casey (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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