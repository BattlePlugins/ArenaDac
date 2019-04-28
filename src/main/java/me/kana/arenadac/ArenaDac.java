package me.kana.arenadac;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import mc.alk.arena.BattleArena;
import mc.alk.arena.util.Log;

public class ArenaDac extends JavaPlugin{

    private static ArenaDac plugin;

    public int lives;
    public boolean useLives;

    @Override
    public void onEnable(){
        plugin = this;

        BattleArena.registerCompetition(this, "ArenaDac", "dac", ArenaDacArena.class, new ArenaDacCommand());

        saveDefaultConfig();
        loadConfig();


        Log.info("[" + getName()+ "] v" + getDescription().getVersion()+ " enabled!");
    }
    @Override
    public void onDisable(){
        Log.info("[" + getName()+ "] v" + getDescription().getVersion()+ " stopping!");
    }

    @Override
    public void reloadConfig(){
        super.reloadConfig();
    }

    public void loadConfig(){
        FileConfiguration config = getConfig();
        lives = config.getInt("max-lives", 2);
        useLives = config.getBoolean("use-lives", true);
    }

    public static ArenaDac getPlugin() {
        return plugin;
    }
}
