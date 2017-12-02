import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

abstract class Command{
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

	// Abstract definition of command architecture
	public abstract boolean runCommand(String... args);

	// Getter access to any currently registered commands
	public final static Command getCommand(String name){
		return cmds.get(name.toLowerCase());
	}

	public final static void inputLoop(){
		// Load .aggie_profile from local directory (AggieStack's version of .bash_profile)
		InputStream defaultProfile = AggieStack.getHook().getClass().getResourceAsStream("/.aggie_profile");
		String PS1 = FileIO.loadYaml(".aggie_profile", defaultProfile).get("ps1");
		if(PS1 == null) PS1 = "\\u@aggiestack $ ";
		PS1 = Tools.unescape(PS1);

		Scanner scanner = new Scanner(System.in);

		// Enter command input loop (AKA AggieStack console)
		System.out.print(PS1);
		while(scanner.hasNextLine()){
			String input = scanner.nextLine();
			if(input.toLowerCase().startsWith("aggiestack ")){
				input = input.substring(11);
			}
			if(input.equals("exit") || input.equals("quit")) break;

			int i = input.indexOf(" ");
			String cmdName = i == -1 ? input : input.substring(0, i);
			String[] args = i == -1 ? new String[]{} : input.substring(i+1).split(" ");

			Command cmd = Command.getCommand(cmdName);
			if(cmd != null){
				boolean result = cmd.runCommand(args);

				String log = cmdName+" >> "+(result ? "SUCCESS" : "FAILURE")+'\n';
				FileIO.appendString("aggiestack-log.txt", log);
			}
			else if(!cmdName.isEmpty()){
				String log = cmdName+" >> INVALID\n";
				FileIO.appendString("aggiestack-log.txt", log);
				System.err.println("Invalid command");
			}
			System.out.print(PS1);
		}
		scanner.close();
	}
}