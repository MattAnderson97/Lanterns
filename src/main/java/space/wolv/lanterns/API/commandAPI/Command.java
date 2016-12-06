package space.wolv.lanterns.API.commandAPI;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * marks a method as a command.
 *
 * used for dynamically registering commands with @Command
 * e.g.
 *
 * @Command
 * (
 *      name = "example";
 *      description = "example command";
 *      usage = "/example";
 *      permission = "example.permission";
 *      permissionMessage = "You don't have permission to use this";
 * )
 * public boolean exampleCommand(CommandSender sender, String name, String[] args)
 * {
 *     ...
 * }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command
{
    /**
     * required
     *
     * the command's name (e.g. /example would have the name "example")
     *
     */
    String name();

    /**
     * Optional
     *
     * The command's description (shows up in /help)
     */

    String description() default "";

    /**
     * Optional
     *
     * The command's usage
     * This will be sent when false is returned
     */
    String usage() default "";

    /**
     * Optional
     *
     * the command's permission node
     * players without this permission node will not be able to execute the command
     */
    String permission() default "";

    /**
     * Optional
     *
     * The message players will recieve if they don't have the required permission node
     */
    String permissionMessage() default "You don't have permission to use this command";

    /**
     * Optional
     *
     * Alternate names for the command
     */
    String[] aliases() default {};

    /**
     * Optional
     *
     * if true, only players can execute the command
     */
    boolean playerCommand() default false;

    /**
     * Optional
     *
     * if true, hides the command from /help
     */
    boolean hidden() default false;
}
