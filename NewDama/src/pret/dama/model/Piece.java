package pret.dama.model;

import java.util.ArrayList;


public abstract class Piece {
	private Board mBoard;
	private boolean mIsOwnedByUser;
	private Position mPosition;		//first position is (0,0). The last one is (7,7)
	
	public Piece(Board board, boolean owner, Position pos){
		this.mBoard=board;
		this.mIsOwnedByUser=owner;
		this.mPosition=pos;
	}
	
	public Piece(Piece piece){
		this.mBoard=piece.mBoard;
		this.mIsOwnedByUser=piece.mIsOwnedByUser;
		this.mPosition=piece.mPosition;
	}
	
	public boolean isOwnedByUser(){
		return mIsOwnedByUser;
	}
	
	public Position getPosition(){
		return mPosition;
	}
	
	public Board getBoard(){
		return mBoard;
	}
	
	public void setPosition(Position p){
		this.mPosition.setRow(p.getRow());
		this.mPosition.setCol(p.getCol());
	}
	
	//If there is eats, return those, otherwise return the moves (no eat)
	public ArrayList<Position> getLegalPositions(Board board){
		ArrayList<Position> temp= new ArrayList<Position>();
		temp=(this.getLegalEats().isEmpty()?this.getLegalMoves():this.getLegalEats());
		return temp;
	}
	
	// A piece is selectable if it could eat or (if no one can eat and it could move)
	public boolean isSelectable(){
		return couldEat() || (getBoard().noneCanEat() && couldMove());
	}
	
	public abstract boolean isADama();
	
	public abstract boolean couldEat();
	
	public abstract boolean couldMove();
	
	public abstract ArrayList<Position> getLegalEats();
	
	public abstract ArrayList<Position> getLegalMoves();
	
	public abstract boolean isEatable();
		
	//Used in IndexOf() method in Board class
	//If two pieces have the same positions and owner, they are equals
	public boolean equals(Object obj){
		if(!(obj instanceof Piece))
			return false;
		Piece p=(Piece) obj;
		if(!this.mPosition.equals(p.mPosition) || this.isOwnedByUser()!=p.isOwnedByUser())
			return false;
		return true;
	}

	public String toString(){
		return "[User? "+isOwnedByUser()+" "+mPosition.getRow()+","+mPosition.getCol()+"]";
	}
}
