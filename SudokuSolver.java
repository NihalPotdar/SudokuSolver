package codes;

import java.io.*;
import java.util.*;

public class SudokuSolver {
	
	public static int Dimension = 9, CounterX = 0, CounterY = 0, BoxAssigment = 0;
	public static Node[][] Cells = new Node[Dimension][Dimension];
	
	public static void main(String[] args) throws FileNotFoundException {
		/*int Menu;
		Scanner Entrance = new Scanner(System.in);
		
		do { // a menu panel is used for testing purposes
			System.out.println();
			System.out.println("0: Exit the Interface.");
			System.out.println("1. Populate. ");
			System.out.println("2. Display. ");
			System.out.println("3. Solution. ");
			Menu = Entrance.nextInt();
			
			if(Menu == 1)
			{
				InstantiateArray(); // creating the array
				GettingData(); // Getting Data from a file
			}
			
			else if(Menu == 2)
			{		
				Display(); // displaying the array
			}
			
			else if(Menu == 3)
			{ // first case
				Solution(); // engaging the solution
				//DisplayPotentials(); // test // checking potentials
			}
			
			else if(Menu == 4)
			{ // check potentials, only a test
				DisplayPotentials(); // test // checking potentials
			}
			
			else if(Menu == 5)
			{ // testing recursion
				GuessWithRecursionAndStack();
			}
		} while(Menu != 0); */		//System.out.println("\nThank you for using this interface");
		
		// MAKING IT EASIER FOR THE USER
		InstantiateArray(); // creating the array
		GettingData(); // Getting Data from a file
		System.out.println("Initial Sudoku Grid"); // for the user
		Display();
		Solution(); // engaging the solution
		if(CheckSolution())
		{ // Solved Sudoku
			System.out.println("\nSolved Sudoku Grid");
			Display(); // displaying the new grid
		}
	}

	public static void InstantiateArray()
	{
		for(int x = 0; x < Dimension; x++)
			for(int y = 0; y < Dimension; y++)
			{	
				if((x%3 == 0) && (y%3 == 0))
					BoxAssigment++; // finding the value of that particular box 
				
				if(x%3 != 0)
					BoxAssigment = Cells[x-1][y].getBoxAssignment(); // setting the ones below the main box with a certain ID similar to the ones above
				
				// Y is the column number and x is the row number
				
				Node Temp = new Node(0, BoxAssigment, y, x); // setting data, box assignment, ColumnNumber and row number
				Cells[x][y] = Temp; // setting the data	to each node
				/*System.out.print(Cells[x][y].getBoxAssignment() + " "); 
				if(y == 8) 
					System.out.println(); */// testing box ID
			}
		
	}
	
	public static void GettingData() throws FileNotFoundException
	{
		File infile = new File("HardSudoku.txt"); // dictating the file
		System.out.println("\nWe are using data from the " + infile + " file\n");
		Scanner Input = new Scanner(infile); // Initializing the means to decipher and read the file
		
		while(Input.hasNext())
		{ // storing all the items in a database
			String Data = Input.next(); // this section works
			Cells[CounterY][CounterX].setData(Integer.parseInt(Data)); 
			
			// Updating the booleans
			if(Integer.parseInt(Data) != 0)
				for(int x = 0; x < Dimension; x++)
				{// setting everything to false except for the case that contains the current number
					if(x == Integer.parseInt(Data)-1)
						continue; 
					BooleanUpdate(Cells[CounterY][CounterX], x+1); // this must always be the same
				}
			//System.out.println(Cells[CounterX][CounterY].getData()); // test
			
			if(CounterX == 8) 
			{
				CounterX = 0; // resetting the variables to avoid null pointer exception
				CounterY++; // moving to the next row once a solution has been found
			}
			else
				CounterX++;
		}
	}
	
