/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package mysql5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.loginserver.dao.AccountDAO;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.model.AccountTime;

/**
 * MySQL5 Account DAO implementation
 * 
 * @author SoulKeeper
 */
public class MySQL5AccountDAO extends AccountDAO
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(MySQL5AccountDAO.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account getAccount(String name)
	{
		Account account = null;
		PreparedStatement st = DB.prepareStatement("SELECT * FROM account_data ad " +
                "LEFT OUTER JOIN account_time at on ad.id = at.id  WHERE ad.`name` = ?");
		try
		{
			st.setString(1, name);
			ResultSet rs = st.executeQuery();
			if (rs.next())
			{
				account = new Account();
				account.setId(rs.getInt("id"));
				account.setName(name);
				account.setPasswordHash(rs.getString("password"));
				account.setAccessLevel(rs.getByte("access_level"));
				account.setLastServer(rs.getByte("last_server"));
				account.setLastIp(rs.getString("last_ip"));
				account.setIpForce(rs.getString("ip_force"));

                AccountTime accountTime = new AccountTime();
                accountTime.setLastLoginTime(rs.getTimestamp("last_active"));
                accountTime.setSessionDuration(rs.getLong("session_duration"));
                accountTime.setAccumulatedOnlineTime(rs.getLong("accumulated_online"));
                accountTime.setAccumulatedRestTime(rs.getLong("accumulated_rest"));
                accountTime.setPenaltyEnd(rs.getTimestamp("penalty_end"));
                accountTime.setExpirationTime(rs.getTimestamp("expiration_time"));

                account.setAccountTime(accountTime);
			}
		}
		catch (Exception e)
		{
			log.error("Can't select account with name: " + name, e);
		}
		finally
		{
			DB.close(st);
		}
		return account;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAccountId(String name)
	{
		int id = -1;
		PreparedStatement st = DB.prepareStatement("SELECT `id` FROM account_data WHERE `name` = ?");
		try
		{
			st.setString(1, name);
			ResultSet rs = st.executeQuery();
			rs.next();
			id = rs.getInt("id");
		}
		catch (SQLException e)
		{
			log.error("Can't select id after account insertion", e);
		}
		finally
		{
			DB.close(st);
		}
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAccountCount()
	{
		PreparedStatement st = DB.prepareStatement("SELECT count(*) AS c FROM account_data");
		ResultSet rs = DB.executeQuerry(st);
		try
		{
			rs.next();
			return rs.getInt("c");
		}
		catch (SQLException e)
		{
			log.error("Can't get account count", e);
		}
		finally
		{
			DB.close(st);
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean insertAccount(Account account)
	{

		int result = 0;
		PreparedStatement st = DB
			.prepareStatement("INSERT INTO account_data(`name`, `password`, access_level, last_server, last_ip, ip_force) VALUES (?, ?, ?, ?, ?, ?)");
		try
		{
			st.setString(1, account.getName());
			st.setString(2, account.getPasswordHash());
			st.setByte(3, account.getAccessLevel());
			st.setByte(4, account.getLastServer());
			st.setString(5, account.getLastIp());
			st.setString(6, account.getIpForce());
			result = st.executeUpdate();
		}
		catch (SQLException e)
		{
			log.error("Can't inser account", e);
		}
		finally
		{
			DB.close(st);
		}

		if (result > 0)
			account.setId(getAccountId(account.getName()));
		return result > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateAccount(Account account)
	{
		int result = 0;
		PreparedStatement st = DB
			.prepareStatement("UPDATE account_data SET `name` = ?, `password` = ?, access_level = ?, last_server = ?, last_ip = ?, ip_force = ? WHERE `id` = ?");
		try
		{
			st.setString(1, account.getName());
			st.setString(2, account.getPasswordHash());
			st.setByte(3, account.getAccessLevel());
			st.setByte(4, account.getLastServer());
			st.setString(5, account.getLastIp());
			st.setString(6, account.getIpForce());
			st.setInt(7, account.getId());
			st.executeUpdate();
		}
		catch (SQLException e)
		{
			log.error("Can't update account");
		}
		finally
		{
			DB.close(st);
		}
		return result > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateAccountTime(final int accountId, final AccountTime accountTime)
	{
		return DB.insertUpdate("REPLACE INTO account_time (id, last_active, expiration_time, " +
                "session_duration, accumulated_online, accumulated_rest, penalty_end) values " +
                "(?,?,?,?,?,?,?)", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
                preparedStatement.setLong(1, accountId);
				preparedStatement.setTimestamp(2, accountTime.getLastLoginTime());
                preparedStatement.setTimestamp(3, accountTime.getExpirationTime());
                preparedStatement.setLong(4, accountTime.getSessionDuration());
                preparedStatement.setLong(5, accountTime.getAccumulatedOnlineTime());
                preparedStatement.setLong(6, accountTime.getAccumulatedRestTime());
				preparedStatement.setTimestamp(7, accountTime.getPenaltyEnd());
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateLastServer(final int accountId, final byte lastServer)
	{
		return DB.insertUpdate("UPDATE account_data SET last_server = ? WHERE id = ?", new IUStH() {
			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setByte(1, lastServer);
				preparedStatement.setInt(2, accountId);
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateLastIp(final int accountId, final String ip)
	{
		return DB.insertUpdate("UPDATE account_data SET last_ip = ? WHERE id = ?", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException
			{
				preparedStatement.setString(1, ip);
				preparedStatement.setInt(2, accountId);
				preparedStatement.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String database, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(database, majorVersion, minorVersion);
	}
}
