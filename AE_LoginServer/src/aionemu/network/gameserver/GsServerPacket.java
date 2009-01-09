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
package aionemu.network.gameserver;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import aionemu.network.BasePacket;
import aionemu.network.IServerPacket;

/**
 * @author -Nemesiss-
 */
public abstract class GsServerPacket extends BasePacket<GsConnection> implements IServerPacket
{
	private static final Logger	log	= Logger.getLogger(GsServerPacket.class.getName());

	protected GsServerPacket()
	{

	}

	@Override public final BasePacket<GsConnection> setConnection(GsConnection con)
	{
		BasePacket<GsConnection> bp = super.setConnection(con);
		return bp;
	}

	protected final void writeD(int value)
	{
		_buf.putInt(value);
	}

	protected final void writeH(int value)
	{
		_buf.putShort((short) value);
	}

	protected final void writeC(int value)
	{
		_buf.put((byte) value);
	}

	protected final void writeF(double value)
	{
		_buf.putDouble(value);
	}

	protected final void writeQ(long value)
	{
		_buf.putLong(value);
	}

	protected final void writeS(String text)
	{
		if (text == null)
		{
			_buf.putChar('\000');
		}
		else
		{
			final int len = text.length();
			for (int i = 0; i < len; i++)
				_buf.putChar(text.charAt(i));
			_buf.putChar('\000');
		}
	}

	protected final void writeB(byte[] data)
	{
		_buf.put(data);
	}

	@Override public boolean write(ByteBuffer buf)
	{
		_buf = buf;
		try
		{
			_buf.putShort((short) 0);
			writeImpl();
			_buf.flip();
			_buf.putShort((short) _buf.limit());
			_buf.position(0);
		}
		finally
		{
			_buf = null;
		}
		return true;
	}

	protected abstract void writeImpl();
}
