
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
package com.aionemu.loginserver.network.ncrypt;

//~--- JDK imports ------------------------------------------------------------

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

public class ScrambledKeyPair
{
    private KeyPair keyPair;
    private byte[]  scrambledModulus;

    public ScrambledKeyPair(KeyPair keyPair)
    {
        this.keyPair     = keyPair;
        scrambledModulus = scrambleModulus(((RSAPublicKey) this.keyPair.getPublic()).getModulus());
    }

    private byte[] scrambleModulus(BigInteger modulus)
    {
        byte[] scrambledMod = modulus.toByteArray();

        if ((scrambledMod.length == 0x81) && (scrambledMod[0] == 0x00))
        {
            byte[] temp = new byte[0x80];

            System.arraycopy(scrambledMod, 1, temp, 0, 0x80);
            scrambledMod = temp;
        }

        // step 1 : 0x4d-0x50 <-> 0x00-0x04
        for (int i = 0; i < 4; i++)
        {
            byte temp = scrambledMod[i];

            scrambledMod[i]        = scrambledMod[0x4d + i];
            scrambledMod[0x4d + i] = temp;
        }

        // step 2 : xor first 0x40 bytes with last 0x40 bytes
        for (int i = 0; i < 0x40; i++)
        {
            scrambledMod[i] = (byte) (scrambledMod[i] ^ scrambledMod[0x40 + i]);
        }

        // step 3 : xor bytes 0x0d-0x10 with bytes 0x34-0x38
        for (int i = 0; i < 4; i++)
        {
            scrambledMod[0x0d + i] = (byte) (scrambledMod[0x0d + i] ^ scrambledMod[0x34 + i]);
        }

        // step 4 : xor last 0x40 bytes with first 0x40 bytes
        for (int i = 0; i < 0x40; i++)
        {
            scrambledMod[0x40 + i] = (byte) (scrambledMod[0x40 + i] ^ scrambledMod[i]);
        }

        return scrambledMod;
    }

//    public void setKeyPair(KeyPair keyPair)
//    {
//        this.keyPair = keyPair;
//    }

//    public void setScrambledModulus(byte[] scrambledModulus)
//    {
//        this.scrambledModulus = scrambledModulus;
//    }

    public KeyPair getKeyPair()
    {
        return keyPair;
    }

    public byte[] getScrambledModulus()
    {
        return scrambledModulus;
    }
}
