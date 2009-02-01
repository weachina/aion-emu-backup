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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

/**
 * <b>DB Documentation</b>
 * <p>
 * This class is used for making SQL query's utilizing the database connection
 * defined in database.properties<br>
 * <br>
 * Here are the functions that one may use to utilize this class in creating an
 * ease of access to database information.
 * </p>
 * <hr>
 * <b>SELECT (select method)</b>
 * <p>
 * Parameters:
 * <ul>
 * <li><b>Required: String query</b> - Query that will be utilized in select
 * statement.</li>
 * <li><b>Required: ReadStH reader</b> - Interface implementation used to read
 * output ResultSet from select statement.</li>
 * <li><i>Optional: String errMsg</i> - Custom error message that will be
 * logged if query fails.</li>
 * </ul>
 * Returns:(<b>boolean </b>) Returns true if the query ran successfully.<br>
 * <br>
 * Purpose:<br>
 * The select function one to grab data from the database with ease. Utilizing
 * the ReadStT, one may set up query parameters (then use ParamReadStH and set
 * params in <code>setParams()</code>) and read the replied data from the
 * query easily.<br>
 * <br>
 * Best practices is to create custom classes that implement ReadStT in order to
 * throw the data around.<br>
 * <br>
 * After the function is called, it automatically closes and recycles the SQL
 * Connection.<br>
 * <br>
 * Example:
 * 
 * <pre>
 * DB.select(&quot;SELECT name FROM test_table WHERE id=?&quot;, new ParamReadStH() {
 * 	public void setParams(PreparedStatement stmt) throws SQLException
 * 	{
 * 		stmt.setInt(1, 50);
 * 	}
 * 
 * 	public void handleRead(ResultSet rset) throws SQLException
 * 	{
 * 		while (rset.next())
 * 		{
 * 			// Usually here in the custom class you would set it to your needed var.
 * 			var = rset.getString(&quot;name&quot;);
 * 		}
 * 	}
 * });
 * </pre>
 * 
 * </p>
 * <hr>
 * <b>INSERT / UPDATE (insertUpdate method)</b>
 * <p>
 * Parameters:
 * <ul>
 * <li><b>Required: String query</b> - Query that will be executed in
 * insert/update statement.</li>
 * <li><b>Required: IUStT batch</b> - Util used to modify query parameters OR
 * add add batches.</li>
 * <li><i>Optional: String errMsg</i> - Custom error message that will be
 * logged if query fails.</li>
 * </ul>
 * Returns:(<b>boolean</b>) Returns true if the query ran successfully.<br>
 * <br>
 * Purpose:<br>
 * The insertUpdate function allows one to insert and update database entries.
 * One may utilize it without needing to modify the query at all or provide a
 * IUStH interface implementation to add parameters to statement and/or gather
 * them in batch.<br>
 * <br>
 * <b> If the IUStH util IS provided in the function's parameters - The coder
 * MUST call the functions stmt.executeBatch() OR stmt.executeUpdate() in order
 * to successfully run the query.<br>
 * If IUSth util is NOT provided, the query will execute as it is. </b><br>
 * <br>
 * Best practices is to create custom classes that implement IUStT in order to
 * modify the query in proficient manners.<br>
 * <br>
 * After the function is called, it automatically closes and recycles the SQL
 * Connection.<br>
 * <br>
 * Example:<br>
 * 
 * <pre>
 * DB.insertUpdate(&quot;UPDATE test_table SET some_column=1&quot;);
 * </pre>
 * 
 * <br>
 * 
 * <pre>
 * DB.insertUpdate(&quot;INSERT INTO test_table VALUES (?)&quot;, new IUStH() {
 * 	public void handleInsertUpdate(PreparedStatement stmt)
 * 	{
 * 		// Usually this would be data from the custom class that implements IUSth
 * 		String[] batchTestVars = { &quot;bob&quot;, &quot;mike&quot;, &quot;joe&quot; };
 * 
 * 		for (String n : batchTestVars)
 * 		{
 * 			stmt.setString(1, n);
 * 			stmt.addBatch();
 * 		}
 * 
 * 		// REQUIRED
 * 		stmt.executeBatch();
 * 	}
 * });
 * 
 * </pre>
 * 
 * <br>
 * 
 * <pre>
 * DB.insertUpdate(&quot;UPDATE test_table SET some_column=? WHERE other_column=?&quot;, new IUStH() {
 * 	public void handleInsertUpdate(PreparedStatement stmt)
 * 	{
 * 		stmt.setString(1, &quot;xxx&quot;);
 * 		stmt.setInt(2, 10);
 * 		stmt.executeUpdate();
 * 	}
 * });
 * 
 * </pre>
 * 
 * </p>
 * 
 * @author Disturbing
 */
