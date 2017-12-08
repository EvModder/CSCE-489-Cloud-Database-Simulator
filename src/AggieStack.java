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
		racks.get(machine.rack.name).machines.add(machine);
	}
	Machine removeMachine(String name){
		Machine machine = getMachine(name);
		if(machine != null && machine.rack != null){
			racks.get(machine.rack.name).machines.remove(machine);
		}
		return machine;
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
		for(Rack rack : racks.values()){//TODO: Does this rack have a copy of the image?
			if(rack.enabled){
				for(Machine machine : rack.machines){
					if(machine.canHost(instance.flavor)){
						instance.setHost(machine);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	long evacuate(Machine machine){
		long unableToRelocate = 0;
		
		for(Instance instance : machine.instances){
			// Attempt to relocate instance; if there is no space for this instance,
			// set 'success' to false, but continue trying to move other instances
			if(!findHost(instance)) ++unableToRelocate;
		}
		return unableToRelocate;
	}

	long evacuate(Rack rack){
		rack.enabled = false;
		long unableToRelocate = 0;
		
		// For each machine in the rack
		for(Machine machine : rack.machines){
			unableToRelocate += evacuate(machine);
		}
		return unableToRelocate;
	}
}