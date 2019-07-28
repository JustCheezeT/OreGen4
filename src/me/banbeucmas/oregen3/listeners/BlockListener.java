package me.banbeucmas.oregen3.listeners;

import me.banbeucmas.oregen3.Oregen3;
import me.banbeucmas.oregen3.data.MaterialChooser;
import me.banbeucmas.oregen3.utils.BlockUtils;
import me.banbeucmas.oregen3.utils.PluginUtils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import static me.banbeucmas.oregen3.Oregen3.getHook;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BlockListener implements Listener {
	
    private Oregen3 plugin = Oregen3.getPlugin();
    private FileConfiguration config = plugin.getConfig();
    
    private File pex = new File("plugins" + System.getProperty("file.separator") + "PermissionsEx" + System.getProperty("file.separator") + "permissions.yml");
    private FileConfiguration pexConf;
    
    String perm1, perm2;
    
    
	@EventHandler
    public void onOre(BlockFromToEvent e) {
		pexConf = new YamlConfiguration();
		try {
			pexConf.load(pex);
		} catch (IOException | InvalidConfigurationException e1) {
            e1.printStackTrace();
        }
		List<String> perm = pexConf.getStringList("users." + getHook().getIslandOwner(e.getBlock().getLocation()) + ".permissions");
		if (perm.contains("oregen4.gen1")) perm1 = perm.get(perm.indexOf("oregen4.gen1"));
		else perm1 = "";
		if (perm.contains("oregen4.gen2")) perm2 = perm.get(perm.indexOf("oregen4.gen2"));
		else perm2 = "";
		
        World world = e.getBlock().getLocation().getWorld();
        if (e.getBlock() == null
                || e.getBlock().getType() == Material.AIR
                || config.getStringList("disabledWorlds").contains(world.getName())) {
            return;
        }

        Block source = e.getBlock();
        Block to = e.getToBlock();
        if ((source.getType() == Material.WATER 
        	|| source.getType() == Material.STATIONARY_LAVA 
        	|| source.getType() == Material.STATIONARY_WATER 
        	|| source.getType() == Material.LAVA)) {

            if(perm1 != "" && (to.getType() == Material.AIR
                    || to.getType() == Material.WATER
                    || to.getType() == Material.STATIONARY_WATER)
                    && source.getType() != Material.STATIONARY_WATER
                    && generateCobble(source.getType(), to)
                    && e.getFace() != BlockFace.DOWN){
                if(source.getType() == Material.LAVA || source.getType() == Material.STATIONARY_LAVA){
                    if(!isSurroundedByWater(to.getLocation())){
                        return;
                    }
                }
                to.setType(randomChance(source.getLocation()));

                world.playSound(to.getLocation(), PluginUtils.getCobbleSound(), 3, 2);
            }
            else if(perm2 != "" && generateCobbleBlock(source, to)){
                to.setType(randomChance(source.getLocation()));
                world.playSound(to.getLocation(), PluginUtils.getCobbleSound(), 3, 2);
            }
        }

    }

    private boolean generateCobbleBlock(Block src, Block to){
        Material material = src.getType();
        for(BlockFace face : BlockUtils.FACES){
            Block check = to.getRelative(face);
            if(BlockUtils.isBlock(check)
                    && (material == Material.WATER
                    || material == Material.STATIONARY_WATER)
                    && config.getBoolean("mode.waterBlock")){
                    return true;
            }
            else if(BlockUtils.isBlock(check)
                    && (material == Material.LAVA
                    || material == Material.STATIONARY_LAVA)
                    && config.getBoolean("mode.lavaBlock")){
                    return true;
            }
        }
        return false;
    }

    /*
    Checks for Water + Lava, block will use another method to prevent confusion
     */
    private boolean generateCobble(Material material, Block b){
        Material mirMat1 = material == Material.WATER || material == Material.STATIONARY_WATER
                ? Material.LAVA : Material.WATER;
        Material mirMat2 = material == Material.WATER || material == Material.STATIONARY_WATER
                ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER;

        for(BlockFace face : BlockUtils.FACES){
            Block check = b.getRelative(face, 1);
            if((check.getType() == mirMat1 || check.getType() == mirMat2)
                    && config.getBoolean("mode.waterLava")){
                return true;
            }
        }

        return false;

    }

    private Material randomChance(Location loc){
        MaterialChooser mc = PluginUtils.getChooser(loc);
        Map<Material, Double> chances = mc.getChances();

        Random r = new Random();

        double chance = 100 * r.nextDouble();

        if(!config.getBoolean("randomFallback")){
            for(Material material : chances.keySet()){
                chance -= chances.get(material);
                if(chance <= 0){
                    return material;
                }
            }
        }
        else{
            int id = r.nextInt(chances.size());
            Material mat = (Material) chances.keySet().toArray()[id];
            if(chance <= mc.getChances().get(mat)){
                return mat;
            }
        }
        return mc.getFallback();
    }

    private boolean isSurroundedByWater(Location fromLoc) {
        Block[] blocks = {
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() + 1, fromLoc.getBlockY(), fromLoc.getBlockZ()),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() - 1, fromLoc.getBlockY(), fromLoc.getBlockZ()),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() + 1),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() - 1) };

        for (Block b : blocks) {
            if (b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER) {
                return true;
            }
        }
        return false;

    }

}