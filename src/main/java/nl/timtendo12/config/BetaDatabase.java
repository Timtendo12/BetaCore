package nl.timtendo12.config;

import nl.timtendo12.Main;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Logger;

public class BetaDatabase {

	public Connection conn = null;
	public Statement statement = null;

	public String connectionURL = null;

	public void connect(Main plugin) {
		Logger log = plugin.getServer().getLogger();
		try {
			connectionURL = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + File.separator + "database.db";
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(connectionURL);

			log.info("Connection to SQLite has been established.");
			this.createTables(plugin);

		} catch (SQLException e) {
			log.severe(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				log.severe(ex.getMessage());
			}
		}
	}

	public void createTables(Main plugin) {
		try {
			// create player table if not exists
			statement = conn.createStatement();

			statement.setQueryTimeout(30);  // set timeout to 30 sec.

			/*
			CREATE TABLE IF NOT EXISTS player (
				id INTEGER PRIMARY KEY AUTOINCREMENT,
				username VARCHAR(255),
				overwrite_respawn TINYINT(1) DEFAULT 0,
				joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
				updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
				created_at DATETIME DEFAULT CURRENT_TIMESTAMP
			);
			 */

			String createPlayerTableSQL = "CREATE TABLE IF NOT EXISTS player (id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
					"  username VARCHAR(255),\n" +
					"  overwrite_respawn TINYINT(1) DEFAULT 1,\n" +
					"  joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
					"  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
					"  created_at DATETIME DEFAULT CURRENT_TIMESTAMP\n" +
					");";

			statement.executeUpdate(createPlayerTableSQL);

			String createBedRespawnTableSQL = "CREATE TABLE IF NOT EXISTS bed_respawns (\n" +
					"  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
					"  player_id INT UNIQUE,\n" +
					"  bed_x INT,\n" +
					"  bed_y INT,\n" +
					"  bed_z INT,\n" +
					"  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
					"  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
					"  FOREIGN KEY (player_id) REFERENCES player(id)\n" +
					");";

			statement.executeUpdate(createBedRespawnTableSQL);

			plugin.getServer().getLogger().info("Table player created");

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	public boolean createUser(Player player)  {

		try {
			conn = DriverManager.getConnection(connectionURL);
			String username = player.getName();
			statement = conn.createStatement();
			String sql = "INSERT INTO player (username) SELECT '" + username + "' WHERE NOT EXISTS (SELECT 1 FROM player WHERE username = '" + username + "');";
			Main.getCurrentServer().getLogger().info(sql);
			statement.executeUpdate(sql);

		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	/**
	 * Method to retrieve the overwrite_respawn value from the player
	 * @param id the id of the player
	 * @return the overwrite_respawn value, null if not found
	 */
	public Object getBedRespawnOverwrite(String id) {

		try {
			conn = DriverManager.getConnection(connectionURL);
			statement = conn.createStatement();
			String sql = "SELECT overwrite_respawn FROM player WHERE id = '" + id + "';";
			Main.getCurrentServer().getLogger().info(sql);
			ResultSet r = statement.executeQuery(sql);

			if (r.next()) {
				r.getInt("overwrite_respawn");

				// return the boolean value of overwrite_respawn
				return r.getInt("overwrite_respawn") == 1;
			}
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}


	// method to retrieve player data from database
	public HashMap<String, String> getPlayer(Player player) {

		String username = player.getName();

		try {
			conn = DriverManager.getConnection(connectionURL);
			statement = conn.createStatement();
			String sql = "SELECT * FROM player WHERE username = '" + username + "';";
			Main.getCurrentServer().getLogger().info(sql);
			ResultSet r = statement.executeQuery(sql);

			if (r.next()) {
				Main.getCurrentServer().getLogger().info("Player found");
				// return the player data
				HashMap<String, String> playerData = new HashMap<String, String>();

				playerData.put("id", String.valueOf(r.getInt("id")));
				playerData.put("username", r.getString("username"));
				playerData.put("overwrite_respawn", String.valueOf(r.getInt("overwrite_respawn")));
				playerData.put("updated_at", r.getString("updated_at"));
				playerData.put("created_at", r.getString("created_at"));
				conn.close();
				return playerData;
			} else {
				Main.getCurrentServer().getLogger().info("Player not found");
				return null;
			}

		} catch (SQLException e) {
			return null;
		}
	}

	public boolean setBedRespawn(int id, int x, int y, int z) {
		try {
			conn = DriverManager.getConnection(connectionURL);
			statement = conn.createStatement();
			String sql = "INSERT OR REPLACE INTO bed_respawns (player_id, bed_x, bed_y, bed_z)\n" +
					"VALUES (" + id + ", " + x + ", " + y + ", " + z + ")";
			Main.getCurrentServer().getLogger().info(sql);
			statement.executeUpdate(sql);
			conn.close();
		} catch (SQLException e) {
			Main.getCurrentServer().getLogger().throwing("SQLite", "setBedRespawn", e);
			return false;
		}

		return true;
	}

	public HashMap<String, Integer> getBedRespawn(Player player) {

		String username = player.getName();

		try {
			conn = DriverManager.getConnection(connectionURL);
			statement = conn.createStatement();

			String sql = "SELECT bed_x, bed_y, bed_z FROM bed_respawns WHERE player_id = (SELECT id FROM player WHERE username = '" + username + "');";
			Main.getCurrentServer().getLogger().info(sql);
			ResultSet r = statement.executeQuery(sql);

			if (r.next()) {
				Main.getCurrentServer().getLogger().info("Bed respawn found");
				// return the bed respawn data
				HashMap<String, Integer> bedRespawnData = new HashMap<String, Integer>();

				bedRespawnData.put("x", r.getInt("bed_x"));
				bedRespawnData.put("y", r.getInt("bed_y"));
				bedRespawnData.put("z", r.getInt("bed_z"));
				conn.close();
				return bedRespawnData;
			} else {
				Main.getCurrentServer().getLogger().info("Bed respawn not found");
				return null;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
