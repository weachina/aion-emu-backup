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
package com.aionemu.gameserver.network.loginserver;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.aionemu.commons.network.packet.BaseClientPacket;

/**
 * @author -Nemesiss-
 * 
 */
public abstract class LsClientPacket extends BaseClientPacket implements Cloneable
{
	/**
	 * Logger for this class.
	 */
	private static final Logger		log	= Logger.getLogger(LsClientPacket.class);
	/**
	 * Owner of this packet.
	 */
	private LoginServerConnection	client;

	/**
	 * Constructs new client packet with specified opcode.
	 * 
	 * @param buf
	 *            packet data
	 * @param client
	 *            packet owner
	 * @param opcode
	 *            packet id
	 */
	protected LsClientPacket(ByteBuffer buf, LoginServerConnection client, int opcode)
	{
		super(buf, opcode);
		this.client = client;
	}

	/**
	 * Constructs new client packet with specified opcode. If using this constructor, user must later manually set
	 * buffer and connection.
	 * 
	 * @param opcode
	 *            packet id
	 */
	protected LsClientPacket(int opcode)
	{
		super(opcode);
	}

	/**
	 * Attaches connection object to this packet.
	 * 
	 * @param connection
	 */
	public void setConnection(LoginServerConnection connection)
	{
		this.client = connection;
	}

	/**
	 * run runImpl catching and logging Throwable.
	 */
	public final void run()
	{
		try
		{
			runImpl();
		}
		catch(Throwable e)
		{
			log.warn("error handling ls (" + getConnection().getIP() + ") message " + this, e);
		}
	}

	/**
	 * @return Connection that is owner of this packet.
	 */
	public final LoginServerConnection getConnection()
	{
		return client;
	}

	/**
	 * Send new LsServerPacket to connection that is owner of this packet. This method is equivalent to:
	 * getConnection().sendPacket(msg);
	 * 
	 * @param msg
	 */
	protected void sendPacket(LsServerPacket msg)
	{
		getConnection().sendPacket(msg);
	}

	/**
	 * Clones this packet object.
	 * 
	 * @return
	 */
	public LsClientPacket clonePacket()
	{
		try
		{
			return (LsClientPacket) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			return null;
		}
	}
}
