/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/main/A_CmsAuthorizationHandler.java,v $
 * Date   : $Date: 2007/01/15 18:48:32 $
 * Version: $Revision: 1.1.2.5 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (c) 2005 Alkacon Software GmbH (http://www.alkacon.com)
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

package org.opencms.main;

import org.opencms.file.CmsObject;
import org.opencms.security.I_CmsAuthorizationHandler;
import org.opencms.util.CmsUUID;

import javax.servlet.http.HttpServletRequest;

/**
 * Abstract class to grant the needed access to the session manager.<p>
 * 
 * @author Michael Moossen
 *
 * @version $Revision: 1.1.2.5 $ 
 * 
 * @since 6.5.4 
 */
public abstract class A_CmsAuthorizationHandler implements I_CmsAuthorizationHandler {

    /**
     * Registers the current session with OpenCms.<p>
     * 
     * @param request the current request
     * @param cms the cms object to register
     * 
     * @return the updated cms context
     * 
     * @throws CmsException if something goes wrong 
     */
    protected CmsObject registerSession(HttpServletRequest request, CmsObject cms) throws CmsException {

        // update the request context
        cms = OpenCmsCore.getInstance().updateContext(request, cms);

        if (!cms.getRequestContext().currentUser().isGuestUser()
            && !OpenCms.getDefaultUsers().isUserExport(cms.getRequestContext().currentUser().getName())) {
            
            // create the session info object, only for 'real' users
            CmsSessionInfo sessionInfo = new CmsSessionInfo(
                cms.getRequestContext(),
                new CmsUUID(),
                request.getSession().getMaxInactiveInterval());

            // register the updated cms object in the session manager
            OpenCmsCore.getInstance().getSessionManager().addSessionInfo(sessionInfo);
        }

        // return the updated cms object
        return cms;
    }
}