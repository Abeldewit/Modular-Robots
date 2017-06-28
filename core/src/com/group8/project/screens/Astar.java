package com.group8.project.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.group8.project.screens.MainGame.DEBUG;

import com.badlogic.gdx.math.Vector3;
import com.group8.project.screens.Vector3D;

public class Astar {

	public Astar(int xLength, int yLength, int zLength, Map<Integer, Vector3D> obstacles, Map<Integer, Vector3D> modules) {
		this.xLength = xLength;
		this.yLength = yLength;
		this.zLength = zLength;
		this.setup();
	}

	private void setup() {


		//make the array to store the nodes in
		grid = new BlockNode[xLength][yLength][zLength];

		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[0].length; j++) {
				for(int k = 0; k < grid[0][0].length; k++) {
					grid[i][j][k] = new BlockNode(i,j,k);
				}
			}
		}



		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[0].length; j++) {
				for(int k = 0; k < grid[0][0].length; k++) {
					grid[i][j][k].addNeighbors(grid);
					
				}
			}
		}
		System.out.println(grid.length);

	}
	
	
	public void setup2(Map<Integer, Vector3D> obstacles, Map<Integer, Vector3D> modules, Vector3D start)
	{
		closedSet.clear();
		path.clear();
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[0].length; j++) {
				for(int k = 0; k < grid[0][0].length; k++) {
					grid[i][j][k].setObstacle(false);	
				}
			}
		}
		
		if(!obstacles.isEmpty())
		{
			for(Map.Entry<Integer, Vector3D> entry : obstacles.entrySet()) {
				Vector3D obstacle = entry.getValue();
				if(obstacle != null)
				{
					grid[(int)obstacle.x][(int)obstacle.y][(int)obstacle.z].setObstacle(true);
					System.out.println("Obstacle at " + obstacle + " is set " + grid[(int)obstacle.x][(int)obstacle.y][(int)obstacle.z].getObstacle());
				}
			}
		}
		
		if(!modules.isEmpty())
		{
			for(Map.Entry<Integer, Vector3D> entry: modules.entrySet())
			{
				//System.out.println("working on " + entry.getValue());
				Vector3D module = entry.getValue();
				if(module != null)
				{
					
						System.out.println("Set module: " + module.toString() + " to obstacle");
						grid[(int)module.x][(int)module.y][(int)module.z].setObstacle(true);
					
				}
			
			}
		}
	}
	
	public void setup3()
	{
		closedSet.clear();
		path.clear();
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[0].length; j++) {
				for(int k = 0; k < grid[0][0].length; k++) {
					grid[i][j][k].setObstacle(false);	
				}
			}
		}
	}

	public void solve(Vector3D startVector, Vector3D endVector) {
		
		System.out.println("run solve");
		
		start = grid[(int)startVector.x][(int)startVector.y][(int)startVector.z];
		start.setObstacle(false);
		end = grid[(int)endVector.x][(int)endVector.y][(int)endVector.z];
		System.out.println("Running Astar Start " + start.getX() + " " + start.getY() + " " + start.getZ());
		System.out.println("Running Astar End: " + end.getX() + " " + end.getY() + " " + end.getZ());
		
		//System.out.println("Closed size:" + closedSet.size());
		openSet.add(start);
		//System.out.println("Open size:" + openSet.size());
		while(!openSet.isEmpty()) {
			//there is still an option to find the path

			//find the node with the lowest 'f' score
			int winIndex = 0;
			for(int i = 0; i < openSet.size(); i++) {
				if(openSet.get(i).getF() < openSet.get(winIndex).getF()) {
					winIndex = i;
				}
			}

			BlockNode current = openSet.get(winIndex);

			//if the lowest 'f' score node is the end, we're done
			if(current == end) {

				//path is found, now output it
				BlockNode temp = current;
				path.add(current);
				while(temp.getPrev() != null) {
					path.add(temp.getPrev());
					temp = temp.getPrev();
				}

				
					for(int i = path.size() -1; i >= 0; i--) {
						//System.out.println(path.get(i).getX() + "  " + path.get(i).getY() + "  " + path.get(i).getZ());
					}
				

				
				
			}

			//confirm the current node has been checked
			openSet.remove(current);
			closedSet.add(current);

			//get the neighbors and loop through them
			List<BlockNode> neighbors = current.getNeighbors();
			for(int i = 0; i < neighbors.size(); i++) {
				BlockNode neighbor = neighbors.get(i);

				if(!closedSet.contains(neighbor) && neighbor.getObstacle() != true) {
					float tempG = current.getG() + 1;

					if(openSet.contains(neighbor)) {
						if(tempG < neighbor.getG()) {
							neighbor.setG(tempG);
						}
					} else {
						neighbor.setG(tempG);
						openSet.add(neighbor);
					}

					neighbor.calcH(end);
					neighbor.setPrevious(current);
				}
			}
		}
		if(openSet.isEmpty() && path.get(0) != end) {
			System.out.println("There is no possible path!");
		}
		openSet.clear();
		closedSet.clear();
		
		
	}

	public List<BlockNode> getPath() {
		ArrayList<BlockNode> correctPath = new ArrayList<BlockNode>();
		for(int i = path.size() - 1; i > -1; i--) {
			correctPath.add(path.get(i));
		}
		return correctPath;
	}

	public void changeNode(Vector3D obstacleCords, boolean obstacle) {
		int x = (int) obstacleCords.x;
		int y = (int) obstacleCords.y;
		int z = (int) obstacleCords.z;
		
		grid[x][y][z].setObstacle(obstacle);
	}
	
	private BlockNode start,end;
	private BlockNode[][][] grid;
	private List<BlockNode> openSet = new ArrayList<BlockNode>();
	private List<BlockNode> closedSet = new ArrayList<BlockNode>();
	private int xLength,yLength,zLength;
	private List<BlockNode> path = new ArrayList<BlockNode>();

}
