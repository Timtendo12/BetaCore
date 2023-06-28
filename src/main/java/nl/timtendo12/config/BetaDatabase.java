package nl.timtendo12.config;

import nl.timtendo12.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class BetaDatabase {

	public Connection conn = null;
	public Statement statement = null;

	public void connect(Main plugin) {
		Logger log = plugin.getServer().getLogger();
		try {
			String url = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "\\database.db";
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(url);

			log.info("Connection to SQLite has been established.");
			this.createTables();

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

	public void createTables() {
		try {
			// create player table if not exists
			statement = conn.createStatement();

			statement.setQueryTimeout(30);  // set timeout to 30 sec.

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS player ( id int NOT NULL, username varchar(255), joined_at datetime DEFAULT CURRENT_TIMESTAMP, updated_at datetime DEFAULT CURRENT_TIMESTAMP, created_at datetime DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (id));");

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean createUser(Player player) {
		try {
			String username = player.getName();
			statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO player (username) SELECT '" + username + "' WHERE NOT EXISTS (SELECT 1 FROM player WHERE username = '" + username + "');");
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

}
