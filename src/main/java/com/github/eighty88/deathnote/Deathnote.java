package com.github.eighty88.deathnote;

import com.github.eighty88.deathnote.itemstacks.DataForInvestigation;
import com.github.eighty88.deathnote.players.GamePlayer;
import com.github.eighty88.deathnote.players.RoleType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public final class Deathnote extends JavaPlugin {

    GameController controller;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);
        for(Player player: Bukkit.getOnlinePlayers()) {
            GamePlayer.registerPlayer(player);
        }
        this.controller = new GameController(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(controller.isStarted()) {
                for(PotionEffect effect : Objects.requireNonNull(GamePlayer.getGrimReaper()).getPlayer().getActivePotionEffects()) {
                    if(effect.getType() == PotionEffectType.INVISIBILITY) {
                        controller.time--;
                        GamePlayer.getGrimReaper().getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Integer.toString(controller.time)));
                    }
                }
            }
        }, 0, 20);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "deathnote":
                switch (args[0]) {
                    case "select":
                        sender.spigot().sendMessage(new ComponentBuilder().append(ChatColor.BLUE + "今すぐ").event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deathnote kill " + args[1] + " false")).create());
                        sender.spigot().sendMessage(new ComponentBuilder().append(ChatColor.BLUE + "40秒後").event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deathnote kill " + args[1] + " true")).create());
                        return true;
                    case "kill":
                        if(controller.size != 0) {
                            if (args[2].equals("true")) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
                                    Objects.requireNonNull(Bukkit.getPlayerExact(args[1])).damage(1000);
                                    Objects.requireNonNull(Bukkit.getPlayerExact(args[1])).getWorld().spawnParticle(Particle.NAUTILUS, Objects.requireNonNull(Bukkit.getPlayerExact(args[1])).getLocation(), 5000, 1, 1, 1, 1);
                                }, 800L);
                            } else {
                                Objects.requireNonNull(Bukkit.getPlayerExact(args[1])).damage(1000);
                                Objects.requireNonNull(Bukkit.getPlayerExact(args[1])).getWorld().spawnParticle(Particle.NAUTILUS, Objects.requireNonNull(Bukkit.getPlayerExact(args[1])).getLocation(), 5000, 1, 1, 1, 1);
                            }
                            controller.size--;
                        }
                        return true;
                    case "get":
                        if(args[1].equals(Objects.requireNonNull(GamePlayer.getKiller()).getPlayer().getName())) {
                            sender.sendMessage(args[1] + "はキラです!");
                        } else {
                            sender.sendMessage(args[1] + "はキラではありません!");
                        }
                        ((Player) sender).getInventory().remove(DataForInvestigation.getItemStack());
                        return false;
                    case "start":
                        controller.start();
                        return true;
                    case "end":
                        controller.end(RoleType.Watching);
                        return true;
                }
            case "test":
                return true;
        }
        return false;
    }
}
