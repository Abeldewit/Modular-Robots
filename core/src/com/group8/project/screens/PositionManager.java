package com.group8.project.screens;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Vector3;
import static com.group8.project.screens.MainGame.DEBUG;

public class PositionManager
{
	
	
	private static HashMap<Integer,Vector3D> OBSTACLES = new HashMap<Integer,Vector3D>();
	private static HashMap<Integer,Vector3D> MODULES = new HashMap<Integer,Vector3D>();	
	
	/**
	 * The method reads in the file with starting parameters of modules or obstacels
	 * @param inputfile The file to be read
	 * @return A HashMap with modules ID and its vectors representing parameters
	 */
	public void startRead(File inputfile, boolean module)
	{		
		HashMap<Integer, Vector3D> map = new HashMap<Integer,Vector3D>();
		if (inputfile.length() == 0 && module) {
			MODULES = map;
		}
		if (inputfile.length() == 0 && !module) {
			OBSTACLES = map;
		}

		
	try 	{ 
    	FileReader fr = new FileReader(inputfile);
        BufferedReader br = new BufferedReader(fr);
        Scanner in = null;

        String record = new String();
		
		//! THe first few lines of the file are allowed to be comments, staring with a // symbol.
		//! These comments are only allowed at the top of the file.
		
		//! -----------------------------------------
      /*  if( (record = br.readLine()) == null ) {
    		in = new Scanner(record);
        	
        }
        else {*/
        while ((record = br.readLine()) != null)
			{
			if( record.startsWith("//")) continue;
				 // Saw a line that did not start with a comment -- time to start reading the data in				
               
		record = record.replace(","," ");
        in = new Scanner(record);
        int i = 0;
        if (record.startsWith("X"))
        {
        	int ID = 1000 + i;
        	i++;
        	in.next();
        	
        	double x = in.nextDouble();       	
        	double y = in.nextDouble();
        	double z = in.nextDouble();
        	map.put(ID, new Vector3D(x,y,z));
        	continue;
        }

        int ID = (int) in.nextDouble();       
        double x = in.nextDouble();
        double y = in.nextDouble();
        double z = in.nextDouble();
        map.put(ID, new Vector3D(x,y,z));
	}
	
		if(DEBUG == 4) {System.out.println(map);}
		fr.close();
		br.close();
		in.close();
	}
	catch (IOException ex)
	{ 
    // catch possible io errors from readLine()
    System.out.println("Error! Problem reading file "+inputfile);
	System.exit(0);
	}
	if (module) {
		MODULES = map;
	}
	else {
		OBSTACLES = map;
	} 
}
	/**
	 * It reads the file with move parameters, taking the starting and ending points for given module
	 * @param inputfile The file to be read
	 * @return arr It is an array containing HashMaps of moves for each steps
	 */
	public ArrayList<Map<Float,Vector3[]>> movesRead(File inputfile)
	{
		
		ArrayList<Map<Float,Vector3[]>> arr = new ArrayList<Map<Float, Vector3[]>>();
		try {
			
			FileReader fr = new FileReader(inputfile);
			BufferedReader br = new BufferedReader(fr);
			String record = new String();	
			Scanner in = null;
			
			int i = 1;
			arr.add(new HashMap<Float,Vector3[]>());
			
			while ((record = br.readLine()) != null)
			{
				record = record.replace(","," ");
				if(record.startsWith("//") ) continue;
				in = new Scanner(record);
				String moduleNumber = ""+i;
				
				if( record.startsWith(moduleNumber))
				{
					in.next();
					
					float ID = in.nextFloat();
					
					float startX = in.nextFloat();
					float startY = in.nextFloat();
					float startZ = in.nextFloat();
					
					float endX = in.nextFloat();
					float endY = in.nextFloat();
					float endZ = in.nextFloat();
					
					Vector3[] vct = new Vector3[2];
					vct[0] = new Vector3(startX,startY,startZ);
					vct[1] = new Vector3(endX,endY,endZ);
					
					arr.get(i-1).put(ID, vct);
					continue;
				}
				else
				{
					i++;
					arr.add(new HashMap<Float,Vector3[]>());
					in.next();
					float ID = in.nextFloat();
					
					float startX = in.nextFloat();
					float startY = in.nextFloat();
					float startZ = in.nextFloat();
					
					float endX = in.nextFloat();
					float endY = in.nextFloat();
					float endZ = in.nextFloat();
					
					Vector3[] vct = new Vector3[2];
					vct[0] = new Vector3(startX,startY,startZ);
					vct[1] = new Vector3(endX,endY,endZ);
					
					arr.get(i-1).put(ID, vct);
					continue;
				}
			}
			
			if(DEBUG == 4){ System.out.println(arr); }
			fr.close();
			br.close();
			in.close();
		}
		catch (IOException ex)
		{
			System.out.println("Error! Problem reading file ");
		}
		return arr;	
	}
	/**
	 * This method takes the modules or obstacles positions and writes them down in a .txt file
	 * @param test1 This is a HashMap of ID's and Vector3's which will be saved in the text file
	 */
	public File positionWriterM(HashMap<Integer,Vector3D> map)
	{
		BufferedWriter writer = null;
		String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String path = "Configurations" + File.separator + "Modules" + File.separator + timeLog + ".txt";
		File file = new File(path);
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
			
			
			System.out.println(file.getCanonicalPath());
			
			writer = new BufferedWriter(new FileWriter(file));
			Set set = map.keySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext())
			{

				int ID = (Integer) iterator.next();
				if(DEBUG == 4) {System.out.println(map.get(ID));};
				String save = "" + ID + " " + map.get(ID).toString();
				save = save.replaceAll("[{}+()=,]", " ");
				writer.write(save);
				writer.newLine();
			
			}
		}
		catch (Exception e)
		{
			System.out.println("Coulnd't save. ");
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch (Exception e)
			{
			}
		}
		return file;
	}
	
	/**
	 * This method takes the modules or obstacles positions and writes them down in a .txt file
	 * @param test1 This is a HashMap of ID's and Vector3's which will be saved in the text file
	 */
	public File positionWriterO(HashMap<Integer,Vector3D> map)
	{
		BufferedWriter writer = null;
		String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String path = "Configurations" + File.separator + "Obstacles" + File.separator + timeLog + ".txt";
		File file = new File(path);
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
			
			
			System.out.println(file.getCanonicalPath());
			
			writer = new BufferedWriter(new FileWriter(file));
			Set set = map.keySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext())
			{

				float ID = (Float) iterator.next();
				if(DEBUG == 4) System.out.println(map.get(ID));

				String save = "" + ID + " " + map.get(ID).toString();
				save = save.replaceAll("[{}+()=,]", " ");
				writer.write(save);
				writer.newLine();
			
			}
		}
		catch (Exception e)
		{
			System.out.println("Coulnd't save. ");
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch (Exception e)
			{
			}
		}
		return file;
	}
	/**
	 * The method takes vector positions for modules ID and saves them in a .txt file
	 * @param arr ArrayList of Map containing modules ID and its beginning and finishing positions
	 */
	public void moveWriter(ArrayList<Map<Float,Vector3[]>> arr)
	{
		BufferedWriter writer = null;
		try
		{
			String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())+"moves";
            File file = new File(timeLog + ".txt");
            
            System.out.println(file.getCanonicalPath());
            
            writer = new BufferedWriter(new FileWriter(file));
            
            for (int i = 0; i < arr.size(); i++)
            {
            	Set set = arr.get(i).keySet();
            	Iterator iterator = set.iterator();
            	while (iterator.hasNext())
            	{
            		float ID = (Float) iterator.next();
	            	int z = i+1;
	            	String save = ""+z+" "+ID+" "+arr.get(i).get(ID)[0]+" "+arr.get(i).get(ID)[1];
	            	save = save.replaceAll("[{}+()=,]"," ");
		            writer.write(save);
		            writer.newLine();
            	}
            	continue;
            }
		}
		catch (Exception e)
		{
			System.out.println("Couldn't save.");
		}
		finally
		{
			try
			{
				writer.close();
			}
		catch (Exception e)
		{
		}
	}
		
	}
	
	public HashMap<Integer, Vector3D> getObstacles() {
		return OBSTACLES;
	}
	
	public HashMap<Integer, Vector3D> getModules() {
		return MODULES;
	}

	public void setObstacles(HashMap<Integer, Vector3D> o) {
		OBSTACLES = o;
	}
	
	public void setModules(HashMap<Integer, Vector3D> m) {
		MODULES = m;
	}
	
}
	
	
	
	
	
	
	
