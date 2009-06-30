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

import com.aionemu.loginserver.network.aion.AionConnection;
import com.aionemu.loginserver.network.aion.AionServerPacket;

import javax.crypto.SecretKey;
import java.nio.ByteBuffer;

/**
 * Format: dd b dddd s d: session id d: protocol revision b: 0x90 bytes : 0x80 bytes for the scrambled RSA public key
 * 0x10 bytes at 0x00 d: unknow d: unknow d: unknow d: unknow s: blowfish key
 */
public final class SM_INIT extends AionServerPacket
{
	/**
	 * Session Id of this connection
	 */
	private final int		sessionId;

	/**
	 * public Rsa key that client will use to encrypt login and password that will be send in RequestAuthLogin client
	 * packet.
	 */
	private final byte[]	publicRsaKey;
	/**
	 * blowfish key for packet encryption/decryption.
	 */
	private final byte[]	blowfishKey;

	/**
	 * Constructor
	 * 
	 * @param client
	 * @param blowfishKey 
	 */
	public SM_INIT(AionConnection client, SecretKey blowfishKey)
	{
		this(client.getScrambledModulus(), blowfishKey.getEncoded(), client.getSessionId());
	}

	/**
	 * Creates new instance of <tt>SM_INIT</tt> packet.
	 * 
	 * @param publicRsaKey      Public RSA key
	 * @param blowfishKey       Blowfish key
	 * @param sessionId         Session identifier
	 */
	private SM_INIT(byte[] publicRsaKey, byte[] blowfishKey, int sessionId)
	{
		super(0x00);

		this.sessionId = sessionId;
		this.publicRsaKey = publicRsaKey;
		this.blowfishKey = blowfishKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode()); // init packet id

		writeD(buf, sessionId); // session id
		writeD(buf, 0x0000c621); // protocol revision
		writeB(buf, publicRsaKey); // RSA Public Key
		// unk
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeD(buf, 0x00);
		writeD(buf, 0x00);

		writeB(buf, blowfishKey); // BlowFish key
		writeD(buf, 197635); // unk
		writeD(buf, 2097152); // unk
	}
}
