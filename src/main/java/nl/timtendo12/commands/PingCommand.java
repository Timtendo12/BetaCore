package nl.timtendo12.commands;

import nl.timtendo12.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static nl.timtendo12.Utility.replaceColorChar;

public class PingCommand extends BaseCommand {
	public PingCommand(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		sender.sendMessage(replaceColorChar("&aPong!", '&'));
		return true;
	}
}
