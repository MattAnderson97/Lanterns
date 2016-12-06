package space.wolv.lanterns.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.wolv.lanterns.API.commandAPI.Command;
import space.wolv.lanterns.API.data.DataKeys;
import space.wolv.lanterns.API.data.Hash;

public class LanternCmd
{
    @Command
    (
        name = "gl",
        aliases = {"glowstonelantern", "glowlantern", "lantern"},
        permission = "lanterns.enable",
        permissionMessage = "&4You do not have access to that command.",
        playerCommand = true
    )
    public boolean lanternCommand(CommandSender sender, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;
        String playerUUID = player.getUniqueId().toString();

        if (Hash.hasBool(playerUUID, DataKeys.PLACINGENABLED))
        {
            Hash.setBool(playerUUID, DataKeys.PLACINGENABLED, !Hash.getBool(playerUUID, DataKeys.PLACINGENABLED));
        }
        else
        {
            Hash.setBool(playerUUID, DataKeys.PLACINGENABLED, true);
        }

        return true;
    }
}
