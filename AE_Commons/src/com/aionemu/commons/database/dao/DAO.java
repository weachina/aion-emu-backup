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

package com.aionemu.commons.database.dao;

import com.aionemu.commons.database.PersistentObject;

/**
 * This class represents basic DAO.<br>
 * DAO implementation must match the set of conditions, check the
 * {@link com.aionemu.commons.database.dao.scriptloader.DAOLoader#getSuitableClasses(Class[])} for details.<br>
 * DAO subclass must have public no-arg constructor, in other case {@link InstantiationException} will be thrown by
 * {@link com.aionemu.commons.database.dao.DAOManager}<br>
 * 
 * DAO implementation should contain only two methods: save and load.
 * 
 * @author SoulKeeper
 */
public interface DAO<T extends PersistentObject<?>>
{

	/**
	 * Generic SAVE action
	 * 
	 * @param object
	 *            object to save
	 */
	public void save(T object);

	/**
	 * Generic LOAD action
	 * 
	 * @param id
	 *            - primary key
	 * @return loads object by primary key
	 */
	public T load(Object id);
}
