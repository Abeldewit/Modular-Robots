package com.group8.project.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.group8.project.screens.Vector3D;
import com.group8.project.screens.Box;
import static com.group8.project.screens.MainGame.DEBUG;


public class RuleEngine {
	//lists of all modules and their vectors

	private Map<Integer, Vector3D> moduleMap = new HashMap<Integer, Vector3D>();
	private Map<Integer, Vector3D> obstacleMap = new HashMap<Integer, Vector3D>();
	public ArrayList<ModelInstance> instanceList = new ArrayList<ModelInstance>();
	public ArrayList<Box> boxList = new ArrayList<Box>();
	public ArrayList<Box> obstacleList = new ArrayList<Box>();
	private List<BlockNode> finalPath;
	public Astar pathFinder;
	private PositionManager reader;
	private Vector3D END;
	
	private boolean unsolved = true;
	private int currentPathStep = 0;

	private ArrayList<Vector3D> smallPath = new ArrayList<Vector3D>();
	private Box current;
	ArrayList<Vector3D> closePath = new ArrayList<Vector3D>();

	private int frameCounter = 0;
	private int stepCounter = 0;
	private int pathCounter = 0;
	private int pathIterator = 0;

	public Gravity grav;


	public RuleEngine(Vector3D END) {
		this.END = END;
		reader = new PositionManager();

		//setting up the Modules and ID List


		obstacleMap = reader.getObstacles();

		reader = new PositionManager();
		obstacleMap = reader.getObstacles();
		moduleMap = reader.getModules();


		this.createModules();


		pathFinder = new Astar(25, 25, 25, obstacleMap, moduleMap);
		
		//Vector3D boxCords = new Vector3D(closest.getX(), closest.getY(), closest.getZ());
		//pathFinder.setup2(obstacleMap, moduleMap, boxCords);
		//	for(Box i : boxList) {
		//		System.out.println("id: " + i.getID());
		//		this.calculatePath(i, END);


		//Only calculates the path of the module closest to the goal
		//calculate path for closest
				Box closest = getClosest();
				System.out.println("Closest: "+closest.getID());
				closePath = calculatePath(closest, END);
				
	}
	//moveBoxes(); 


	//finds Box closest to the goal
	public Box getClosest()
	{
		Box closest = null;

		double closeDist = Double.MAX_VALUE;
		
		for(Box current : boxList) {
			
			double distance = current.getDist(END);
			
			if( distance <  closeDist) {
				
				closeDist = distance;
				closest = current;
			}
		}
		
		return closest;
	}

	//finds Box farthest of the goal
	public Box getFarthest()
	{
		Box farthest = null;

		double furthest = Double.MIN_VALUE;
		//System.out.println(boxList);
		for(Box current : boxList) {
			
			double distance = current.getDist(END);
			
			if( distance > furthest ) {
				
				furthest = distance;
				farthest = current;
			}
		}
		
		return farthest;
	}
	

	//main RuleEngine method that is run every frame
	public void runRules() 
	{

		if(frameCounter % 10 == 0) {
		System.out.println(stepCounter);
		moveBoxes(stepCounter);
		stepCounter++;
		
		/*
		pathFinder.solve(new Vector3D(0,0,0), new Vector3D(14,0,0));
		
		System.out.println(pathFinder.getPath());
		pathFinder.solve(new Vector3D(0,0,0), new Vector3D(24,0,24));
		System.out.println(pathFinder.getPath());
		*/
		}
		
		
		
		//checkGravity();
		//System.out.println("ModuleList: " + moduleMap);

		frameCounter++;

	}

	//loop through the map and add the modules to a list
	public void createModules() {
		for(Map.Entry<Integer, Vector3D> entry : moduleMap.entrySet()) {
			int ID = entry.getKey();
			Box newBox = new Box(entry.getValue(), false, ID);
			boxList.add(newBox);
			instanceList.add(newBox.getInstance());
		}
		for(Map.Entry<Integer, Vector3D> entry : obstacleMap.entrySet()) {
			int ID = entry.getKey();
			Box newBox = new Box(entry.getValue(), true, ID);
			obstacleList.add(newBox);
			instanceList.add(newBox.getInstance());
		}
	}

	public ArrayList<ModelInstance> getModules() {
		return instanceList;
	}


	//methods

	public void checkGravity() {
		grav = new Gravity(boxList, obstacleList);
		for (Box current : grav.getList()) {
			moveBox(current, new Vector3D(0,-1,0));
		}
	}


