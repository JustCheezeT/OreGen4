package me.banbeucmas.oregen3;

import me.banbeucmas.oregen3.commands.Commands;
import me.banbeucmas.oregen3.data.DataManager;
import me.banbeucmas.oregen3.listeners.BlockListener;
import me.banbeucmas.oregen3.listeners.GUIListener;
import me.banbeucmas.oregen3.utils.StringUtils;
import me.banbeucmas.oregen3.utils.hooks.SkyblockHook;
import me.banbeucmas.oregen3.utils.hooks.SuperiorSkyblock2Hook;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Oregen3 extends JavaPlugin implements Listener {
    private static Oregen3 plugin;
    private static SkyblockHook hook;
    public static boolean DEBUG = false;

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        updateConfig();

        boolean sSBHook = Bukkit.getServer().getPluginManager().isPluginEnabled("SuperiorSkyblock2");
        hook = new SuperiorSkyblock2Hook();

        CommandSender sender = Bukkit.getConsoleSender();
        //Send Message
        sender.sendMessage(StringUtils.getColoredString("&7&m-------------&f[Oregen4&f]&7-------------"));
        sender.sendMessage("");
        sender.sendMessage(StringUtils.getColoredString("&fPlugin made by &e&oBanbeucmas and recoded by InfiGamez"));
        sender.sendMessage(StringUtils.getColoredString("       &f&oVersion: &e" + getDescription().getVersion()));
        sender.sendMessage("");
        sender.sendMessage(StringUtils.getColoredString("------------------------------------"));


        if(getConfig().getBoolean("enableDependency")){
            getConfig().set("enableDependency", sSBHook);
            saveConfig();
        }




        DataManager.loadData();
        getCommand("oregen4").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
    }

    public void onDisable() {
        plugin = null;
        DataManager.unregisterAll();
    }

    public static SkyblockHook getHook() {
        return hook;
    }

    public static Oregen3 getPlugin() {
        return plugin;
    }

    public static void updateConfig(){
        if(!getPlugin().getConfig().isSet("version")){
            getPlugin().getConfig().set("version", "1.1.0");

            List<String> l = new ArrayList<>();
            l.add("FENCE");
            l.add("ACACIA_FENCE");
            l.add("BIRCH_FENCE");
            l.add("DARK_OAK_FENCE");
            l.add("IRON_FENCE");
            getPlugin().getConfig().set("blocks", l);
            getPlugin().getConfig().set("mode.lavaBlock", false);
            getPlugin().getConfig().set("mode.waterBlock", true);
            getPlugin().getConfig().set("mode.lavaFence", null);
            getPlugin().getConfig().set("mode.waterFence", null);
            getPlugin().saveConfig();
        }
        if(getPlugin().getConfig().getString("version").equals("1.1.0")){
            getPlugin().getConfig().set("version", "1.2.0");
            getPlugin().getConfig().set("enableDependency", true);
            getPlugin().saveConfig();
            updateConfig();
        }
        if(getPlugin().getConfig().getString("version").equals("1.2.0")){
            getPlugin().getConfig().set("version", "1.3.0");
            getPlugin().getConfig().set("messages.gui.title", "&eChances");
            getPlugin().getConfig().set("messages.gui.block.displayName", "&6%name%");
            getPlugin().getConfig().set("messages.gui.block.lore",
                    Arrays.asList("&6Chances: &e%chance%&6%"));
            getPlugin().saveConfig();
        }
    }
}
