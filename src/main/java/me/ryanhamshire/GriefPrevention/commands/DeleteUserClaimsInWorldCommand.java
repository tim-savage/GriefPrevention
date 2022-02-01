package me.ryanhamshire.GriefPrevention.commands;

import me.ryanhamshire.GriefPrevention.CustomLogEntryTypes;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class DeleteUserClaimsInWorldCommand implements CommandExecutor, TabCompleter
{
    // reference to plugin main class
    private final GriefPrevention plugin;

    /**
     * Class constructor
     *
     * @param plugin reference to main class
     */
    public DeleteUserClaimsInWorldCommand(final GriefPrevention plugin)
    {
        // set reference to main class
        this.plugin = plugin;

        // register this class as command executor
        Objects.requireNonNull(plugin.getCommand("deleteuserclaimsinworld")).setExecutor(this);
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, @NotNull final String[] args)
    {
        if (args.length == 1)
        {
            // match first argument to all server world names
            return plugin.commandManager.matchWorldNames(args[0]);
        }
        // if not first argument, return empty list
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, @NotNull final String[] args)
    {
        //must be executed at the console
        if (!(sender instanceof ConsoleCommandSender))
        {
            GriefPrevention.sendMessage(sender, TextMode.Err, Messages.ConsoleOnlyCommand);
            return true;
        }

        //requires exactly one parameter, the world name
        if (args.length != 1) return false;

        //try to find the specified world
        World world = Bukkit.getServer().getWorld(args[0]);
        if (world == null)
        {
            GriefPrevention.sendMessage(sender, TextMode.Err, Messages.WorldNotFound);
            return true;
        }

        //delete all USER claims in that world
        plugin.dataStore.deleteClaimsInWorld(world, false);
        GriefPrevention.AddLogEntry("Deleted all user claims in world: " + world.getName() + ".", CustomLogEntryTypes.AdminActivity);
        return true;
    }
}
