package pret.dama.controller;

import java.util.ArrayList;

import pret.dama.model.Board;
import pret.dama.model.Piece;
import pret.dama.model.Position;

public class AI {
	private Board mBoard;
	private ArrayList<Move> rightStep;

	public AI(Board board){
		this.mBoard=board;
	}
	
	public Board getBoard(){
		return mBoard;
	}

	/*Formula to find next Position: newPos*2-piece.getPosition (Array position 0)
	 * The scenario are represented (where X is the piece and the number the array position):
	 *  	 X|__|2				2|__|X
	 *  	__|__|__	OR     __|__|__
	 *  	 1|__|0				0|__|1
	 *   
	 * N.B. In case the piece is a Dama this method doesn't consider the other two situations:
	 *  	 1|__|0				2|__|X
	 *  	__|__|__	OR     __|__|__
	 *  	 X|__|2				0|__|1
	 */
	private Position[] findNextPositions(Piece piece, Position newPos){
		Position[] posToCheck= new Position[3];
		
		posToCheck[0]= new Position(newPos.getRow()*2-piece.getPosition().getRow(), newPos.getCol()*2-piece.getPosition().getCol());
		posToCheck[1]= new Position(newPos.getRow()*2-piece.getPosition().getRow(), piece.getPosition().getCol());
		posToCheck[2]= new Position(piece.getPosition().getRow(), newPos.getCol()*2-piece.getPosition().getCol());
		
		return posToCheck;
	}
	
	//Return true if the piece moved in newPos become eatable by a Dama
	private boolean isDamaEatable(Piece piece, Position newPos) {
		Position[] posToCheck=findNextPositions(piece, newPos);

		if(getBoard().areRivalsAt(piece.getPosition(), posToCheck[0]) && mBoard.getPieceAt(posToCheck[0]).isADama())
			return true;
		if(getBoard().areRivalsAt(piece.getPosition(), posToCheck[1]) && mBoard.isCellEmpty(posToCheck[2]) &&
				mBoard.getPieceAt(posToCheck[1]).isADama())
			return true;
		if(getBoard().areRivalsAt(piece.getPosition(), posToCheck[2]) && mBoard.isCellEmpty(posToCheck[1]) &&
				mBoard.getPieceAt(posToCheck[2]).isADama())
			return true;
		return false;
	}
	
	//Return true if the piece moved in newPos become eatable by a Piece (pawn or dama)
	private boolean isPieceEatable(Piece piece, Position newPos) {
		Position[] posToCheck=findNextPositions(piece, newPos);

		if(mBoard.areRivalsAt(piece.getPosition(), posToCheck[0]))
			return true;
		if(mBoard.areRivalsAt(piece.getPosition(), posToCheck[1]) && mBoard.isCellEmpty(posToCheck[2]))
			return true;
		if(mBoard.areRivalsAt(piece.getPosition(), posToCheck[2]) && mBoard.isCellEmpty(posToCheck[1]) &&
				mBoard.getPieceAt(posToCheck[2]).isADama())
			return true;
		return false;
	}
	
	//return the number of the remained Moves and remove moves that cause a user eaten in the next turn
	private int removeEatable() {
		for(Move m:new ArrayList<Move>(rightStep))
			if(rightStep.size()>1){	//If there is only one piece return that though it is eatable
				if(!m.getPiece().isADama() && isPieceEatable(m.getPiece(), m.getNewPosition())){
					rightStep.remove(m);
				}
				else if(m.getPiece().isADama() && isDamaEatable(m.getPiece(), m.getNewPosition())){
					rightStep.remove(m);
				}
			}
		
		return rightStep.size();
	}
	
	//Move a piece that is eatable if it doesn't move away in this turn
	private void selectEatable() {
		ArrayList<Move> temp=new ArrayList<Move>(rightStep);
		for(Move m:temp)
			if(!m.getPiece().isEatable()){
				rightStep.remove(m);
			}
		//If there aren't piece eatable, restore the list
		if(rightStep.size()==0)
			rightStep= new ArrayList<Move>(temp);
	}
	
	//Do an eat/multiple eat
	private void eat(Board board, Position pos){
		if(pos!=null && board.getPieceAt(pos).couldEat()){
			Move temp= new Move(board.getPieceAt(pos), board.getPieceAt(pos).getLegalPositions(board).get(0));
			temp.exec();
			eat(board, temp.getNewPosition());
		}
	}
	
	//Add the moves/step (eat and move) using the piece and the relatives legal positions
	private void add(Piece p) {
		for(Position pos:p.getLegalPositions(mBoard))
			rightStep.add(new Move(p, pos));
	}
	
	public void getTheStep(){
		rightStep= new ArrayList<Move>();

		//If someone can eat, find it and do an eat, else...
		if(!mBoard.noneCanEat()){
			for(Piece[] pArray:mBoard.getMatrix())
				for(Piece p:pArray)
					if(p!=null && p.couldEat())
						add(p);
			eat(mBoard, rightStep.get(0).getPiece().getPosition());
		}else{
			for(Piece[] pArray:mBoard.getMatrix())
				for(Piece p:pArray)
					if(p!=null && p.couldMove())
						add(p);
			//Now there is all the possible moves (nothing is an eat)
			if(removeEatable()==1)
				rightStep.get(0).exec();
			else{
				selectEatable();
				if(rightStep.get(0).getPiece().isADama())
					rightStep.get((int)(Math.random()*(rightStep.size()-1))).exec(); //take a random moves to remove looping moves by Dama
				else
					rightStep.get(0).exec();
			}
		}
	}
	
}
