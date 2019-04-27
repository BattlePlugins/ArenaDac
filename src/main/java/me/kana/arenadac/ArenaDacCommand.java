package me.kana.arenadac;

import org.bukkit.entity.Player;

import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;

public class ArenaDacCommand extends CustomCommandExecutor{

	@MCCommand(cmds={"vie"}, admin=false)
	public boolean vie(Player p) {
		if(ArenaDacArena.nbr_vie_player == null){
			p.sendMessage("[ArenaDac] Vous ne participez � aucune partie !");
			return false;
		}
		else{
			if(ArenaDacArena.nbr_vie_player.get(p) == null){
				p.sendMessage("[ArenaDac] Vous ne participez � aucune partie !");
				return false;
			}
			else{
				p.sendMessage("[ArenaDac] Vie(s): " + ArenaDacArena.nbr_vie_player.get(p));
				return true;				
			}
		}
    }
}
