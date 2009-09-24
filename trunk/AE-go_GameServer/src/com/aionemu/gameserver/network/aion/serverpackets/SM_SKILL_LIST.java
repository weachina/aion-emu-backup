/**
 * This file is part of aion-emu <aion-emu.com>.
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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.Map;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * In this packet Server is sending Skill Info?
 * 
 * @author -Nemesiss-
 * 
 */
public class SM_SKILL_LIST extends AionServerPacket
{
	private Player player;
	
	/**
	 * Constructs new <tt>SM_SKILL_LIST </tt> packet
	 */
	
	public SM_SKILL_LIST(Player player)
	{
		this.player = player;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, 4);// skills list size
		// for skills
		// {
		writeH(buf, 1351);// id (3001)
		writeD(buf, 1);// lvl
		writeD(buf, 0);// use time? [s]
		writeC(buf, 0);// unk
		writeH(buf, 1373);// id (3001)
		writeD(buf, 1);// lvl
		writeD(buf, 0);// use time? [s]
		writeC(buf, 0);// unk
		writeH(buf, 1801);// id (3001)
		writeD(buf, 1);// lvl
		writeD(buf, 0);// use time? [s]
		writeC(buf, 0);// unk
		writeH(buf, 1803);// id (3001)
		writeD(buf, 1);// lvl
		writeD(buf, 0);// use time? [s]
		writeC(buf, 0);// unk
		// }
		writeD(buf, 0); //unk
		
		final int size = player.getSkillList().getSize();
		writeH(buf, size);//skills list size
				
		if (size > 0)
		{
			for (Map.Entry<Integer, Integer> entry : player.getSkillList().entrySet())
			{
				writeH(buf, entry.getKey());//id
				writeD(buf, entry.getValue());//lvl
				writeD(buf, 0);//use time? [s]
				writeC(buf, 0);//unk
			}
		}
		writeD(buf, 0);// unk

	}
}
