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
 * This packet is used to update mp / max mp value.
 * 
 * @author Luno
 * 
 */
public class SM_STATUPDATE_MP extends AionServerPacket
{

	private int	currentHp;
	private int	maxHp;

	/**
	 * 
	 * @param currentHp
	 * @param maxHp
	 */
	public SM_STATUPDATE_MP(int currentHp, int maxHp)
	{
		super(0x14); // EU only

		this.currentHp = currentHp;
		this.maxHp = maxHp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeOP(buf, getOpcode());

		writeD(buf, currentHp);
		writeD(buf, maxHp);
	}

}
