/*
* File   : $Source: /alkacon/cvs/opencms/src/com/opencms/workplace/Attic/CmsAdminDatatypes.java,v $
* Date   : $Date: 2005/02/18 14:23:15 $
* Version: $Revision: 1.39 $
*
* This library is part of OpenCms -
* the Open Source Content Mananagement System
*
* Copyright (C) 2001 - 2005 The OpenCms Group
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* For further information about OpenCms, please see the
* OpenCms Website: http://www.opencms.org 
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.opencms.workplace;

import org.opencms.file.CmsObject;
import org.opencms.file.types.I_CmsResourceType;
import org.opencms.i18n.CmsEncoder;
import org.opencms.main.CmsException;
import org.opencms.main.OpenCms;

import com.opencms.template.A_CmsXmlContent;
import com.opencms.template.CmsXmlTemplateFile;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Template class for displaying OpenCms workplace admin datatypes
 * <P>
 *
 * @author Mario Stanke
 * @version $Revision: 1.39 $ $Date: 2005/02/18 14:23:15 $
 * @see com.opencms.workplace.CmsXmlWpTemplateFile
 * 
 * @deprecated Will not be supported past the OpenCms 6 release.
 */

public class CmsAdminDatatypes extends CmsWorkplaceDefault {


    /** XML datablock tag used for setting the resource type name */
    private static final String C_TAG_RESTYPE = "restype";


    /** XML datablock tag used for setting an entry in the list of datatypes */
    private static final String C_TYPELISTENTRY = "extensionentry";


    /** XML datablock tag used for setting all collected entries */
    private static final String C_TAG_ALLENTRIES = "allentries";


    /** XML datablock tag used for getting a processed resource type entry */
    private static final String C_TAG_RESTYPEENTRY = "restypeentry";


    /** XML datablock tag used for getting a processed separator entry */
    private static final String C_TAG_SEPARATORENTRY = "separatorentry";


    /** XML datablock tag used for getting the complete and processed content to be returned */
    private static final String C_TAG_SCROLLERCONTENT = "scrollercontent";

    /**
     *  checks if name is in the right format for file extensions and trims leading stars and dots
     *
     * @return the formatted String without "*.", '.' or '*'
     */

    private String format(String name) throws CmsException {
        int z = 0;
        while('*' == name.charAt(z) || '.' == name.charAt(z)) {
            z++;
        }

        // cut off leading stars and dots
        String res = name.substring(z, name.length());
        if(res.indexOf(' ') != -1) {

            // name contained a blank
            throw new CmsException(CmsException.C_BAD_NAME);
        }
        return res.toLowerCase(); // extensions not case sensitive
    }

    /**
     * Gets the content of a defined section in a given template file and its subtemplates
     * with the given parameters.
     *
     * @see #getContent(CmsObject, String, String, Hashtable, String)
     * @param cms CmsObject Object for accessing system resources.
     * @param templateFile Filename of the template file.
     * @param elementName Element name of this template in our parent template.
     * @param parameters Hashtable with all template class parameters.
     * @param templateSelector template section that should be processed.
     */

    public byte[] getContent(CmsObject cms, String templateFile, String elementName,
            Hashtable parameters, String templateSelector) throws CmsException {
        if(OpenCms.getLog(this).isDebugEnabled() && C_DEBUG) {
            OpenCms.getLog(this).debug("Getting content of element "
                    + ((elementName == null) ? "<root>" : elementName));
            OpenCms.getLog(this).debug("Template file is: " + templateFile);
            OpenCms.getLog(this).debug("Selected template section is: "
                    + ((templateSelector == null) ? "<default>" : templateSelector));
        }

        CmsXmlWpTemplateFile xmlTemplateDocument = new CmsXmlWpTemplateFile(cms, templateFile);
        CmsXmlLanguageFile lang = xmlTemplateDocument.getLanguageFile();
        String action = (String)parameters.get("action");
        String resTypeName = (String)parameters.get("restype");
        String extensionName = (String)parameters.get("extension");
        xmlTemplateDocument.setData("RESTYPE", resTypeName);
        if("new".equals(action)) {
            templateSelector = "newextension";
            String name = (String)parameters.get("NAME");
            if(name == null || name.equals("")) {


            // form has not yet been submitted
            }
            else {
                try {
                    String formattedName;
                    formattedName = format(name);
                    Map h = OpenCms.getResourceManager().getExtensionMapping();
                    if(h == null) {
                        h = new HashMap();
                    }
                    if(h.containsKey(formattedName)) {
                        throw new CmsException(CmsException.C_NOT_EMPTY);
                    }
                    h.put(formattedName, resTypeName);
                    // not possible anymore right now
                    // TODO: make this possible again
                    //cms.writeFileExtensions(h);
                    templateSelector = "";
                }
                catch(CmsException e) {
                    if(e.getType() == CmsException.C_NOT_EMPTY) {
                        templateSelector = "errorinuse";
                    }
                    else {
                        if(e.getType() == CmsException.C_BAD_NAME) {
                            templateSelector = "errorformat";
                        }
                        else {
                            StringBuffer errmesg = new StringBuffer();
                            errmesg.append(lang.getLanguageValue("error.reason.newextension1")
                                    + " '" + name + "' " + lang.getLanguageValue("error.reason.newextension2")
                                            + " '" + resTypeName + "' "
                                                    + lang.getLanguageValue("error.reason.newextension3") + "\n\n");
                            errmesg.append(CmsException.getStackTraceAsString(e));
                            xmlTemplateDocument.setData("NEWDETAILS", errmesg.toString());
                            templateSelector = "newerror";
                        }
                    }
                }
            }
        }
        else {
            if("delete".equals(action)) {
                if("true".equals(parameters.get("sure"))) {

                    // the user is sure to delete the property definition
                    //try {
                        Map h = OpenCms.getResourceManager().getExtensionMapping();
                        if(h != null) {
                            h.remove(extensionName);
                        }
                        // not possible anymore right now
                        // TODO: make this possible again
                        //cms.writeFileExtensions(h);
                        templateSelector = "";
                    //}
                    /**catch(CmsException e) {
                        xmlTemplateDocument.setData("DELETEDETAILS", CmsException.getStackTraceAsString(e));
                        templateSelector = "errordelete";
                    }*/
                }
                else {
                    templateSelector = "RUsuredelete";
                }
                xmlTemplateDocument.setData("EXTENSION_NAME", extensionName);
            }
        }

        // Now load the template file and start the processing
        return startProcessing(cms, xmlTemplateDocument, elementName, parameters, templateSelector);
    }

