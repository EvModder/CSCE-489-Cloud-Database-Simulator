import java.util.HashMap;
import java.util.Scanner;

public class AggieStack{
	public static void main(String... args){new AggieStack();}
	private static AggieStack hook;
	public static AggieStack getHook(){return hook;}
	protected HashMap<String, Machine> machines = new HashMap<String, Machine>();
	protected HashMap<String, Image> images = new HashMap<String, Image>();
	protected HashMap<String, Flavor> flavors = new HashMap<String, Flavor>();
	protected HashMap<String, Instance> instances = new HashMap<String, Instance>();
	protected HashMap<String, Rack> racks = new HashMap<String, Rack>();

	public AggieStack(){
		hook = this;
		new CommandAdmin();
		new CommandConfig();
		new CommandServer();
		new CommandShow();
		Scanner scanner = new Scanner(System.in);

		while(scanner.hasNext()){
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
			else{
				String log = cmdName+" >> INVALID\n";
				FileIO.appendString("aggiestack-log.txt", log);
				System.err.println("Invalid command");
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
		images.put(image.name.toLowerCase(), image);
	}
	Image getImage(String name){
		return images.get(name.toLowerCase());
	}
	void addFlavor(Flavor flavor){
		flavors.put(flavor.name.toLowerCase(), flavor);
	}
	Flavor getFlavor(String name){
		return flavors.get(name.toLowerCase());
	}
	void addInstance(Instance instance){
		instances.put(instance.name.toLowerCase(), instance);
	}
	Instance getInstance(String name){
		return instances.get(name.toLowerCase());
	}
	void addRack(Rack rack){
		racks.put(rack.name, rack);
	}
	Rack getRack(String name){
		return racks.get(name.toLowerCase());
	}

	public boolean findHost(Instance instance){
		for(Machine machine : machines.values()) {
			if(machine.canHost(instance.flavor)){
				instance.setHost(machine);
				return true;
			}
		}
		return false;
	}
}