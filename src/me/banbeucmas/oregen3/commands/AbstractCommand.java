package me.banbeucmas.oregen3.commands;

import me.banbeucmas.oregen3.Oregen3;
import me.banbeucmas.oregen3.utils.StringUtils;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {

	private String permission;
	private String[] args;
	private CommandSender sender;
	private Oregen3 plugin = Oregen3.getPlugin();


	public AbstractCommand(String permission, String[] args, CommandSender sender) {
		this.permission = permission;
		this.args = args;
		this.sender = sender;
	}

	public abstract ExecutionResult now();

	public void execute() {
		switch (now()) {
			case DONT_CARE:
				break;
			case MISSING_ARGS:
			    if(getFormat() != null){
				    sender.sendMessage(StringUtils.getPrefixString("&cCú pháp: &f" + getFormat()));
			    }
			    break;
            case NO_PERMISSION:
			    sender.sendMessage(StringUtils.getPrefixString("&4Thiếu Quyền Lợi: &c" + permission));
			    break;
		    case NO_PLAYER:
			    sender.sendMessage(StringUtils.getPrefixString("&4Người chơi không tồn tại hoặc không online"));
			    break;
		    case NOT_PLAYER:
			    sender.sendMessage(StringUtils.getPrefixString("&4Chỉ người chơi mới có thể dùng lệnh :v"));
		    case CONSOLE_NOT_PERMITTED:
			    sender.sendMessage(StringUtils.getPrefixString("&4Dùng CONSOLE làm chi ba :v"));
			    break;
		    default:
			    break;
		}
	}

	public String getPermission() {
		return permission;
	}

	public String[] getArgs() {
		return args;
	}

	public String getFormat() {
		return null;
	}

	public CommandSender getSender() {
		return sender;
	}

	public Oregen3 getPlugin() {
		return plugin;
	}
}
