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
package com.aionemu.loginserver.network.gameserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.log4j.Logger;

import com.aionemu.commons.network.AConnection;
import com.aionemu.commons.network.Dispatcher;
import com.aionemu.loginserver.GameServerTable;
import com.aionemu.loginserver.utils.ThreadPoolManager;

/**
 * Object representing connection between LoginServer and GameServer.
 * @author -Nemesiss-
 */
public class GsConnection extends AConnection
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(GsConnection.class);

	/**
	 * Possible states of GsConnection
	 */
	public static enum State
	{
		CONNECTED, AUTHED
	}

	/**
	 * Server Packet "to send" Queue
	 */
	private final Deque<GsServerPacket>	sendMsgQueue	= new ArrayDeque<GsServerPacket>();

	/**
	 * Current state of this connection
	 */
	private State						state;

	/**
	 * Constructor.
	 * 
	 * @param sc
	 * @param d
	 * @throws IOException
	 */
	public GsConnection(SocketChannel sc, Dispatcher d) throws IOException
	{
		super(sc, d);
		state = State.CONNECTED;

		String ip = getIP();
		log.info("GS connection from: " + ip);
	}

	/**
	 * Called by Dispatcher. ByteBuffer data contains one packet that should be processed.
	 * 
	 * @param data
	 * @return True if data was processed correctly, False if some error occurred and connection should be closed NOW.
	 */
	@Override
	public boolean processData(ByteBuffer data)
	{
		GsClientPacket pck = GsPacketHandler.handle(data, this);
		log.info("recived packet: " + pck);
		if (pck != null)
			ThreadPoolManager.getInstance().executeGsPacket(pck);
		return true;
	}

	/**
	 * This method will be called by Dispatcher, and will be repeated till return false.
	 * 
	 * @param data
	 * @return True if data was written to buffer, False indicating that there are not any more data to write.
	 */
	@Override
	protected final boolean writeData(ByteBuffer data)
	{
		synchronized (guard)
		{
			GsServerPacket packet = sendMsgQueue.pollFirst();
			if (packet == null)
				return false;

			packet.write(this, data);
			return true;
		}
	}

	/**
	 * This method is called by Dispatcher when connection is ready to be closed.
	 * 
	 * @return time in ms after witch onDisconnect() method will be called. Always return 0.
	 */
	@Override
	protected final long getDisconnectionDelay()
	{
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onDisconnect()
	{
		GameServerTable.unregisterGameServer(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onServerClose()
	{
		// TODO mb some packet should be send to gameserver before closing?
		close(/*packet,*/ true);
	}

	/**
	 * Sends GsServerPacket to this client.
	 * 
	 * @param bp
	 *            GsServerPacket to be sent.
	 */
	public final void sendPacket(GsServerPacket bp)
	{
		synchronized (guard)
		{
			/**
			 * Connection is already closed or waiting for last (close packet) to be sent
			 */
			if (isWriteDisabled())
				return;

			log.info("sending packet: " + bp);

			sendMsgQueue.addLast(bp);
			enableWriteInterest();
		}
	}

	/**
	 * Its guaranted that closePacket will be sent before closing connection, but all past and future packets wont.
	 * Connection will be closed [by Dispatcher Thread], and onDisconnect() method will be called to clear all other
	 * things. forced means that server shouldn't wait with removing this connection.
	 * 
	 * @param closePacket
	 *            Packet that will be send before closing.
	 * @param forced
	 *            have no effect in this implementation.
	 */
	public final void close(GsServerPacket closePacket, boolean forced)
	{
		synchronized (guard)
		{
			if (isWriteDisabled())
				return;

			log.info("sending packet: " + closePacket + "and closing connection after that.");

			pendingClose = true;
			isForcedClosing = forced;
			sendMsgQueue.clear();
			sendMsgQueue.addLast(closePacket);
			enableWriteInterest();
		}
	}

	/**
	 * @return Current state of this connection.
	 */
	public State getState()
	{
		return state;
	}

	/**
	 * @param state
	 *            Set current state of this connection.
	 */
	public void setState(State state)
	{
		this.state = state;
	}

	/**
	 * @return String info about this connection
	 */
	@Override
	public String toString()
	{
		return "GameServer "+getIP();
	}
}
