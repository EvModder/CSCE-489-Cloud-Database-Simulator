class CommandAdmin extends Command{
	private AggieStack stack;
	private IPAddressValidator ipValidator;
	
	CommandAdmin(){
		super("admin show <hardware/instances/evacuate/remove/add>");
		stack = AggieStack.getHook();
		ipValidator = new IPAddressValidator();
	}

	@Override public boolean runCommand(String... args){
		if(args.length < 2){
			System.err.println("Invalid number of arguments!");
			return false;
		}
		args[0] = args[0].toLowerCase();
		if(args[0].equals("can_host")){
			if(args.length != 3){
				System.err.println("Invalid number of arguments!");
				return false;
			}
			Machine machine = stack.getMachine(args[1]);
			Flavor flavor = stack.getFlavor(args[2]);
			if(machine == null){
				System.err.println("Invalid machine specified: "+args[1]);
				return false;
			}
			if(flavor == null){
				System.err.println("Invalid flavor specified: "+args[2]);
				return false;
			}
			System.out.println(machine.canHost(flavor) ? "yes" : "no");
		}
		else if(args[0].equals("show")){
			args[1] = args[1].toLowerCase();
			if(args[1].equals("hardware")){
				showAvailableHardware();
			}
			else if(args[1].equals("instances")){
				showRunningInstances();
			}
			else{
				System.err.println("Invalid 'show' arguments");
				return false;
			}
		}
		else if(args[0].equals("evacuate")){
			Rack rack = stack.getRack(args[1]);
			if(rack == null){
				System.err.println("Rack not found: "+args[1]);
				return false;
			}
			long stuck = stack.evacuate(rack);
			
			if(stuck != 0){
				System.err.println("Evacuation of "+rack.name+" failed!");
				System.err.println("Unable to relocate "+stuck+" instances");
				return false;
			}
			else{
				// Success is printed in log file, so we don't need to print here
//				System.out.println("Evacuated all virtual instances from rack: "+rack.name);
			}
		}
		else if(args[0].equals("remove")){
			Machine machine = stack.removeMachine(args[1]);
			if(machine == null){
				System.err.println("Machine not found: "+args[1]);
				return false;
			}
			long stuck = stack.evacuate(machine);
			
			if(stuck != 0){
				System.err.println("Evacuation of "+machine.name+" failed!");
				System.err.println("Unable to relocate "+stuck+" instances");
				stack.addMachine(machine);
				return false;
			}
		}
		else if(args[0].equals("add")){
			if(args.length != 12){
				System.err.println("Invalid number of arguments!");
				return false;
			}//admin add —mem 8 —disk 4 —vcpus 4 -ip 128.0.0.1 -rack r1 newmachine 
			String name = null, ip = null;
			Rack rack = null;
			long memory = -1, disks = -1, vcpus = -1;
			
			for(int i = 1; i < 12; ++i){
				// Notice! The P4 input files use unicode dash '—', so I replace it with a normal '-'
				String flag = args[i].toLowerCase().replaceAll("—", "-").replaceAll("--", "-").replaceAll("s$", "");
				if(flag.equals("-mem")){
					if(memory != -1){
						System.err.println("Please specify only a single quantity for memory");
						return false;
					}
					try{memory = Long.parseLong(args[++i]);}
					catch(NumberFormatException ex){
						System.err.println("Invalid data (number format) for 'mem' flag: "+args[i]);
						return false;
					}
				}
				else if(flag.equals("-disk")){
					if(disks != -1){
						System.err.println("Please specify only a single quantity for disk count");
						return false;
					}
					try{disks = Long.parseLong(args[++i]);}
					catch(NumberFormatException ex){
						System.err.println("Invalid data (number format) for 'disk' flag: "+args[i]);
						return false;
					}
				}
				else if(flag.equals("-vcpu")){
					if(vcpus != -1){
						System.err.println("Please specify only a single quantity for VCPUs");
						return false;
					}
					try{vcpus = Long.parseLong(args[++i]);}
					catch(NumberFormatException ex){
						System.err.println("Invalid data (number format) for 'vcpus' flag: "+args[i]);
						return false;
					}
				}
				else if(flag.equals("-rack")){
					if(rack != null){
						System.err.println("Please specify only a single rack");
						return false;
					}
					rack = stack.getRack(args[++i]);
					if(rack == null){
						System.err.println("Invalid rack specified: "+args[i]);
						return false;
					}
				}
				else if(flag.equals("-ip")){
					if(ip != null){
						System.err.println("Please specify only a single IP");
						return false;
					}
					ip = args[++i];
					if(!ipValidator.validate(ip)){
						System.err.println("Invalid data (IP format) for 'ip' flag: "+args[i]);
						return false;
					}
				}
				else{
					if(name != null){
						System.err.println("Please specify only a single name");
						return false;
					}
					name = args[i];
					if(stack.getMachine(name) != null) {
						System.err.println("Invalid data (Duplicate name) for 'name' flag: "+args[i]);
					}
				}
			}
			if(rack == null){
				System.err.println("Please specify a rack");
				return false;
			}
			if(name == null){
				System.err.println("Please specify a machine name");
				return false;
			}
			if(ip == null){
				System.err.println("Please specify an IP");
				return false;
			}
			if(memory == -1){
				System.err.println("Please specify the amount of memory");
				return false;
			}
			if(disks == -1){
				System.err.println("Please specify the number of disks");
				return false;
			}
			if(vcpus == -1){
				System.err.println("Please specify the number of vcpus");
				return false;
			}
			stack.addMachine(new Machine(name, rack, ip, memory, disks, vcpus));
		}
		else{
			System.err.println("Invalid arguments");
			return false;
		}
		return true;
	}
	
	void showAvailableHardware(){
		StringBuilder builder = new StringBuilder(" -- Machines (Available):\n");
		builder.append(stack.machines.size()).append('\n');
		
		for(Machine m : stack.machines.values()){
			builder.append(m.name).append(' ').append(m.ip).append(' ').append(m.memory).append(' ')
						.append(m.disks).append(' ').append(m.vcpus).append('\n');
		}
		System.out.print(builder.toString());
	}

	void showRunningInstances(){
		StringBuilder builder = new StringBuilder(" -- Instances (Running/Allocated):\n");
		builder.append(stack.instances.size()).append('\n');
		
		for(Instance i : stack.instances.values()){
			if(i.host != null){
				builder.append(i.name).append(" is hosted on ").append(i.host.name).append('\n');
			}
		}
		System.out.print(builder.toString());
	}
}