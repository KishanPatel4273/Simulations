package SandLab;

import java.awt.*;
import java.util.*;

public class SandLab
{
  public static void main(String[] args)
  {
    SandLab lab = new SandLab(120, 80);
    lab.run();
  }
  
  //add constants for particle types here
  public static final int EMPTY = 0;
  public static final int METAL = 1;
  public static final int SAND = 2;
  public static final int WATER = 3;

  //do not add any more fields
  private int[][] grid;
  private SandDisplay display;
  
  public SandLab(int numRows, int numCols)
  {
    String[] names;
    names = new String[4];
    names[EMPTY] = "Empty";
    names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "WATER";
    grid = new int[numRows][numCols];
    display = new SandDisplay("Falling Sand", numRows, numCols, names);
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool)
  {
	  if(tool == EMPTY){
		  grid[row][col] = EMPTY;
		  return;
	  }
	  if(tool == METAL){
		  grid[row][col] = METAL;
		  return;
	  }
	  if(tool == SAND){
		  grid[row][col] = SAND;
		  return;
	  }
	  if(tool == WATER){
		  grid[row][col] = WATER;
		  return;
	  }
  }

  //copies each element of grid into the display
  public void updateDisplay()
  {
	  for(int r = 0; r < grid.length; r++){
		  for(int c = 0; c < grid[r].length; c++){
			  Color color = Color.BLACK;
			  if(grid[r][c] == METAL){
				  color = Color.GRAY;
			  }
			  if(grid[r][c] == SAND){
				  color = Color.YELLOW;
			  }
			  if(grid[r][c] == WATER){
				  color = Color.BLUE;
			  }
			  display.setColor(r, c, color);
		  }
	  }
  }

  //called repeatedly.
  //causes one random particle to maybe do something.
  public void step()
  {
	  int ranRow = (int) (Math.random() * grid.length);
	  int ranCol = (int) (Math.random() * grid[0].length);
	  if(grid[ranRow][ranCol] == SAND){
		  if(ranRow + 1 < grid.length && grid[ranRow+1][ranCol] == EMPTY){
			  locationClicked(ranRow,ranCol, EMPTY);
			  locationClicked(ranRow + 1,ranCol, SAND);
			  return;
		  }
		  if(ranRow + 1 < grid.length && grid[ranRow+1][ranCol] == WATER){
			  locationClicked(ranRow,ranCol, WATER);
			  locationClicked(ranRow + 1,ranCol, SAND);
			  return;
		  }
	  }
	  if(grid[ranRow][ranCol] == WATER){
		  int ranLoc = (int) (Math.random() * 3) + 1;// left:1, down:2, right:3
		  if(ranLoc == 1 && ranCol - 1 >= 0 && grid[ranRow][ranCol - 1] == EMPTY){
			  locationClicked(ranRow,ranCol, EMPTY);
			  locationClicked(ranRow,ranCol - 1, WATER);
			  return;
		  }
		  if(ranLoc == 2 && ranRow + 1 < grid.length && grid[ranRow + 1][ranCol] == EMPTY){
			  locationClicked(ranRow,ranCol, EMPTY);
			  locationClicked(ranRow + 1,ranCol, WATER);
			  return;
		  }
		  if(ranLoc == 3 && ranCol + 1 < grid[0].length && grid[ranRow][ranCol + 1] == EMPTY){
			  locationClicked(ranRow,ranCol, EMPTY);
			  locationClicked(ranRow,ranCol + 1, WATER);
			  return;
		  }
	  }
  }
  
  //do not modify
  public void run()
  {
    while (true)
    {
      for (int i = 0; i < display.getSpeed(); i++)
        step();
      updateDisplay();
      display.repaint();
      display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  //test if mouse clicked
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
    }
  }
}
