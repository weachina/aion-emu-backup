/*
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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.Random;

/**
 * 
 * @author alexa026
 * 
 */
public class SM_BUYLIST extends AionServerPacket
{

	private int	targetObjectId;
	private int	listid;
	private int	type;
	
	public SM_BUYLIST(int targetObjectId,int listid ,int type)
	{
		this.targetObjectId = targetObjectId;
		this.listid = listid ;// empty
		this.type = type;// empty
	}

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{		

	
		writeD(buf, targetObjectId);
		writeD(buf, listid); //<---- this structure is wrong and need futher investigation for 1.5.x
		writeC(buf, 0); // unknown
		writeH(buf, 1); // unknown
		writeD(buf, type); // unknown

	}	
}