/*
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
package com.aionemu.gameserver.network.factories;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionPacketHandler;
import com.aionemu.gameserver.network.aion.Version;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHARACTER_LIST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHAT_MESSAGE_PUBLIC;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHAT_MESSAGE_WHISPER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHECK_NICKNAME;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CLIENT_COMMAND_LOC;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CREATE_CHARACTER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_DELETE_CHARACTER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_EMOTION;
import com.aionemu.gameserver.network.aion.clientpackets.CM_ENTER_WORLD;
import com.aionemu.gameserver.network.aion.clientpackets.CM_L2AUTH_LOGIN_CHECK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEVEL_READY;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LOGIN_OUT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MACRO_CREATE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MACRO_DELETE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MAC_ADDRESS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MAC_ADDRESS2;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MAY_LOGIN_INTO_GAME;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MOVE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PING;
import com.aionemu.gameserver.network.aion.clientpackets.CM_QUIT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_RECONNECT_AUTH;
import com.aionemu.gameserver.network.aion.clientpackets.CM_RESTORE_CHARACTER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_TARGET_SELECT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_TIME_CHECK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_VERSION_CHECK;
import com.google.inject.Injector;

/**
 * This factory is responsible for creating {@link AionPacketHandler} object. It also initializes created handler with a
 * set of packet prototypes.<br>
 * Object of this classes uses <tt>Injector</tt> for injecting dependencies into prototype objects.<br>
 * <br>
 * 
 * @author Luno
 * 
 */
public class AionPacketHandlerFactory
{
	private Injector			injector;
	private AionPacketHandler	handler	= new AionPacketHandler();

	/**
	 * Creates new instance of <tt>AionPacketHandlerFactory</tt><br>
	 * 
	 * @param injector
	 */
	public AionPacketHandlerFactory(Injector injector)
	{
		this.injector = injector;

		addPacket(new CM_RECONNECT_AUTH(Version.Chiness ? 0x9B : 0x99), State.AUTHED);
		addPacket(new CM_L2AUTH_LOGIN_CHECK(Version.Chiness ? 0xB9 : 0xBF), State.CONNECTED);
		addPacket(new CM_VERSION_CHECK(Version.Chiness ? 0x4c : 0x50), State.CONNECTED);
		addPacket(new CM_TIME_CHECK(Version.Chiness ? 0x3e : 0x42), State.CONNECTED, State.AUTHED, State.IN_GAME);
		addPacket(new CM_TARGET_SELECT(Version.Chiness ? 0x33 : 0x31), State.IN_GAME);
		addPacket(new CM_RESTORE_CHARACTER(Version.Chiness ? 0xB5 : 0xBB), State.AUTHED);
		addPacket(new CM_QUIT(Version.Chiness ? 0x4F : 0x4D), State.AUTHED, State.IN_GAME);
		addPacket(new CM_PING(Version.Chiness ? 0x20 : 0x24), State.AUTHED, State.IN_GAME);
		addPacket(new CM_MOVE(Version.Chiness ? 0x1C : 0x20), State.IN_GAME);
		addPacket(new CM_MAY_LOGIN_INTO_GAME(Version.Chiness ? 0x96 : 0x9A), State.AUTHED);
		addPacket(new CM_MACRO_DELETE(Version.Chiness ? 0x9C : 0xA0), State.IN_GAME);
		addPacket(new CM_MACRO_CREATE(Version.Chiness ? 0xA3 : 0xA1), State.IN_GAME);
		addPacket(new CM_MAC_ADDRESS2(Version.Chiness ? 0xA2 : 0xA6), State.IN_GAME);
		addPacket(new CM_MAC_ADDRESS(Version.Chiness ? 0x91 : 0x97), State.CONNECTED, State.AUTHED, State.IN_GAME);
		addPacket(new CM_LOGIN_OUT(Version.Chiness ? 0x4E : 0x52), State.AUTHED, State.IN_GAME);
		addPacket(new CM_LEVEL_READY(Version.Chiness ? 0x45 : 0x4B), State.IN_GAME);
		addPacket(new CM_ENTER_WORLD(Version.Chiness ? 0x44 : 0x48), State.AUTHED);
		addPacket(new CM_EMOTION(Version.Chiness ? 0x27 : 0x25), State.IN_GAME);
		addPacket(new CM_DELETE_CHARACTER(Version.Chiness ? 0xB4 : 0xB8), State.AUTHED);
		addPacket(new CM_CREATE_CHARACTER(Version.Chiness ? 0xBB : 0xB9), State.AUTHED);
		addPacket(new CM_CLIENT_COMMAND_LOC(Version.Chiness ? 0xC2 : 0xC6), State.IN_GAME);
		addPacket(new CM_CHECK_NICKNAME(Version.Chiness ? 0x9D : 0xA3), State.AUTHED);
		addPacket(new CM_CHAT_MESSAGE_WHISPER(Version.Chiness ? 0x30 : 0x34), State.IN_GAME);
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(Version.Chiness ? 0x37 : 0x35), State.IN_GAME);
		addPacket(new CM_CHARACTER_LIST(Version.Chiness ? 0xBA : 0xBE), State.AUTHED);
	}

	public AionPacketHandler getPacketHandler()
	{
		return handler;
	}

	private void addPacket(AionClientPacket prototype, State... states)
	{
		injector.injectMembers(prototype);
		handler.addPacketPrototype(prototype, states);
	}

}