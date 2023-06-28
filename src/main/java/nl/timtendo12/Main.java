package nl.timtendo12;

import nl.timtendo12.commands.ChickenBombCommand;
import nl.timtendo12.commands.HealCommand;
import nl.timtendo12.commands.PingCommand;
import nl.timtendo12.commands.TimeCommand;
import nl.timtendo12.config.BetaDatabase;
import nl.timtendo12.config.BetaSettings;
import nl.timtendo12.listeners.BetaPlayerListener;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

	public static BetaSettings settings;

	public static BetaDatabase database;

	private final BetaPlayerListener betaPlayerListener = new BetaPlayerListener(this);

	@Override
	public void onEnable() {

		settings = new BetaSettings(new File(this.getDataFolder(), "config.yml"));
		database = new BetaDatabase();

		// get test.yml from the jar and copy it to the data folder

		File configFile = new File(this.getDataFolder(), "test.yml");
		if (!configFile.exists()) {
			// copy the test.yml file from the jar to the data folder
			try {
				String strpath = this.getDataFolder() + File.separator + "test.yml";
				Path path = Paths.get(strpath);
				if (configFile.createNewFile()) Files.copy(path, configFile.toPath());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		Logger log = this.getServer().getLogger();
		log.info("Enabling MineCraftBetaTest version " + getDescription().getVersion());

		// check if database.db file exists
		File sqlite = new File(this.getDataFolder(), "database.db");
		if (!sqlite.exists()) {
			log.info("Database file not found, creating one");
			try {
				if (sqlite.createNewFile()) {
					log.info("Database file created");
				} else {
					log.severe("Failed to create database file");
				}
			} catch (Exception e) {
				log.severe("Failed to create database file");
				log.severe(e.getMessage());
			}
		}

		// connecting
		database.connect(this);

		// registering commands
		log.info("Registering commands");
		this.getCommand("ping").setExecutor(new PingCommand(this));
		this.getCommand("time").setExecutor(new TimeCommand(this));
		this.getCommand("heal").setExecutor(new HealCommand(this));
		this.getCommand("chickenbomb").setExecutor(new ChickenBombCommand(this));

		//registering events
		log.info("Registering events");
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, betaPlayerListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, betaPlayerListener, Event.Priority.Normal, this);
	}

	@Override
	public void onDisable() {
		Logger log = this.getServer().getLogger();
		log.info("Goodbye world!");
	}

	public static BetaSettings getSettings() {
		return settings;
	}
	
	public static BetaDatabase getPluginDatabase() {
		return database;
	}
}