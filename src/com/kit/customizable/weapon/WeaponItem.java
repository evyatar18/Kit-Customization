package com.kit.customizable.weapon;

import org.bukkit.Material;

import com.kit.customizable.kit.KitContent;

public class WeaponItem extends KitContent {
	
	public static enum WeaponType {
		BOW("Bow"),
		WOOD_SWORD("Wooden Sword"),
		GOLD_SWORD("Golden Sword"),
		STONE_SWORD("Stone Sword"),
		IRON_SWORD("Iron Sword"),
		DIAMOND_SWORD("Diamond Sword");
		
		public final String name;
		
		WeaponType(String name) {
			this.name = name;
		}
		
		public static WeaponType match(String weaponName) {
			for (WeaponType weapon : values()) {
				if (weapon.name.equals(weaponName)) {
					return weapon;
				}
			}
			return null;
		}
	}
	
	private final WeaponType weaponType;
	
	public WeaponItem(WeaponType weapon, int weights) {
		super(weapon.name, weights);
		
		Material mat = Material.valueOf(weapon.name());
		super.setMenuItem(mat);
		super.setGameItem(mat);
		
		this.weaponType = weapon;
	}
	
	public WeaponType getType() {
		return this.weaponType;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == this)
			return true;
		
		if (!(object instanceof WeaponItem))
			return false;
		
		return ((WeaponItem) object).weaponType == this.weaponType;
	}
}
