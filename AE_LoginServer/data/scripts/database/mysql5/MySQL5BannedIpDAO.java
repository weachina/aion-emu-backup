package mysql5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.commons.database.ReadStH;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.scripting.metadata.OnClassLoad;
import com.aionemu.commons.scripting.metadata.OnClassUnload;
import com.aionemu.loginserver.dao.BannedIpDAO;
import com.aionemu.loginserver.model.BannedIP;

/**
 * BannedIP DAO implementation for MySQL5
 * 
 * @author SoulKeeper
 */
public class MySQL5BannedIpDAO extends BannedIpDAO
{

	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(MySQL5BannedIpDAO.class);

	/**
	 * Register dao on class load
	 */
	@OnClassLoad
	public static void onClassLoad()
	{
		try
		{
			DAOManager.registerDAO(MySQL5BannedIpDAO.class);
		}
		catch (IllegalAccessException e)
		{
			log.error("Can't register DAO", e);
		}
		catch (InstantiationException e)
		{
			log.error("Can't register DAO", e);
		}
	}

	/**
	 * Unregister DAO on class unload
	 */
	@OnClassUnload
	public static void onClassUnload()
	{
		DAOManager.unregisterDAO(MySQL5BannedIpDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BannedIP insert(String mask)
	{
		return insert(mask, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BannedIP insert(final String mask, final Timestamp expireTime)
	{
		BannedIP result = new BannedIP();
		result.setMask(mask);
		result.setTimeEnd(expireTime);

		if (insert(result))
		{
			return result;
		}
		else
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean insert(final BannedIP bannedIP)
	{
		boolean insert = DB.insertUpdate("INSERT INTO ban_ip(mask, time_end) VALUES (?, ?)", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1, bannedIP.getMask());
				if (bannedIP.getTimeEnd() == null)
				{
					preparedStatement.setNull(2, Types.TIMESTAMP);
				}
				else
				{
					preparedStatement.setTimestamp(2, bannedIP.getTimeEnd());
				}
				preparedStatement.execute();
			}
		});

		if (!insert)
		{
			return false;
		}

		final BannedIP result = new BannedIP();
		DB.select("SELECT * FROM ban_ip WHERE mask = ?", new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1, bannedIP.getMask());
			}

			@Override
			public void handleRead(ResultSet resultSet) throws SQLException
			{
				resultSet.next(); // mask is unique, only one result allowed
				result.setId(resultSet.getInt("id"));
				result.setMask(resultSet.getString("mask"));
				result.setTimeEnd(resultSet.getTimestamp("time_end"));
			}
		});

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update(final BannedIP bannedIP)
	{
		return DB.insertUpdate("UPDATE banned_ip SET mask = ?, time_end = ? WHERE id = ?", new IUStH() {
			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1, bannedIP.getMask());
				if (bannedIP.getTimeEnd() == null)
				{
					preparedStatement.setNull(2, Types.TIMESTAMP);
				}
				else
				{
					preparedStatement.setTimestamp(2, bannedIP.getTimeEnd());
				}
				preparedStatement.setInt(3, bannedIP.getId());
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(final String mask)
	{
		return DB.insertUpdate("DELETE FROM banned_ip WHERE mask = ?", new IUStH() {
			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1, mask);
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(final BannedIP bannedIP)
	{
		return DB.insertUpdate("DELETE FROM banned_ip WHERE id = ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setInt(1, bannedIP.getId());
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<BannedIP> getAllBans()
	{

		final Set<BannedIP> result = new HashSet<BannedIP>();
		DB.select("SELECT * FROM banned_ip", new ReadStH() {
			@Override
			public void handleRead(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					BannedIP ip = new BannedIP();
					ip.setId(resultSet.getInt("id"));
					ip.setMask(resultSet.getString("mask"));
					ip.setTimeEnd(resultSet.getTimestamp("time_end"));
					result.add(ip);
				}
			}
		});
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
