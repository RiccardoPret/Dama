package pret.dama.controller;

import pret.dama.model.Piece;
import pret.dama.model.Position;


public class Move {
	
	private Piece mPiece;
	private Position mPosition;
	private boolean mIsLegal;
	private boolean isAnEat;	//If mCurrentpiece change, there is no problem to determine if the move is an eat
	
	public Move(Piece piece, Position pos){
		this.mPiece=piece;
		this.mPosition=pos;
		this.mIsLegal=legalComputation();
		this.isAnEat=isLegal() && Math.abs(mPiece.getPosition().getRow()-mPosition.getRow())==2;
	}
	
	public boolean isLegal(){
		return mIsLegal;
	}
	
	//Check if the move is legal
	private boolean legalComputation(){
		return mPiece.getLegalPositions(mPiece.getBoard()).contains(mPosition)==true;
	}
	
	//exec the move
	public void exec(){
		mPiece.getBoard().changePosition(mPiece, mPosition);
	}
	
	//Return true if the move is an eat
	public boolean isAnEat(){
		return isAnEat;
	}
	
	//Return the position that the piece will go
	public Position getNewPosition(){
		return mPosition;
	}
	
	public Piece getPiece(){
		return mPiece;
	}
	
	//Used in ArrayList remove method
	//If two moves have the same piece and position, they are equals
	public boolean equals(Object obj){
		if(!(obj instanceof Move))
			return false;
		Move m=(Move) obj;
		if(!this.mPiece.equals(m.mPiece) || !this.mPosition.equals(m.mPosition))
			return false;
		return true;
	}
	
	public String toString(){
		return ""+mPiece+"-->"+mPosition;
	}
}
