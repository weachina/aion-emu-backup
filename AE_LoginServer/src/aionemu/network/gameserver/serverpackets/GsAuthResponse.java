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
package aionemu.network.gameserver.serverpackets;

import aionemu.network.gameserver.GsServerPacket;
import aionemu_commons.network.IServerPacket;

/**
 * @author -Nemesiss-
 */
public class GsAuthResponse extends GsServerPacket implements IServerPacket
{
	public static final int	RESPONSE_OK				= 0;
	public static final int	RESPONSE_WRONG_HEXID	= 1;

	private final int		response;

	public GsAuthResponse(int response)
	{
		this.response = response;
	}

	@Override protected void writeImpl()
	{
		writeC(0x00);
		writeC(response);
	}

	@Override public String getType()
	{
		return "0x00 GsAuthResponse";
	}
}
