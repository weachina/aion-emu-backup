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
package com.aionemu.loginserver.network;

import com.aionemu.commons.network.nio.NioServer;
import com.aionemu.commons.network.nio.ServerCfg;
import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.network.aion.AionAcceptor;
import com.aionemu.loginserver.network.gameserver.GsAcceptor;

/**
 * @author -Nemesiss-
 */
public class IOServer
{
	private final static NioServer	instance	= new NioServer(Config.NIO_READ_THREADS, Config.NIO_WRITE_THREADS,
													new ServerCfg(Config.LOGIN_BIND_ADDRESS, Config.LOGIN_PORT,
														new AionAcceptor()), new ServerCfg("127.0.0.1", 9014,
														new GsAcceptor()));

	public static NioServer getInstance()
	{
		return instance;
	}
}
