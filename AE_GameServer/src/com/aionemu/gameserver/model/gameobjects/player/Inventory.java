/**
 * This file is part of aion-unique <aionunique.smfnew.com>.
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

package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Random;

import com.aionemu.gameserver.configs.Config;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;

/**
 *
 * @author Avol
 */
public class Inventory
{
	public int kinah;
	public int totalItemsCount;
	public int itemUniqueIdArray[];
	public int itemIdArray[];
	public int itemNameIdArray[];
	public int itemCountArray[];
	public int totalDbItemsCount;
	
	public void getInventoryFromDb(int activePlayer) {
		PreparedStatement ps = DB.prepareStatement("SELECT `itemUniqueId`, `itemId`, `itemNameId`,`itemCount`FROM `inventory` WHERE `itemOwner`=" + activePlayer);
		try
		{
			ResultSet rs = ps.executeQuery();
			rs.last();
			int row = rs.getRow();
			int row2 = 0;
			totalItemsCount = row;
			itemUniqueIdArray = new int[row+1];
			itemIdArray = new int[row+1];
			itemNameIdArray = new int[row+1];
			itemCountArray = new int[row+1];
			
			while (row > 0) {
				rs.absolute(row);
				itemUniqueIdArray[row2] = rs.getInt("itemUniqueId");
				itemIdArray[row2] = rs.getInt("itemId");
				itemNameIdArray[row2] = rs.getInt("itemNameId");
				itemCountArray[row2] = rs.getInt("itemCount");
				row2 = row2 +1;
				row = row - 1;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(Inventory.class).error("Error loading items", e);
			
		}
		finally
		{
			DB.close(ps);
		}
	}

	public void getKinahFromDb(int activePlayer) {
		PreparedStatement ps2 = DB.prepareStatement("SELECT `kinah` FROM `players` WHERE `id`=" + activePlayer);
		try
		{
			ResultSet rs = ps2.executeQuery();
			rs.absolute(1);
			kinah = rs.getInt("kinah");
		}
		catch(SQLException e)
		{
			Logger.getLogger(Inventory.class).error("Error loading kinah", e);
		}
		finally
		{
			DB.close(ps2);
		}
	}

	public void getDbItemsCountFromDb() {
		PreparedStatement ps5 = DB.prepareStatement("SELECT * FROM `inventory`");
		try
		{
			ResultSet rs = ps5.executeQuery();
			rs.last();
			totalDbItemsCount = rs.getRow();
		}
		catch(SQLException e)
		{
			Logger.getLogger(Inventory.class).error("Error loading kinah", e);
		}
		finally
		{
			DB.close(ps5);
		}
	}

	public void putKinahToDb(int activePlayer, int count) {
		PreparedStatement ps3 = DB.prepareStatement("UPDATE `players` SET `kinah` = ? WHERE `id`= ? ");
		try
		{
			ps3.setInt(1, count);
			ps3.setInt(2, activePlayer);
			ps3.executeUpdate();
		}
		catch(SQLException e)
		{
			Logger.getLogger(Inventory.class).error("Error storing kinah", e);
		}
		finally
		{
			DB.close(ps3);
		}
	}

	public void putItemToDb(int activePlayer, int itemId, int itemNameId, int itemCount) {
		PreparedStatement ps4 = DB.prepareStatement("INSERT INTO `inventory` (`itemId`,`itemNameId`,`itemCount`,`itemOwner`) VALUES(?,?,?,?)");
		try
		{
			ps4.setInt(1, itemId);
			ps4.setInt(2, itemNameId);
			ps4.setInt(3, itemCount);
			ps4.setInt(4, activePlayer);
			ps4.executeUpdate();
		}
		catch(SQLException e)
		{
			Logger.getLogger(Inventory.class).error("Error storing item", e);
		}
		finally
		{
			DB.close(ps4);
		}
	}

	public int getKinahCount() {
		return kinah;
	}

	public int getDbItemsCount() {
		return totalDbItemsCount;
	}

	public int getItemsCount() {
		return totalItemsCount;
	}

	public int getItemUniqueIdArray(int row) {
		return itemUniqueIdArray[row];
	}
	public int getItemIdArray(int row) {
		return itemIdArray[row];
	}
	public int getItemNameIdArray(int row) {
		return itemNameIdArray[row];
	}
	public int getItemCountArray(int row) {
		return itemCountArray[row];
	}
}