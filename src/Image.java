public class Image{
	String name, path;
	long size;// Image file size in MB

	Image(String name, long size, String path){
		this.name = name;
		this.size = size;
		this.path = path;
	}
}