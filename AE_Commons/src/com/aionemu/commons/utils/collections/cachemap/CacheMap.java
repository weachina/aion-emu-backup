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
package com.aionemu.commons.utils.collections.cachemap;

/**
 * This interface represents a Map structure for cache usage. 
 * @author Luno
 *
 */
public interface CacheMap<K, V>
{

	/**
	 * Adds a pair <key,value> to cache map.<br>
	 * <br>
	 * 
	 * <font color='red'><b>NOTICE:</b> </font> if there is already a value with given id in the map,
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param key map key
	 * @param value map value
	 */
	public void put(K key, V value);

	/**
	 * Returns cached value correlated to given key.
	 * 
	 * @param key map key
	 * @return value or null
	 */
	public V get(K key);
	
	/**
	 * Checks whether this map contains a value related to given key.
	 * @param key map key
	 * @return true if contains, false in other case
	 */
	public boolean contains(K key);
	
	/**
	 * Removes an entry from the map, that has given key.
	 * @param key map key
	 */
	public void remove(K key);
}
