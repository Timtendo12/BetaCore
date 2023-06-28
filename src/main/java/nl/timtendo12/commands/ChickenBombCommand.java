package nl.timtendo12.commands;

import nl.timtendo12.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import static nl.timtendo12.Utility.replaceColorChar;

public class ChickenBombCommand extends BaseCommand {

	public ChickenBombCommand(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

		boolean isPlayerJapanese = false;

		Player player = sender.getServer().getPlayer(sender.getName());
		Location L = player.getLocation();

		// get first argument and check if it is "IamJapanese"
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("IamJapanese")) {
				isPlayerJapanese = true;
				player.sendMessage(replaceColorChar("&7You are now &cJapanese&7!", '&'));
			}
		}

		// get the players looking direction
		float yaw = L.getYaw();
		float pitch = L.getPitch();

		player.sendMessage(replaceColorChar("&7Dropping &8Chicken Bomb &7at: " + yaw + ", " + pitch + " from " + L.getX() + ", " + L.getZ(), '&'));

		// get the location, and add 2 to the y axis
		Location CL = L.add(0, 2, 0);

		// spawn the chicken
		Chicken C = (Chicken) player.getWorld().spawnCreature(CL, CreatureType.CHICKEN);

		// set the passenger to the player if the player is japanese
		if (isPlayerJapanese) C.setPassenger(player);

		// set the chickens velocity
		C.setVelocity(L.getDirection().multiply(5));

		// wait 5 seconds and then explode the chicken
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				// explode the chicken
				C.getWorld().createExplosion(C.getLocation(), 5);
				//to be sure
				C.setHealth(0);

				sender.sendMessage(replaceColorChar("&8Chicken bomb &7has been dropped!", '&'));
			}
		}, 100L);

		return true;
	}
}
