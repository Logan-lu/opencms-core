/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/db/CmsDefaultUsers.java,v $
 * Date   : $Date: 2007/01/15 18:48:32 $
 * Version: $Revision: 1.32.4.3 $
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

package org.opencms.db;

import org.opencms.main.CmsRuntimeException;
import org.opencms.security.CmsOrganizationalUnit;
import org.opencms.util.CmsStringUtil;

/**
 * Provides access to the names of the OpenCms default users and groups.<p>
 * 
 * @author Alexander Kandzior 
 * @author Armen Markarian 
 * 
 * @version $Revision: 1.32.4.3 $
 * 
 * @since 6.0.0
 */
public class CmsDefaultUsers {

    /** Default name for the "Deleted Resource" user. */
    public static final String DEFAULT_USER_DELETED_RESOURCE = "Admin";

    /** Default name for the "Guests" group. */
    protected static final String DEFAULT_GROUP_GUESTS = "Guests";

    /** Default name for the "Admin" user. */
    protected static final String DEFAULT_USER_ADMIN = "Admin";

    /** Default name for the "Export" user. */
    protected static final String DEFAULT_USER_EXPORT = "Export";

    /** Default name for the "Guest" user. */
    protected static final String DEFAULT_USER_GUEST = "Guest";

    /** Guests group name. */
    private String m_groupGuests;

    /** Administrator user name. */
    private String m_userAdmin;

    /** Deleted resource user name. */
    private String m_userDeletedResource;

    /** Export user name. */
    private String m_userExport;

    /** Guest user name. */
    private String m_userGuest;

    /**
     * Constructor that initializes all names with default values.<p>
     * 
     * See the constants of this class for the defaule values that are uses.<p> 
     */
    public CmsDefaultUsers() {

        m_userAdmin = DEFAULT_USER_ADMIN;
        m_userGuest = DEFAULT_USER_GUEST;
        m_userExport = DEFAULT_USER_EXPORT;
        m_userDeletedResource = DEFAULT_USER_DELETED_RESOURCE;
        m_groupGuests = DEFAULT_GROUP_GUESTS;
    }

    /**
     * Public constructor. <p>
     * 
     * @param userAdmin the name of the default admin user
     * @param userGuest the name of the guest user
     * @param userExport the name of the export user
     * @param userDeletedResource the name of the deleted resource user, can be <code>null</code>
     * @param groupGuests the name of the guests group
     */
    public CmsDefaultUsers(
        String userAdmin,
        String userGuest,
        String userExport,
        String userDeletedResource,
        String groupGuests) {

        init(userAdmin, userGuest, userExport, userDeletedResource, groupGuests);
    }

    /**
     * Returns the name of the guests group.<p>
     * 
     * @return the name of the guests group
     */
    public String getGroupGuests() {

        return m_groupGuests;
    }

    /**
     * Returns the name of the default administrator user.<p>
     * 
     * @return the name of the default administrator user
     */
    public String getUserAdmin() {

        return m_userAdmin;
    }

    /**
     * Returns the name of the default deleted resource user.<p>
     * 
     * @return the name of the default deleted resource user
     */
    public String getUserDeletedResource() {

        return m_userDeletedResource;
    }

    /**
     * Returns the name of the user used to generate the static export.<p>
     * 
     * @return the name of the user used to generate the static export
     */
    public String getUserExport() {

        return m_userExport;
    }

    /**
     * Returns the name of the default guest user.<p>
     * 
     * @return the name of the default guest user
     */
    public String getUserGuest() {

        return m_userGuest;
    }

