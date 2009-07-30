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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.Version;

/**
 * dunno wtf this packet is doing. Not sure about id/name
 * 
 * @author -Nemesiss-
 * 
 */
public class SM_ENTER_WORLD_CHECK extends AionServerPacket
{
	/**
	 * Constructs new <tt>SM_ENTER_WORLD_CHECK </tt> packet
	 */
	public SM_ENTER_WORLD_CHECK()
	{
		super(Version.Chiness ? 0xF4 : 0x04);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeOP(buf, getOpcode());
		writeC(buf, 0x00);
		writeC(buf, 0x00);
		writeC(buf, 0x00);
	}
}