	public ArrayList<Vector3D> calculatePath(Box current, Vector3D end) {

		//System.out.println(current);
		//get coordinates from the box
		Vector3D boxCords = new Vector3D(current.getX(), current.getY(), current.getZ());
		ArrayList<Vector3D> VectorPath = new ArrayList<Vector3D>();
		HashMap<Integer, Vector3D> newModuleMap = new HashMap<Integer, Vector3D>();
		for(Box box : boxList) {
			newModuleMap.put(box.getID(), new Vector3D(box.getX(),box.getY(),box.getZ()));
		}
		System.out.println("New Map:" + newModuleMap);
		//run the Astar from the coordinates of the box to the end
		final long start = System.currentTimeMillis();
		if(obstacleMap.isEmpty() &&  moduleMap.isEmpty()) 	pathFinder.setup3();
		//HIER TOEVOEGEN WAT IK HAD VERWIJDERD
		if(!newModuleMap.isEmpty()) {pathFinder.setup2(obstacleMap, newModuleMap, boxCords);}
		pathFinder.solve(boxCords, end); 

		final long finish = System.currentTimeMillis();

		System.out.println("Took: " + (finish - start) + "ms");




		//create a new list with the vector changes that have to happen per step
		List<BlockNode> path = pathFinder.getPath();
		
		
		
		Vector3D tmp = new Vector3D(0,0,0);

		for(BlockNode next : path) {

			Vector3D step = new Vector3D(next.getX(), next.getY(), next.getZ());
			Vector3D change = tmp.to(step);
			VectorPath.add(change);
			tmp = step;
		}

		//System.out.println("FinalPath: " + finalPath);
		//System.out.println("VectorPath: " + VectorPath);
		ArrayList<Vector3D> returnPath = VectorPath;
		VectorPath = null;
		return returnPath;


	}

	public void moveBoxes(int frame)
	{	
		if(unsolved == true)
		{
			
			
			//only calculate path when smallEnd is reached
			
			
			
			System.out.println("Smallpath size:" + smallPath.size());
			if(frame == 0 || frame > smallPath.size()-1) {
				pathCounter++;
				smallPath.clear();
				current = getFarthest();
				System.out.println("Working on: " + current.getID());
				smallPath = getShortPath(pathCounter);
				pathIterator = 0;
				System.out.println("smallPath:" + smallPath);
			} else {
			System.out.println("Moving " + current.getID() + " with " + smallPath.get(pathIterator));
			moveBox(current, smallPath.get(pathIterator));
			}
			
		}
		pathIterator++;
	}
	
	public ArrayList<Vector3D> getShortPath(int counter) {
		
		//calculate path for furthest to first step of closest
		Box closest = getClosest();
		Vector3D closeNextStep = closePath.get(counter);
		Vector3D realStep = new Vector3D(closest.getX()+closeNextStep.x, closest.getY()+closeNextStep.y, closest.getZ()+closeNextStep.z);
		Box farthest = getFarthest();
		System.out.println("Farthest: "+farthest.getID());
		ArrayList<Vector3D> farPath = calculatePath(farthest, realStep);
		System.out.println("farPath size: " + farPath.size());
		System.out.println("farPath: " + farPath);
		return farPath;
	}
	
	//returns current pathStep's location
		public Vector3D getCurrentPathStep()
		{
			
			
			BlockNode temp = finalPath.get(currentPathStep);
			Vector3D pathStep = new Vector3D(temp.getX(), temp.getY(), temp.getZ());
			System.out.println("pathStep: " + pathStep );
			if(currentPathStep < finalPath.size() -1)
			{
				currentPathStep ++;
			}
			
			return pathStep;
		}
	
	public void moveBox(Box current, Vector3D translation) {


		current.move(translation);

		checkAttachment(current);
		instanceList.add(current.getID(), current.getInstance());
	}


	public boolean checkAttachment(Box current) {
		//  if |A.x - B.x| + |A.y - B.y| + |A.z - B.z| == 1 then the boxes are next to each other

		double currentX = current.getX();
		double currentY = current.getY();
		double currentZ = current.getZ();

		for(Box box : boxList) {
			if(( Math.abs(currentX - box.getX()) + Math.abs(currentY - box.getY()) + Math.abs(currentZ - box.getZ()) ) == 1) {
				current.setAttached(box);
			}
		}

		if(current.getAttached().isEmpty()) { 
			return false;
		} else if(!current.getAttached().isEmpty()){ 
			return true;
		} else { 
			System.err.println("Attachment error");
			return false; 
		}
	}
	public ArrayList<Box> attachmentOrder(Box box) {
		ArrayList<Box> OrderedTrain = new ArrayList<Box>();

		if(!box.getAttached().isEmpty()) {
			ArrayList<Box> attached = box.getAttached();

			double closest = Math.abs(attached.get(0).getX() - END.x) + Math.abs(attached.get(0).getY() - END.y) + Math.abs(attached.get(0).getZ() - END.z);

			for(Box neighbor : attached) {
				double neighborDist = Math.abs(neighbor.getX() - END.x) + Math.abs(neighbor.getY() - END.y) + Math.abs(neighbor.getZ() - END.z);
				if(neighborDist < closest) {
					closest = neighborDist;
					OrderedTrain.add(OrderedTrain.size(), neighbor);
				} else {
					OrderedTrain.add(0, neighbor);
				}
			}	
		}

		return OrderedTrain;
	
	}

