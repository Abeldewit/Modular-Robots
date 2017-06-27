package com.group8.project.screens;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.group8.project.screens.Vector3D;
import static com.group8.project.screens.MainGame.DEBUG;

public class Box 
{	 	
	public Box(Vector3D cords, boolean obstacle, int ID)
	{
		this.x = cords.x;
		this.y = cords.y;
		this.z = cords.z;
		this.ID = ID;
		this.obstacle = obstacle;
		this.stepCount = 0;
		this.moved = false;
	
		Model box = getModelBuilder().createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal);
		
		if (obstacle) {
			box = getModelBuilder().createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.Normal);
		}
		
		boxInstance = new ModelInstance(box);
		boxInstance.transform.setTranslation((float)x,(float)y,(float)z);
	}
	
	public ModelInstance getInstance()
	{
		return boxInstance;
	}
	
	
	//sets the X, Y and Z coordinates for the modules
	public void setXYZ(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		boxInstance.transform.setTranslation((float)x, (float)y, (float)z);
	}
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public double getZ()
	{
		return z;
	}
	public int getID() {
		return ID;
	}
	
	public void move(Vector3D translation)
	{
		this.x = x + translation.x;
		this.y = y + translation.y;
		this.z = z + translation.z;
		
		if(!(x > 25) && !(y > 25) && !(z > 25)) {
			boxInstance.transform.setTranslation((float)this.x, (float)this.y, (float)this.z);
			if(DEBUG == 3) {System.out.println("Moved box " + ID + " to X: " + this.x + "Y: " + this.y + "Z: " + this.z);}
		}
	}
	
	public double getDist(Vector3D end) {
		return (Math.abs(x - end.x) + Math.abs(y - end.y) + Math.abs(z - end.z));
	}

	public ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	public void setModelBuilder(ModelBuilder modelBuilder) {
		this.modelBuilder = modelBuilder;
	}

	public void setAttached(Box neighbor) {
		attachList.add(neighbor);
	}
	public ArrayList<Box> getAttached() {
		return attachList;
	}
	public void resetAttached() {
		attachList.clear();
	}
	
	public int stepCount() {
		int tmp = stepCount;
		stepCount++;
		return tmp;
	}
	
	public void setPath(ArrayList<Vector3D> path) {
		shortPath = path;
	}
	public ArrayList<Vector3D> getPath() {
		return shortPath;
	}
	public void setMoved() {
		this.moved = true;
	}
	public void setReady() {
		this.moved = false;
	}
	public boolean getMoved() {
		return moved;
	}
	
	private double x,y,z;
	private ArrayList<Box> attachList = new ArrayList<Box>();
	private int ID;
	private boolean obstacle;
	private ModelBuilder modelBuilder = new ModelBuilder();
	private ModelInstance boxInstance;
	private int stepCount;
	private ArrayList<Vector3D> shortPath = new ArrayList<Vector3D>();
	private boolean moved;
	
	
}
