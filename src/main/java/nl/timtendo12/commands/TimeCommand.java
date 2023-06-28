package nl.timtendo12.commands;

import nl.timtendo12.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static nl.timtendo12.Utility.replaceColorChar;

public class TimeCommand extends BaseCommand {

	public TimeCommand(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

		// check if there are more than 0 arguments
		if (args.length > 0) {

			// returns the time in ticks, -1 if invalid
			int time = this.getTime(args, sender);

			if (time < 0) {
				sender.sendMessage(replaceColorChar("&4Invalid time, Exepected a &cnumber&4 or &cday&4/&cnight&4/&cnoon&4/&cmidnight&4!", '&'));
				return true;
			}


			// check if arg is a number, then use the args[0] as time
			if (isStringInt(args[0])) {
				sender.sendMessage(replaceColorChar("&aSetting time to &2" + time, '&'));
			} else sender.sendMessage(replaceColorChar("&aSetting time to &2" + args[0], '&'));

				// set time
			sender.getServer().getWorld("world").setTime(time);
		} else sender.sendMessage(replaceColorChar("&4Invalid arguments, Exepected a &cnumber&4 or &cday&4/&cnight&4/&cnoon&4/&cmidnight&4!", '&'));

		return true;
	}


	public boolean isStringInt(String s)
	{
		try
		{
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex)
		{
			return false;
		}
	}

	public int getTime(String[] args, CommandSender sender) {

		switch (args[0]) {
			case "night":
				return 13000;
			case "noon":
				return 6000;
			case "midnight":
				return 18000;
			case "set":
				// check if there are more than 1 arguments
				if (args.length < 2) {
					sender.sendMessage(replaceColorChar("&4Expected a time!", '&'));
					return -1;
				}

				// little hackeywackey
				args[0] = args[1];
				return this.getTime(args, sender);
			default:
				//check if args[0] is a number
				try {
					return Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {

					if (args[0].equals("day")) {
						return 0;
					}
					return -1;
				}
		}
	}
}
