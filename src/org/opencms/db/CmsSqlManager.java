/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/db/CmsSqlManager.java,v $
 * Date   : $Date: 2005/02/04 16:56:45 $
 * Version: $Revision: 1.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (C) 2002 - 2004 Alkacon Software (http://www.alkacon.com)
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
 
package org.opencms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Superclass for all SQL manager implementations.<p>
 * 
 * @author Carsten Weinholz (c.weinholz@alkacon.com)
 * @version $Revision: 1.1 $ $Date: 2005/02/04 16:56:45 $
 * @since 6.0 alpha 3
 */
public class CmsSqlManager {

    /**
     * Returns a connection to the database using the given pool identified by its name.<p>
     * 
     * @param dbPoolName the pool name
     * @return a database connection
     * @throws SQLException if something goes wrong
     */
    public Connection getConnection(String dbPoolName) throws SQLException {
                
        return getConnectionByUrl(CmsDbPool.C_DBCP_JDBC_URL_PREFIX + CmsDbPool.C_OPENCMS_URL_PREFIX + dbPoolName);
    }
    
    /**
     * Returns a connection to the database using the given pool identified by its full url.<p>
     * 
     * @param dbPoolUrl the pool url
     * @return a database connection
     * @throws SQLException if something goes wrong
     */
    public Connection getConnectionByUrl(String dbPoolUrl) throws SQLException {
        
        return DriverManager.getConnection(dbPoolUrl);
    }
}
