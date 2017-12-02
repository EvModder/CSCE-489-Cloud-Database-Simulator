// A virtual server (instance of a flavor)
public class Instance{
	String name;
	Image image;
	Flavor flavor;
	Machine host;

	Instance(String name, Image image, Flavor flavor){
		this.name = name;
		this.image = image;
		this.flavor = flavor;
	}

	public void setHost(Machine newHost){
		// Free space on old host (if exists)
		if(host != null){
			host.memory += flavor.ram;
			host.disks += flavor.disks;
			host.vcpus += flavor.vcpus;
		}

		// Claim space on new host (if exists)
		if(newHost != null){
			newHost.memory -= flavor.ram;
			newHost.disks -= flavor.disks;
			newHost.vcpus -= flavor.vcpus;
		}
		host = newHost;
	}
}