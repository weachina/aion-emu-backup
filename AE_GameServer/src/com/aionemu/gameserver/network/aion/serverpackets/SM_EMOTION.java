/*
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

package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.Version;

/**
 * Emotion packet
 * 
 * @author SoulKeeper
 */
public class SM_EMOTION extends AionServerPacket
{

	/**
	 * Object id of emotion sender
	 */
	private int	senderObjectId;

	/**
	 * Some unknown variable
	 */
	private int	unknown;

	/**
	 * ID of emotion
	 */
	private int	emotionId;

	/**
	 * Constructs new server packet with specified opcode
	 * 
	 * @param senderObjectId
	 *            who sended emotion
	 * @param unknown
	 *            Dunno what it is, can be 0x10 or 0x11
	 * @param emotionId
	 *            emotion to play
	 */
	public SM_EMOTION(int senderObjectId, int unknown, int emotionId)
	{
		super(Version.Chiness ? 0x14 : 0x24);
		this.senderObjectId = senderObjectId;
		this.emotionId = emotionId;
		this.unknown = unknown;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeOP(buf, getOpcode());
		writeD(buf, senderObjectId);
		writeC(buf, unknown);
		writeC(buf, 0x01); // unknown
		writeC(buf, 0x00); // unknown
		writeH(buf, 0x00); // unknown
		writeC(buf, 0xC0); // unknown
		writeC(buf, 0x40); // unknown
		if(unknown == 0x10)
		{
			writeD(buf, 0x00); // unknown
			writeH(buf, emotionId);
			writeC(buf, 0x01); // unknown
		}
	}
}
