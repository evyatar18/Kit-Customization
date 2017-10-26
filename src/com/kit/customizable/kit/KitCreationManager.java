package com.kit.customizable.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.kit.customizable.ItemUtils;
import com.kit.customizable.armor.ArmorItem;
import com.kit.customizable.armor.ArmorItem.ArmorPart;
import com.kit.customizable.skill.SkillType;
import com.kit.customizable.weapon.WeaponItem;

public class KitCreationManager {
	
	private KitCreationManager() { }
	
	private static final KitCreationManager instance = new KitCreationManager();
	public static KitCreationManager getInstance() { return instance; }
	
	public final Listener logoutListener = new Listener() {
		
		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent event) {
			String player = event.getPlayer().getName();
			
			KitCreationManager.this.armor.remove(player);
			KitCreationManager.this.weapon.remove(player);
			KitCreationManager.this.skill.remove(player);
		}
	};
	
	private final String NONE = "None";
	
	private final Map<String, ArmorItem[]> armor = new HashMap<>();
	private final Map<String, WeaponItem> weapon = new HashMap<>();
	private final Map<String, SkillType> skill   = new HashMap<>();
	
	private final Map<String, Kit> kits = new HashMap<>();
	
	
	public void setArmorComponents(Player player, ArmorItem[] armorComponents) {
		Validate.notNull(player);
		Validate.notNull(armorComponents);
		Validate.isTrue(armorComponents.length == ArmorPart.values().length);
		
		this.armor.put(player.getName(), armorComponents);
	}
	
	public ArmorItem[] getArmorComponents(Player player) {
		return this.armor.get(player.getName());
	}
	
	private int getArmorPartIndex(ArmorPart part) {
		switch(part) {
			case BOOTS: {
				return 3;
			}
			case CHESTPLATE: {
				return 1;
			}
			case HELMET: {
				return 0;
			}
			case LEGGINGS: {
				return 2;	
			}
			default: {
				return -1;
			}
		}
	}
	
	public ArmorItem getArmorComponent(Player player, ArmorPart armorPart) {
		ArmorItem[] armorItems = getArmorComponents(player);
		return armorItems == null ? null : armorItems[getArmorPartIndex(armorPart)];
	}
	
	public void removeArmorPart(Player player, ArmorPart part) {
		ArmorItem[] armorItems = this.armor.get(player.getName());
		if (armorItems != null) {
			armorItems[getArmorPartIndex(part)] = null;
		}
	}
	
	public void setArmorComponent(Player player, ArmorItem armorItem) {
		int index = getArmorPartIndex(armorItem.armorPart);
		
		ArmorItem[] armorItems = getArmorComponents(player);
		
		if (armorItems == null) {
			armorItems = new ArmorItem[ArmorPart.values().length];
			this.armor.put(player.getName(), armorItems);
		}
		
		armorItems[index] = armorItem;
	}
	
	public void setWeapon(Player player, WeaponItem weaponItem) {
		Validate.notNull(player);
		
		this.weapon.put(player.getName(), weaponItem);
	}
	
	public WeaponItem getWeapon(Player player) {
		return this.weapon.get(player.getName());
	}
	
	public void setSkill(Player player, SkillType skillType) {
		Validate.notNull(player);
		this.skill.put(player.getName(), skillType);
	}
	
	public SkillType getSkill(Player player) {
		return this.skill.get(player.getName());
	}
	
	public int createKit(Player player) {
		ArmorItem[] armorComponents = getArmorComponents(player);
		WeaponItem weapon = getWeapon(player);
		
		if (armorComponents == null || weapon == null)
			return 0;
		
		Kit kit = new Kit(armorComponents, weapon, SkillType.DEFAULT);
		this.kits.put(player.getName(), kit);
		
		KitConfigurations.getInstance().getConfiguration(player.getUniqueId()).saveKit(0, kit);
		
		this.weapon.remove(player.getName());
		this.skill.remove(player.getName());
		this.armor.remove(player.getName());
		return 1;
	}
	
	public Kit getKit(Player player) {
		Kit kit;
		
		if ((kit = this.kits.get(player.getName())) == null) {
			kit = KitConfigurations.getInstance().getConfiguration(player.getUniqueId()).getKit(0);
		}
		
		return kit;
	}
	
	public int getUsedKilograms(Player player) {
		int used = 0;
		
		ArmorItem[] armorItems;
		if ((armorItems = getArmorComponents(player)) != null) {
			for (ArmorItem item : armorItems) {
				if (item != null) {
					used += item.getWeight();
				}
			}
		}
		
		WeaponItem weaponItem;
		
		if ((weaponItem = getWeapon(player)) != null) {
			used += weaponItem.getWeight();
		}
		
		return used;
	}
	
	public int getMaxKilograms(Player player) {
		// TODO: get max kilograms from configuration and add default max to it
		return 100;
	}
	
	public ItemStack getUsedKGItem(Player player) {
		int maxKG = getMaxKilograms(player),
			usedKG = getUsedKilograms(player),
			leftKG = maxKG - usedKG;
		return ItemUtils.createItem(Material.ANVIL, leftKG, "" + ChatColor.YELLOW + leftKG + " Kilograms Are Available",
				Arrays.asList("", ChatColor.GRAY + "Usage: " + usedKG + "/" + maxKG + " Kilograms"));
	}
	
	public ItemStack getChosenItem(Player player, Class<?> kind) {
		String[] itemNames;
		
		if (kind == WeaponItem.class) {
			itemNames = new String[1];
			WeaponItem used = getWeapon(player);
			
			if (used != null) {
				itemNames[0] = used.getName();
			}
			else {
				itemNames[0] = NONE;
			}
		}
		else if (kind == ArmorItem.class) {
			itemNames = new String[ArmorPart.values().length];
			
			ArmorItem[] armorItems = getArmorComponents(player);
			
			if (armorItems == null)
				armorItems = new ArmorItem[ArmorPart.values().length];
			
			ArmorItem currentItem;
			for (int i = 0; i < armorItems.length; i++) {
				currentItem = armorItems[i];
				
				if (currentItem == null) {
					itemNames[i] = NONE;
				}
				else {
					itemNames[i] = currentItem.getName();
				}	
			}
		}
		else if (kind == SkillType.class) {
			itemNames = new String[1];
			// TODO: implement skills
			
			SkillType skill = getSkill(player);
			
			if (skill == null) {
				itemNames[0] = SkillType.DEFAULT.getName();
			}
			else {
				itemNames[0] = skill.getName();
			}
		}
		else {
			return null;
		}
		
		return ItemUtils.createItem(Material.BOOK, 1, ChatColor.RESET + "SELECTION", chosenItems(itemNames));
	}
	
	private List<String> chosenItems(String[] itemNames) {
		itemNames = Arrays.stream(itemNames).map(itemName -> ChatColor.YELLOW + itemName)
						.toArray(String[]::new);
		List<String> list = new ArrayList<>();
		list.add("");
		list.add(ChatColor.GRAY + "Chosen: ");
		list.addAll(Arrays.asList(itemNames));
		return list;
	}

	
}
