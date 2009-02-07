/**
 * This file is part of aion-emu.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.commons.database;

// Common SQL

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

/**
 * <b>Database Factory</b><br>
 * <br>
 * This file is used for creating a pool of connections for the server.<br>
 * It utilizes database.properties and creates a pool of connections and automatically recycles them when closed.<br>
 * <br>
 * DB.java utilizes the class.<br>
 * <br>
 * <p/> This class depends on file {@value com.aionemu.commons.database.DatabaseConfig#CONFIG_FILE}.
 * 
 * @author Disturbing
 * @author SoulKeeper
 */
public class DatabaseFactory
{

	/**
	 * Logger for this class
	 */
	private static final Logger			log	= Logger.getLogger(DatabaseFactory.class);

	/**
	 * Data Source Generates all Connections This vaiable is also used as indicator for "initalized" state of
	 * DatabaseFactory
	 */
	private static DataSource			dataSource;

	/**
	 * Connection Pool holds all connections - Idle or Active
	 */
	private static GenericObjectPool	connectionPool;

	/**
	 * Initializes DatabaseFactory.
	 */
	public synchronized static void init()
	{
		if (dataSource != null)
		{
			return;
		}

		DatabaseConfig.load();

		try
		{
			java.lang.Class.forName(DatabaseConfig.DATABASE_DRIVER).newInstance();
		}
		catch (Exception e)
		{
			log.fatal("Error obtaining DB driver", e);
			throw new Error("DB Driver doesnt exist!");
		}

		connectionPool = new GenericObjectPool();

		if (DatabaseConfig.DATABASE_CONNECTIONS_MIN > DatabaseConfig.DATABASE_CONNECTIONS_MAX)
		{
			log.error("Please check your database configuration. Minimum amount of connections is > maximum");
			DatabaseConfig.DATABASE_CONNECTIONS_MAX = DatabaseConfig.DATABASE_CONNECTIONS_MIN;
		}

		connectionPool.setMaxIdle(DatabaseConfig.DATABASE_CONNECTIONS_MIN);
		connectionPool.setMaxActive(DatabaseConfig.DATABASE_CONNECTIONS_MAX);

		try
		{
			dataSource = setupDataSource();
			getConnection().close();
		}
		catch (Exception e)
		{
			log.fatal("Error with connection string: " + DatabaseConfig.DATABASE_URL, e);
			throw new Error("DatabaseFactory not initialized!");
		}

		log.info("Successfully connected to database");
	}

	/**
	 * Sets up Connection Factory and Pool
	 * 
	 * @return DataSource configured datasource
	 * @throws Exception
	 *             if initialization failed
	 */
	private static DataSource setupDataSource() throws Exception
	{
		// Create Connection Factory
		ConnectionFactory conFactory = new DriverManagerConnectionFactory(DatabaseConfig.DATABASE_URL,
			DatabaseConfig.DATABASE_USER, DatabaseConfig.DATABASE_PASSWORD);

		// Makes Connection Factory Pool-able (Wrapper for two objects)
		new PoolableConnectionFactory(conFactory, connectionPool, null, null, false, true);

		// Create data source to utilize Factory and Pool
		return new PoolingDataSource(connectionPool);
	}

	/**
	 * Returns an active connection from pool. This function utilizes the dataSource which grabs an object from the
	 * ObjectPool within its limits. The GenericObjectPool.borrowObject()' function utilized in
	 * 'DataSource.getConnection()' does not allow any connections to be returned as null, thus a null check is not
	 * needed. Throws SQLException in case of a Failed Connection
	 * 
	 * @return Connection pooled connection
	 * @throws java.sql.SQLException
	 *             if can't get connection
	 */
	static Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}

	/**
	 * Returns number of active connections in the pool.
	 * 
	 * @return int Active DB Connections
	 */
	public int getActiveConnections()
	{
		return connectionPool.getNumActive();
	}

	/**
	 * Returns number of Idle connections. Idle connections represent the number of instances in Database Connections
	 * that have once been connected and now are closed and ready for re-use. The 'getConnection' function will grab
	 * idle connections before creating new ones.
	 * 
	 * @return int Idle DB Connections
	 */
	public int getIdleConnections()
	{
		return connectionPool.getNumIdle();
	}

	/**
	 * Shuts down pool and closes connections
	 */
	public static synchronized void shutdown()
	{
		try
		{
			connectionPool.close();
		}
		catch (Exception e)
		{
			log.warn("Failed to shutdown DatabaseFactory", e);
		}

		// set datasource to null so we can call init() once more...
		dataSource = null;
	}
}