package nl.timtendo12.commands;

import nl.timtendo12.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static nl.timtendo12.Utility.replaceColorChar;

public class HealCommand extends BaseCommand {

	public HealCommand(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

		//TODO: check if player has given a username

		// check if there are more than 0 arguments
		if (args.length > 0) {

			// check if the are more than 1 arguments
			if (args.length > 1) {
				sender.sendMessage(replaceColorChar("&4Invalid arguments, Exepected a optional &cplayername&4!", '&'));
				return true;
			}

			Player target = sender.getServer().getPlayer(args[0]);

			if (target != null) {
				// heal the player
				sender.getServer().getPlayer(args[0]).setHealth(20);
				sender.sendMessage(replaceColorChar("&dYou have healed &c" + args[0] + " &d<3", '&'));
				target.sendMessage(replaceColorChar("&dYou have been healed by &5" + sender.getName() + " &c<3", '&'));
			} else sender.sendMessage(replaceColorChar("&4Player &c" + args[0] + " &4is not online!", '&'));
			return true;
		}

		// heal the player
		sender.getServer().getPlayer(sender.getName()).setHealth(20);
		sender.sendMessage(replaceColorChar("&dYou have healed yourself &c<3", '&'));

		return true;
	}

}
