class CommandConfig extends Command{
	private AggieStack stack;
	private IPAddressValidator ipValidator;
	
	CommandConfig(){
		super("hardware <flags: --hardware, --images, --flavors>");
		stack = AggieStack.getHook();
		ipValidator = new IPAddressValidator();
	}

	@Override public boolean runCommand(String... args){
		if(args.length != 2){
			System.err.println("Invalid number of arguments!");
			return false;
		}
		String select = args[0].toLowerCase().replaceAll("--", "");
		if(select.equals("hardware")){
			return loadHardware(args[1]);
		}
		else if(select.equals("images")){
			return loadImages(args[1]);
		}
		else if(select.equals("flavors")){
			return loadFlavors(args[1]);
		}
		else{
			System.err.println("Invalid config setting");
			return false;
		}
	}

	boolean loadHardware(String filename){
		String file = FileIO.loadFile(filename, "");
		if(file == ""){
			System.err.println("File not found");
			return false;
		}
		int i = file.indexOf('\n');
		if(i == -1) return true; //0 racks and machines

		String[] lines = file.split("\n");
		boolean racks = true;
		for(i=1; i<lines.length; ++i){
			if(lines[i].isEmpty()) continue;
			String[] data = lines[i].split(" ");
			if(racks){
				if(data.length == 1){
					racks = false;
					continue;
				}
				else if(data.length != 2){
					System.err.println("Invalid data (invalid args) for Rack at line "+i+": "
								+(data.length > 0 ? data[0] : "null"));
				}
				else try{
					//name = data[0]; storage = data[1]
					stack.addRack(new Rack(data[0], Long.parseLong(data[1])));
				}
				catch(NumberFormatException ex){
					System.err.println("Invalid data (number format) for Rack at line "+i+": "+data[0]);
				}
			}
			else{
				if(data.length != 6){
					System.err.println("Invalid data (invalid args) for Machine at line "+i+": "
								+(data.length > 0 ? data[0] : "null"));
				}
				else if(!ipValidator.validate(data[2])){
					System.err.println("Invalid data (IP format) for Machine at line "+i+": "+data[0]);
				}
				else if(stack.getRack(data[1]) == null){
					System.err.println("Invalid data (Nonexistent rack) for Machine at line "+i+": "+data[0]);
				}
				else if(stack.getMachine(data[0]) != null) {
					System.err.println("Invalid data (Duplicate name) for Machine at line "+i+": "+data[0]);
				}
				else try{
					//name = data[0]; rack = data[1] ip = data[2]; mem = data[3]; disks = data[4]; vcpus = data[5]
					stack.addMachine(new Machine(data[0], stack.getRack(data[1]), data[2],
								Long.parseLong(data[3]), Long.parseLong(data[4]), Long.parseLong(data[5])));
				}
				catch(NumberFormatException ex){
					System.err.println("Invalid data (number format) for Machine at line "+i+": "+data[0]);
				}
			}
		}
		return true;
	}
	
	boolean loadImages(String filename){
		String file = FileIO.loadFile(filename, "");
		if(file == ""){
			System.err.println("File not found");
			return false;
		}
		int i = file.indexOf('\n');
		if(i == -1) return true; //0 images
		
//		int num = Integer.parseInt(file.substring(0, i));
		
		String[] lines = file.split("\n");
		for(i=1; i<lines.length; ++i){
			if(lines[i].isEmpty()) continue;
			String[] data = lines[i].split(" ");
			if(data.length != 3){
				System.err.println("Invalid data (invalid args) for Image at line "+i+": "
							+(data.length > 0 ? data[0] : "null"));
			}
			else{
				//name = data[0]; filesize = data[1]; path = data[2];
				stack.addImage(new Image(data[0], Long.parseLong(data[1]), data[2]));
			}
		}
		return true;
	}
	
	boolean loadFlavors(String filename){
		String file = FileIO.loadFile(filename, "");
		if(file == ""){
			System.err.println("File not found");
			return false;
		}
		int i = file.indexOf('\n');
		if(i == -1) return true; //0 flavors
		
		String[] lines = file.split("\n");
		for(i=1; i<lines.length; ++i){
			if(lines[i].isEmpty()) continue;
			String[] data = lines[i].split(" ");
			if(data.length != 4){
				System.err.println("Invalid data (invalid args) for Flavor at line "+i+": "
							+(data.length > 0 ? data[0] : "null"));
			}
			else try{
				//name = data[0]; RAM = data[1]; disks = data[2]; vcpus = data[3]
				stack.addFlavor(new Flavor(data[0], Long.parseLong(data[1]),
							Long.parseLong(data[2]), Long.parseLong(data[3])));
			}
			catch(NumberFormatException ex){
				System.err.println("Invalid data (number format) for Flavor at line "+i+": "+data[0]);
			}
		}
		return true;
	}
}