    /**
     * Used by the <code>&lt;PREFSSCROLLER&gt;</code> tag for getting
     * the content of the scroller window.
     * <P>
     * Gets all available resource types and returns a list
     * using the datablocks defined in the own template file.
     *
     * @param cms CmsObject Object for accessing system resources.
     * @param lang reference to the currently valid language file
     * @param parameters Hashtable containing all user parameters <em>(not used here)</em>.
     * @return Index representing the user's current filter view in the vectors.
     * @throws CmsException
     */

    public String getDatatypes(CmsObject cms, A_CmsXmlContent doc,
            CmsXmlLanguageFile lang, Hashtable parameters, Object callingObj) throws CmsException {
        StringBuffer result = new StringBuffer();
        Map extensions = OpenCms.getResourceManager().getExtensionMapping();
        Map extByFiletypes = turnAround(extensions);
        CmsXmlTemplateFile templateFile = (CmsXmlTemplateFile)doc;
        
        // get all available resource types
        List allResTypes = OpenCms.getResourceManager().getResourceTypes();
        for (int i=0; i<allResTypes.size(); i++) {  
            I_CmsResourceType type = (I_CmsResourceType)allResTypes.get(i);
            String resTypeName = type.getTypeName();
            Vector suffList = (Vector)extByFiletypes.get(resTypeName);
            result.append(getResourceEntry(cms, doc, lang, parameters, callingObj, resTypeName, suffList));
            if(i < (allResTypes.size() - 1)) {
                result.append(templateFile.getProcessedDataValue(C_TAG_SEPARATORENTRY, callingObj));
            }
        }
        templateFile.setData(C_TAG_ALLENTRIES, result.toString());
        return templateFile.getProcessedDataValue(C_TAG_SCROLLERCONTENT, callingObj);
    }

    /**
     *
     * gets the HTML code for entry in the lists of resources.
     *
     * @param cms CmsObject Object for accessing system resources.
     * @param doc the template file which is used
     * @param lang reference to the currently valid language file
     * @param parameters Hashtable containing all user parameters <em>(not used here)</em>.
     * @param callingObject Object for accessing system resources.
     * @param resTypeName name of the resource type
     * @param suffList Vector of all extensions of that resource type
     * @return String which holds a HTML table
     * @throws CmsException
     */

    private String getResourceEntry(CmsObject cms, A_CmsXmlContent doc, CmsXmlLanguageFile lang,
            Hashtable parameters, Object callingObject, String resTypeName, Vector suffList)
                    throws CmsException {
        StringBuffer output = new StringBuffer();
        CmsXmlWpTemplateFile templateFile = (CmsXmlWpTemplateFile)doc;
        templateFile.setData(C_TAG_RESTYPE, resTypeName);

        templateFile.setData(C_TAG_RESTYPE + "_esc", CmsEncoder.escapeWBlanks(resTypeName,
            cms.getRequestContext().getEncoding()));
        output.append(templateFile.getProcessedDataValue(C_TAG_RESTYPEENTRY, callingObject));
        if(suffList != null) {
            for(int z = 0;z < suffList.size();z++) {
                String suffix = (String)suffList.elementAt(z);
                templateFile.setData("EXTENSION_NAME", suffix);
                templateFile.setData("EXTENSION_NAME_ESC", CmsEncoder.escapeWBlanks(suffix,
                    cms.getRequestContext().getEncoding()));
                output.append(templateFile.getProcessedDataValue(C_TYPELISTENTRY, callingObject));
            }
        }
        return output.toString();
    }

    /**
     * Indicates if the results of this class are cacheable.
     *
     * @param cms CmsObject Object for accessing system resources
     * @param templateFile Filename of the template file
     * @param elementName Element name of this template in our parent template.
     * @param parameters Hashtable with all template class parameters.
     * @param templateSelector template section that should be processed.
     * @return <EM>true</EM> if cacheable, <EM>false</EM> otherwise.
     */

    public boolean isCacheable(CmsObject cms, String templateFile, String elementName,
            Hashtable parameters, String templateSelector) {
        return false;
    }

    /**
     *  Given the Hashtable h, interpreted as a binary relation (subset of A x B) with keys in A
     *  and values in B this function returns, as a Hashtable, the same binary relation with keys
     *  in B and values in A stored in Vectors
     *
     * @param h the Hashtable to be 'turned around'
     * @return a Hashtable of Vectors
     */

    private Map turnAround(Map h) {
        if(h == null) {
            return null;
        }
        Map g = new HashMap();
        Iterator en = h.keySet().iterator();
        while(en.hasNext()) {
            Object key = en.next();
            Object value = h.get(key);
            Vector List = (Vector)g.get(value);
            if(List == null) {
                Vector newEntry = new Vector();
                newEntry.addElement(key);
                g.put(value, newEntry);
            }
            else {
                List.addElement(key);
            }
        }
        return g;
    }
}
