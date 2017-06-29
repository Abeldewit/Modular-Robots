package com.group8.project.screens;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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
		

		start = grid[(int)startVector.x][(int)startVector.y][(int)startVector.z];
		start.setObstacle(false);
		end = grid[(int)endVector.x][(int)endVector.y][(int)endVector.z];
		System.out.println("--------- ASTAR ---------");
		System.out.println("Running Astar Start " + start.getX() + " " + start.getY() + " " + start.getZ());
		System.out.println("Running Astar End: " + end.getX() + " " + end.getY() + " " + end.getZ());
		System.out.println("");
		closedSet.add(start);
		
		BlockNode current = null;
		PriorityQueue<BlockNode> queue = new PriorityQueue<BlockNode>(new Comparator<BlockNode>() {
			@Override
			public int compare(BlockNode nodeA, BlockNode nodeB) {
				if (nodeA.getF() < nodeB.getF())
					return -1;
				else if (nodeA.getF() > nodeB.getF())
					return 1;
				else
					return 0;
			}
		});
		queue.add(start);
		while(!queue.isEmpty()) {
			//there is still an option to find the path
			current = queue.poll();
			closedSet.add(current);
			if (current.getPrev() != null && current.getPrev().getPrev()!= null) {
				if (current.vectorized().equals(current.getPrev().getPrev().vectorized())) {
					System.out.println("bla");
				}
			}

			//if the lowest 'f' score node is the end, we're done
			//path is found, now output it
			if(current == end) break;

			//confirm the current node has been checked btw

			//get the neighbors and loop through them
			for(BlockNode neighbor : current.getNeighbors()) {
				if (closedSet.contains(neighbor)) continue;
				if(!neighbor.getObstacle()) {
					float tempG = current.getG() + 1;
					if(queue.contains(neighbor)) {
						if(tempG > neighbor.getG()) {
							continue;
							//neighbor.setG(tempG);
						} 
					} else {
						queue.add(neighbor);
					}
					neighbor.setG(tempG);
					neighbor.calcH(end);
					neighbor.setPrevious(current);
				}
			}
		}
		if(path.size()==0) {
			System.out.println("There is no possible path!");
			
		}
		BlockNode temp = current;
		path.add(current);
		while(temp.getPrev() != null) {
			System.out.println(temp.vectorized().toString());
			path.add(temp.getPrev()); 
			temp = temp.getPrev();
		}
			for(int i = path.size() -1; i >= 0; i--) {
				//System.out.println(path.get(i).getX() + "  " + path.get(i).getY() + "  " + path.get(i).getZ());
			}
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					for (int k = 0; k < grid[0][0].length; k++) {
						grid[i][j][k].clear(); 
					}
				}
			}
		clear();
		
	}
	
	public void clear() {
		openSet.clear();
		closedSet.clear();
		start = null;
		end = null;
	
		//add more
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
