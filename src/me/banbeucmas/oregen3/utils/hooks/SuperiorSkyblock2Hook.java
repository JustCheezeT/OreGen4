package me.banbeucmas.oregen3.utils.hooks;

import java.util.UUID;

import org.bukkit.Location;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
public class SuperiorSkyblock2Hook implements SkyblockHook{

    private SuperiorSkyblock api;

    public SuperiorSkyblock2Hook() {
        api = SuperiorSkyblockAPI.getSuperiorSkyblock();
    }

	@SuppressWarnings("deprecation")
	@Override
	public long getIslandLevel(UUID uuid) {
        return api.getGrid().getIsland(uuid).getIslandLevel();
    }

	@Override
	public UUID getIslandOwner(Location loc) {
		return api.getGrid().getIslandAt(loc).getOwner().getUniqueId();  
	}

	@Override
	public boolean isOnIsland(Location loc) {
		return api.getGrid().getIslandAt(loc) != null;
	}
	
	@Override
	public Island getIsland(Location loc) {
		return api.getGrid().getIslandAt(loc);
	}
	
}
