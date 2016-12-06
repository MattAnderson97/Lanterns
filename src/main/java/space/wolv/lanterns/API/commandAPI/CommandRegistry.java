package space.wolv.lanterns.API.commandAPI;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Dynamically load commands per plugin without the need for
 * explicitly defining them in your <i>plugin.yml</i>.
 *
 * @see Command
 */
public class CommandRegistry
{
    protected final JavaPlugin plugin;
    protected final CommandMap commandMap;

    /**
     * Create a new CommandRegistry instance for a plugin.
     * @param plugin Plugin instance.
     * @throws Exception
     */
    public CommandRegistry(JavaPlugin plugin) throws Exception
    {
        this.plugin = plugin;

        final Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMap.setAccessible(true);
        this.commandMap = (CommandMap) commandMap.get(Bukkit.getServer());
    }

    /**
     * Register new commands using an instance of a class.
     * <br>
     * Methods with the {@link Command} annotation are transformmed into commands.
     * @param instance Instance of an object containing command methods.
     */
    public void register(Object instance)
    {
        Class<?> clazz = instance.getClass();
        Method[] methods = clazz.getMethods();

        for (Method method : methods)
        {
            Command meta = method.getDeclaredAnnotation(Command.class);

            if (meta == null)
            {
                continue;
            }
            else
            {
                try
                {
                    register(instance, method, meta);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Private method that continues the command registration process through reflection.
     * @param instance Instance of an object containing command methods.
     * @param method Method object representing a valid command method.
     * @param meta Metadata from the command method's {@link Command} annotation.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void register(Object instance, Method method, Command meta) throws Exception
    {
        String attempt = "Attempting to register the command \"" + meta.name() + "\"";

        // Checking if the provided method is valid.

        if (!(method.getReturnType().equals(boolean.class)))
        {
            throw new Exception(attempt + " but its method does not return boolean.");
        }

        method.setAccessible(true);
        Parameter[] parameters = method.getParameters();

        if
                (
                (parameters.length != 3) || !(parameters[0].getType().equals(CommandSender.class)) ||
                        !(parameters[1].getType().equals(String.class)) || !(parameters[2].getType().equals(String[].class))
                )
        {
            throw new Exception
                    (
                            "\n" + attempt + " but its method has improper parameters.\n" +
                                    "Parameters should be the following: org.bukkit.command.CommandSender, String, and String[]."
                    );
        }

        // Creating the command's executor.

        CommandExecutor commandExecutor = new CommandExecutor()
        {
            @Override
            public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
            {
                try
                {
                    if (meta.playerCommand())
                    {
                        if (sender instanceof Player) return (boolean) method.invoke(instance, sender, label, args);
                        else
                        {
                            sender.sendMessage("[IgnisCore] Command only executable by players");
                            return true;
                        }
                    }
                    else return (boolean) method.invoke(instance, sender, label, args);
                }
                catch (Exception e)
                {
                    if (sender instanceof Player)
                    {
                        Player player = (Player) sender;
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAn internal error has occured!"));
                    }
                    e.printStackTrace();
                }
                return false;
            }
        };

        // Creating a new command.

        Class<PluginCommand> pluginCommandClass = PluginCommand.class;

        Constructor<PluginCommand> constructor = pluginCommandClass.getDeclaredConstructor(String.class, Plugin.class);
        constructor.setAccessible(true);
        PluginCommand command = constructor.newInstance(meta.name(), this.plugin);

        command.setExecutor(commandExecutor);

        // Preparing to add command to plugin's description.

        Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();

        if (!meta.hidden())
        {
            /* The following will create a new commands map for the plugin's description if
             * it is currently null (i.e. no commands were defined in plugin.yml).
             * ---
             * If the command is hidden, there is no need to create a new command map.
             */
            if (commands == null)
            {
                commands = new HashMap<String, Map<String, Object>>();

                Field commandsField = plugin.getDescription().getClass().getDeclaredField("commands");
                commandsField.setAccessible(true);
                commandsField.set(plugin.getDescription(), commands);
            }
        }

        Map<String, Object> commandMeta = new HashMap<String, Object>();

        // Applying command metadata.

        if (!meta.description().isEmpty())
        {
            command.setDescription(meta.description());
            commandMeta.put("description", meta.description());
        }

        if (!meta.usage().isEmpty())
        {
            command.setUsage(meta.usage());
            commandMeta.put("usage", meta.usage());
        }

        if (!meta.permission().isEmpty())
        {
            command.setPermission(meta.permission());
            commandMeta.put("permission", meta.permission());
        }

        if (!meta.permissionMessage().isEmpty())
        {
            command.setPermissionMessage(meta.permissionMessage());
            commandMeta.put("permission-message", meta.permissionMessage());
        }

        if (meta.aliases().length > 0)
        {
            ArrayList<String> aliases = new ArrayList<String>(Arrays.asList(meta.aliases()));

            command.setAliases(aliases);
            commandMeta.put("aliases", meta.aliases());
        }

        // Registering the command.

        commandMap.register(meta.name(), command);

        if (!meta.hidden())
        {
            // Adding command to plugin description.

            commands.put(command.getName(), commandMeta);

            // Generating the command's HelpTopic and registering it in the HelpMap.

            HelpMap helpMap = Bukkit.getServer().getHelpMap();

            GenericCommandHelpTopic helpTopic = new GenericCommandHelpTopic(command);

            Field helpTopicsField = helpMap.getClass().getDeclaredField("helpTopics");
            helpTopicsField.setAccessible(true);
            Map<String, HelpTopic> helpTopics = (Map<String, HelpTopic>) helpTopicsField.get(helpMap);

            helpTopics.put(command.getName(), helpTopic);
        }
    }
}