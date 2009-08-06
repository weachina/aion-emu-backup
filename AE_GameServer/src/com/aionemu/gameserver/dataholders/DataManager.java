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
package com.aionemu.gameserver.dataholders;

import org.apache.log4j.Logger;

/**
 * 
 * This class is holding whole static data, that is loaded from /data/static_data directory.<br>
 * The data is loaded by XMLDataLoader using JAXB.<br>
 * <br>
 * 
 * This class temporarily also contains data loaded from txt files by DataLoaders. It'll be changed later.
 * 
 * @author Luno
 * 
 */

public class DataManager
{
	/** Logger used by this class and {@link StaticData} class */
	static Logger						log	= Logger.getLogger(DataManager.class);
	
	/**
	 * Npc data is keeping information about all npcs.
	 * 
	 * @see NpcData
	 */
	public final NpcData				NPC_DATA;

	/**
	 * Spawn data is keeping information about all spawn definitions.
	 * 
	 * @see SpawnData
	 */
	public final SpawnData				SPAWN_DATA;

	/**
	 * World maps data is keeping information about all world maps.
	 * 
	 * @see WorldMapsData
	 */
	public final WorldMapsData			WORLD_MAPS_DATA;

	/**
	 * Experience table is keeping information about experience required for each level.
	 * 
	 * @see PlayerExperienceTable
	 */
	public final PlayerExperienceTable	PLAYER_EXPERIENCE_TABLE;

	/**
	 * 
	 */
	public final PlayerStatsData PLAYER_STATS_DATA;

	/**
	 * Constructor creating <tt>DataManager</tt> instance.<br>
	 * NOTICE: calling constructor implies loading whole data from /data/static_data immediately
	 */
	public DataManager()
	{
		log.info("####################   STATIC DATA [section beginning] ####################");

		//now this outstanding spawndata and (still) npcdata:
		NPC_DATA = new NpcData();
		SPAWN_DATA = new SpawnData(NPC_DATA);
		
		
		XmlDataLoader loader = new XmlDataLoader();

		long start = System.currentTimeMillis();
		StaticData data = loader.loadStaticData();
		long time = System.currentTimeMillis() - start;

		
		WORLD_MAPS_DATA = data.worldMapsData;
		PLAYER_EXPERIENCE_TABLE = data.playerExperienceTable;
		PLAYER_STATS_DATA = data.statsData;
		
		
		//some sexy time message
		long seconds = time/1000;
		
		String timeMsg = seconds > 0 ? seconds+" seconds" : time+" miliseconds";
		
		log.info("###### [load time: " + timeMsg+"] ######");
		log.info("####################      STATIC DATA [section end]    ####################");
	}
}