public class Flavor{
	String name;
	int ram, disks, vcpus;// RAM is in GB

	Flavor(String name, int ram, int disks, int vcpus){
		this.name = name;
		this.ram = ram;
		this.disks = disks;
		this.vcpus = vcpus;
	}
}
