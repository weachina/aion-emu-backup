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
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_DP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_HP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_MP;
import com.aionemu.gameserver.services.DecayService;
import com.aionemu.gameserver.services.PlayerService;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.MapRegion;
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
	private PlayerStats			playerStats;
	private MacroList			macroList;
	private SkillList			skillList;
	private FriendList			friendList;
	private BlockList			blockList;
	private ResponseRequester	requester;
	private boolean				lookingForGroup	= false;

	private int					itemId			= 0;
	private int					itemNameId		= 0;
	private int					atcount			= 1;

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
		this.playerStats = new PlayerStats(this);
		this.currentHP = playerStats.getMaxHP();
		this.currentMP = playerStats.getMaxMP();
		this.currentDP = 0;

		this.requester = new ResponseRequester(this);

		controller.setOwner(this);

	}

	public PlayerCommonData getCommonData()
	{
		return playerCommonData;
	}

	public PlayerStats getStats()
	{
		return playerStats;
	}

	@Override
	public String getName()
	{
		if((playerCommonData.isAdmin()) && (!playerCommonData.getName().contains("*MJ*")))
		{
			return new String("*MJ* " + playerCommonData.getName());
		}
		else
		{
			return playerCommonData.getName();
		}
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

	public long getExp()
	{
		return this.getCommonData().getExp();
	}

	public void setHP (int hp) {
		if (hp>=playerStats.getMaxHP()) {
			this.currentHP = playerStats.getMaxHP();
		} else {
			this.currentHP = hp;
		}
		PacketSendUtility.broadcastPacket(this, new SM_STATUPDATE_HP(this.currentHP, playerStats.getMaxHP()),true);
	}
	
	public void setMP (int mp) {
		if (mp>=playerStats.getMaxMP()) {
			this.currentMP = playerStats.getMaxMP();
		} else {
			this.currentMP = mp;
		}
		PacketSendUtility.broadcastPacket(this, new SM_STATUPDATE_MP(this.currentMP, playerStats.getMaxMP()),true);
	}
		
	public void setDP (int dp) {
		if (dp>=playerStats.getMaxDP()) {
			this.currentDP = playerStats.getMaxDP();
		} else {
			this.currentDP = dp;
		}
		PacketSendUtility.broadcastPacket(this, new SM_STATUPDATE_DP(this.currentDP), true);
	}
	
	public void setItemId(int e)
	{
		this.itemId = e;
	}

	public void setItemNameId(int e)
	{
		this.itemNameId = e;
	}

	public int getItemId()
	{
		return itemId;
	}

	public int getItemNameId()
	{
		return itemNameId;
	}

	public void setatcount(int e)
	{
		this.atcount = e;
	}

	public int getatcount()
	{
		return atcount;
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
	public void onLoggedIn()
	{
		if(this.getCommonData().isAdmin())
		{
			MapRegion mapRegion = this.getActiveRegion();
			if(mapRegion != null)
			{
				World world = mapRegion.getWorld();
				if(world != null)
				{
					Iterator<Player> iter = world.getPlayersIterator();
					if(iter != null)
					{
						StringBuilder sbMessage = new StringBuilder("<Annonce> [>>>MJ] " + this.getName()
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
	}

	/**
	 * This method is called when player leaves the game. It's main responsibility is to call all registered listeners.<br>
	 * <br>
	 * 
	 * <b><font color='red'>NOTICE: </font>this method is supposed to be called only from
	 * {@link PlayerService#playerLoggedOut(Player)}</b>
	 */
	@Enhancable(callback = PlayerLoggedOutListener.class)
	public void onLoggedOut()
	{
		if(this.getCommonData().isAdmin())
		{
			MapRegion mapRegion = this.getActiveRegion();
			if(mapRegion != null)
			{
				World world = mapRegion.getWorld();
				if(world != null)
				{
					Iterator<Player> iter = world.getPlayersIterator();
					if(iter != null)
					{
						StringBuilder sbMessage = new StringBuilder("<Annonce> [<<<MJ] " + this.getName()
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
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getMaxDP()
	 */
	@Override
	public int getMaxDP()
	{
		return playerStats.getMaxDP();
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getMaxHP()
	 */
	@Override
	public int getMaxHP()
	{
		return playerStats.getMaxHP();
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getMaxMP()
	 */
	@Override
	public int getMaxMP()
	{
		return playerStats.getMaxMP();
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getBlock()
	 */
	@Override
	public int getBlock()
	{
		return playerStats.getBlock();
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#getPower()
	 */
	@Override
	public int getPower()
	{
		return playerStats.getPower();
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.Creature#onDie()
	 */
	@Override
	public void onDie()
	{
		this.getCommonData().setExp((int)Math.round(this.getCommonData().getExpNeed()*0.03));
		this.setHP((int)Math.round(this.getMaxHP()*0.7));
		RespawnService.getInstance().scheduleRespawnTask(this);
		DecayService.getInstance().scheduleDecayTask(this);
	}
}
