package com.kit.customizable;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
	
	public static class KitItems {
		public static final ItemStack ARROW = createItem(Material.ARROW, "Arrow");

		public static ItemStack createMenuItem(Material material, String name, int weight) {
			return createItem(material, 1, name, 
					Arrays.asList("", ChatColor.GRAY + "Weight ", "" + ChatColor.YELLOW + weight + " Kilograms"));
		}
		
		public static ItemStack createGameItem(Material material, String name) {
			ItemStack gameItem = createItem(material, name);
			ItemMeta gameItemMeta = gameItem.getItemMeta();
			gameItemMeta.setUnbreakable(true);
			return gameItem;
		}
	}
	
	public static Enchantment unrealEnchantment = new EnchantmentWrapper(500);
	
	public static ItemStack createItem(Material material, String name) {
		return createItem(material, 1, (short) 0, name, null);
	}
	 
	public static ItemStack createItem(Material material, short durability, String name) {
		return createItem(material, 1, durability, name, null);
	}
	
	public static ItemStack createItem(Material material, int amount, String name) {
		return createItem(material, amount, (short) 0, name, null);
	}
	
	public static ItemStack createItem(Material material, int amount, String name, List<String> lore) {
		return createItem(material, amount, (short) 0, name, lore);
	}
	
	public static ItemStack createItem(Material material, int amount, short durability, String name, List<String> lore) {
		ItemStack stack = new ItemStack(material, amount, durability);
		
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		
		if (lore != null) { meta.setLore(lore); }
		
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public static ItemStack enchant(ItemStack item) {
		item.addUnsafeEnchantment(unrealEnchantment, 1);
		return item;
	}
	
	public static ItemStack removeEnchant(ItemStack item) {
		item.removeEnchantment(unrealEnchantment);
		return item;
	}
	
	public static ItemStack resetItem(String reset) {	
		return createItem(Material.BARRIER, "Reset " + reset);
	}
	
	public static final ItemStack BLANK = createItem(Material.STAINED_GLASS_PANE, (short) 15, "-");
	public static final ItemStack APPLY = createItem(Material.INK_SACK, (short) 2, "Apply");
	
	private static final String internalPattern = "[, ]{2}internal=[a-zA-Z0-9]+[=]{2}";
	
	public static boolean isSimilar(ItemStack item1, ItemStack item2, boolean sameInventory) {
		String item1String = item1.toString(),
			   item2String = item2.toString();
		
		if (!sameInventory) {
			item1String = item1String.replaceAll(internalPattern, "");
			item2String = item2String.replaceAll(internalPattern, "");
		}
		
		return item1String.equals(item2String);
	}
}
