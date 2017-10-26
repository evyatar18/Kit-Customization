package com.kit.customizable.weapon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.kit.customizable.ItemUtils;
import com.kit.customizable.weapon.WeaponItem.WeaponType;

public class WeaponGenerator {
	
	static int rateWeapon(WeaponType type) {
		switch(type) {
		case BOW:
			return 3;
		case DIAMOND_SWORD:
			return 4;
		case GOLD_SWORD:
			return 1;
		case IRON_SWORD:
			return 3;
		case WOOD_SWORD:
			return 1;
		case STONE_SWORD:
			return 2;
		default:
			break;
		}
		return 0;
	}
	
	static int calcualteWeight(WeaponType weapon) {
		int weaponRate = rateWeapon(weapon);
		// (weaponRate + (weaponRate <= 1 ? 2 : 4))
		return ((int) (weaponRate + (weaponRate > 1 ? 4 : 2)))*5;
	}
	
	
	private List<WeaponItem> weaponItems = new ArrayList<>();
	
	private WeaponGenerator() { }
	
	private static WeaponGenerator instance = new WeaponGenerator();
	public static WeaponGenerator getInstance() { return instance; }
	
	
	
	public WeaponItem getWeapon(WeaponType type) {
		for (WeaponItem weaponItem : weaponItems) {
			if (weaponItem.getType() == type) {
				return weaponItem;
			}
		}
		
		WeaponItem item;
		weaponItems.add(item = new WeaponItem(type, calcualteWeight(type)));
		return item;
	}
	
	public WeaponItem getWeapon(String name) {
		WeaponType type = WeaponType.match(name);
		if (type == null)
			return null;
		return getWeapon(type);
	}
	
	public WeaponItem getWeapon(ItemStack itemStack, boolean menuItem) {	
		for (WeaponItem weaponItem : weaponItems) {
			if (ItemUtils.isSimilar(itemStack, menuItem ? weaponItem.getMenuItem() : weaponItem.getGameItem(), false)) {
				return weaponItem;
			}
		}
		return null;
	}
}
