import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class AggieStack{
	public static void main(String... args){new AggieStack();}
	private static AggieStack instance;
	public static AggieStack getInstance(){return instance;}
	protected HashMap<String, Machine> machines = new HashMap<String, Machine>();
	protected ArrayList<Image> images = new ArrayList<Image>();
	protected HashMap<String, Flavor> flavors = new HashMap<String, Flavor>();

	public AggieStack(){
		instance = this;
		new CommandAdmin();
		new CommandConfig();
		new CommandShow();
		Scanner scanner = new Scanner(System.in);

		while(scanner.hasNext()){
			String input = scanner.nextLine();
			if(input.toLowerCase().startsWith("aggiestack ")){
				input = input.substring(11);
			}
			if(input.equals("exit") || input.equals("quit")) break;
			
			String cmdName = input;
			String[] args = new String[]{};

			int i = input.indexOf(" ");
			if(i != -1){
				cmdName = input.substring(0, i);
				args = input.substring(i+1).split(" ");
			}

			Command cmd = Command.getCommand(cmdName);
			if(cmd != null){
				boolean result = cmd.runCommand(args);
				
				String log = cmdName+" >> "+(result ? "SUCCESS" : "FAILURE")+'\n';
				FileIO.appendString("aggiestack-log.txt", log);
			}
			else{
				String log = cmdName+" >> INVALID\n";
				FileIO.appendString("aggiestack-log.txt", log);
			}
		}
		scanner.close();
	}
	
	void addMachine(Machine machine){
		machines.put(machine.name.toLowerCase(), machine);
	}
	Machine getMachine(String name){
		return machines.get(name.toLowerCase());
	}
	void addImage(Image image){
		images.add(image);
	}
	void addFlavor(Flavor flavor){
		flavors.put(flavor.name.toLowerCase(), flavor);
	}
	Flavor getFlavor(String name){
		return flavors.get(name.toLowerCase());
	}
}