	public void moveOver(Box box1, Box box2) {
		boolean connected = false;
		if(checkAttachment(box1)) {
			ArrayList<Box> box1attachment = box1.getAttached();

			if(box1attachment != null) {
				for(Box neighbor : box1attachment) {
					if(neighbor == box2) {
						connected = true;
					} else {
						connected = false;
					}
				}
			}
		}


		if(box1.getDist(END) > box2.getDist(END) && connected && box1.getY() == box2.getY()) {
			System.out.println("Step 1");
			moveBox(box1, new Vector3D(0.0,1.0,0.0));
			Vector3D moveTop = new Vector3D(box2.getX() - box1.getX(), 0.0, box2.getZ() - box1.getZ());
			moveBox(box1, moveTop);
		} else if(box1.getY() > box2.getY() && connected) {
			System.out.println("Step 2");
			Vector3D boxStep = box2.getPath().get(stepCounter);
			stepCounter++;
			moveBox(box1, boxStep);

		} else if(box1.getY() > box2.getY() && !connected) {
			System.out.println("Step 3");
			moveBox(box1, new Vector3D(0.0,-1.0,0.0));
		}

		if(box1.getDist(END) < box2.getDist(END) && connected && box2.getY() == box1.getY()) {
			System.out.println("Step 1");
			moveBox(box2, new Vector3D(0.0,1.0,0.0));
			Vector3D moveTop = new Vector3D(box1.getX() - box2.getX(), 0.0, box1.getZ() - box2.getZ());
			moveBox(box2, moveTop);
		} else if(box2.getY() > box1.getY() && connected) {
			System.out.println("Step 2");
			Vector3D boxStep = box1.getPath().get(stepCounter);
			stepCounter++;
			moveBox(box2, boxStep);

		} else if(box2.getY() > box1.getY() && !connected) {
			System.out.println("Step 3");
			moveBox(box2, new Vector3D(0.0,-1.0,0.0));
		}



		box1.resetAttached();
		box2.resetAttached();

	}

	public ArrayList<Vector3D> calculateFinalPath(Box current, Vector3D end) {

		//System.out.println(current);
		//get coordinates from the box
		Vector3D boxCords = new Vector3D(current.getX(), current.getY(), current.getZ());
		ArrayList<Vector3D> VectorPath = new ArrayList<Vector3D>();
		HashMap<Integer, Vector3D> newModuleMap = new HashMap<Integer, Vector3D>();
		for(Box box : boxList) {
			newModuleMap.put(box.getID(), new Vector3D(box.getX(),box.getY(),box.getZ()));
		}
		//System.out.println("New Map:" + newModuleMap);
		//run the Astar from the coordinates of the box to the end
		final long start = System.currentTimeMillis();
		if(obstacleMap.isEmpty() &&  moduleMap.isEmpty()) 	pathFinder.setup3();
		if(!obstacleMap.isEmpty() || !moduleMap.isEmpty()) 	pathFinder.setup2(obstacleMap, newModuleMap, boxCords);
		pathFinder.solve(boxCords, end);

		final long finish = System.currentTimeMillis();

		System.out.println("Took: " + (finish - start) + "ms");




		//create a new list with the vector changes that have to happen per step
		List<BlockNode> path = pathFinder.getPath();
		System.out.println("Path size " + path.size());
		
		
			finalPath = path;
		
		Vector3D tmp = new Vector3D(0,0,0);

		for(BlockNode next : path) {

			Vector3D step = new Vector3D(next.getX(), next.getY(), next.getZ());
			Vector3D change = tmp.to(step);
			VectorPath.add(change);
			tmp = step;
		}

		//System.out.println("FinalPath: " + finalPath);
		//System.out.println("VectorPath: " + VectorPath);	
		return VectorPath;


	}
}