	public static void Display()
	{
		// Printing out the grid
		System.out.println("**********************");
		System.out.println("The Sudoku Grid");
		System.out.println("**********************");
		
		for(int x = 0; x < Dimension; x++)
		{
			for(int y = 0; y < Dimension; y++)
			{
				if(y%3 == 0 && y!= 0) // adding a space after each box has been printed out
					System.out.print(" ");
				
				if(x%3 == 0 && x!= 0 && y == 0) // adding a line after each box is done
					System.out.println();
				
				System.out.print(Cells[x][y].getData() +" "); // printing out the individual cell
			}
			System.out.println(); // adding a line after each box
		}
	}
	
	public static void Solution()
	{ // the interface that runs the solution
		boolean ChangeIsOccuring = false;
		// Make a duplicate array at the top of the while loop					
		
		do{// Checks each box, column and row for numbers that should be placed (easy Sudoku)
			//ChangeIsOccuring = false;
			int[][] Duplicate = new int[Dimension][Dimension]; // creating a duplicate array and copying the duplicate array with the content of the original			
			Duplicate = CopyingAndStoringArray(Duplicate); // saves the content of the 2D array
			
			ChangeIsOccuring = false; // resetting the variable
			FirstAndSecondRule();
			OnlyPossibilityInBox();
			ThirdAndFourthRules();
		
			for(int Row = 0; Row < Dimension; Row++)
				for(int Column = 0; Column < Dimension; Column++)
					for(int Possibilities = 0; Possibilities < Dimension; Possibilities++)
					{
						if(Cells[Row][Column].getData() != Duplicate[Row][Column])
						{
							ChangeIsOccuring = true; 
						}
					}
		} while(ChangeIsOccuring); // do this only when the grid is changing
		
		if(!CheckSolution()) // using a brute force method if none of the rules can be implemented
		{	
			if(GuessWithRecursionAndStack())
			{
				//System.out.println("\nRecursion!!\n"); // test
				UpdateBooleans(); // updating the booleans as we have been using a stack
			}
			else
				System.out.println("\nSudoku cannot be solved!");
		}

		//System.out.println(CheckSolution()); // test
	}
	
	public static void FirstAndSecondRule()
	{ // Checks each row, column and box for numbers that can be placed		
		for(int x = 0; x < Dimension; x++)
		{ // goes through the row
			for(int y = 0; y < Dimension; y++)
			{ // goes through the column
				if(Cells[x][y].getData() == 0)
				{ // if the cell has not been populated yet
					
					// Box
					int BoxID = Cells[x][y].getBoxAssignment(); // to identify a specific box
					BoxCheck(BoxID, x, y); // goes through a box and sets booleans to false depending on numbers available
					
					
					// Column
					int ColumnID = Cells[x][y].getColumnNumber(); // to identify a specific column
					ColumnCheck(ColumnID, x,y); // goes through a column and sets booleans to false depending on numbers available
					
					
					// Row
					int RowID = Cells[x][y].getRowNumber(); // to identify a specific row
					RowCheck(RowID, x, y); // goes through a row and sets booleans to false depending on numbers available
					
				} // end if statement
			} // end column for loop
		} // end row for loop
		WritePossibilities(); // making changes to data once booleans have been set to false
	} // end method

	public static void OnlyPossibilityInBox()
	{ // finding a cell that has a potential that none of the other cells in the box do
		// go through each box to see if there is only one place that a number can go
		for(int Box = 0; Box < Dimension; Box++) // BoxID 
		{
			for(int Possibilities = 1; Possibilities <= Dimension; Possibilities++) // Going through each potential number
			{
				int TotalPossibility = AmountOfEachPotentialInBox(Possibilities, Box); // represents the total possibilities for a particular number within a given box
				FindingLonePossibility(TotalPossibility, Box, Possibilities); // finding the only possibility where a number can exist in a box
			}
		}		
	}
	
