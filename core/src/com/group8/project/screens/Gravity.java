package com.group8.project.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Gravity {
	
	private static ArrayList<Box> ml;
	private static ArrayList<Box> ol;
	private static ArrayList<Box> ReadyList;
	private final static boolean DEBUG = false;
	private int stack;
	private ArrayList<Box> readyList;
	private ArrayList<Box> gravityList;

	
	public Gravity(ArrayList<Box> ModuleList, ArrayList<Box> ObstacleList) {
		
		gravityList = new ArrayList<Box>();
		for (int m = 0; m < ModuleList.size(); m++) {
			gravityList.add(ModuleList.get(m));
		}
		for (int o = 0; o < ObstacleList.size(); o++) {
			gravityList.add(ObstacleList.get(o));
		}
		
		this.setup();
		
	}
	
	public void setup() {
		readyList = new ArrayList<Box>();
		ArrayList<Box> list = gravityList;
		
		
				
		while(allMoved(list)) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getMoved() == false) {
				Box lowest = list.get(i);
					for (int j = 0; j < list.size(); j++) {
						if (j == i) continue;
						if (list.get(j).getX() == lowest.getX() && list.get(j).getZ() == lowest.getZ() && list.get(j).getY() < lowest.getY() && list.get(j).getMoved() == false) {
							lowest = list.get(j);
						}
					}
					if (lowest.getY() > 0 && checkUnder(lowest)) {
						
						readyList.add(lowest);
						lowest.setMoved();
						
					}
					else {
						lowest.setMoved();
						
					}
				}
			}
		}

		
		setReady(list);
	}
	
		
	public boolean allMoved(ArrayList<Box> list) {
		for (Box box : list) {
			if (box.getMoved() == false) return true;
		}
		return false;
	}
	
	public void setReady(ArrayList<Box> list) {
		for (Box box : list) {
			box.setReady();
		}
	}
	
	public ArrayList<Box> getList() {
		return readyList;
	}
	
	public boolean checkUnder(Box box) {
		for (Box tmp : gravityList) {
			if (tmp.getY() == box.getY()-1) return false;
		}
	return true;
	}
	

}


