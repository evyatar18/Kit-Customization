package com.kit.customizable.skill.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.kit.customizable.skill.SkillEvent;
import com.kit.customizable.skill.SkillTrigger;
import com.kit.customizable.skill.SkillType;

public class Pyromaniac implements Listener {
	
	@EventHandler
	public void onHit(SkillEvent event) {
		if (event.getUsedSkill() == SkillType.Pyro && event.getTrigger() == SkillTrigger.HIT_ENTITY) {
			// should find a way to determine which entity was hit
		}
	}
}
