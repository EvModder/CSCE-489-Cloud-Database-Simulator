import java.util.HashMap;

public abstract class Command{
	String name;
	private static HashMap<String, Command> cmds = new HashMap<String, Command>();
	
	Command(String name){
		cmds.put(name.toLowerCase(), this);
	}
	Command(){
		String name = getClass().getSimpleName().substring(7).toLowerCase();
		cmds.put(name.toLowerCase(), this);
	}
	
	public abstract boolean runCommand(String... args);
	
	public final static Command getCommand(String name){
		return cmds.get(name.toLowerCase());
	}
}