    /**
     * Checks if a given user name is the name of one of the OpenCms default users.<p>
     *
     * @param userName the group name to check
     * 
     * @return <code>true</code> if user name is one of OpenCms default users, <code>false</code> if it is not
     * or if <code>userName</code> is <code>null</code> or an empty string (no trim)
     * 
     * @see #getUserAdmin()
     * @see #getUserExport()
     * @see #getUserGuest()
     * @see #getUserDeletedResource()
     */
    public boolean isDefaultUser(String userName) {

        if (CmsStringUtil.isEmptyOrWhitespaceOnly(userName)) {
            return false;
        }

        // first check without ou prefix, to stay backwards compatible
        boolean isDefault = m_userAdmin.equals(userName);
        isDefault = isDefault || m_userGuest.equals(userName);
        isDefault = isDefault || m_userExport.equals(userName);
        isDefault = isDefault || m_userDeletedResource.equals(userName);

        // now check with ou prefix
        isDefault = isDefault || userName.endsWith(CmsOrganizationalUnit.appendFqn(null, m_userAdmin));
        isDefault = isDefault || userName.endsWith(CmsOrganizationalUnit.appendFqn(null, m_userGuest));
        isDefault = isDefault || userName.endsWith(CmsOrganizationalUnit.appendFqn(null, m_userExport));
        isDefault = isDefault || userName.endsWith(CmsOrganizationalUnit.appendFqn(null, m_userDeletedResource));

        return isDefault;
    }

    /**
     * Checks if a given group name is the name of the guests group.<p>
     * 
     * @param groupName the group name to check
     * 
     * @return <code>true</code> if a given group name is the name of the guests group
     */
    public boolean isGroupGuests(String groupName) {

        if (CmsStringUtil.isEmptyOrWhitespaceOnly(groupName)) {
            return false;
        }
        return m_groupGuests.equals(groupName)
            || groupName.endsWith(CmsOrganizationalUnit.appendFqn(null, m_groupGuests));
    }

    /**
     * Checks if a given user name is the name of the admin user.<p>
     * 
     * @param userName the user name to check
     * 
     * @return <code>true</code> if a given user name is the name of the admin user
     */
    public boolean isUserAdmin(String userName) {

        if (CmsStringUtil.isEmptyOrWhitespaceOnly(userName)) {
            return false;
        }
        return m_userAdmin.equals(userName) || userName.endsWith(CmsOrganizationalUnit.appendFqn(null, m_userAdmin));
    }

    /**
     * Checks if a given user name is the name of the export user.<p>
     * 
     * @param userName the user name to check
     * 
     * @return <code>true</code> if a given user name is the name of the export user
     */
    public boolean isUserExport(String userName) {

        if (CmsStringUtil.isEmptyOrWhitespaceOnly(userName)) {
            return false;
        }
        return m_userExport.equals(userName) || userName.endsWith(CmsOrganizationalUnit.appendFqn(null, m_userExport));
    }

    /**
     * Checks if a given user name is the name of the guest user.<p>
     * 
     * @param userName the user name to check
     * 
     * @return <code>true</code> if a given user name is the name of the guest user
     */
    public boolean isUserGuest(String userName) {

        if (CmsStringUtil.isEmptyOrWhitespaceOnly(userName)) {
            return false;
        }
        return m_userGuest.equals(userName) || userName.endsWith(CmsOrganizationalUnit.appendFqn(null, m_userGuest));
    }

    /**
     * Initializes this instance.<p>
     * 
     * @param userAdmin the name of the default admin user
     * @param userGuest the name of the guest user
     * @param userExport the name of the export user
     * @param userDeletedResource the name of the deleted resource user, can be <code>null</code>
     * @param groupGuests the name of the guests group
     */
    protected void init(
        String userAdmin,
        String userGuest,
        String userExport,
        String userDeletedResource,
        String groupGuests) {

        // check if all required user and group names are not null or empty
        if (CmsStringUtil.isEmptyOrWhitespaceOnly(userAdmin)
            || CmsStringUtil.isEmptyOrWhitespaceOnly(userGuest)
            || CmsStringUtil.isEmptyOrWhitespaceOnly(userExport)
            || CmsStringUtil.isEmptyOrWhitespaceOnly(groupGuests)) {
            throw new CmsRuntimeException(Messages.get().container(Messages.ERR_USER_GROUP_NAMES_EMPTY_0));
        }
        // set members
        m_userAdmin = userAdmin.trim();
        m_userGuest = userGuest.trim();
        m_userExport = userExport.trim();
        if (CmsStringUtil.isEmptyOrWhitespaceOnly(userDeletedResource)) {
            m_userDeletedResource = userAdmin;
        } else {
            m_userDeletedResource = userDeletedResource.trim();
        }
        m_groupGuests = groupGuests.trim();
    }
}