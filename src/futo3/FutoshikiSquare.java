package futo3;
/**
 * "Model Answer" to stage 3 of Futoshiki Exercise
 * 
 * @author Rudi Lutz 
 * @version 1.0
 */
public  class FutoshikiSquare {
    private int row, column;
    private int value = 0;
    public FutoshikiSquare(int row, int column) {
        this.row = row;
        this.column = column;
    }
    
    public boolean isEmpty() {
        return this.value == 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    
    public String getSymbol() {
        if(isEmpty()) {
            return " ";
        } else {
            return "" + getValue();
        }
    }
    
 
}
