public class CommandConfig extends Command{
	private AggieStack stack;
	private IPAddressValidator ipValidator;
	
	CommandConfig(){
		stack = AggieStack.getInstance();
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
		if(i == -1) return true; //0 machines
		
//		int num = Integer.parseInt(file.substring(0, i));
		
		String[] lines = file.split("\n");
		for(i=1; i<lines.length; ++i){
			if(lines[i].isEmpty()) continue;
			String[] data = lines[i].split(" ");
			if(data.length != 5){
				System.err.println("Invalid data (invalid args) for Machine #"+i);
			}
			else if(!ipValidator.validate(data[1])){
				System.err.println("Invalid data (IP format) for Machine #"+i);
			}
			else try{
				//name = data[0]; ip = data[1]; mem = data[2]; disks = data[3]; vcpus = data[4]
				stack.addMachine(new Machine(data[0], data[1], Long.parseLong(data[2]),
							Long.parseLong(data[3]), Long.parseLong(data[4])));
			}
			catch(NumberFormatException ex){
				System.err.println("Invalid data (number format) for Machine #"+i);
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
			if(data.length != 2){
				System.err.println("Invalid data (invalid args) for Image #"+i);
			}
			else{
				//name = data[0]; path = data[1];
				stack.addImage(new Image(data[0], data[1]));
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
				System.err.println("Invalid data (invalid args) for Flavor #"+i);
			}
			else try{
				//name = data[0]; RAM = data[1]; disks = data[2]; vcpus = data[3]
				stack.addFlavor(new Flavor(data[0], Integer.parseInt(data[1]), 
							Integer.parseInt(data[2]), Integer.parseInt(data[3])));
			}
			catch(NumberFormatException ex){
				System.err.println("Invalid data (number format) for Flavor #"+i);
			}
		}
		return true;
	}
}