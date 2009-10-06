package mysql5;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.PlayerMacrossesDAO;
import com.aionemu.gameserver.model.gameobjects.player.MacroList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import mysql5.MySQL5DAOUtils;

import org.apache.log4j.Logger;

/**
 * Created on: 13.07.2009 19:33:07
 *
 * @author Aquanox
 */
public class MySQL5PlayerMacrossesDAO extends PlayerMacrossesDAO
{
	private static Logger log = Logger.getLogger(MySQL5PlayerMacrossesDAO.class);
	
	public static final String INSERT_QUERY = "INSERT INTO `player_macrosses` (`player_id`, `order`, `macro`) VALUES (?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `player_macrosses` WHERE `player_id`=? AND `order`=?";
	public static final String SELECT_QUERY = "SELECT `order`, `macro` FROM `player_macrosses` WHERE `player_id`=?";

	/**
	 * Add a macro information into database
	 *
	 * @param playerId player object id
	 * @param macro    macro contents.
	 */
	@Override
	public void addMacro(final int playerId, final int macroPosition, final String macro)
	{
		DB.insertUpdate(INSERT_QUERY, new IUStH() {
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				log.debug("[DAO: MySQL5PlayerMacrossesDAO] storing macro "+playerId+" "+macroPosition);
				stmt.setInt(1, playerId);
				stmt.setInt(2, macroPosition);
				stmt.setString(3, macro);
				stmt.execute();
			}
		});
	}

	/** {@inheritDoc} */
	@Override
	public void deleteMacro(final int playerId, final int macroPosition)
	{
		DB.insertUpdate(DELETE_QUERY, new IUStH()
		{
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				log.debug("[DAO: MySQL5PlayerMacrossesDAO] removing macro "+playerId+" "+macroPosition);
				stmt.setInt(1, playerId);
				stmt.setInt(2, macroPosition);
				stmt.execute();
			}
		});
	}

	/** {@inheritDoc} */
	@Override
	public MacroList restoreMacrosses(final int playerId)
	{
		final Map<Integer, String> macrosses = new HashMap<Integer, String>();
		DB.select(SELECT_QUERY, new ParamReadStH()
		{
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, playerId);
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException
			{
				log.debug("[DAO: MySQL5PlayerMacrossesDAO] loading macroses for playerId: "+playerId);
				while(rset.next())
				{
					int order = rset.getInt("order");
					String text = rset.getString("macro");

					macrosses.put(order, text);
				}
			}
		});
		return new MacroList(macrosses);
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
