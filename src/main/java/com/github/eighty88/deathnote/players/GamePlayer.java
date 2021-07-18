package com.github.eighty88.deathnote.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class GamePlayer {
    RoleType roleType = RoleType.Watching;
    Player player;
    boolean isChecked = false;

    private static HashMap<Player, GamePlayer> list = new HashMap<>();

    public GamePlayer(Player player) {
        this.player = player;
    }

    public static void registerPlayer(Player player) {
        list.put(player, new GamePlayer(player));
    }

    public static void registerPlayers(Collection<Player> players) {
        for(Player player:players) {
            registerPlayer(player);
        }
    }

    public static void unregisterPlayer(Player player) {
        list.remove(player);
    }

    public static void unregisterPlayers(Collection<Player> players) {
        for(Player player:players) {
            unregisterPlayer(player);
        }
    }

    public static GamePlayer getPlayerExact(String Name) {
        return list.get(Bukkit.getPlayerExact(Name));
    }

    public void setRole(RoleType roleType) {
        this.roleType = roleType;
    }

    public RoleType getRole() {
        return this.roleType;
    }

    public Player getPlayer() {
        return this.player;
    }

    public static GamePlayer getL() {
        for(GamePlayer player:list.values()) {
            if(player.getRole() == RoleType.L) {
                return player;
            }
        }
        return null;
    }

    public static GamePlayer getKiller() {
        for(GamePlayer player:list.values()) {
            if(player.getRole() == RoleType.Killer) {
                return player;
            }
        }
        return null;
    }

    public static GamePlayer getGrimReaper() {
        for(GamePlayer player:list.values()) {
            if(player.getRole() == RoleType.GrimReaper) {
                return player;
            }
        }
        return null;
    }

    public static GamePlayer getMass() {
        for(GamePlayer player:list.values()) {
            if(player.getRole() == RoleType.Mass) {
                return player;
            }
        }
        return null;
    }

    public static Collection<GamePlayer> getOnlinePlayers() {
        return list.values();
    }

    public static Set<Player> getPlayers() {
        return list.keySet();
    }

    public boolean isChecked() {
        return !this.isChecked;
    }

    public void check() {
        this.isChecked = true;
    }
}
