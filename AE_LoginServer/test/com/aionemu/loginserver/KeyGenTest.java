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

import junit.framework.TestCase;

import java.security.GeneralSecurityException;

import org.junit.Test;
import com.aionemu.loginserver.network.ncrypt.KeyGen;

public class KeyGenTest extends TestCase {

    @Test
    public void testKeyGenInit() {
        try {
            KeyGen.init();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
