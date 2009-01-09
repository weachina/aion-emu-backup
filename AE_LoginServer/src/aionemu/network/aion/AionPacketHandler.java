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
package aionemu.network.aion;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import aionemu.network.aion.AionConnection.State;
import aionemu.network.aion.clientpackets.AuthGameGuard;
import aionemu.network.aion.clientpackets.RequestAuthLogin;
import aionemu.network.aion.clientpackets.RequestServerList;
import aionemu.network.aion.clientpackets.RequestServerLogin;

/**
 * @author -Nemesiss-
 */
public class AionPacketHandler
{
	private static final Logger	log	= Logger.getLogger(AionPacketHandler.class.getName());

	public static AionClientPacket handle(ByteBuffer data, AionConnection client)
	{
		AionClientPacket msg = null;
		State state = client.getState();
		int id = data.get() & 0xff;

		switch (state)
		{
			case CONNECTED:
			{
				switch (id)
				{
					case 0x07:
						msg = new AuthGameGuard(data, client);
						break;
					default:
						unkownPacket(state, id);
				}
				break;
			}
			case AUTHED_GG:
			{
				switch (id)
				{
					case 0x00:
						msg = new RequestAuthLogin(data, client);
						break;
					default:
						unkownPacket(state, id);
				}
				break;
			}
			case AUTHED_LOGIN:
			{
				switch (id)
				{
					case 0x05:
						msg = new RequestServerList(data, client);
						break;
					case 0x02:
						msg = new RequestServerLogin(data, client);
						break;
					default:
						unkownPacket(state, id);
				}
				break;
			}
		}
		return msg;
	}

	private static final void unkownPacket(State state, int id)
	{
		log.info("Unkown packet recived from Aion client: " + id + " state=" + state);
	}
}
