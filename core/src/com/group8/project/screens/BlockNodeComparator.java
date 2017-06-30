package com.group8.project.screens;

import java.util.Comparator;

public class BlockNodeComparator implements Comparator<BlockNode> {
	@Override
	public int compare(BlockNode block1, BlockNode block2) {
		if(block1.getF() > block2.getF()) {
			return 1;
		}
		if(block1.getF() < block2.getF()) {
			return -1;
		}
		return 0;
	}
}
