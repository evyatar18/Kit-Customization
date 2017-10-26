package com.kit.customizable.armor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kit.customizable.ItemUtils;
import com.kit.customizable.Main;
import com.kit.customizable.armor.ArmorItem.ArmorMaterial;
import com.kit.customizable.armor.ArmorItem.ArmorPart;

public class ArmorInventory {
	
	public static final ItemStack
		RESET_HELMET = ItemUtils.resetItem(ArmorPart.HELMET.getName()),
		RESET_CHESTPLATE = ItemUtils.resetItem(ArmorPart.CHESTPLATE.getName()),
		RESET_LEGGINGS = ItemUtils.resetItem(ArmorPart.LEGGINGS.getName()),
		RESET_BOOTS = ItemUtils.resetItem(ArmorPart.BOOTS.getName());
	
	/*
	 * ---------
	 * -HHHH--x-
	 * -CCCC--x-
	 * -LLLL--x-
	 * -BBBB--x-
	 * --------a
	 */
	
	private int invLines = 6;
	private final ItemStack[] inventoryContents = new ItemStack[9*invLines];
	
	private ArmorInventory() {
		initItems();
		initInventory();
		
		Main.getPlugin(Main.class).getLogger().info("Armor Inventory > Initialized.");
	}
	
	private static final ArmorInventory instance = new ArmorInventory();
	public static ArmorInventory getInstance() { return instance; }
	
	public final Map<ArmorItem, Integer> slots = new HashMap<>();
	
	private ArmorItem[] armorItems = new ArmorItem[ArmorMaterial.values().length * ArmorPart.values().length];
	
	private void initItems() {
		int iterator = 0;
		
		for (ArmorPart part : ArmorPart.values()) {
			for (ArmorMaterial material : ArmorMaterial.values()) {
				armorItems[iterator] = ArmorGenerator.getInstance().getArmor(part, material);
				iterator++;
			}
		}
	}
	
	private void initInventory() {
		final int helmetLine = 1,
				  chestLine = 2,
				  leggingsLine = 3,
				  bootsLine = 4;
		
		final Function<Integer, ItemStack> getResetItem = (x) -> { 
			switch(x) {
			case helmetLine: { return RESET_HELMET; }
			case chestLine: { return RESET_CHESTPLATE; }
			case leggingsLine: { return RESET_LEGGINGS; }
			case bootsLine: { return RESET_BOOTS; }
			default: { return ItemUtils.BLANK; }
			}
		};
		
		List<ArmorItem> sortedItems = Arrays.stream(armorItems).sorted((x, y) -> 
			ArmorGenerator.rateArmorMaterial(x.armorMaterial) - ArmorGenerator.rateArmorMaterial(y.armorMaterial))
				.collect(Collectors.toList());
		
		
		final Function<ArmorPart, ArmorItem[]> getArmorItems = armorPart -> { 
			return sortedItems.stream().filter(x -> x.armorPart == armorPart).toArray(ArmorItem[]::new);
		};
		
		ArmorItem[] armorItems;
		for (int i = 0; i < invLines; i++) {
			if (i == helmetLine) {
				// helmet line
				armorItems = getArmorItems.apply(ArmorPart.HELMET);
			} 
			else if (i == chestLine) {
				armorItems = getArmorItems.apply(ArmorPart.CHESTPLATE);
			}
			else if (i == leggingsLine) {
				armorItems = getArmorItems.apply(ArmorPart.LEGGINGS);
			}
			else if (i == bootsLine) {
				armorItems = getArmorItems.apply(ArmorPart.BOOTS);
			} 
			else { 
				armorItems = null; 
			}
			
			for (int j = 0; j < 9; j++) {
				int currentSlot = i*9 + j;
				
				// 'apply' item
				if (currentSlot == inventoryContents.length - 1) {
					inventoryContents[currentSlot] = ItemUtils.APPLY;
					continue;
				}
				
				if (armorItems == null) { inventoryContents[currentSlot] = ItemUtils.BLANK; continue; }
				
				if (i == 0 || i == 5) {
					inventoryContents[currentSlot] = ItemUtils.BLANK;
				}
				else {
					if (j == 0 || j == 5 || j == 6 || j == 8) {
						inventoryContents[currentSlot] = ItemUtils.BLANK;
					}
					else {
						if (j >= 1 && j <= 4) {
							ArmorItem currentItem = armorItems[j - 1]; // minus 1 since it starts from slot 1
							inventoryContents[currentSlot] = currentItem.getMenuItem();
							this.slots.put(currentItem, currentSlot);
						}
						else {
							inventoryContents[currentSlot] = getResetItem.apply(i);
						}
					}
				}
			}			
		}
	}
	
	public Inventory getInventory(Player player) {
		Inventory inv = Bukkit.createInventory(player, inventoryContents.length, "Choose Your Armor");
		inv.setContents(inventoryContents);
		return inv;
	}
}
