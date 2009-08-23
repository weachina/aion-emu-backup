/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.commons.scripting.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

/**
 * Method marked as {@link OnClassLoad} will be called when class was loaded by script.<br>
 * It's more useful alternative for
 * 
 * <pre>
 * static {
 * 	...
 * }
 * </pre>
 * 
 * block.<br>
 * <br>
 * Only static methods with no arguments can be marked with this annotation.<br>
 * 
 * This is only used if {@link com.aionemu.commons.scripting.ScriptContext#getClassListener()} returns
 * {@link com.aionemu.commons.scripting.classlistener.DefaultClassListener} instance.
 * 
 * @author SoulKeeper
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClassLoad
{
}
