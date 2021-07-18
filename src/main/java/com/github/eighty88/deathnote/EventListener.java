package com.github.eighty88.deathnote;

import com.github.eighty88.deathnote.itemstacks.DataForInvestigation;
import com.github.eighty88.deathnote.itemstacks.DeathNotesShard;
import com.github.eighty88.deathnote.players.GamePlayer;
import com.github.eighty88.deathnote.players.RoleType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class EventListener implements Listener {

    Deathnote deathnote;


    public EventListener(Deathnote deathnote) {
        this.deathnote = deathnote;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        try {
            if (deathnote.controller.type == GameController.GrimReaperType.Ryuk) {
                for (Entity entity : Objects.requireNonNull(GamePlayer.getGrimReaper()).getPlayer().getNearbyEntities(3, 3, 3)) {
                    if (entity instanceof Player) {
                        GamePlayer player = GamePlayer.getPlayerExact(Objects.requireNonNull(((Player) entity).getPlayer()).getName());
                        if (player.isChecked()) {
                            player.check();
                        }
                    }
                }
            } else if (deathnote.controller.type == GameController.GrimReaperType.Rem) {
                for (Entity entity : Objects.requireNonNull(GamePlayer.getMass()).getPlayer().getNearbyEntities(3, 3, 3)) {
                    if (entity instanceof Player) {
                        GamePlayer player = GamePlayer.getPlayerExact(Objects.requireNonNull(((Player) entity).getPlayer()).getName());
                        if (player.isChecked()) {
                            player.check();
                        }
                    }
                }
            }
            for (Player player : GamePlayer.getPlayers()) {
                player.setCompassTarget(Objects.requireNonNull(GamePlayer.getL()).getPlayer().getLocation());
            }
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(Objects.requireNonNull(GamePlayer.getKiller()).getPlayer().getName().equals(e.getEntity().getName())) {
            deathnote.controller.end(RoleType.L);
        } else if(Objects.requireNonNull(GamePlayer.getL()).getPlayer().getName().equals(e.getEntity().getName())) {
            deathnote.controller.end(RoleType.Killer);
        } else if(deathnote.controller.type == GameController.GrimReaperType.Ryuk && Objects.requireNonNull(GamePlayer.getMass()).getPlayer().getName().equals(e.getEntity().getName()) && (GamePlayer.getPlayerExact(Objects.requireNonNull(e.getEntity().getKiller()).getName()).getRole() == RoleType.PoliceOfficer || GamePlayer.getPlayerExact(e.getEntity().getKiller().getName()).getRole() == RoleType.SpecialPoliceOfficer)) {
            GamePlayer.getKiller().getPlayer().sendMessage("Lは" + GamePlayer.getL().getPlayer().getName() + "です!");
        }

        if(GamePlayer.getPlayerExact(e.getEntity().getName()).getRole() == RoleType.PoliceOfficer || GamePlayer.getPlayerExact(e.getEntity().getName()).getRole() == RoleType.SpecialPoliceOfficer) {
            if(GamePlayer.getPlayerExact(Objects.requireNonNull(e.getEntity().getKiller()).getName()).getRole() == RoleType.PoliceOfficer || GamePlayer.getPlayerExact(e.getEntity().getKiller().getName()).getRole() == RoleType.SpecialPoliceOfficer) {
                for(GamePlayer player : GamePlayer.getOnlinePlayers()) {
                    if(player.getRole() == RoleType.PoliceOfficer && player.isChecked()) {
                        player.check();
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!GamePlayer.getPlayers().contains(e.getPlayer())) {
            GamePlayer.registerPlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(!deathnote.controller.isStarted()) {
            GamePlayer.unregisterPlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent e) {
        if(Objects.equals(e.getInventory().getResult(), new ItemStack(Material.QUARTZ_BLOCK))) {
            if(e.getWhoClicked().getName().equals(Objects.requireNonNull(GamePlayer.getL()).getPlayer().getName())) {
                e.getWhoClicked().getInventory().addItem(DataForInvestigation.getItemStack());
            }
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onPlayerPickUpItem(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player) {
            if(Objects.requireNonNull(GamePlayer.getGrimReaper()).getPlayer().getName().equals(e.getEntity().getName())) {
                if(e.getItem().getItemStack().getType() == Material.APPLE) {
                    Objects.requireNonNull(GamePlayer.getKiller()).getPlayer().getInventory().addItem(DeathNotesShard.getItemStack());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().equals(DeathNotesShard.getItemStack())) {
            for(GamePlayer player:GamePlayer.getOnlinePlayers()) {
                if(player.getRole() == RoleType.PoliceOfficer || player.getRole() == RoleType.L || player.getRole() == RoleType.SpecialPoliceOfficer) {
                    if (player.isChecked()) {
                        e.getPlayer().spigot().sendMessage(new ComponentBuilder().append(ChatColor.GREEN + player.getPlayer().getName()).append(ChatColor.BLUE + "削除").event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deathnote select " + player.getPlayer().getName())).create());
                        deathnote.controller.size++;
                    }
                }
            }
            e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        try {
            if (e.getPlayer().getName().equals(Objects.requireNonNull(GamePlayer.getGrimReaper()).getPlayer().getName())) {
                for (PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
                    if (effect.getType() == PotionEffectType.INVISIBILITY) {
                        e.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                        deathnote.controller.time = (int) Math.ceil(effect.getDuration() / 20);
                        if(deathnote.controller.time != 0)
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.deathnote, () -> e.getPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL)), 1200);
                        break;
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void onPlayerExpirePotionEffect(EntityPotionEffectEvent e) {
        if(e.getCause() == EntityPotionEffectEvent.Cause.EXPIRATION) {
            if(Objects.requireNonNull(GamePlayer.getGrimReaper()).getPlayer().getName().equals(e.getEntity().getName()) && !deathnote.controller.isUsed) {
                if(Objects.requireNonNull(e.getOldEffect()).getType() == PotionEffectType.INVISIBILITY || e.getEntity() instanceof Player) {
                    deathnote.controller.time = (int) Math.ceil(e.getOldEffect().getDuration() / 20);
                    if(deathnote.controller.time != 0)
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.deathnote, () -> ((Player) e.getEntity()).getInventory().addItem(new ItemStack(Material.SNOWBALL)), 1200);
                }
            }
        }
    }
}
