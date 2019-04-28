package me.kana.arenadac;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import mc.alk.arena.alib.bukkitadapter.MaterialAdapter;
import mc.alk.arena.controllers.TeleportController;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.util.TeamUtil;

public class ArenaDacArena extends Arena{

    Location startLoc;
    Location divingLoc;
    int currentTeamIndex = 0;
    Player playerJumping = null;
    static Map<UUID, Integer> playerLives = new HashMap<UUID, Integer>();
    int vieok = 0;
    Material mat;

    @Override
    public void onStart(){
        startLoc = getSpawn(2, false).getLocation();
        divingLoc = getSpawn(3, false).getLocation();

        teams.forEach(team ->  {
            for (Player player : team.getBukkitPlayers()) {

                TeleportController.teleport(player, startLoc);
                playerLives.put(player.getUniqueId(), ArenaDac.getPlugin().lives);
            }
        });

        teleportToDivingBoard(new Random().nextInt(teams.size()));
    }

    public void teleportToDivingBoard(int index){
        Player[] teamArray = teams.get(index).getBukkitPlayers().toArray(new Player[teams.size()]);
        TeleportController.teleport(teamArray[new Random().nextInt()], divingLoc);
    }

    @SuppressWarnings("deprecation")
    @ArenaEventHandler(suppressCastWarnings=true)
    public void onPlayerMove(PlayerMoveEvent e) {
        Location l = e.getPlayer().getLocation();
        Block b = l.getBlock();
        Material m = b.getType();

        if (m == MaterialAdapter.getMaterial("STATIONARY_WATER") || m == Material.WATER || m == Material.LAVA){
            if (playerJumping.equals(e.getPlayer())) {
                DyeColor c = TeamUtil.getDyeColor(currentTeamIndex);
                byte data = c.getData();

                if(ArenaDac.getPlugin().useLives)
                    updateLives(l, e.getPlayer());

                if(vieok == 0)
                    mat = MaterialAdapter.getMaterial("WOOL");
                else
                    mat = Material.GLASS;


                for(int i = 0; i < 3; i++){
                    if(b.getRelative(0, i, 0).getType() == Material.WATER || b.getRelative(0, i, 0).getType() == MaterialAdapter.getMaterial("STATIONARY_WATER") || b.getRelative(0, i, 0).getType() == Material.LAVA){
                        b.getRelative(0, i, 0).setType(mat);
                        b.getRelative(0, i, 0).setData(data);
                    }
                }

                for(int i = -4; i < 0; i++){
                    if(b.getRelative(0, i, 0).getType() == Material.WATER || b.getRelative(0, i, 0).getType() == MaterialAdapter.getMaterial("STATIONARY_WATER") || b.getRelative(0, i, 0).getType() == Material.LAVA){
                        b.getRelative(0, i, 0).setType(mat);
                        b.getRelative(0, i, 0).setData(data);
                    }
                }

                if (teams.size() != currentTeamIndex + 1)
                    currentTeamIndex = currentTeamIndex +1;
                else
                    currentTeamIndex = 0;

                teleportToDivingBoard(currentTeamIndex);

                TeleportController.teleport(e.getPlayer(), startLoc);
            }
        }
    }

    @ArenaEventHandler(suppressCastWarnings=true)
    public void onEntityDamage(EntityDamageEvent e){
        Player p = (Player) e.getEntity();
            if(playerJumping == p){
                int v = playerLives.get(p.getUniqueId()) - 1;

                if(v == 0){
                    e.setDamage(20);
                    p.sendMessage("[ArenaDac] You have missed your jump!");
                }
                else{
                    e.setDamage(0);
                    p.sendMessage("[ArenaDac] You have missed your jump!");
                    p.sendMessage("[ArenaDac] You have lost a life :(");
                    playerLives.replace(p.getUniqueId(), v);
                    p.sendMessage("[ArenaDac] Lives left: " + v);
                    TeleportController.teleport(p, startLoc);
                }

                if (teams.size() != currentTeamIndex + 1)
                    currentTeamIndex = currentTeamIndex + 1;
                else
                    currentTeamIndex = 0;

                teleportToDivingBoard(currentTeamIndex);
            }
            else{
                e.setDamage(0);
            }
    }

    public void updateLives(Location loc, Player player){
        Material m1 = loc.getBlock().getRelative(1, -1, 0).getType();
        Material m2 = loc.getBlock().getRelative(-1, -1, 0).getType();
        Material m3 = loc.getBlock().getRelative(0, -1, 1).getType();
        Material m4 = loc.getBlock().getRelative(0, -1, -1).getType();

        if(m1.name().contains("WOOL") && m2.name().contains("WOOL") && m3.name().contains("WOOL") && m4.name().contains("WOOL")){
            int v = playerLives.get(player.getUniqueId()) + 1;
            playerLives.replace(player.getUniqueId(), v);
            player.sendMessage("[ArenaDac] Congratulations you have received a life!");
            player.sendMessage("[ArenaDac] Lives left: " + v);
            vieok = 1;
        } else {
            vieok = 0;
        }
    }
}
