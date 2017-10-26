package com.kit.customizable.armor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.kit.customizable.ItemUtils;
import com.kit.customizable.armor.ArmorItem.ArmorMaterial;
import com.kit.customizable.armor.ArmorItem.ArmorPart;

public class ArmorGenerator {
	
	static int rateArmorPart(ArmorPart part) {
		switch(part) {
		case BOOTS:
			return 1;
		case CHESTPLATE:
			return 3;
		case HELMET:
			return 0;
		case LEGGINGS:
			return 2;
		default:
			break;
		}
		return 0;
	}
	
	static int rateArmorMaterial(ArmorMaterial material) {
		switch(material){
		case DIAMOND:
			return 4;
		case GOLD:
			return 2;
		case IRON:
			return 3;
		case LEATHER:
			return 1;
		default:
			break;
		}
		return 0;
	}
	
	static int calcualteWeight(ArmorPart part, ArmorMaterial material) {
		int materialRate = rateArmorMaterial(material),
			partRate = rateArmorPart(part);
		return (partRate + materialRate)*5 + 5;
	}
	
	private ArmorGenerator() { }
	private static ArmorGenerator instance = new ArmorGenerator();
	
	public static ArmorGenerator getInstance() { return instance; }
	
	private List<ArmorItem> armorItems = new ArrayList<>();
	
	public ArmorItem getArmor(String armorName) {
		String[] split = armorName.split(" ");
		return getArmor(split[0], split[1]);
	}
	
	public ArmorItem getArmor(String partName, String materialName) {
		ArmorPart part = ArmorPart.match(partName);
		if (part == null)
			return null;
		
		ArmorMaterial material = ArmorMaterial.match(materialName);
		if (material == null)
			return null;
		return getArmor(part, material);
	}
	
	public ArmorItem getArmor(ArmorPart part, ArmorMaterial material) {
		for (ArmorItem armorItem : armorItems) {
			if (armorItem.armorPart == part && armorItem.armorMaterial == material) {
				return armorItem;
			}
		}
		
		ArmorItem item;
		armorItems.add(item = new ArmorItem(part, material, calcualteWeight(part, material)));
		return item;
	}
	
	public ArmorItem getArmor(ItemStack itemStack, boolean menuItem) {
		for (ArmorItem armorItem : this.armorItems) { 
			if (ItemUtils.isSimilar(itemStack, menuItem ? armorItem.getMenuItem() : armorItem.getGameItem(), false)) {
				return armorItem;
			}
		}
		return null;
	}
}
