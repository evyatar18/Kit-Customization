package com.kit.customizable.armor;

import org.bukkit.Material;

import com.kit.customizable.kit.KitContent;

public class ArmorItem extends KitContent {
	
	public static enum ArmorPart {
		HELMET("Helmet"),
		CHESTPLATE("Chestplate"),
		LEGGINGS("Leggings"),
		BOOTS("Boots");
		
		private String name;
		
		ArmorPart(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name; }
		
		public static ArmorPart match(String partName) {
			for (ArmorPart part : values()) {
				if (part.name.equals(partName)) {
					return part;
				}
			}
			return null;
		}
	}
	
	public static enum ArmorMaterial {
		LEATHER("Leather"),
		GOLD("Gold"),
		IRON("Iron"),
		DIAMOND("Diamond");
		
		private String type;
		
		ArmorMaterial(String type) {
			this.type = type;
		}
		
		public String getName() {
			return this.type;
		}

		public static ArmorMaterial match(String materialName) {
			for (ArmorMaterial material : values()) {
				if (material.type.equals(materialName)) {
					return material;
				}
			}
			return null;
		}
	}
	
	private static Material getMaterial(ArmorPart part, ArmorMaterial material) {
		String materialName = material.name() + "_" + part.name();
		return Material.valueOf(materialName);
	}
	
	public static String getArmorName(ArmorPart part, ArmorMaterial material) {
		return material.getName() + " " + part.getName();
	}
	
	public final ArmorPart armorPart;
	public final ArmorMaterial armorMaterial;
	
	
	public ArmorItem(ArmorPart part, ArmorMaterial material, int weights) {
		super(getArmorName(part, material), weights);
		
		this.armorPart = part;
		this.armorMaterial = material;
		
		Material mat = getMaterial(part, material);		
		super.setGameItem(mat);
		super.setMenuItem(mat);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == this)
			return true;
		
		if (!(object instanceof ArmorItem))
			return false;
		
		ArmorItem i = (ArmorItem) object;
		return i.armorPart == this.armorPart && i.armorMaterial == this.armorMaterial;
	}
}
