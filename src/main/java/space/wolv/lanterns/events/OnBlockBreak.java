package space.wolv.lanterns.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import space.wolv.lanterns.API.data.DataKeys;
import space.wolv.lanterns.API.data.Hash;

public class OnBlockBreak implements Listener
{
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        if (brokenBlock.getType() == Material.GLOWSTONE || brokenBlock.getType() == Material.GLASS)
        {
            if (player.hasPermission("lanterns.break"))
            {
                if (Hash.hasBool(playerUUID, DataKeys.PLACINGENABLED))
                {
                    if (Hash.hasLocations(playerUUID))
                    {
                        Hash.removeLocation(playerUUID, brokenBlock.getLocation());
                        player.sendMessage("Lantern removed");
                    }
                }
            }
        }
    }
}
