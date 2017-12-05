/* Nathaniel Leake, 424003778, nateleake@tamu.edu
This code has been used before in projects by me.  It is my own work
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FileIO{
	public static final String DIR = "./";

	public static String loadFile(String filename, String defaultContent){
		BufferedReader reader = null;
		try{reader = new BufferedReader(new FileReader(DIR+filename));}
		catch(FileNotFoundException e){
			if(defaultContent == null || defaultContent.isEmpty()) return defaultContent;
			
			//Create Directory
			File dir = new File(DIR);
			if(!dir.exists())dir.mkdir();
			
			//Create the file
			File conf = new File(DIR+filename);
			try{
				conf.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(conf));
				writer.write(defaultContent);
				writer.close();
			}
			catch(IOException e1){e1.printStackTrace();}
			return defaultContent;
		}
		StringBuilder file = new StringBuilder();
		if(reader != null){
			try{
				String line = reader.readLine();
				
				while(line != null){
					line = line.replace("//", "#").trim();
					if(!line.startsWith("#")){
						file.append(line.split("#")[0].trim());
						file.append('\n');
					}
					line = reader.readLine();
				}
				reader.close();
			}catch(IOException e){}
		}
		if(file.length() > 0) file.substring(0, file.length()-1);
		return file.toString();
	}

	public static boolean saveFile(String filename, String content){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(DIR+filename));
			writer.write(content); writer.close();
			return true;
		}
		catch(IOException e){return false;}
	}

	private static Map<String,String> loadYamlish(File file){
		HashMap<String,String> map = new HashMap<String,String>();
		BufferedReader reader = null;
		try{reader = new BufferedReader(new FileReader(file));}
		catch(FileNotFoundException e){return map;}
		if(reader != null){
			try{
				String line;
				
				while((line = reader.readLine()) != null){
					line = line.replace("//", "#").replace(":", "=").trim();
					int idx = line.indexOf('#');
					if(idx >= 0) line = line.substring(0,idx);
					if(line.contains("=")){
						String[] keyval = line.split("=");
						map.put(keyval[0].trim().toLowerCase(), keyval[1].trim().replaceAll("\"$|^\"", ""));
					}
				}
				reader.close();
			}catch(IOException e){}
		}
		return map;
	}

	public static Map<String,String> loadYaml(String configName, InputStream defaultConfig){
		File file = new File(DIR+configName);
		if(!file.exists() && defaultConfig != null){
			try{
				//Create Directory
				File dir = new File(DIR);
				if(!dir.exists())dir.mkdir();
				
				//Create config file from default
				BufferedReader reader = new BufferedReader(new InputStreamReader(defaultConfig));
				
				String line = reader.readLine();
				StringBuilder builder = new StringBuilder(line);
				
				while((line = reader.readLine()) != null){
					builder.append('\n');
					builder.append(line);
				}
				reader.close();
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(builder.toString()); writer.close();
			}
			catch(IOException ex){
				ex.printStackTrace();
				System.err.println("Unable to locate a default config!");
			}
			System.out.println("Could not locate configuration file!");
			System.out.println("Generating a new one with default settings.");
		}
		return loadYamlish(file);
	}
	
	public static boolean appendString(String filename, String content){
		try{
			FileWriter writer = new FileWriter(DIR+filename, true);
			writer.write(content); writer.close();
			return true;
		}
		catch(FileNotFoundException e){
			System.out.print("Generating new log file!");
			
			if(!new File(DIR+filename).exists()){
				return saveFile(filename, content);
			}
			else return false;
		}catch(IOException e){
			return false;
		}
	}
}