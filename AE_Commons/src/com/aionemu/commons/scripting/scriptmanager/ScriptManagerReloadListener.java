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

package com.aionemu.commons.scripting.scriptmanager;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;

/**
 * ScriptManager reload listener
 * 
 * @author SoulKeeper
 */
public abstract class ScriptManagerReloadListener implements Callback
{

	@Override
	public final CallbackResult beforeCall(Object obj, Object[] args)
	{
		beforeReload();
		return CallbackResult.newContinue();
	}

	@Override
	public final CallbackResult afterCall(Object obj, Object[] args, Object methodResult)
	{
		afterReload();
		return CallbackResult.newContinue();
	}

	/**
	 * Invoked before {@link ScriptManager#reload()} execution
	 */
	public abstract void beforeReload();

	/**
	 * Invoked after {@link ScriptManager#reload()} execution
	 */
	public abstract void afterReload();

	@Override
	public Class getBaseClass()
	{
		return ScriptManagerReloadListener.class;
	}
}