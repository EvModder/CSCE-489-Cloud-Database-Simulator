public class CommandServer extends Command{
	private AggieStack stack;

	CommandServer(){
		stack = AggieStack.getHook();
	}

	@Override public boolean runCommand(String... args){
		if(args.length == 0 || (args[0] = args[0].toLowerCase()).equals("list")){
			StringBuilder builder = new StringBuilder(" -- Instances:\n");
			builder.append(stack.instances.size()).append('\n');
			for(Instance i : stack.instances.values()){
				builder.append(i.name).append(' ').append(i.image.name).append(' ').append(i.flavor.name).append('\n');
			}
			System.out.print(builder.toString());
		}
		else if(args[0].equals("create")){
			if(args.length != 6){
				System.err.println("Invalid number of arguments!");
				return false;
			}
			String imgName = null, flvName = null, instName = args[5];
			for(int i = 1; i < 5; ++i){
				String flag = args[1].toLowerCase();
				if(flag.equals("--image")){
					if(imgName != null){
						System.err.println("Please specify only a single image");
						return false;
					}
					imgName = args[++i];
				}
				else if(flag.equals("--flavor")){
					if(flvName != null){
						System.err.println("Please specify only a single flavor");
						return false;
					}
					flvName = args[++i];
				}
				else instName = args[i];
			}
			if(imgName == null){
				System.err.println("Please specify an image");
				return false;
			}
			if(flvName == null){
				System.err.println("Please specify a flavor");
				return false;
			}
			Image image = stack.getImage(imgName);
			Flavor flavor = stack.getFlavor(flvName);
			if(image == null){
				System.err.println("Invalid image specified: " + imgName);
				return false;
			}
			if(flavor == null){
				System.err.println("Invalid flavor specified: " + flvName);
				return false;
			}
			// create the machine with instName, image, & flavor
			Instance instance = new Instance(instName, image, flavor);
			if(stack.findHost(instance)){
				System.out.println("Located host machine for " + instName + ", instance installed successfully");
			}
			else{
				System.err.println("Unable to find a host for the specified instance");
			}
		}
		else if(args[0].equals("delete")){
			if(args.length == 1){
				System.err.println("Please specify a instance (by name)");
				return false;
			}
			Instance instance = stack.instances.remove(args[1]);
			if(instance == null){
				System.err.println("No such instance found: " + args[1]);
				return false;
			}
			// Free resources from current host
			instance.setHost(null);
			System.out.println("Instance has been deleted");
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
			builder.append(m.name).append(' ').append(m.ip).append(' ').append(m.memory).append(' ').append(m.disks).append(' ').append(m.vcpus).append('\n');
		}
		System.out.print(builder.toString());
	}
}