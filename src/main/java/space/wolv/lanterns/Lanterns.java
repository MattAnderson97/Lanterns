package space.wolv.lanterns;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import space.wolv.lanterns.API.commandAPI.CommandRegistry;
import space.wolv.lanterns.commands.LanternCmd;
import space.wolv.lanterns.events.OnBlockBreak;
import space.wolv.lanterns.events.OnBlockPlace;

public class Lanterns extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        //register commands
        try
        {
            CommandRegistry commands = new CommandRegistry(this);
            commands.register(new LanternCmd());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //register events
        Bukkit.getPluginManager().registerEvents(new OnBlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new OnBlockBreak(), this);
    }

    @Override
    public void onDisable()
    {

    }
}
