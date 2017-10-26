package com.kit.customizable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageSender {
	
	private static final List<Message> messages = new ArrayList<>();
	
	private static enum Color {
		
		BAD(ChatColor.RED),
		GOOD(ChatColor.GREEN),
		INFO(ChatColor.GRAY),
		VARIABLE(ChatColor.YELLOW);
		
		private ChatColor color;
		
		Color(ChatColor color) {
			this.color = color;
		}
		
		@Override
		public String toString() {
			return this.color + "";
		}
	}
	
	private static class Message {
		private final String id, message;
		
		private Message(String msgName, String message) {
			this.id = msgName;
			this.message = message;
			messages.add(this);
		}
	}
	
	public static final Message 
				KIT_CMD_YES = new Message("kit.cmd.no", Color.BAD + "You don't have a kit."),
			    KIT_CMD_NO = new Message("kit.cmd.yes", Color.INFO + "Kit has been applied."),
			    
			    KIT_CREATION_SUCCESS = new Message("kit.creation.success", Color.GOOD + "Kit creation succeed."),
			    KIT_CREATION_FAILED = new Message("kit.creation.fail", Color.BAD + "Kit creation failed. {}"),
			    
			    KIT_CONTENT_CHANGED_PREVIOUS = new Message("kit.content.changed.previous", Color.INFO + "{} was changed to previous "
			    		+ "selection."),
			    KIT_CONTENT_CHANGED_SELECTED = new Message("kit.content.changed.selected", Color.INFO + "Applied {}."),
			    KIT_CONTENT_RESET = new Message("kit.content.reset", Color.VARIABLE + "{} " + Color.INFO + " was reset."),
			    KIT_CONTENT_CAN_USE = new Message("kit.content.use.yes", Color.INFO + "You're now using: " + Color.VARIABLE + "{}" +
				    			Color.INFO + "."),
			    KIT_CONTENT_CANNOT_USE = new Message("kit.content.use.no", Color.BAD + "You cannot use " + Color.VARIABLE + "{}" +
			    			Color.BAD + ".")
			    
			    ;
	
	
	
	public static void send(CommandSender commandSender, Message msg, String... arguments) {
		String message = msg.message;
		
		if (arguments != null) {
			for (String argument : arguments) { message = message.replaceFirst(Pattern.quote("{}"), argument); }
		}
		
		
		commandSender.sendMessage(message);
	}
	
	public static void send(CommandSender commandSender, String message, String... arguments) {
		for (Message m : messages) {
			if (m.id.equalsIgnoreCase(message)) {
				send(commandSender, m, arguments);
				return;
			}
		}
		
		System.err.println("Could not send message with id; " + message);
	}
}
