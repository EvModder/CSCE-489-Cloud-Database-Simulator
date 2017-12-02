import java.util.HashMap;

public abstract class Command{
	String name, usage;
	static HashMap<String, Command> cmds = new HashMap<String, Command>();
	
	Command(String name, String usage){
		this.name = name;
		this.usage = usage;
		cmds.put(name.toLowerCase(), this);
	}
	Command(String usage){
		name = getClass().getSimpleName().substring(7).toLowerCase();
		this.usage = usage;
		cmds.put(name.toLowerCase(), this);
	}
	Command(){
		name = getClass().getSimpleName().substring(7).toLowerCase();
		usage = name;
		cmds.put(name.toLowerCase(), this);
	}
	
	public abstract boolean runCommand(String... args);
	
	public final static Command getCommand(String name){
		return cmds.get(name.toLowerCase());
	}
}