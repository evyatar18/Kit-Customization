package com.kit.customizable.kit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kit.customizable.Main;

public class KitConfigurations {
	
	public static class KitConfiguration {
		
		public static final String KIT_PATH = "kit";
		
		private final UUID playerUUID;
		
		private final File configFile;
		private FileConfiguration config;
		
		
		private boolean ready;
		
		public KitConfiguration(UUID playerUUID) {
			this.playerUUID = playerUUID;
			this.configFile = new File(Main.getInstance().getDataFolder(), playerUUID.toString());
			
			init();
			if (ready) {
				reloadConfiguration();
			}
		}
		
		private void init() {
			File directory = this.configFile.getParentFile();
			
			if (!directory.isDirectory()) { directory.delete(); }
			if (!directory.exists()) { directory.mkdirs(); }
			
			String configName = this.configFile.getName();
			
			for (int i = 0; i < 5; i++) {
				Main.getInstance().getLogger().info("Attempt #" + i + " to create config file: " + configName);
				try { 
					this.configFile.createNewFile();
					break;
				}
				catch (IOException ex) {
					Main.getInstance().getLogger().log(Level.SEVERE, "An error has occurred", ex);
					if (i == 4) {
						this.ready = false;
						return;
					}
				}
			}
				
			this.ready = true;
		}
		
		public void reloadConfiguration() {
			if (!this.ready)
				return;
			
			this.config = YamlConfiguration.loadConfiguration(this.configFile);
		}
		
		public void saveConfiguration() {
			try {
				this.config.save(configFile);
			}
			catch (IOException ex) {
				throw new Error(ex);
			}
		}
		
		public void saveKit(int index, Kit kit) {
			if (!this.ready)
				return;
			
			this.config.set(KIT_PATH + "." + index, kit.serialize());
			this.saveConfiguration();
		}
		
		public Kit getKit(int index) {
			if (!this.ready)
				return null;
			
			String kitPath = KIT_PATH + "." + index;
			
			ConfigurationSection section = this.config.getConfigurationSection(kitPath);
			
			if (section == null)
				return null;
			
			Map<String, Object> map = section.getValues(false);
			return Kit.deserialize(map);
		}  
		
		@Override
		public boolean equals(Object obj) {
			if (!this.ready)
				return false;
			
			if (obj == null)
				return false;
			if (obj == this)
				return true;
			
			return ((KitConfiguration) obj).playerUUID == this.playerUUID;
		}
	}
	
	private KitConfigurations() { }

	private static KitConfigurations instance = new KitConfigurations();
	public static KitConfigurations getInstance() { return instance; }
	
	
	private List<KitConfiguration> configurations = new ArrayList<>();
	
	public KitConfiguration getConfiguration(UUID playerUUID) {
		KitConfiguration configuration;
		
		for (KitConfiguration conf : this.configurations) {
			if (conf.playerUUID.equals(playerUUID)) {
				return conf;
			}
		}
		
		configuration = new KitConfiguration(playerUUID);
		return configuration;
	}
}