	public static void ThirdAndFourthRules()
	{
		//System.out.println(NumberOfPossibilities(3, 1)); // this works		
		//DisplayPotentials(); // test // Works!
		
		for(int Box = 0; Box < Dimension; Box++) // BoxID 
		{
			for(int Possibilities = 1; Possibilities <= Dimension; Possibilities++) // Going through each potential number
			{
				int TotalPossibility = AmountOfEachPotentialInBox(Possibilities, Box); // represents the total possibilities for a particular number within a given box

				for(int Row = 0; Row < Dimension; Row++)
				{
					if(TotalPossibility != 0 && TotalPossibility != 1 && AmountOfEachPotentialInRow(Row, Box, Possibilities) == TotalPossibility) // When the total number of possibilities for a particular potential is equal to the maximum number of potentials in the box
					{// this works!
						//System.out.println("Test " + Row + " " + Possibilities + "BOX: " + Box); // test
						SetRestofRowToFalse(Row, Box, Possibilities); // setting the rest of a particular row to false if it doesn't equal the given data
					}					
				}
				
				for(int Column = 0; Column < Dimension; Column++)
				{
					if(TotalPossibility != 0 && TotalPossibility != 1 && AmountOfEachPotentialInColumn(Column, Box, Possibilities) == TotalPossibility)
					{
						//System.out.println("Test " + Column + " " + Possibilities + "BOX: " + Box); // test
						SetRestofColumnToFalse(Column, Box, Possibilities); // setting the rest of a particular column to false if it doesn't equal the given data
					}
				}
			}
		} // end BoxLoop
		
		WritePossibilities(); // making changes to data once booleans have been set to false
	}
	
	public static void BooleanUpdate(Node CurrentNode, int Number)
	{ // check the node and update 
		// provide a number and set that possibility to false
		boolean Duplicate[] = CurrentNode.getPossibilities(); // getting booleans
		Duplicate[Number-1] = false; // setting a specific boolean to false
		CurrentNode.setPossibilities(Duplicate); // assigning the changes to the node
	}
	
	public static void WritePossibilities()
	{ // check for the booleans at each node and write a number into it
		for(int x = 0; x < Dimension; x++) // going through each column
			for(int y = 0; y < Dimension; y++) // going through each row
			{
				boolean Duplicate[] = Cells[x][y].getPossibilities();
				int Counter = 0;
				for(int BooleanIndex = 0; BooleanIndex < Dimension; BooleanIndex++) // checking for each possibility
				{
					if(Duplicate[BooleanIndex]) // checking if this node is available
					{
						Counter = Counter+1;
						Cells[x][y].setData(BooleanIndex+1); // writing the data into the current node
						// We have to check there are multiple booleans which are true
					}
				}
				if(Counter > 1) // there are multiple possibilities
					Cells[x][y].setData(0);
			}
	}
	
	public static void BoxCheck(int BoxID, int Row, int Column)
	{ // goes through a box and sets certain possibilities to false depending on that
		for(int SecondaryX = 0; SecondaryX < Dimension; SecondaryX++) // goes through every single node searching for the box ID
			for(int SecondaryY = 0; SecondaryY < Dimension; SecondaryY++)
				if(Cells[SecondaryX][SecondaryY].getData() != 0 && Cells[SecondaryX][SecondaryY].getBoxAssignment() == BoxID)
					BooleanUpdate(Cells[Row][Column], Cells[SecondaryX][SecondaryY].getData()); // if this is the correct box, then set one of the possibilities of the current number to false
	}
	
	public static void ColumnCheck(int ColumnID, int Row, int Column)
	{ // goes through a column and sets certain possibilities to false depending on that
		for(int PlacementX = 0; PlacementX < Dimension; PlacementX++) // goes through every single node searching for the box ID
			for(int SecondaryY = 0; SecondaryY < Dimension; SecondaryY++)
				if(Cells[SecondaryY][PlacementX].getData() != 0 && Cells[SecondaryY][PlacementX].getColumnNumber() == ColumnID)
					BooleanUpdate(Cells[Row][Column], Cells[SecondaryY][PlacementX].getData()); // if this is the correct column, then set one of the possibilities of the current number to false
	}
	
