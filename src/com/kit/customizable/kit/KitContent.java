package com.kit.customizable.kit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.kit.customizable.ItemUtils.KitItems;

public abstract class KitContent {
	
	private final int weight;
	private final String name;
	
	private ItemStack gameItem,
					  menuItem;
	
	public KitContent(String name, int weight) {
		this.weight = weight;
		this.name = name;
	}
	
	public final int getWeight() {
		return this.weight;
	}
	
	public final String getName() {
		return this.name;
	}
	
	protected final void setGameItem(Material material) {
		this.gameItem = KitItems.createGameItem(material, this.name);
	}
	
	public final ItemStack getGameItem() {
		return this.gameItem;
	}
	
	protected final void setMenuItem(Material material) {
		this.menuItem = KitItems.createMenuItem(material, this.name, this.weight);
	}
	
	public final ItemStack getMenuItem() {
		return this.menuItem;
	}
	
	@Override
	public final String toString() {
		return this.name;
	}
}
