public class Rack{
	String name;
	long storage;
	final long TOTAL_CAPACITY;

	Rack(String name, long storage){
		this.name = name;
		TOTAL_CAPACITY = this.storage = storage;
	}
}