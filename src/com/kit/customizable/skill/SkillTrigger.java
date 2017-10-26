package com.kit.customizable.skill;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public enum SkillTrigger {
	
	SNEAK,
	RIGHT_CLICK,
	LEFT_CLICK,
	MIDDLE_CLICK,
	BLOCK, // optional
	HIT_ENTITY,
	HIT_PLAYER
	;
	
	
	public static Listener triggersListener = new Listener() {
		
		
		
		@EventHandler
		public void onInteract(PlayerInteractEvent event) {
			Action eventAction = event.getAction();
			
			
			SkillTrigger trigger = null;
			
			switch(eventAction) {
				case RIGHT_CLICK_AIR:
				case RIGHT_CLICK_BLOCK: {
					trigger = SkillTrigger.RIGHT_CLICK;
					break;
				}
				case LEFT_CLICK_AIR:
				case LEFT_CLICK_BLOCK: {
					trigger = SkillTrigger.LEFT_CLICK;
					break;
				}
				default: { }
			}
			
			if (trigger != null) {
				Bukkit.getPluginManager().callEvent(new SkillEvent(event.getPlayer(), trigger));
			}
		}
		
		@EventHandler
		public void onHit(EntityDamageByEntityEvent event) {
			if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity))
				return;
			
			Player player = (Player) event.getDamager();
			
			
			Bukkit.getPluginManager().callEvent(new SkillEvent(player, SkillTrigger.HIT_ENTITY));
			
			if (event.getDamager() instanceof Player) {
				Bukkit.getPluginManager().callEvent(new SkillEvent(player, SkillTrigger.HIT_PLAYER));
			}
		}
	};
}
