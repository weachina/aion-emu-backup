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
package com.aionemu.loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import com.aionemu.loginserver.network.aion.AionConnection;
import com.aionemu.loginserver.network.aion.AionServerPacket;
import com.aionemu.loginserver.network.aion.AionAuthResponse;

/**
 * @author -Nemesiss-
 * 
 */
public class SM_PLAY_FAIL extends AionServerPacket
{
	/**
	 * response - why play fail
	 */
	private AionAuthResponse	response;

	/**
	 * Constructor
	 * 
	 * @param response
	 */
	public SM_PLAY_FAIL(AionAuthResponse response)
	{
		this.response = response;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, 0x06);
		writeD(buf, response.getMessageId());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType()
	{
		return "0x01 SM_PLAY_FAIL";
	}
}
