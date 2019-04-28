package me.kana.arenadac;

import mc.alk.arena.competition.Competition;
import mc.alk.arena.competition.match.Match;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import org.bukkit.entity.Player;

import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;

public class ArenaDacCommand extends CustomCommandExecutor{

	@MCCommand(cmds={"lives"})
	public boolean lives(ArenaPlayer sender) {
		Competition comp = sender.getCompetition();
		if (comp == null || (!(comp instanceof Match))) {
			return sendMessage(sender, "&cYou aren't in an ArenaDac game!");
		}

		Arena arena = ((Match) comp).getArena();
		if (!(arena instanceof ArenaDacArena)) {
			return sendMessage(sender, "&cArena " + arena.getName() + " is not an ArenaDac arena!");
		}

		if(ArenaDacArena.playerLives == null){
			sender.sendMessage("[ArenaDac] There is not currently a game active");
			return false;
		}
		else{
			if(ArenaDacArena.playerLives.get(sender.getID()) == null){
				sender.sendMessage("[ArenaDac] You are not currently in a game!");
				return false;
			} else {
				sender.sendMessage("[ArenaDac] Lives left: " + ArenaDacArena.playerLives.get(sender.getID()));
				return true;				
			}
		}
    }
}
