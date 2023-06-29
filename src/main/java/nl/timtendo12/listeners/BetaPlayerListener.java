package nl.timtendo12.listeners;

import nl.timtendo12.Main;
import nl.timtendo12.config.BetaDatabase;
import nl.timtendo12.config.BetaSettings;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import static nl.timtendo12.Utility.replaceColorChar;


public class BetaPlayerListener extends PlayerListener {

	private BetaSettings settings;
	private BetaDatabase database;
	String chatFormat;
	String worldName;
	String time;
	String playerName;
	String message;

	public BetaPlayerListener(Main plugin) {
		this.settings = Main.getSettings();
	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		settings = Main.getSettings();

		if (!settings.getConfigBoolean("EnableChatFormat")) return;

		// cancel the event
		event.setCancelled(true);

		// get the chat format from the config
		chatFormat = settings.getConfigString("chat-format");

		// get the world name
		worldName = event.getPlayer().getWorld().getName();

		// get current system time as HH:MM
		time = String.format("%tR", System.currentTimeMillis());
		// get the player name
		playerName = event.getPlayer().getDisplayName();
		// get the message
		message = event.getMessage();

		// replace the variables
		chatFormat = chatFormat.replace("%world%", worldName);
		chatFormat = chatFormat.replace("%time%", time);
		chatFormat = chatFormat.replace("%player%", playerName);
		chatFormat = chatFormat.replace("%message%", message);

		// send the message to all players
		event.getPlayer().getServer().broadcastMessage(replaceColorChar(chatFormat, '&'));
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		settings = Main.getSettings();
		database = Main.getPluginDatabase();

		if (!settings.getConfigBoolean("EnableJoinMessage")) return;

		event.setJoinMessage(replaceColorChar(settings.getConfigString("join-message-format"), '&').replace("%player%", event.getPlayer().getDisplayName()));

		if (!database.createUser(event.getPlayer())) {
			event.getPlayer().sendMessage(replaceColorChar("&cFailed to create user in database!", '&'));
		} else event.getPlayer().sendMessage(replaceColorChar("&aCreated user in database!", '&'));
	}
}
