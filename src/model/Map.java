package model;

import java.awt.Point;
import java.io.Serializable;
/*
 * file: Transection.java
 * author: Long Chen, Hang Chen
 * 
 * the map class, which will include all the grid will use in this game, it has siwtch map function to allow switch 
 */
public class Map implements Serializable{
	private char[][] grid1;
	private char[][] grid2;
	private char[][] grid;
	private boolean isGrid1;

	public Map() {
		grid1 = new char[150][150];
		buildGrid1();
		grid = grid1;
		isGrid1 = true;
		grid2 = new char[100][100];
		buildGrid2();
		
		
	}
	private void buildGrid1(){
		buildTree(grid1);

		buildGress(16, 16, grid1, 119, 119);
		//build lake
		buildRec(20, 25, grid1, 10, 5, 'o', 'w');

		buildRec(40, 55, grid1, 10, 15, 'o', 'w');
		buildRec(38, 15, grid1, 20, 25, 'o', 'w');
		buildRec(70, 45, grid1, 10, 15, 'o', 'w');
		
		buildRec(120, 45, grid1, 10, 15, 'o', 'o');
		buildRec(70, 89, grid1, 10, 15, 'o', 'o');
		buildRec(120, 23, grid1, 10, 15, 'o', 'o');
		buildRec(70, 100, grid1, 10, 15, 'o', 'o');
		
		//build path
		buildRec(9, 109, grid1, 133, 6, ' ', ' ');
		buildRec(23, 109, grid1, 6, 33, ' ', ' ');
		buildRec(61, 9, grid1, 6, 100, ' ', ' ');
		//buildWall
		int wallSize = 15;
		buildWall(grid1, wallSize);
		//D is door
		grid1[15][20] = 'D';
//		
//		for (int i = 0; i < grid1.length; i++) {
//		for (int j = 0; j < grid1[0].length; j++) {
//			System.out.print(grid1[i][j]);
//		}
//		System.out.println("");
//	}
	}
	private void buildGrid2() {
		buildTree(grid2);
		buildGress(16, 16, grid2, 69, 69);
		
		buildRec(40, 55, grid2, 10, 15, 'o', 'w');
		buildRec(38, 15, grid2, 20, 25, 'o', 'w');
		buildRec(70, 45, grid2, 10, 15, 'o', 'w');
		//buildWall
		int wallSize = 15;
		buildWall(grid2, wallSize);
		grid2[85][20] = 'D';
	}

	private void buildWall(char[][] gridWall, int wallSize) {
		//build wall
		for(int i = 0; i < gridWall.length; i++){
			if(i > wallSize && i < gridWall[0].length-wallSize){
				gridWall[wallSize][i] = 'w';
				gridWall[i][wallSize] = 'w';
				gridWall[i][gridWall[0].length - wallSize] = 'w';
				gridWall[gridWall.length - wallSize][i] = 'w';
			}
		}
		
	}
	public char[][] getCurrentMap () {
		return grid;
	}
	public void switchMap() {
		if(isGrid1) {
			grid = grid2;
			isGrid1 = false;
			return;
		}
		grid = grid1;
		isGrid1 = true;
	}
	public Point getTranPos() {
		if(isGrid1)
			return new Point(15,20);
		return new Point(85, 20);

	}
	private int xsize  = 31;
	private int ysize  = 31;

	public char[][] visiableMap(int x, int y){
		char[][] visiable = new char[xsize][ysize];
		if(x < xsize/2+1)
			x = 0;
		else if(grid.length- x < xsize/2 )
			x -= xsize - grid.length + x;
		else
			x -= (xsize/2+1);
		
		if(y < ysize/2+1)
			y = 0;
		else if(grid[0].length- y < ysize/2 )
			y -= ysize - grid.length + y;
		else
			y -= (ysize/2+1);
		for (int i = x; i < xsize + x; i++) {
			for (int j = y; j < xsize + y; j++) {
				visiable[i-x][j-y] = grid[i][j];
			}
		}
		return visiable;
	}
	private void buildGress(int x, int y, char[][] grid, int Length, int width) {
		for (int i = x; i < Length + x; i++) {
			for (int j = y; j < width + y; j++) {
					grid[i][j] = 'g';
			}
		}
	}

	private void buildRec(int x, int y, char[][] grid, int width, int length, char inside, char outside) {
		for (int i = x; i < width + x; i++) {
			for (int j = y; j < length + y; j++) {
				if (i == x || j == y || i == x + width - 1 || j == y + length - 1) {
					grid[i][j] = outside;
					continue;
				}
				grid[i][j] = inside;
			}
		}

	}

	private void buildTree(char[][] gridBuildTree) {
		for (int i = 0; i < gridBuildTree.length; i++) {
			gridBuildTree[i][0] = 't';
			gridBuildTree[i][gridBuildTree[0].length - 1] = 't';
			if (i % 6 == 0) {
				gridBuildTree[i][0] = 'T';
				gridBuildTree[i + 1][0] = 'T';
				gridBuildTree[i][1] = 'T';
				gridBuildTree[i + 1][1] = 'T';

				gridBuildTree[i][gridBuildTree[0].length - 2] = 'T';
				gridBuildTree[i + 1][gridBuildTree[0].length - 2] = 'T';
				gridBuildTree[i][gridBuildTree[0].length - 1] = 'T';
				gridBuildTree[i + 1][gridBuildTree[0].length - 1] = 'T';
				i++;
			}
		}
		for (int i = 0; i < gridBuildTree[0].length; i++) {
			gridBuildTree[0][i] = 't';
			gridBuildTree[gridBuildTree.length - 1][i] = 't';
			if (i % 6 == 0) {
				gridBuildTree[0][i] = 'T';
				gridBuildTree[0][i + 1] = 'T';
				gridBuildTree[1][i] = 'T';
				gridBuildTree[1][i + 1] = 'T';

				gridBuildTree[gridBuildTree.length - 2][i] = 'T';
				gridBuildTree[gridBuildTree.length - 2][i + 1] = 'T';
				gridBuildTree[gridBuildTree.length - 1][i] = 'T';
				gridBuildTree[gridBuildTree.length - 1][i + 1] = 'T';
				i++;
			}
		}


	}

}
