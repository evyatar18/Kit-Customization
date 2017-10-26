package com.kit.customizable.skill;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class SkillEvent extends PlayerEvent implements Cancellable {

	private static HandlerList handlerList = new HandlerList();
	
	@Override
	public HandlerList getHandlers() { return handlerList; }
	public static HandlerList getHandlerList() { return handlerList; }
	
	
	
	private boolean isCancelled;
	
	private final SkillTrigger trigger;
	private final SkillType usedSkill;
	
	public SkillEvent(Player who, SkillTrigger triggerType) {
		super(who);
		this.trigger = triggerType;
		this.usedSkill = SkillType.getUsedSkill(who.getName());
	}
	
	public SkillTrigger getTrigger() {
		return this.trigger;
	}
	
	public SkillType getUsedSkill() {
		return this.usedSkill;
	}
	
	public boolean isCancelled() { return this.isCancelled; }
	public void setCancelled(boolean cancel) { 
		this.isCancelled = cancel;
	}
	
}