	public static void RowCheck(int RowID, int Row, int Column)
	{ // goes through a row and sets certain possibilities to false depending on that
		for(int SecondaryX = 0; SecondaryX < Dimension; SecondaryX++) // goes through every single node searching for the box ID
			for(int PlacementY = 0; PlacementY < Dimension; PlacementY++)
				if(Cells[SecondaryX][PlacementY].getData() != 0 && Cells[SecondaryX][PlacementY].getRowNumber() == RowID)
					BooleanUpdate(Cells[Row][Column], Cells[SecondaryX][PlacementY].getData()); // if this is the correct row, then set one of the possibilities of the current number to false
	}
	
	public static boolean GuessWithRecursionAndStack()
	{ // method gives input related to if this is solved
		//System.out.println(Cells[0][4].getData());
		//Display();
		for(int Row = 0; Row < Dimension; Row++)
		{
			for(int Column = 0; Column < Dimension; Column++)
			{
				if(Cells[Row][Column].getData() == 0)
				{
					// Placing the stack here prevents problems in the case that it backtracks
					Stack<Integer> Stacklist = new Stack<Integer>();
					UpdateStackWithBoolean(Stacklist, Row, Column); // we use the stack to see if this solution is possible relative to the booleans
					for(int Possibilities = 1; Possibilities <= Dimension; Possibilities++)
					{
						//if(Cells[Row][Column].search(Possibilities) == -1 && (NumberofOccurencesInBox(Possibilities, Cells[Row][Column].getBoxAssignment()) == 0) && (NumberofOccurencesInRow(Possibilities, Cells[Row][Column].getRowNumber()) == 0) && (NumberofOccurencesInColumn(Possibilities, Cells[Row][Column].getColumnNumber()) == 0))
						if(Stacklist.search(Possibilities) == -1 && Combine(Possibilities, Row, Column))
						{ // data is possible at this spot
							/*if(Row == 0 && Column == 2 && Possibilities == 1) // test for the stack
								System.out.println("Yes");*/ // this WORKS!!
							
							Cells[Row][Column].setData(Possibilities); // assigning data to the grid cells
							Stacklist.push(Possibilities);  // pushing data to the stack
							
							if(GuessWithRecursionAndStack()) // if this number is right
								return true; // go back as this number is right (end recursive instance)
							else // number is wrong
								Cells[Row][Column].setData(0); // emptying the cell							
						} // end rules when this particular statement is possible
					}
					return false; // no possibility for given cells or combination of numbers is incorrect
				}
			}
		}
		
		return true;
	}
	
	public static int[][] CopyingAndStoringArray(int[][] Duplicate)
	{ // creating and saving the values of a duplicate 2D array
		for(int x = 0; x < Dimension; x++)
			for(int y= 0; y < Dimension; y++)
				Duplicate[x][y] = Cells[x][y].getData();
		return Duplicate;
	}
	
	public static void DisplayPotentials()
	{
		for(int x = 0; x < Dimension; x++) // column
		{
			for(int y = 0; y < Dimension; y++) // row
			{
				// dividing by boxes
				if(y%3 == 0 && y!= 0) // adding a space after each box has been printed out
					System.out.print(" ");
				
				if(x%3 == 0 && x!= 0 && y == 0) // adding a line after each box is done
					System.out.println();
				
				boolean Duplicate[] = Cells[x][y].getPossibilities();
				for(int Potential = 0; Potential < Dimension; Potential++) // each potential
				{
					if(Duplicate[Potential]) // if a particular potential is true
						System.out.print(Potential+1); // "+1" since arrays always begin at zero but sudoku at one
					
					else 
						System.out.print("0"); // for spacing and easy identification purposes
				}
				System.out.print(" "); // adding a space after each cell
			}
			System.out.println(); // printing out a new line after the current set of values have been printed
		}
	}	
	
