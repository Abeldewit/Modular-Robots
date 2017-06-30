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

	private ArrayList<Vector3D> boxPath = new ArrayList<Vector3D>();
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
		obstacleMap = reader.getObstacles();
		moduleMap = reader.getModules();
		this.createModules();
		pathFinder = new Astar(25, 25, 25, obstacleMap, moduleMap);
		getFinalPath();
		System.out.println("Trail: " + closePath);

	}
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
			System.out.println("Step: "+stepCounter);
			moveBoxes(stepCounter);
			stepCounter++;
			System.out.println("------------------------- END OF MOVEBOXES -------------------------");
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
		System.out.println("-------------------------------");
		return returnPath;


	}

	public void moveBoxes(int frame)
	{
		/*check whether small path is completed, then calculate a new main trail for the closest
		 * and a small path for the farthest
		 */

		if(frame == 0 || boxPath.isEmpty()) {
			if(!closePath.isEmpty()) {
				System.out.println("BoxPath empty: " + boxPath.isEmpty());

				Box closest = getClosest();

				if(closePath.get(0).x == 0 && closePath.get(0).y == 0 && closePath.get(0).z == 0) { closePath.remove(0);}
				System.out.println("Next on Trail: " + closePath.get(0));
				System.out.println("Closest cordinates: " + closest.getVector());
				Vector3D nextCord = new Vector3D(closePath.get(0).x + closest.getX(), closePath.get(0).y + closest.getY(), closePath.get(0).z + closest.getZ());
				boolean onTop = false;
				for(Box box : boxList) {
					if(nextCord.x == box.getX() && nextCord.z == box.getZ() && nextCord.y > box.getY()) {
						onTop = true;
					}
				}
				if(onTop) {
					closePath.remove(0);
					nextCord = new Vector3D(closePath.get(0).x + nextCord.x, closePath.get(0).y + nextCord.y, closePath.get(0).z + nextCord.z);
					System.out.println("New nextcord");
					onTop = false;
				}
				System.out.println("Next cord: " + nextCord);

				boxPath = getShortPath(nextCord);

				boxPath.remove(0);

				current = getFarthest();
				System.out.println("small Trail: " + boxPath);
				closePath.remove(0);
			}
		} 
		
		else {
			System.out.println("Translating: " + current.getID() + " to " + boxPath.get(0));
			moveBox(current, boxPath.get(0));
			
			boxPath.remove(0);
			
			
			
			
			
		}
		checkGravity(current);

	}
	
	public void checkGravity(Box current) {
		System.out.println("Gravity is checked");
		boolean fall = false;
		
		if(current.getY() > 0) {
			fall = true;
			if(checkAttachment(current)) {
				fall = false;
			}
			
			for(Map.Entry<Integer, Vector3D> entry : obstacleMap.entrySet()) {
				if(entry.getValue().x == current.getX() && entry.getValue().z == current.getZ() && (current.getY() - entry.getValue().y) == 1 ) {
					fall = false;
				}
			}
			
		}
		
		
		
		if(fall) {
			moveBox(current, new Vector3D(0.0, -1.0, 0.0));
			System.out.println("Gravity did fall to: " + current.getID());
		}
		System.out.println(fall);
	}



	public ArrayList<Vector3D> getShortPath(Vector3D smallEnd) {
		System.out.println("Running shortpath method");
		Box farthest = getFarthest();
		ArrayList<Vector3D> farPath = calculatePath(farthest, smallEnd);

		return farPath;
	}

	public void getFinalPath() {
		Box closest = getClosest();
		System.out.println("Closest: "+closest.getID());
		closePath = calculatePath(closest, END);
		closePath.remove(0);
	}


	//returns current pathStep's location
	public void getCurrentPathStep(Box current)
	{

		

		
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
			if( Math.abs(currentX - box.getX()) == 1 && (currentY - box.getY()) == 1) {
				current.setAttached(box);
			}
			if( Math.abs(currentZ - box.getZ()) == 1 && (currentY - box.getY()) == 1) {
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

	

	public ArrayList<Vector3D> calculateFinalPath(Box current, Vector3D end) {

		System.out.println("Running CalculateFinalPath now");
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