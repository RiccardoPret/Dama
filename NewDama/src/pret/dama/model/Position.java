package pret.dama.model;

public class Position{
	
	int x;		//first x is 0
	int y;		//first y is 0
	
	public Position(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public int getRow(){
		return x;
	}
	
	public int getCol(){
		return y;
	}
	
	public void setRow(int x){
		this.x=x;
	}
	
	public void setCol(int y){
		this.y=y;
	}

	public void setPosition(Position pos){
		this.x=pos.x;
		this.y=pos.y;
	}
	
	public Position add(int a, int b){
		return new Position(this.x+a, this.y+b);
	}
	
	public boolean canExist(){
		return x>-1 && x<8 && y>-1 && y<8;
	}
	
	public boolean isABorder(){
		return x==0 || x== 7;
	}
	
	//Used in contain() method
	//Two posisions are equals if they are the same coordinates
	public boolean equals(Object obj){
		if(!(obj instanceof Position))
			return false;
		Position pos=(Position) obj;
		if(this.x!=pos.x || this.y!=pos.y)
			return false;
		return true;
	}
	
	public String toString(){
		return "Pos: ("+x+", "+y+")";
	}
}
