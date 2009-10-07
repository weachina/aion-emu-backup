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

import com.aionemu.gameserver.dataholders.PlayerInitialData;
import com.aionemu.gameserver.dataholders.PlayerInitialData.LocationData;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CASTSPELL;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CASTSPELL_END;
import com.aionemu.gameserver.network.aion.serverpackets.unk.SM_UNKF5;
import com.aionemu.gameserver.skillengine.SkillHandler;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.google.inject.Inject;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author ATracer
 *
 */
public class ReturnSkillHandler extends SkillHandler
{
   private static final Logger log =
Logger.getLogger(ReturnSkillHandler.class);

   @Inject
   private World   world;

   @Inject
   private PlayerInitialData playerInitialData;

   public ReturnSkillHandler() {
       super(1801);
   }

   /* (non-Javadoc)
    * @see com.aionemu.gameserver.skillengine.SkillHandler#useSkill(com.aionemu.gameserver.model.gameobjects.Creature,
java.util.List)
    */
   @Override
   public void useSkill(Creature creature, List<Creature> targets)
   {
       log.info("You are using return");
       final Player player = (Player) creature;
       world = player.getActiveRegion().getWorld();
       log.info("[Return] Player " + player.getName() + " teleported to bind point in map " + player.getActiveRegion().getMapId());
       WorldPosition bp = player.getBindPoint();
       world.setPosition(player, bp.getMapId(), bp.getX(), bp.getY(), bp.getZ(), bp.getHeading());
       PacketSendUtility.sendPacket(player, new SM_UNKF5(player));
   }

}