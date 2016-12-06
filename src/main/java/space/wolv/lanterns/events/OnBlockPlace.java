package space.wolv.lanterns.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import space.wolv.lanterns.API.data.DataKeys;
import space.wolv.lanterns.API.data.Hash;

public class OnBlockPlace implements Listener
{
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Block placedBlock = event.getBlock();
        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        if (placedBlock.getType() == Material.GLOWSTONE || placedBlock.getType() == Material.GLASS)
        {
            if (player.hasPermission("lanterns.place"))
            {
                if (Hash.hasBool(playerUUID, DataKeys.PLACINGENABLED))
                {
                    if (Hash.hasLocations(playerUUID))
                    {
                        Hash.addLocation(playerUUID, placedBlock.getLocation());
                        player.sendMessage("Lantern placed");
                    }
                }
            }
        }
    }
}