public final class DB
{
	/** Logger */
	protected static final Logger	log	= Logger.getLogger(DB.class.getName());

	/**
	 * Empty Constructor
	 */
	private DB()
	{

	}

	/**
	 * Executes Select Query. Uses ReadSth to utilize params and return data.
	 * Recycles connection after competion.
	 * 
	 * @param query
	 * @param reader
	 * @return boolean Success
	 */
	public static boolean select(String query, ReadStH reader)
	{
		return select(query, reader, null);
	}

	/**
	 * Executes Select Query. Uses ReadSth to utilize params and return data.
	 * Recycles connection after completion.
	 * 
	 * @param query
	 * @param reader
	 * @param errMsg
	 * @return boolean Success
	 */
	public static boolean select(String query, ReadStH reader, String errMsg)
	{
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rset;
		boolean error = false;

		try
		{
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(query);
			if (reader instanceof ParamReadStH)
				((ParamReadStH) reader).setParams(stmt);
			rset = stmt.executeQuery();
			reader.handleRead(rset);
		}
		catch (Exception e)
		{
			if (errMsg == null)
				log.warn("Error executing select query " + e, e);
			else
				log.warn(errMsg + " " + e, e);
			error = true;
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
				if (stmt != null)
					stmt.close();
			}
			catch (Exception e)
			{
				log.warn("Failed to close DB connection " + e, e);
			}
		}

		return error;
	}

	/**
	 * Executes Insert or Update Query not needing any further modification or
	 * batching. Recycles connection after completion.
	 * 
	 * @param query
	 * @return boolean Success
	 */
	public static boolean insertUpdate(String query)
	{
		return insertUpdate(query, null, null);
	}

	/**
	 * Executes Insert or Update Query not needing any further modification or
	 * batching. Recycles connection after completion.
	 * 
	 * @param query
	 * @param errMsg
	 * @return success
	 */
	public static boolean insertUpdate(String query, String errMsg)
	{
		return insertUpdate(query, null, errMsg);
	}

	/**
	 * Executes Insert / Update Query. Utilizes IUSth for Batching and Query
	 * Editing. MUST MANUALLY EXECUTE QUERY / BATACH IN IUSth (No need to close
	 * Statement after execution)
	 * 
	 * @param query
	 * @param batch
	 * @return boolean Success
	 */
	public static boolean insertUpdate(String query, IUStH batch)
	{
		return insertUpdate(query, batch, null);
	}

	/**
	 * Executes Insert or Update Query. Utilizes IUSth for Batching and Query
	 * Editing. Defines custom error message if error occurs. MUST MANUALLY
	 * EXECUTE QUERY / BATACH IN IUSth (No need to Statement after execution)
	 * Recycles connection after completion
	 * 
	 * @param query
	 * @param batch
	 * @param errMsg
	 * @return boolean Success
	 */
	public static boolean insertUpdate(String query, IUStH batch, String errMsg)
	{
		Connection con = null;
		PreparedStatement stmt = null;
		boolean success = true;

		try
		{
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(query);
			if (batch != null)
				batch.handleInsertUpdate(stmt);
			else
				stmt.executeUpdate();

		}
		catch (Exception e)
		{
			if (errMsg == null)
				log.warn("Failed to execute IU query " + e, e);
			else
				log.warn(errMsg + " " + e, e);

			success = false;
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
				if (stmt != null)
					stmt.close();
			}
			catch (Exception e)
			{
				log.warn("Failed to close DB connection " + e, e);
			}
		}

		return success;
	}
}
