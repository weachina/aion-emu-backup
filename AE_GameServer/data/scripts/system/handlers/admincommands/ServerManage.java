package admincommands;

import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.ShutdownHook;

public class ServerManage extends AdminCommand {
	
	public ServerManage() {
		super("servermanage");
	}
	
	public void executeCommand(Player admin, String[] params) {
		if(params == null || params.length != 1) {
			PacketSendUtility.sendMessage(admin, "usage: //servermanage [stop|restart|info]");
		}
		else 
		{
			if(params[0] == "stop") {
				World w = admin.getActiveRegion().getWorld();
				ShutdownHook sdh = new ShutdownHook(w);
				PacketSendUtility.sendMessage(admin, "Reboot started");
			}
			else if(params[0] == "restart") {	
				PacketSendUtility.sendMessage(admin, "method not yet implemented");
			}
			else if(params[0] == "info") {
				PacketSendUtility.sendMessage(admin, "method not yet implemented");
			}
			else {
				PacketSendUtility.sendMessage(admin, "invalid action specified");
			}
		}
	}
}