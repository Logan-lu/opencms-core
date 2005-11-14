/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/jsp/decorator/CmsDecoratorConfiguration.java,v $
 * Date   : $Date: 2005/11/14 15:04:05 $
 * Version: $Revision: 1.1.2.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (C) 2005 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.jsp.decorator;

import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;

import java.util.Locale;

/**
 * The CmsDecoratorConfiguration initalizes and stores the text decorations.<p>
 * 
 * It uses uses the information of one or more <code>{@link CmsDecorationDefintion}</code> to create the
 * pre- and postfixs for text decorations.
 *
 * @author Michael Emmerich  
 * 
 * @version $Revision: 1.1.2.1 $ 
 * 
 * @since 6.1.3 
 */

public class CmsDecoratorConfiguration {

    /** The xpath for the decoration configuration. */
    private static final String XPATH_DECORATION = "decoration";

    /** The xpath for the filename configuration. */
    private static final String XPATH_FILENAME = "filename";

    /** The xpath for the markfirst configuration. */
    private static final String XPATH_MARKFIRST = "markfirst";

    /** The xpath for the name configuration. */
    private static final String XPATH_NAME = "name";

    /** The xpath for the posttext configuration. */
    private static final String XPATH_POSTTEXT = "posttext";

    /** The xpath for the posttextfirst configuration. */
    private static final String XPATH_POSTTEXTFIRST = "posttextfirst";

    /** The xpath for the pretext configuration. */
    private static final String XPATH_PRETEXT = "pretext";

    /** The xpath for the pretextfirst configuration. */
    private static final String XPATH_PRETEXTFIRST = "pretextfirst";

    /** The xpath for the uselocale configuration. */
    private static final String XPATH_USELOCALE = "uselocale";

    /** The CmsObject. */
    private CmsObject m_cms;

    /** The config file. */
    private String m_configFile;

    /** The locale for extracting the configuration data. */
    private Locale m_configurationLocale = new Locale("en");

    /** Map of configured decorations. */
    private CmsDecorationBundle m_decorations;

    /** The locale for to build the configuration for. */
    private Locale m_locale;

    /**
     * Constructor, creates a new, empty CmsDecoratorConfiguration.<p>
     * 
     * @param cms the CmsObject
     *
     */
    public CmsDecoratorConfiguration(CmsObject cms) {

        m_decorations = new CmsDecorationBundle();
        m_configFile = null;
        m_cms = cms;
        m_locale = m_cms.getRequestContext().getLocale();
    }

    /**
     * Constructor, creates a new, CmsDecoratorConfiguration with a given config file.<p>
     * 
     * @param cms the CmsObject
     * @param configFile the configuration file
     * @throws CmsException if something goes wrong
     */
    public CmsDecoratorConfiguration(CmsObject cms, String configFile)
    throws CmsException {

        m_decorations = new CmsDecorationBundle();
        m_configFile = configFile;
        m_cms = cms;
        m_locale = m_cms.getRequestContext().getLocale();
        init();
    }

    /**
     * Constructor, creates a new, CmsDecoratorConfiguration with a given config file and locale.<p>
     * 
     * @param cms the CmsObject
     * @param configFile the configuration file
     * @param locale to locale to build this configuration for
     * @throws CmsException if something goes wrong
     */
    public CmsDecoratorConfiguration(CmsObject cms, String configFile, Locale locale)
    throws CmsException {

        m_decorations = new CmsDecorationBundle(locale);
        m_configFile = configFile;
        m_cms = cms;
        m_locale = locale;
        init();
    }

    /**
     * Adds decorations defined in a <code>{@link CmsDecorationDefintion}</code> object to the map of all decorations.<p>
     * @param decorationDefinition the <code>{@link CmsDecorationDefintion}</code> the decorations to be added
     * @throws CmsException if something goes wrong
     */
    public void addDecorations(CmsDecorationDefintion decorationDefinition) throws CmsException {

        m_decorations.putAll(decorationDefinition.createDecorationBundle(m_cms, m_configurationLocale).getAll());
    }

    /**
     * Gets the decoration bundle.<p>
     *@return the decoration bundle to be used
     */
    public CmsDecorationBundle getDecorations() {

        return m_decorations;
    }

    /**
     * Sets the decoration bundle, overwriting an exiting one.<p> 
     * 
     * @param decorations new decoration bundle
     */
    public void setDecorations(CmsDecorationBundle decorations) {

        m_decorations = decorations;
    }

    /**
     * Builds a CmsDecorationDefintion from a given configuration file.<p>
     * 
     * @param configuration the configuration file
     * @param i the number of the decoration definition to create
     * @return CmsDecorationDefintion created form configuration file
     */
    private CmsDecorationDefintion getDecorationDefinition(CmsXmlContent configuration, int i) {

        CmsDecorationDefintion decDef = new CmsDecorationDefintion();
        String name = configuration.getValue(XPATH_DECORATION + "[" + i + "]/" + XPATH_NAME, m_configurationLocale).getStringValue(
            m_cms);
        String markfirst = configuration.getValue(
            XPATH_DECORATION + "[" + i + "]/" + XPATH_MARKFIRST,
            m_configurationLocale).getStringValue(m_cms);
        String pretext = configuration.getValue(
            XPATH_DECORATION + "[" + i + "]/" + XPATH_PRETEXT,
            m_configurationLocale).getStringValue(m_cms);
        String posttext = configuration.getValue(
            XPATH_DECORATION + "[" + i + "]/" + XPATH_POSTTEXT,
            m_configurationLocale).getStringValue(m_cms);
        String pretextfirst = configuration.getValue(
            XPATH_DECORATION + "[" + i + "]/" + XPATH_PRETEXTFIRST,
            m_configurationLocale).getStringValue(m_cms);
        String posttextfirst = configuration.getValue(
            XPATH_DECORATION + "[" + i + "]/" + XPATH_POSTTEXTFIRST,
            m_configurationLocale).getStringValue(m_cms);
        String filenname = configuration.getValue(
            XPATH_DECORATION + "[" + i + "]/" + XPATH_FILENAME,
            m_configurationLocale).getStringValue(m_cms);

        decDef.setName(name);
        decDef.setMarkFirst(markfirst.equals("true"));
        decDef.setPreText(pretext);
        decDef.setPostText(posttext);
        decDef.setPreTextFirst(pretextfirst);
        decDef.setPostTextFirst(posttextfirst);
        decDef.setConfigurationFile(filenname);

        return decDef;
    }

    /**
     * Initialises the configuration.<p>
     * @throws CmsException if something goes wrong
     */
    private void init() throws CmsException {

        // get the configuration file
        CmsResource res = m_cms.readResource(m_configFile);
        CmsFile file = CmsFile.upgrade(res, m_cms);
        CmsXmlContent configuration = CmsXmlContentFactory.unmarshal(m_cms, file);

        // get the uselocale flag
        // if this flag is not set to true, we must build locale independent decoration bundles
        String uselocale = configuration.getValue(XPATH_USELOCALE, m_configurationLocale).getStringValue(m_cms);
        if (!uselocale.equals("true")) {
            m_locale = null;
        }
        // get the number of decoration definitions
        int decorationDefCount = configuration.getIndexCount(XPATH_DECORATION, m_configurationLocale);
        // get all the decoration definitions
        for (int i = 1; i <= decorationDefCount; i++) {
            CmsDecorationDefintion decDef = getDecorationDefinition(configuration, i);
            CmsDecorationBundle decBundle = decDef.createDecorationBundle(m_cms, m_locale);
            // merge it to the already existing decorations
            m_decorations.putAll(decBundle.getAll());
        }
    }

}
