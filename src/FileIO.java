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

public class FileIO{
	public static final String DIR = "./";
	public static String loadFile(String filename, InputStream defaultValue){
		BufferedReader reader = null;
		try{reader = new BufferedReader(new FileReader(DIR+filename));}
		catch(FileNotFoundException e){
			if(defaultValue == null) return null;
			
			//Create Directory
			File dir = new File(DIR);
			if(!dir.exists())dir.mkdir();
			
			//Create the file
			File conf = new File(DIR+filename);
			StringBuilder builder = new StringBuilder();
			String content = null;
			try{
				conf.createNewFile();
				reader = new BufferedReader(new InputStreamReader(defaultValue));
				
				String line = reader.readLine();
				builder.append(line);
				while(line != null){
					builder.append('\n');
					builder.append(line);
					line = reader.readLine();
				}
				reader.close();
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(conf));
				writer.write(content = builder.toString()); writer.close();
			}
			catch(IOException e1){e1.printStackTrace();}
			return content;
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

	public static String loadResource(Object pl, String filename){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(pl.getClass().getResourceAsStream("/"+filename)));
		
			StringBuilder file = new StringBuilder();
			String line = reader.readLine();
			while(line != null){
				line = line.replace("//", "#").trim();
				if(!line.startsWith("#")) file.append(line.split("#")[0].trim()).append('\n');
				line = reader.readLine();
			}
			reader.close();
			return file.toString();
		}
		catch(IOException ex){ex.printStackTrace();}
		return "";
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