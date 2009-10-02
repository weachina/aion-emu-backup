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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Iterator;

import com.aionemu.commons.callbacks.Enhancable;
import com.aionemu.gameserver.controllers.PlayerController;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.listeners.PlayerLoggedInListener;
import com.aionemu.gameserver.model.gameobjects.player.listeners.PlayerLoggedOutListener;
import com.aionemu.gameserver.model.gameobjects.player.SkillList;
import com.aionemu.gameserver.model.gameobjects.stats.GameStats;
import com.aionemu.gameserver.model.gameobjects.stats.LifeStats;
import com.aionemu.gameserver.model.gameobjects.stats.PlayerGameStats;
import com.aionemu.gameserver.model.gameobjects.stats.PlayerLifeStats;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.services.PlayerService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.StatsFunctions;
import com.aionemu.gameserver.world.World;

/**
 * This class is representing Player object, it contains all needed data.
 * 
 * 
 * @author -Nemesiss-
 * @author SoulKeeper
 * @author alexa026
 * 
 */
public class Player extends Creature
{
	private PlayerAppearance	playerAppearance;
	private PlayerCommonData	playerCommonData;
	private PlayerLifeStats		playerLifeStats;
	private PlayerGameStats		playerGameStats;
	private MacroList			macroList;
	private SkillList			skillList;
	private FriendList			friendList;
	private BlockList			blockList;
	private PlayerItems         playerItems;
	private ResponseRequester	requester;
	private boolean				lookingForGroup	= false;

	/** When player enters game its char is in kind of "protection" state, when is blinking etc */
	private boolean				protectionActive;

	/**
	 * Connection of this Player.
	 */
	private AionConnection		clientConnection;

	public Player(PlayerController controller, PlayerCommonData plCommonData, PlayerAppearance appereance)
	{
		super(plCommonData.getPlayerObjId(), controller, plCommonData.getPosition());

		this.playerCommonData = plCommonData;
		this.playerAppearance = appereance;
		StatsFunctions.computeStats(this);
		StatsFunctions.doStatsEvolution(this, 1);
		this.requester = new ResponseRequester(this);

		controller.setOwner(this);

	}

	public PlayerCommonData getCommonData()
	{
		return playerCommonData;
	}

	@Override
	public String getName()
	{
//		if((playerCommonData.isAdmin()) && (!playerCommonData.getName().contains("*MJ*")))
//		{
//			return new String("*MJ* " + playerCommonData.getName());
//		}
//		else
//		{
			return playerCommonData.getName();
//		}
	}

	public PlayerAppearance getPlayerAppearance()
	{
		return playerAppearance;
	}

	/**
	 * Set connection of this player.
	 * 
	 * @param clientConnection
	 */
	public void setClientConnection(AionConnection clientConnection)
	{
		this.clientConnection = clientConnection;
	}

	/**
	 * Get connection of this player.
	 * 
	 * @return AionConnection of this player.
	 * 
	 */
	public AionConnection getClientConnection()
	{
		return this.clientConnection;
	}

	public boolean isProtectionActive()
	{
		return protectionActive;
	}

	/**
	 * After entering game player char is "blinking" which means that it's in under some protection, after making an
	 * action char stops blinking.
	 * 
	 * @param protectionActive
	 */
	public void setProtectionActive(boolean protectionActive)
	{
		this.protectionActive = protectionActive;
		if(!protectionActive)
			PacketSendUtility.sendPacket(this, new SM_PLAYER_STATE(this));
	}

	public MacroList getMacroList()
	{
		return macroList;
	}

	public void setMacroList(MacroList macroList)
	{
		this.macroList = macroList;
	}

	public SkillList getSkillList()
	{
		return skillList;
	}

	public void setSkillList(SkillList skillList)
	{
		this.skillList = skillList;
	}

	/**
	 * Gets this players Friend List
	 * 
	 * @return
	 */
	public FriendList getFriendList()
	{
		return friendList;
	}

	/**
	 * Is this player looking for a group
	 * 
	 * @return
	 */
	public boolean isLookingForGroup()
	{
		return lookingForGroup;
	}

