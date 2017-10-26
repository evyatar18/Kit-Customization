package com.kit.customizable.skill;

import java.util.HashMap;
import java.util.Map;

public enum SkillType {
	
	DEFAULT("Default"),
	Pyro("Pyromaniac");
	
	
	
	private String name;
	
	SkillType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	
	
	public static SkillType match(String string) {
		for (SkillType skill : values()) {
			if (skill.getName().equals(string)) {
				return skill;
			}
		}
		return null;
	}
	
	private static Map<String, SkillType> usedSkills = new HashMap<>();
	
	public static void useSkill(String playerName, SkillType skill) { usedSkills.put(playerName, skill); }
	public static SkillType getUsedSkill(String playerName) { return usedSkills.get(playerName); }
}
