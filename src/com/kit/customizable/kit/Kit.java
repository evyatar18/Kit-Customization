package com.kit.customizable.kit;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.kit.customizable.ItemUtils.KitItems;
import com.kit.customizable.armor.ArmorGenerator;
import com.kit.customizable.armor.ArmorItem;
import com.kit.customizable.armor.ArmorItem.ArmorPart;
import com.kit.customizable.skill.SkillType;
import com.kit.customizable.weapon.WeaponGenerator;
import com.kit.customizable.weapon.WeaponItem;

public class Kit implements ConfigurationSerializable {
	
	
	
	private WeaponItem weaponItem;
	private ArmorItem[] armorItems;
	private SkillType skillType;
	
	public Kit(ArmorItem[] armorItems, WeaponItem weaponItem, SkillType skillType) {
		Validate.notNull(armorItems, "armorItems cannot be null.");
		Validate.notNull(weaponItem, "weaponItem cannot be null.");
		Validate.isTrue(armorItems.length == 4, "armorItems.length must be equal to 4.");
		Validate.notNull(skillType, "skillType cannot be null.");
		
		this.armorItems = armorItems;
		this.weaponItem = weaponItem;
		this.setSkillType(skillType);
	}
	
	public WeaponItem getWeapon() {
		return this.weaponItem;
	}
	
	public ArmorItem[] getArmorContents() {
		return this.armorItems;
	}

	/**
	 * @return the skillType
	 */
	public SkillType getSkillType() {
		return skillType;
	}

	/**
	 * @param skillType the skillType to set
	 */
	public void setSkillType(SkillType skillType) {
		this.skillType = skillType;
	}
	
	public static void apply(Kit kit, Player player) {
		ItemStack weaponItem = kit.weaponItem.getGameItem();
		
		ItemStack[] armorComponents = new ItemStack[ArmorPart.values().length];
			
		int counter = 0;
		
		ArmorItem armorItem;
		for (int i = kit.armorItems.length - 1; i >= 0; i--) {
			armorItem = kit.armorItems[i];
			armorComponents[counter] = armorItem == null ? null : armorItem.getGameItem();
			counter++;
		}
		
		PlayerInventory playerInventory = player.getInventory();
		
		playerInventory.clear();
		playerInventory.setArmorContents(armorComponents);
		
		if (weaponItem.getType() == Material.BOW) {
			weaponItem.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
			weaponItem.addEnchantment(Enchantment.ARROW_INFINITE, 1);
			weaponItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
			
			playerInventory.setItem(9, KitItems.ARROW);
		}
		playerInventory.setItem(0, weaponItem);
	}

	private static final String
						 weapon_path = "weapon",
						 armor_path = "armor",
						 skill_path = "skill";
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> serialize = new HashMap<>();
		serialize.put(weapon_path, this.weaponItem.getName());
		
		String armorItems = "";
		for (ArmorItem armorItem : this.armorItems) {
			armorItems += "{";
			armorItems += armorItem == null ? "null" : armorItem.getName();
			armorItems += "}";
		}
		serialize.put(armor_path, armorItems);
		
		serialize.put(skill_path, this.skillType.getName());
		
		return serialize;
	}
	
	private static String 
				quotedBracket1 = Pattern.quote("{"),
				quotedBracket2 = Pattern.quote("}");
	
	public static Kit deserialize(Map<String, Object> map) {
		WeaponItem weapon;
		ArmorItem[] armor = new ArmorItem[4];
		SkillType skill;
		
		String workingOn;
		
		workingOn = String.valueOf(map.get(weapon_path));
		if (workingOn == null) { return null; }
		else { weapon = WeaponGenerator.getInstance().getWeapon(workingOn); }
		
		workingOn = String.valueOf(map.get(skill_path));
		if (workingOn == null) { return null; }
		else { skill = SkillType.match(workingOn); }
		
		workingOn = String.valueOf(map.get(armor_path));
		if (workingOn == null) { return null; }
		else { 
			int counter = 0;
			for (String armorItem : Pattern.compile(quotedBracket1 + "[a-zA-z]" + quotedBracket2).split(workingOn)) {
				armor[counter] = ArmorGenerator.getInstance().getArmor(armorItem);
				counter++;
			}
		}
		
		return new Kit(armor, weapon, skill);
	}
}
