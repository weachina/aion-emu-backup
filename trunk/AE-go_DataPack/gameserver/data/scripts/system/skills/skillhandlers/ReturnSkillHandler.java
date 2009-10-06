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
package skillhandlers;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.skillengine.SkillHandler;

import org.apache.log4j.Logger;

/**
 * @author ATracer
 *
 */
public class ReturnSkillHandler extends SkillHandler
{
    private static final Logger log = Logger.getLogger(ReturnSkillHandler.class);
    
    public ReturnSkillHandler() {
        super(1801);
    }

    /* (non-Javadoc)
     * @see com.aionemu.gameserver.skillengine.SkillHandler#useSkill(com.aionemu.gameserver.model.gameobjects.Creature, java.util.List)
     */
    @Override
    public void useSkill(Creature creature, List<Creature> targets)
    {
    	Player p = null;
    	if(p instanceof Player) {
    		p = (Player) creature;
    	}
    	else {
    		log.info("Return skill used, but caster is not a player. Execution aborted.");
    	}
    	if(p != null) {
    		World w = p.getActiveRegion().getWorld();
    		w.setPosition(p, p.getActiveRegion().getMapId(), 5, 25, 17, (byte) 1);
    	byte heading = 0;
    	w.setPosition(creature, creature.getActiveRegion().getMapId(), creature.getX() + 10, creature.getY() + 10, creature.getZ() + 10, heading);
        log.info("You are using return");
    	}
    }

}
