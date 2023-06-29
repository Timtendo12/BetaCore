package nl.timtendo12.config;

import nl.timtendo12.Main;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
					"  overwrite_respawn TINYINT(1) DEFAULT 0,\n" +
					"  joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
					"  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
					"  created_at DATETIME DEFAULT CURRENT_TIMESTAMP\n" +
					");";

			statement.executeUpdate(createPlayerTableSQL);

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

}
