/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
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
