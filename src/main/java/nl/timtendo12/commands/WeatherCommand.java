package nl.timtendo12.commands;

import nl.timtendo12.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static nl.timtendo12.Utility.replaceColorChar;

public class WeatherCommand extends BaseCommand {

	public WeatherCommand(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

		// check if there are more than 0 arguments and less than 2
		if (args.length == 1) {

			// get the player from the sender
			Player player = sender.getServer().getPlayer(sender.getName());

			switch (args[0]) {
				case "clear":
				case "sun":
					player.getWorld().setStorm(false);
					player.getWorld().setThundering(false);
					sender.sendMessage(replaceColorChar("&aWeather set to &6sunny!", '&'));
					break;
				case "rain":
					player.getWorld().setStorm(true);
					player.getWorld().setThundering(false);
					sender.sendMessage(replaceColorChar("&aWeather set to &3rainy!", '&'));
					break;
				case "storm":
				case "thunder":
					player.getWorld().setStorm(true);
					player.getWorld().setThundering(true);
					sender.sendMessage(replaceColorChar("&aWeather set to &8thunder!", '&'));
					break;
				default:
					sender.sendMessage(replaceColorChar("&4Invalid weather, expected &c(sun,clear)/rain/(thunder,storm)&4!", '&'));
					break;
			}

			return true;
		} else sender.sendMessage(replaceColorChar("&4Invalid argument(s), expected &c(sun,clear)/rain/(thunder,storm)&4!", '&'));
		return true;
	}
}
