package com.group8.project.screens;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Vector3;
import static com.group8.project.screens.MainGame.DEBUG;

public class BlockNode {
	public BlockNode(int x,int y, int z) {
		//position
		this.x = x;
		this.y = y;
		this.z = z;
		cords = new Vector3(x,y,z);
		//A* values
		this.f = 0;
		this.g = 0;
		this.h = 0;
		this.obstacle = false;
	}
	
	public Vector3 vectorized() {
		return new Vector3(x,y,z);
	}

	public void calcH(BlockNode end) {
		Vector3 endCords = new Vector3(end.getX(),end.getY(),end.getZ());
		this.h = Math.abs((cords.x - endCords.x) + (cords.y - endCords.y) + (cords.z - endCords.z));
	}

	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}

	public float getF() {
		this.f = g + h;
		return f;
	}
	
	public void clear() {
		previous = null;
		f = 0;
		g = 0;
		h = 0;
	}
	public float getG() {
		return g;
	}
	public float getH() {
		return h;
	}
	
public boolean getObstacle() {
		return obstacle;
	}
	public List<BlockNode> getNeighbors() {
		return neighbors;
	}
	public BlockNode getPrev() {
		return previous;
	}

	public void addNeighbors(BlockNode[][][] grid) {
		int xLength = grid.length;
		int yLength = grid[0].length;
		int zLength = grid[0][0].length;
		int intX = (int) x;
		int intY = (int) y;
		int intZ = (int) z;

		if(x < xLength -1) {
			this.neighbors.add(grid[intX+1][intY][intZ]);
		}
		if(x > 0) {
			this.neighbors.add(grid[intX-1][intY][intZ]);
		}
		if(y < yLength -1) {
			this.neighbors.add(grid[intX][intY+1][intZ]);
		}
		if(y > 0) {
			this.neighbors.add(grid[intX][intY-1][intZ]);
		}
		if(z < zLength -1) {
			this.neighbors.add(grid[intX][intY][intZ+1]);
		}
		if(z > 0) {
			this.neighbors.add(grid[intX][intY][intZ-1]);
		}

	}

	public void setF(float f) {
		this.f = f;
	}
	public void setG(float g) {
		this.g = g;
	}
	public void setH(float h) {
		this.h = h;
	}
	public void setObstacle(boolean val) {
		obstacle = val;
	}
	public void setPrevious(BlockNode prev) {
		this.previous = prev;
	}

	private float x,y,z;
	private Vector3 cords;
	private float f,g,h;
	private boolean obstacle;
	private List<BlockNode> neighbors = new ArrayList<BlockNode>();
	private BlockNode previous = null;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockNode other = (BlockNode) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}


}
