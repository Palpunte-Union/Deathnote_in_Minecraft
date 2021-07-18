package com.github.eighty88.deathnote.itemstacks;

import com.github.eighty88.deathnote.players.GamePlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import java.util.ArrayList;
import java.util.List;

public class DataForInvestigation {
    public static ItemStack getItemStack() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "誰か一人キラではないか調査できる。");
        ItemStack result = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) result.getItemMeta();
        assert meta != null;
        int lines = 0;
        ComponentBuilder builder = new ComponentBuilder();
        for(GamePlayer player :GamePlayer.getOnlinePlayers()) {
            if(lines == 14) {
                meta.spigot().addPage(builder.create());
                lines = 0;
                builder = new ComponentBuilder();
            }
            builder.append(ChatColor.RESET + player.getPlayer().getName()).append(ChatColor.AQUA + "調査する").event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deathnote get " + player.getPlayer().getName())).append("\n");
            lines++;
        }
        meta.setLore(lore);
        meta.setTitle(ChatColor.RESET + "調査資料");
        result.setItemMeta(meta);
        return result;
    }
}
