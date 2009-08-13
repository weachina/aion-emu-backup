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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.aionemu.commons.callbacks.EnhancedObject;
import com.aionemu.gameserver.model.gameobjects.player.listeners.PlayerLoggedInListener;
import com.aionemu.gameserver.model.gameobjects.player.listeners.PlayerLoggedOutListener;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_NOTIFY;

/**
 * Represents a player's Friend list
 * 
 * @author Ben
 *
 */
public class FriendList implements Iterable<Friend>
{
	public static final int 		MAX_FRIENDS 		= 10;
	
	private Status 					status 				= Status.OFFLINE;
	
	// Stores friend Oids and their friend notes
	private final Queue<Friend> 	friends;
	
	private Player 					player;
	
	/**
	 * Constructs an empty friend list for the given player
	 * @param player Player who has this friendlist
	 */
	public FriendList(Player player)
	{
		this(player, new ConcurrentLinkedQueue<Friend>());
	}
	/**
	 * Constructs a friend list for the given player, with the given friends
	 * @param player Player who has this friend list
	 * @param friends Friends on the list
	 */
	public FriendList(Player owner, Collection<Friend> newFriends)
	{
		this.friends = new ConcurrentLinkedQueue<Friend>(newFriends);
		this.player = owner;
		
		//Add the listeners for when the play logs in and out
		((EnhancedObject)player).addCallback(new PlayerLoggedInListener(){
			
			@Override
			protected void onLoggedIn(Player loggedInPlayer)
			{
				// Set status
				setStatus(FriendList.Status.ONLINE);
				//Send list to self
				loggedInPlayer.getClientConnection().sendPacket(new SM_FRIEND_LIST());
				
			}
		});
		
		((EnhancedObject)player).addCallback(new PlayerLoggedOutListener(){
			
			@Override
			protected void onLoggedOut(Player loggedOutPlayer)
			{
				// Set status)
				setStatus(FriendList.Status.OFFLINE);
				
			}
		});
			
		
	}
	/**
	 * Gets the friend with this objId<br />
	 * Returns null if it is not our friend
	 * @param objId objId of friend
	 * @return 
	 */
	public Friend getFriend(int objId) 
	{
		for (Friend friend : friends)
		{
			if (friend.getOid() == objId)
				return friend;
		}
		return null;
	}

	/**
	 * Returns number of friends in list
	 * @return Num Friends in list
	 */
	public int getSize() 
	{
		return friends.size();
	}
	
	/**
	 * Adds the given friend to the list<br />
	 * To add a friend in the database, see <tt>PlayerService</tt>
	 * @param friend
	 */
	public void addFriend(Friend friend)
	{
		friends.add(friend);
	}
	
	/**
	 * Gets the Friend by this name
	 * @param name Name of friend
	 * @return Friend matching name
	 */
	public Friend getFriend(String name) 
	{
		for (Friend friend : friends)
			if (friend.getName().equalsIgnoreCase(name))
				return friend;
		return null;
	}

	/**
	 * Deletes given friend from this friends list<br />
	 * <ul><li>Note: This will only affect this player, not the friend.</li>
	 * <li>Note: Sends the packet to update the client automatically</li>
	 * <li>Note: You should use requestDel to delete from both lists</li></ul>
	 * @param friend
	 * @return
	 */
	public void delFriend(int friendOid)
	{
		Iterator<Friend> it = iterator();
		while (it.hasNext())
			if (it.next().getOid() == friendOid)
				it.remove();
	}
	
	/**
	 * Gets players status
	 * @return Status
	 */
	public Status getStatus()
	{
		return status;
	}
	
	/**
	 * Sets the status of the player<br />
	 * <ul><li>Note: Does not update friends</li></ul>
	 * @param status
	 */
	public void setStatus(Status status) 
	{
		Status previousStatus = this.status;
		this.status = status;
		
		for (Friend friend : friends) // For all my friends
		{
			
			if (friend.isOnline()) // If the player is online
			{
				Player friendPlayer = friend.getPlayer();
				friendPlayer.getClientConnection().sendPacket(new SM_FRIEND_LIST()); // Send him a new friend list packet
				
				if (previousStatus == Status.OFFLINE) 
				{
					//Show LOGIN message
					friendPlayer.getClientConnection().sendPacket(new SM_FRIEND_NOTIFY(SM_FRIEND_NOTIFY.LOGIN,player.getName()));
				}
				else if (status == Status.OFFLINE)
				{
					//Show LOGOUT message
					friendPlayer.getClientConnection().sendPacket(new SM_FRIEND_NOTIFY(SM_FRIEND_NOTIFY.LOGOUT,player.getName()));
				}
			}
		}
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Friend> iterator()
	{
		return friends.iterator();
	}
	
	public enum Status
	{
		/**
		 * User is offline or invisible
		 */
		OFFLINE(0),
		/**
		 * User is online
		 */
		ONLINE(1),
		/**
		 * User is away or busy
		 */
		AWAY(2);
		
		int value;
		
		private Status(int value)
		{
			this.value = value;
		}
		
		public int getIntValue()
		{
			return value;
		}
		
		/**
		 * Gets the Status from its int value<br />
		 * Returns null if out of range
		 * @param value range 0-2
		 * @return
		 */
		public static Status getByValue(int value)
		{
			for (Status stat : values())
				if (stat.getIntValue() == value)
					return stat;
			return null;
		}
	}
}
