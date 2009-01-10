/**
 * This file is part of aion-emu.
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
package aionemu.network.aion.serverpackets;

import aionemu.account.AuthResponse;
import aionemu.network.aion.AionServerPacket;

/**
 * @author KID
 */
public class LoginFail extends AionServerPacket
{
	private AuthResponse	response;

	public LoginFail(AuthResponse response)
	{
		this.response = response;
	}

	protected void writeImpl()
	{
		writeC(0x01);
		writeD(response.getMessageId());
	}

	public String getType()
	{
		return "0x01 LoginFail";
	}
}