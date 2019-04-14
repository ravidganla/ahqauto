/*
 * Copyright (c) 2014 Objective Corporation Limited
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Objective Corporation Limited ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Objective.
 */
package com.objective.kse.ui.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

import com.objective.kse.ui.base.StakeholderPageBase;

/**
 * Class that contains helper methods for accessing the DB.
 *
 * @author ravi
 */
public class DbHelper extends StakeholderPageBase {
	/**
	 * Opens a connection to the DB.
	 *
	 * @return connection
	 * @throws Exception
	 */
	private static Connection openConnection() throws Exception {
		Properties properties = new Properties();
        properties.put("user", System.getProperty("dbUsername"));
        properties.put("password", System.getProperty("dbPassword"));
		properties.put("Trusted_Connection", "yes");
		properties.put("SelectMethod", "Cursor");
		properties.put("maxStatements", "50");
		properties.put("charset", "UTF-8");
		//String url = "jdbc:jtds:sqlserver://dev_db/publisher3_development";
		String url = System.getProperty("dbAddress");
		
		Class.forName("net.sourceforge.jtds.jdbcx.JtdsDataSource").newInstance();
		return DriverManager.getConnection(url, properties);
	}

	public static ResultSet executeQuery(String query) throws Exception {
		Connection conn = openConnection();
		PreparedStatement pst = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return pst.executeQuery();
	}

	public static void executeUpdateQuery(String query) throws Exception {
		Connection conn = openConnection();
		PreparedStatement pst = conn.prepareStatement(query);
		pst.executeUpdate();
		conn.close();
	}


	/*
	* Retrieves the last inserted link id of the row as the result of the inset query from content owner link table
	* @
	* */
	public static int executeInsertQueryOnContentOwnerTable(String query, Integer contentId, Integer personId, boolean isGroupId, Integer groupId, Integer groupMinimum) throws Exception {
		Connection conn = openConnection();
		int lastInserted_id = 0;
		PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst.setLong(1, contentId);
		if (personId == null) {
			pst.setNull(2, Types.INTEGER);
		} else {
			pst.setLong(2, personId);
		}
		pst.setBoolean(3, isGroupId);
		if (groupId == null) {
			pst.setNull(4, Types.INTEGER);
		} else {
			pst.setLong(4, groupId);
		}
		if (groupMinimum == null) {
			pst.setNull(5, Types.INTEGER);
		} else {
			pst.setInt(5, groupMinimum);
		}
		pst.executeUpdate();
		ResultSet rs = pst.getGeneratedKeys();
		if (rs.next()) {
			lastInserted_id = rs.getInt(1);
		}
		conn.close();
		return lastInserted_id;
	}


	public static int executeInsertQueryContentOwnerGroup(String query, int value1, int value2, boolean value3, int value4) throws Exception {
		Connection conn = openConnection();
		int lastInserted_id = 0;
		PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst.setInt(1, value1);
		pst.setInt(2, value2);
		pst.setBoolean(3, value3);
		pst.setInt(4, value4);
		pst.executeUpdate();
		ResultSet rs = pst.getGeneratedKeys();
		if (rs.next()) {
			lastInserted_id = rs.getInt(1);
		}
		conn.close();
		return lastInserted_id;
	}

	public static void executeDeleteQuery(String query, int value1) throws Exception {
		Connection conn = openConnection();
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, value1);
		pst.executeUpdate();
		conn.close();
	}
}
