public class Flavor{
	String name;
	long ram, disks, vcpus;// RAM is in GB

	Flavor(String name, long ram, long disks, long vcpus){
		this.name = name;
		this.ram = ram;
		this.disks = disks;
		this.vcpus = vcpus;
	}
}