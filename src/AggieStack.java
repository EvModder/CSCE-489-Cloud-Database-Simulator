import java.util.HashMap;

public class AggieStack{
	public static void main(String... args){new AggieStack();}
	private static AggieStack hook;
	public static AggieStack getHook(){return hook;}
	protected HashMap<String, Machine> machines = new HashMap<String, Machine>();
	protected HashMap<String, Image> images = new HashMap<String, Image>();
	protected HashMap<String, Flavor> flavors = new HashMap<String, Flavor>();
	protected HashMap<String, Instance> instances = new HashMap<String, Instance>();
	protected HashMap<String, Rack> racks = new HashMap<String, Rack>();
	final String PS1 = System.getProperty("user.name")+"@aggiestack $ ";

	AggieStack(){
		hook = this;
		// Register commands
		new CommandAdmin();
		new CommandConfig();
		new CommandHelp();
		new CommandServer();
		new CommandShow();
		
		// Run AggieStack console (command input loop)
		Command.inputLoop();
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

	boolean findHost(Instance instance){
		for(Machine machine : machines.values()){
			if(machine.canHost(instance.flavor)){
				instance.setHost(machine);
				return true;
			}
		}
		return false;
	}
}