	public static void FindingLonePossibility(int TotalPossibility, int Box, int Possibilities)
	{ //finding the only space where a number can go in a box
		if(TotalPossibility == 1) // finding the cell that only has this possibility
			for(int x = 0; x < Dimension; x++) // going through each row
				for(int y = 0; y < Dimension; y++) // going through each column
					if(Cells[x][y].getBoxAssignment() == Box) // trying to find the particular cell with only one possibility
						if(Cells[x][y].FindBooleanWithIndex(Possibilities-1)) // checking to see if it is the cell with a lone possibility
							for(int Possibility = 1; Possibility <= Dimension; Possibility++)
							{ // going through every potential number
								if(Possibility != Possibilities)
									BooleanUpdate(Cells[x][y], Possibility);
							}
	}
	
	public static int AmountOfEachPotentialInRow(int Row, int BoxID, int Possibility)
	{ // calculating the number of instances of a specific potential in a row
		int Counter = 0;
		
		for(int Column = 0; Column < Dimension; Column++) // going through each cell in a row
		{
			if(Cells[Row][Column].getBoxAssignment() == BoxID) // going through each cell of a particular box
			{
				if(Cells[Row][Column].FindBooleanWithIndex(Possibility-1)) // if the current cell in this row has the current potential as true
				{
					Counter++; // counting the number of instances where a particular potential is possible within a particular row
				}
				//System.out.println("Here"); // test
			}
		}
		
		return Counter;
	}
	
	public static int AmountOfEachPotentialInColumn(int Column, int BoxID, int Possibility)
	{
		int Counter = 0;
		
		for(int Row = 0; Row < Dimension; Row++) // going through each cell in a row
		{
			if(Cells[Row][Column].getBoxAssignment() == BoxID) // going through each cell of a particular box
			{
				if(Cells[Row][Column].FindBooleanWithIndex(Possibility-1)) // if the current cell in this row has the current potential as true
				{
					Counter++; // counting the number of instances where a particular potential is possible within a particular row
				}
				//System.out.println("Here"); // test
			}
		}
		
		return Counter;
	}
	
	public static void SetRestofRowToFalse(int Row, int BoxID, int Possibility)
	{ // takes certain indexes and sets and everything else in that Row to false
		for(int Column = 0; Column < Dimension; Column++) // going through each cell in a row
			if(Cells[Row][Column].getBoxAssignment() != BoxID) // going through each cell of a particular box
			{ // Finding a cell that is in the same row but not in the same box and setting it to false
				boolean Duplicate[] = Cells[Row][Column].getPossibilities();
				Duplicate[Possibility-1] = false;
				Cells[Row][Column].setPossibilities(Duplicate);
			}					
	}
	
	public static void SetRestofColumnToFalse(int Column, int BoxID, int Possibility)
	{// takes certain indexes and sets and everything else in that Column to false
		for(int Row = 0; Row < Dimension; Row++) // going through each cell in a row
		{
			if(Cells[Row][Column].getBoxAssignment() != BoxID) // going through each cell of a particular box
			{ // Finding a cell that is in the same row but not in the same box and setting it to false
				boolean Duplicate[] = Cells[Row][Column].getPossibilities();
				Duplicate[Possibility-1] = false;
				Cells[Row][Column].setPossibilities(Duplicate);
			}
		}
	}
	
	public static int AmountOfEachPotentialInBox(int Number, int BoxID)
	{
		int Counter = 0;
		// Takes a number, then checks for the number of instances where that potential is true
		for(int x = 0; x < Dimension; x++) // column
			for(int y = 0; y < Dimension; y++) // row
				if(Cells[x][y].getBoxAssignment() == BoxID) // checking if we are at the right box
					if(Cells[x][y].FindBooleanWithIndex(Number-1))// seeking the boolean that bears the same number as the one we are seeking
						Counter = Counter+1;
		return Counter; // returns the number of instances where a particular number is true within a particular box
	}	
	
