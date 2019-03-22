package codes;

public class Node {
	private int data;
	private int BoxAssignment;
	private int RowNumber;
	private int ColumnNumber;
	private int Dimension = 9; // same for every single node
	private boolean Possibilities[] = new boolean[Dimension];

	public Node(int data, int BoxAssignment, int RowNumber, int ColumnNumber) {
		this.data = data;
		this.BoxAssignment = BoxAssignment;
		this.RowNumber = RowNumber;
		this.ColumnNumber = ColumnNumber;
		InstantiateBooleans();
	}

	public Node() {
		data = -1;
		BoxAssignment = -1;
		RowNumber = -1;
		ColumnNumber = -1;
	}
	
	public boolean FindBooleanWithIndex(int Number)
	{
		return Possibilities[Number];
	}
	
	public void InstantiateBooleans()
	{
		for(int x = 0; x < Dimension; x++)
		{
			Possibilities[x] = true;
		}
			
	}
	
	public boolean[] getPossibilities() {
		return Possibilities;
	}

	public void setPossibilities(boolean[] possibilities) {
		Possibilities = possibilities;
	}

	public int getRowNumber() {
		return RowNumber;
	}

	public void setRowNumber(int rowNumber) {
		RowNumber = rowNumber;
	}

	public int getColumnNumber() {
		return ColumnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		ColumnNumber = columnNumber;
	}

	public int getBoxAssignment() {
		return BoxAssignment;
	}

	public void setBoxAssignment(int boxAssignment) {
		BoxAssignment = boxAssignment;
	}
	
	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}
}

