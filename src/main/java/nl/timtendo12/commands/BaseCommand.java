package nl.timtendo12.commands;

import nl.timtendo12.Main;
import org.bukkit.command.CommandExecutor;

public abstract class BaseCommand implements CommandExecutor {

	protected Main plugin;

	public BaseCommand(Main plugin) {
		this.plugin = plugin;
	}
}
