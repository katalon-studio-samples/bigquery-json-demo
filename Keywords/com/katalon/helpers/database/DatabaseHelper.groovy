package com.katalon.helpers.database

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.db.DatabaseConnection
import com.kms.katalon.core.db.DatabaseSettings
import com.kms.katalon.core.db.ConnectionProperty

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Statement

public class DatabaseHelper {

	static Connection getGlobalConnection() {
		DatabaseSettings settings = new DatabaseSettings(RunConfiguration.getProjectDir())
		//println(settings)

		List<ConnectionProperty> properties = settings.getConnectionProperties()

		//properties.each { cp ->
		//	println "${cp.name} = ${cp.value}"
		//}

		DatabaseConnection dbConnection = settings.getDatabaseConnection()

		// Create an empty Properties object
		Properties javaProperties = new Properties()

		// Iterate over the list and populate the Properties object
		properties.each { cp ->
			javaProperties.setProperty(cp.getName(), cp.getValue())
		}

		dbConnection.setConnectionProperties(javaProperties)
		//println(dbConnection)
		Connection conn = dbConnection.getConnection()

		return conn
	}
}
