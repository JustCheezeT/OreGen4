package me.banbeucmas.oregen3.utils.hooks;

import org.bukkit.Location;

import com.bgsoftware.superiorskyblock.api.island.Island;

import java.util.UUID;

public interface SkyblockHook {

    long getIslandLevel(UUID uuid);

    UUID getIslandOwner(Location loc);

	boolean isOnIsland(Location loc);

	Island getIsland(Location loc);
}
