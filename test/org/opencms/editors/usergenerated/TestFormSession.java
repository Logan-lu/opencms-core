/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
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
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.editors.usergenerated;

import org.opencms.i18n.CmsEncoder;
import org.opencms.main.CmsEvent;
import org.opencms.main.I_CmsEventListener;
import org.opencms.main.OpenCms;
import org.opencms.test.OpenCmsTestCase;
import org.opencms.util.CmsFileUtil;
import org.opencms.xml.CmsXmlContentDefinition;
import org.opencms.xml.CmsXmlEntityResolver;
import org.opencms.xml.CmsXmlException;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the form session methods.<p>
 */
public class TestFormSession extends OpenCmsTestCase {

    /** Schema id. */
    private static final String SCHEMA_ID_IMAGE = "http://www.opencms.org/image.xsd";

    /** Schema id. */
    private static final String SCHEMA_ID_LINK = "http://www.opencms.org/link.xsd";

    /** Schema id. */
    private static final String SCHEMA_ID_PARAGRAPH = "http://www.opencms.org/paragraph.xsd";

    /** Schema id. */
    private static final String SCHEMA_ID_TEXTBLOCK = "http://www.opencms.org/textblock.xsd";

    /**
     * Default JUnit constructor.<p>
     * 
     * @param arg0 JUnit parameters
     */
    public TestFormSession(String arg0) {

        super(arg0);
    }

    /**
     * Test suite for this test class.<p>
     * 
     * @return the test suite
     */
    public static Test suite() {

        TestSuite suite = new TestSuite();
        suite.setName(TestFormSession.class.getName());

        suite.addTest(new TestFormSession("testGetValues"));

        TestSetup wrapper = new TestSetup(suite) {

            @Override
            protected void setUp() {

                setupOpenCms("simpletest", "/");
            }

            @Override
            protected void tearDown() {

                removeOpenCms();
            }
        };

        return wrapper;
    }

    /**
     * Tests the add values method.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testGetValues() throws Exception {

        CmsXmlEntityResolver resolver = new CmsXmlEntityResolver(null);
        CmsXmlContentDefinition definition = unmarshalDefinition(resolver);
        CmsXmlEntityResolver.cacheSystemId(
            SCHEMA_ID_TEXTBLOCK,
            definition.getSchema().asXML().getBytes(CmsEncoder.ENCODING_UTF_8));
        String fileContent = CmsFileUtil.readFile(
            "org/opencms/editors/usergenerated/tb_00001.xml",
            CmsEncoder.ENCODING_UTF_8);
        // now create the XML content
        CmsXmlContent xmlContent = CmsXmlContentFactory.unmarshal(fileContent, CmsEncoder.ENCODING_UTF_8, resolver);
        CmsFormSession session = new CmsFormSession(getCmsObject());
        Map<String, String> values = session.getValues(xmlContent, new Locale("en"));
        assertEquals("Full width example", values.get("Title[1]"));
    }

    /**
     * Read the given file and cache it's contents as XML schema with the given system id.
     *
     * @param fileName the file name to read
     * @param systemId the XML schema system id to use
     *
     * @throws IOException in case of errors reading the file
     */
    private void cacheXmlSchema(String fileName, String systemId) throws IOException {

        // read the XML schema
        byte[] schema = CmsFileUtil.readFile(fileName);
        // store the XML schema in the resolver
        CmsXmlEntityResolver.cacheSystemId(systemId, schema);
    }

    /**
     * Unmarshals the content definition used within the tests.<p>
     * 
     * @param resolver the entity resolver
     * 
     * @return the content definition
     * 
     * @throws IOException if reading the files fails
     * @throws CmsXmlException if parsing the schema fails
     */
    private CmsXmlContentDefinition unmarshalDefinition(CmsXmlEntityResolver resolver)
    throws IOException, CmsXmlException {

        // fire "clear cache" event to clear up previously cached schemas
        OpenCms.fireCmsEvent(new CmsEvent(I_CmsEventListener.EVENT_CLEAR_CACHES, new HashMap<String, Object>()));
        cacheXmlSchema("org/opencms/editors/usergenerated/image.xsd", SCHEMA_ID_IMAGE);
        cacheXmlSchema("org/opencms/editors/usergenerated/link.xsd", SCHEMA_ID_LINK);
        cacheXmlSchema("org/opencms/editors/usergenerated/paragraph.xsd", SCHEMA_ID_PARAGRAPH);

        String schemaContent = CmsFileUtil.readFile(
            "org/opencms/editors/usergenerated/textblock.xsd",
            CmsEncoder.ENCODING_UTF_8);
        CmsXmlContentDefinition definition = CmsXmlContentDefinition.unmarshal(
            schemaContent,
            SCHEMA_ID_TEXTBLOCK,
            resolver);
        return definition;
    }
}