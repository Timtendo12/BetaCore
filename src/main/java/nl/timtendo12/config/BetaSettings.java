package nl.timtendo12.config;

import org.bukkit.util.config.Configuration;

import java.io.File;

public class BetaSettings extends Configuration {

	public BetaSettings(File settingsFile) {
		super(settingsFile);
		this.reload();
	}

	private void write() {
		generateConfigOption("config-version", 1);

		generateConfigOption("chat-format", "[%time%][%world%]%player%: %message%");

		generateConfigOption("EnableChatFormat", true);

		generateConfigOption("join-message-format", "&aWelcome to the server, %player%!");

		generateConfigOption("EnableJoinMessage", true);
	}

	public void generateConfigOption(String key, Object defaultValue) {
		if (this.getProperty(key) == null) {
			this.setProperty(key, defaultValue);
		}
		final Object value = this.getProperty(key);
		this.removeProperty(key);
		this.setProperty(key, value);
	}

	public void generateComment(String key, String comment) {
		this.setHeader();
	}

	//Getters Start
	public Object getConfigOption(String key) {
		return this.getProperty(key);
	}

	public String getConfigString(String key) {
		return String.valueOf(getConfigOption(key));
	}

	public Integer getConfigInteger(String key) {
		return Integer.valueOf(getConfigString(key));
	}

	public Long getConfigLong(String key) {
		return Long.valueOf(getConfigString(key));
	}

	public Double getConfigDouble(String key) {
		return Double.valueOf(getConfigString(key));
	}

	public Boolean getConfigBoolean(String key) {
		return Boolean.valueOf(getConfigString(key));
	}

	private void reload() {
		this.load();
		this.write();
		this.save();
	}
}
