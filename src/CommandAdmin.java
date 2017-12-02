class CommandAdmin extends Command{
	private AggieStack stack;
	
	CommandAdmin(){
		super("admin show <hardware/instances>");
		stack = AggieStack.getHook();
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