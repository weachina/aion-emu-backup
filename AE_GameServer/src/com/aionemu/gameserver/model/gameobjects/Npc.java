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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.templates.NpcTemplate;
import com.aionemu.gameserver.model.templates.SpawnTemplate;
import com.aionemu.gameserver.services.DecayService;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * This class is a base class for all in-game NPCs, what includes: monsters and npcs that player can talk to (aka
 * Citizens)
 * 
 * @author Luno
 * 
 */
public class Npc extends Creature
{
	/**
	 *  Template keeping all base data for this npc 
	 */
	private NpcTemplate		template;

	/**
	 *  Spawn template of this npc. Currently every spawn template is responsible for spawning just one npc.
	 */
	private SpawnTemplate	spawn;
	
	/**
	 * Constructor creating instance of Npc.
	 * 
	 * @param spawn
	 *            SpawnTemplate which is used to spawn this npc
	 * @param objId
	 *            unique objId
	 */
	public Npc(SpawnTemplate spawn, int objId, NpcController controller)
	{
		super(objId, controller, new WorldPosition());

		this.template = spawn.getNpc();
		this.spawn = spawn;
		this.currentHP = template.getNpcStatsTemplate().getMaxHp();
		this.currentMP = template.getNpcStatsTemplate().getMaxMp();
		this.currentDP = 0;
		controller.setOwner(this);
	}

	public NpcTemplate getTemplate()
	{
		return template;
	}

	public SpawnTemplate getSpawn()
	{
		return spawn;
	}

	@Override
	public String getName()
	{
		return getTemplate().getName();
	}

	public int getNpcId()
	{
		return getTemplate().getNpcId();
	}

	/**
	 * Return NpcController of this Npc object.
	 * 
	 * @return NpcController.
	 */
	@Override
	public NpcController getController()
	{
		return (NpcController) super.getController();
	}

	@Override
	public byte getLevel()
	{
		return getTemplate().getLevel();
	}
	
	public void setHP (int hp)
	{
		if (hp>=template.getNpcStatsTemplate().getMaxHp()) {
			currentHP = template.getNpcStatsTemplate().getMaxHp();
		} else {
			currentHP = hp;
		}
	}
	
	public void onDie()
	{
		RespawnService.getInstance().scheduleRespawnTask(this);
		DecayService.getInstance().scheduleDecayTask(this);
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#setDP(int)
	 */
	@Override
	public void setDP(int dp)
	{
		if (dp>this.getMaxDP()) {
			this.currentDP = this.getMaxDP();
		} else {
			this.currentDP = dp;
		}
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#setMP(int)
	 */
	@Override
	public void setMP(int mp)
	{
		if (this.currentMP>=this.getMaxMP()) {
			this.currentMP = this.getMaxMP();
		} else {
			this.currentMP = mp;
		}
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getMaxDP()
	 */
	@Override
	public int getMaxDP()
	{
		return 100;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getMaxHP()
	 */
	@Override
	public int getMaxHP()
	{
		return template.getNpcStatsTemplate().getMaxHp();
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getMaxMP()
	 */
	@Override
	public int getMaxMP()
	{
		return template.getNpcStatsTemplate().getMaxMp();
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getBlock()
	 */
	@Override
	public int getBlock()
	{
		return template.getNpcStatsTemplate().getBlock()*2/5;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getPower()
	 */
	@Override
	public int getPower()
	{
		return template.getNpcStatsTemplate().getMainHandAttack()*5;
	}
}
