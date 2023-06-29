package nl.timtendo12.commands;

import nl.timtendo12.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WeatherCommand extends BaseCommand{

	public WeatherCommand(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		return true;
	}
}
