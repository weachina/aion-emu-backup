/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.skillengine;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.templates.SkillTemplate;

/**
 * @author ATracer
 *
 */
public abstract class SkillHandler
{
	
	private int skillId;
	
	private SkillTemplate skillTemplate;
	
	public SkillHandler(int skillId)
	{
		this.skillId = skillId;
	}
	
	public abstract int useSkill(Creature creature, List<Creature> targets);

	/**
	 * @return the skillTemplate
	 */
	public SkillTemplate getSkillTemplate()
	{
		return skillTemplate;
	}
	
	/**
	 * @return skillId of this handler
	 */
	public int getSkillId()
	{
		return skillId;
	}

	/**
	 * @param skillTemplate the skillTemplate to set
	 */
	public void setSkillTemplate(SkillTemplate skillTemplate)
	{
		this.skillTemplate = skillTemplate;
	}
	
	

}
