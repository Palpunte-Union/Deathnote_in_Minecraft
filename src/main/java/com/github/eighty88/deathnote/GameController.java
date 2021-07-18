package com.github.eighty88.deathnote;

import com.github.eighty88.deathnote.itemstacks.DeathNotesShard;
import com.github.eighty88.deathnote.itemstacks.Sword;
import com.github.eighty88.deathnote.players.GamePlayer;
import com.github.eighty88.deathnote.players.RoleType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameController {

    Deathnote deathnote;

    GrimReaperType type = GrimReaperType.Ryuk;

    boolean isStarted = false;

    boolean isUsed = false;

    int size = 0;

    int time = 120;

    int a;

    public GameController(Deathnote plugin) {
        this.deathnote = plugin;
    }

    public void start() {
        this.type = getRandom();
        if (!lotteryRoles()) {
            return;
        }
        isStarted = true;
        isUsed = false;
        time = 0;
        a = Bukkit.getScheduler().scheduleSyncDelayedTask(this.deathnote, () -> Objects.requireNonNull(GamePlayer.getKiller()).getPlayer().getInventory().addItem(DeathNotesShard.getItemStack()), 12000);
    }

    public void end(RoleType roleType) {
        isStarted = false;
        Bukkit.broadcastMessage(ChatColor.GREEN + "-----=====" + ChatColor.UNDERLINE + "今回の役職" + ChatColor.RESET.toString() + ChatColor.GREEN + "=====-----");
        Bukkit.broadcastMessage(ChatColor.RED + "キラ: " + Objects.requireNonNull(GamePlayer.getKiller()).getPlayer().getName());
        Bukkit.broadcastMessage(ChatColor.GRAY + "死神: " + Objects.requireNonNull(GamePlayer.getGrimReaper()).getPlayer().getName());
        Bukkit.broadcastMessage(ChatColor.YELLOW + "ミサ: " + Objects.requireNonNull(GamePlayer.getMass()).getPlayer().getName());
        Bukkit.broadcastMessage(ChatColor.GREEN + "L: " + Objects.requireNonNull(GamePlayer.getL()).getPlayer().getName());
        Bukkit.broadcastMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "==============================");
        refreshPlayers();
        Bukkit.getScheduler().cancelTask(a);
        size = 0;
        if (roleType == RoleType.L) {
            for(Player player: Bukkit.getOnlinePlayers()) {
                player.sendTitle("キラ逮捕", "", 20, 20, 20);
            }
        } else if (roleType == RoleType.Killer){
            for(Player player: Bukkit.getOnlinePlayers()) {
                player.sendTitle("L死亡", "", 20, 20, 20);
            }
        }
    }

    public GrimReaperType getRandom() {
        if(Math.ceil(Math.random() * 2) == 1) {
            return GrimReaperType.Ryuk;
        } else {
            return GrimReaperType.Rem;
        }
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    public boolean lotteryRoles() {
        List<GamePlayer> players = new ArrayList<>(GamePlayer.getOnlinePlayers());
        Collections.shuffle(players);
        if(players.size() < 6) {
            Bukkit.broadcastMessage("人数が不足しています!");
            return false;
        }
        Collections.shuffle(players);
        players.get(1).setRole(RoleType.GrimReaper);
        players.get(1).getPlayer().sendMessage("あなたは死神です");
        players.get(1).getPlayer().getInventory().addItem(Sword.getFake());
        players.get(1).getPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL));
        players.remove(1);
        Collections.shuffle(players);
        players.get(1).setRole(RoleType.Killer);
        players.get(1).getPlayer().sendMessage("あなたはキラです");
        players.get(1).getPlayer().getInventory().addItem(Sword.getFake());
        players.get(1).getPlayer().getInventory().addItem(DeathNotesShard.getItemStack());
        players.remove(1);
        Collections.shuffle(players);
        players.get(1).setRole(RoleType.L);
        players.get(1).getPlayer().sendMessage("あなたはLです");
        players.get(1).getPlayer().getInventory().addItem(Sword.getFake());
        players.remove(1);
        Collections.shuffle(players);
        players.get(1).setRole(RoleType.Mass);
        players.get(1).getPlayer().sendMessage("あなたはミサです");
        players.get(1).getPlayer().getInventory().addItem(Sword.getFake());
        players.remove(1);
        Collections.shuffle(players);
        players.get(1).setRole(RoleType.SpecialPoliceOfficer);
        players.get(1).getPlayer().sendMessage("あなたはLがわかる警察です");
        players.get(1).getPlayer().getInventory().addItem(Sword.getItemStack());
        players.get(1).getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));
        players.remove(1);
        Collections.shuffle(players);
        for(GamePlayer player: players) {
            player.setRole(RoleType.PoliceOfficer);
            players.get(1).getPlayer().sendMessage("あなたは警察です");
            player.getPlayer().getInventory().addItem(Sword.getItemStack());
        }
        Objects.requireNonNull(GamePlayer.getKiller()).getPlayer().sendMessage(Objects.requireNonNull(GamePlayer.getGrimReaper()).getPlayer().getName() + "が死神です");
        GamePlayer.getGrimReaper().getPlayer().sendMessage(GamePlayer.getKiller().getPlayer().getName() + "がキラです");
        return true;
    }

    public enum GrimReaperType {
        Ryuk,Rem
    }

    public void refreshPlayers() {
        GamePlayer.unregisterPlayers(GamePlayer.getPlayers());
        GamePlayer.registerPlayers((Set<Player>) Bukkit.getOnlinePlayers());
    }
}
