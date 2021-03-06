/* COPYRIGHT (c) 2012 Joshua McCurry
 * This work is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License
 * and use of this software or its code is an agreement to this license.
 * A full copy of this license can be found at
 * http://creativecommons.org/licenses/by-nc-sa/3.0/. 
 */
package com.modcrafting.ultrabans.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.modcrafting.ultrabans.UltraBan;

public class Spawn implements CommandExecutor{
	public static final Logger log = Logger.getLogger("Minecraft");
	UltraBan plugin;
	String permission = "ultraban.spawn";
	public Spawn(UltraBan ultraBan) {
		this.plugin = ultraBan;
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		boolean auth = false;
		Player player = null;
		String admin = config.getString("defAdminName", "server");
		if (sender instanceof Player){
			player = (Player)sender;
			if(player.hasPermission(permission) || player.isOp()) auth = true;
			admin = player.getName();
		}else{
			auth = true; //if sender is not a player - Console
		}
		if(auth){
			if (args.length < 1) return false;
			String p = args[0]; //type name correct or 
			if(plugin.autoComplete) p = plugin.util.expandName(p); 
			Player victim = plugin.getServer().getPlayer(p);
			String idoit = null;
			if (victim != null){
				idoit = victim.getName();
			}else{
				sender.sendMessage(ChatColor.GRAY + "Player must be online!");
				return true;
			}
			String fspawnMsgVictim = config.getString("messages.fspawnMsgVictim");
			if(fspawnMsgVictim.contains(plugin.regexAdmin)) fspawnMsgVictim = fspawnMsgVictim.replaceAll(plugin.regexAdmin, admin);
			if(fspawnMsgVictim.contains(plugin.regexVictim)) fspawnMsgVictim = fspawnMsgVictim.replaceAll(plugin.regexVictim, idoit);
			if(fspawnMsgVictim != null) victim.sendMessage(plugin.util.formatMessage(fspawnMsgVictim));
			
			String fspawnMsgBroadcast = config.getString("messages.fspawnMsgBroadcast", "%victim% is now at spawn!");
			if(fspawnMsgBroadcast.contains(plugin.regexAdmin)) fspawnMsgBroadcast = fspawnMsgBroadcast.replaceAll(plugin.regexAdmin, admin);
			if(fspawnMsgBroadcast.contains(plugin.regexVictim)) fspawnMsgBroadcast = fspawnMsgBroadcast.replaceAll(plugin.regexVictim, idoit);
			if(fspawnMsgBroadcast != null)  sender.sendMessage(plugin.util.formatMessage(fspawnMsgBroadcast));
				//Further Research	
				World wtlp = victim.getWorld();
				Location tlp = wtlp.getSpawnLocation();
				victim.teleport(tlp);
		}
		
		return true;
	}
}