	public static boolean Combine(int Number, int Row, int Column)
	{
		// A number is not possible in this box
		if(NumberofOccurencesInBox(Number, Cells[Row][Column].getBoxAssignment()) != 0)
			return false;
		if(NumberofOccurencesInRow(Number, Cells[Row][Column].getRowNumber()) != 0)
			return false; // A number is not possible in this row
		if(NumberofOccurencesInColumn(Number, Cells[Row][Column].getColumnNumber()) != 0)
			return false; // A number is not possible in this column
		return true; // if nothing has been set to false (its possible), then the program reaches this spot
	}
	
	public static int NumberofOccurencesInBox(int Number, int BoxID)
	{
		int Counter = 0;
		// Takes a number, then checks for the number of instances where that particular possibility occurs
		for(int x = 0; x < Dimension; x++) // column
			for(int y = 0; y < Dimension; y++) // row
				if(Cells[x][y].getBoxAssignment() == BoxID) // checking if we are at the right box
					if(Cells[x][y].getData() == Number)// seeking the boolean that bears the same number as the one we are seeking
						Counter = Counter+1;
		return Counter;
	}
	
	public static int NumberofOccurencesInColumn(int Number, int ColumnID)
	{
		int Counter = 0;
		// Takes a number, then checks for the number of instances where that particular possibility occurs
		for(int x = 0; x < Dimension; x++) // column
			for(int y = 0; y < Dimension; y++) // row
				if(Cells[x][y].getColumnNumber() == ColumnID) // checking if we are at the right box
					if(Cells[x][y].getData() == Number)// seeking the boolean that bears the same number as the one we are seeking
						Counter = Counter+1;
		return Counter;
	}
	
	public static int NumberofOccurencesInRow(int Number, int RowID)
	{
		int Counter = 0;
		// Takes a number, then checks for the number of instances where that particular possibility occurs
		for(int x = 0; x < Dimension; x++) // column
			for(int y = 0; y < Dimension; y++) // row
				if(Cells[x][y].getRowNumber() == RowID) // checking if we are at the right box
					if(Cells[x][y].getData() == Number)// seeking the boolean that bears the same number as the one we are seeking
						Counter = Counter+1;
		return Counter;
	}
	
	public static void UpdateStackWithBoolean(Stack<Integer> Stacklist, int Row, int Column)
	{ // goes through the current cell and updates the stack relative to its boolean
		for(int Possibilities = 0; Possibilities < Dimension; Possibilities++)
		{ // going through each boolean
			if(!Cells[Row][Column].FindBooleanWithIndex(Possibilities))
				Stacklist.push(Possibilities+1); // populating the stack
		}
	}
	
	public static void UpdateBooleans()
	{
		for(int Row = 0; Row < Dimension; Row++)
			for(int Column = 0; Column < Dimension; Column++)
				for(int Possibilities = 0; Possibilities < Dimension; Possibilities++)
					if(Cells[Row][Column].FindBooleanWithIndex(Possibilities) && Cells[Row][Column].getData() != (Possibilities+1))
						BooleanUpdate(Cells[Row][Column], Possibilities+1);
	}
	
	public static boolean CheckSolution()
	{		
		// check box
		for(int BoxID = 1; BoxID<= Dimension; BoxID++) // going through each box
			for(int Possibility = 1; Possibility <= Dimension; Possibility++) // going through each possibility in a cell
			{	
				/*System.out.println(Possibility + " " + BoxID);
				System.out.println(NumberofOccurencesInBox(Possibility, BoxID)); TEST*/ 
				
				if(NumberofOccurencesInBox(Possibility, BoxID) != 1) // if there are multiple instances of a number in a box
					return false;
			}
		
			for(int ID = 0; ID < Dimension; ID++) // going through each box
				for(int Possibility = 1; Possibility <= Dimension; Possibility++) // going through each possibility in a cell
				{					
					if(NumberofOccurencesInColumn(Possibility, ID) != 1 || NumberofOccurencesInRow(Possibility, ID) != 1)
						return false;
				}
					
		return true;
	}
}