	/**
	 * Sets whether or not this player is looking for a group
	 * 
	 * @param lookingForGroup
	 */
	public void setLookingForGroup(boolean lookingForGroup)
	{
		this.lookingForGroup = lookingForGroup;
	}

	/**
	 * Sets this players friend list. <br />
	 * Remember to send the player the <tt>SM_FRIEND_LIST</tt> packet.
	 * 
	 * @param list
	 */
	public void setFriendList(FriendList list)
	{
		this.friendList = list;
	}

	public BlockList getBlockList()
	{
		return blockList;
	}

	public void setBlockList(BlockList list)
	{
		this.blockList = list;
	}
	
	public void setItems(PlayerItems playerItems)
	{
		this.playerItems = playerItems;
	}

	public PlayerItems getItems () {
		return playerItems;
	}
	
	/**
	 * Gets the ResponseRequester for this player
	 * 
	 * @return
	 */
	public ResponseRequester getResponseRequester()
	{
		return requester;
	}

	public boolean isOnline()
	{
		return getClientConnection() != null;
	}

	public PlayerClass getPlayerClass()
	{
		return playerCommonData.getPlayerClass();
	}

	public Gender getGender()
	{
		return playerCommonData.getGender();
	}

	/**
	 * Return PlayerController of this Player Object.
	 * 
	 * @return PlayerController.
	 */
	@Override
	public PlayerController getController()
	{
		return (PlayerController) super.getController();
	}

	@Override
	public byte getLevel()
	{
		return (byte) playerCommonData.getLevel();
	}

	/**
	 * This method is called when player logs into the game. It's main responsibility is to call all registered
	 * listeners.<br>
	 * <br>
	 * 
	 * <b><font color='red'>NOTICE: </font>this method is supposed to be called only from
	 * {@link PlayerService#playerLoggedIn(Player)}</b>
	 */
	@Enhancable(callback = PlayerLoggedInListener.class)
	public void onLoggedIn(World world)
	{
		if(this.getCommonData().isAdmin())
		{
			if(world != null)
			{
				Iterator<Player> iter = world.getPlayersIterator();
				if(iter != null)
				{
					StringBuilder sbMessage = new StringBuilder("<Annonce> " + this.getName()
							+ " vient de se connecter");
					
					String sMessage = sbMessage.toString().trim();
					while(iter.hasNext())
					{
						PacketSendUtility.sendMessage(iter.next(), sMessage);
					}
				}		
			}
		}
	}

	/**
	 * This method is called when player leaves the game. It's main responsibility is to call all registered listeners.<br>
	 * <br>
	 * 
	 * <b><font color='red'>NOTICE: </font>this method is supposed to be called only from
	 * {@link PlayerService#playerLoggedOut(Player)}</b>
	 */
	@Enhancable(callback = PlayerLoggedOutListener.class)
	public void onLoggedOut(World world)
	{
		if(this.getCommonData().isAdmin())
		{
			if(world != null)
			{
				Iterator<Player> iter = world.getPlayersIterator();
				if(iter != null)
				{
					StringBuilder sbMessage = new StringBuilder("<Annonce> " + this.getName()
						+ " est sorti du jeu");
						String sMessage = sbMessage.toString().trim();
					while(iter.hasNext())
					{
						PacketSendUtility.sendMessage(iter.next(), sMessage);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getGameStats()
	 */
	@Override
	public PlayerGameStats getGameStats()
	{
		return playerGameStats;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getLifeStats()
	 */
	@Override
	public PlayerLifeStats getLifeStats()
	{
		return playerLifeStats;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#setGameStats(com.aionemu.gameserver.model.gameobjects.stats.GameStats)
	 */
	@Override
	public void setGameStats(GameStats gameStats)
	{
		this.playerGameStats = (PlayerGameStats)gameStats;
		
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#setLifeStats(com.aionemu.gameserver.model.gameobjects.stats.LifeStats)
	 */
	@Override
	public void setLifeStats(LifeStats lifeStats)
	{
		this.playerLifeStats = (PlayerLifeStats)lifeStats;
	}
}
