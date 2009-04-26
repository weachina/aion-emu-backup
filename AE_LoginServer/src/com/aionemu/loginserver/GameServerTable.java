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
package com.aionemu.loginserver;

import org.apache.log4j.Logger;

import com.aionemu.loginserver.network.gameserver.GsAuthResponse;
import com.aionemu.loginserver.network.gameserver.GsConnection;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;

/**
 * @author -Nemesiss-
 */
public class GameServerTable
{
	private static final Logger	log	= Logger.getLogger(GameServerTable.class);

	private GameServerInfo[]	gameservers;

	public class GameServerInfo
	{
		private final int		id;
		private final String	ip;
		private final String	hex_id;
		private GsConnection	gs_conn;

		public GameServerInfo(int id, String ip, String hex_id)
		{
			this.id = id;
			this.ip = ip;
			this.hex_id = hex_id;
		}

		public GsConnection getGsConnection()
		{
			return gs_conn;
		}

		public void setGsConnection(GsConnection gs_conn)
		{
			this.gs_conn = gs_conn;
		}

		public int getId()
		{
			return id;
		}

		public String getIp()
		{
			return ip;
		}

		public String getHex_id()
		{
			return hex_id;
		}
	}

	public static void load()
	{
		// TODO! load from sql registred gameservers
	}

	public static GsAuthResponse registerGameServer(GsConnection gsConnection, int requestedId,
		String externalHost, String internalHost, int port, int max_palyers, String password)
	{
		// TODO!
		return GsAuthResponse.AUTHED;
	}

	public static void unregisterGameServer(GsConnection gsConnection)
	{
		log.info("GameServer " + gsConnection.getIP() + " unregistered.");
		// TODO!
	}
}
