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

/**
 * 
 * @author alexa026
 * 
 */
public class SM_DIALOG extends AionServerPacket
{
	
	private int	targetObjectId;
	private int	unk;
	
	public SM_DIALOG(int targetObjectId, int unk)
	{
		this.targetObjectId = targetObjectId;
		this.unk = unk;
	}
	/*public SM_DIALOG(int targetObjectId)
	{
		this.targetObjectId = targetObjectId;
		
	}*/

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{		
		writeD(buf, targetObjectId);
		//writeD(buf, 4688); // unknown
		writeD(buf, unk); // unknown
		writeC(buf, 0); // unknown

	}	
}
