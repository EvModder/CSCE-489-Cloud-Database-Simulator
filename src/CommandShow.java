class CommandShow extends Command{
	private AggieStack stack;
	
	CommandShow(){
		stack = AggieStack.getHook();
	}

	@Override public boolean runCommand(String... args){
		if(args.length != 1){
			System.err.println("Invalid number of arguments!");
			return false;
		}
		String select = args[0].toLowerCase();
		if(select.equals("hardware")) showHardware();
		else if(select.equals("images")) showImages();
		else if(select.equals("flavors")) showFlavors();
		else if(select.equals("all")) showAll();
		else{
			System.err.println("Invalid 'show' selection");
			return false;
		}
		return true;
	}
	
	void showHardware(){
		StringBuilder builder = new StringBuilder(" -- Machines:\n");
		builder.append(stack.machines.size()).append('\n');
		
		for(Machine m : stack.machines.values()){
			builder.append(m.name).append(' ').append(m.rack.name).append(' ').append(m.ip).append(' ')
						.append(m.TOTAL_MEMORY).append(' ').append(m.TOTAL_DISKS).append(' ')
						.append(m.TOTAL_VCPUS).append('\n');
		}
		System.out.print(builder.toString());
	}
	
	void showImages(){
		StringBuilder builder = new StringBuilder(" -- Images:\n");
		builder.append(stack.images.size()).append('\n');
		
		for(Image img : stack.images.values()){
			builder.append(img.name).append(' ').append(img.size).append("MB ").append(img.path).append('\n');
		}
		System.out.print(builder.toString());
	}
	
	void showFlavors(){
		StringBuilder builder = new StringBuilder(" -- Flavors:\n");
		builder.append(stack.flavors.size()).append('\n');
		
		for(Flavor flavor : stack.flavors.values()){
			builder.append(flavor.name).append(' ').append(flavor.ram).append(' ')
						.append(flavor.disks).append(' ').append(flavor.vcpus).append('\n');
		}
		System.out.print(builder.toString());
	}
	
	void showAll(){
		showHardware();
		showImages();
		showFlavors();
	}
}