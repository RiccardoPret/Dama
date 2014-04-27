package pret.dama.model;

import java.util.ArrayList;


public class Pawn extends Piece {

	public Pawn(Board board, boolean owner, Position position) {
		super(board, owner, position);
	}

	public Pawn(Piece piece){
		super(piece);
	}
	
	private Position pNear[]= new Position[2];
	private Position pNext[]= new Position[2];
	
	//Init all the 2 near position and the 2 next position if the move is an eat
	private void init(){
		int row=(this.isOwnedByUser()?-1:1);	//find the way to eat. If it's user's --> check for upper position (less index)
		pNear[0]=getPosition().add(row, 1);
		pNear[1]=getPosition().add(row, -1);
		pNext[0]=getPosition().add(2*row, 2);
		pNext[1]=getPosition().add(2*row, -2);
	}
	
	//The position to go must to be empty,
	//the piece to eat must owned by the other player
	//the piece mustn't be a Dama
	private boolean eatCondition(int i){
		return getBoard().isCellEmpty(pNext[i]) && 
				getBoard().areRivalsAt(this.getPosition(), pNear[i]) && 
				!getBoard().isADamaAt(pNear[i]);
	}
	
	//Could move (doesn't check eat) if the position to go is empty and it's its turn
	@Override
	public boolean couldMove() {
		init();
		for(int i=0; i<2; i++)
			if(this.getBoard().isCellEmpty(pNear[i]) && isOwnedByUser()==getBoard().isUserTurn())
				return true;

		return false;
	}
	
	//Could eat if respect the eat condition and it's its turn
	@Override
	public boolean couldEat() {
		init();

		for(int i=0; i<2; i++)
			if(eatCondition(i) && isOwnedByUser()==getBoard().isUserTurn())
				return true;
		
		return false;
	}

	//Return the legal Moves (no eat). Same condition of couldMove() method
	public ArrayList<Position> getLegalMoves(){
		ArrayList<Position> temp= new ArrayList<Position>();	//array that contains the legal
		init();
		
		for(int i=0; i<2; i++)
			if(this.getBoard().isCellEmpty(pNear[i]) && isOwnedByUser()==getBoard().isUserTurn())
				temp.add(new Position(pNear[i].getRow(), pNear[i].getCol()));
		
		return temp;
	}
	
	//Return the legal Eat. Same condition of couldEat() method
	public ArrayList<Position> getLegalEats(){
		ArrayList<Position> temp= new ArrayList<Position>();	//array that contains the legal eats
		init();

		for(int i=0; i<2; i++)
			if(eatCondition(i)  && isOwnedByUser()==getBoard().isUserTurn())
				temp.add(new Position(pNext[i].getRow(), pNext[i].getCol()));	//Remember to create a new object
		
		return temp;
	}
	
	@Override
	public boolean isADama() {
		return false;
	}

	//A pawn is eatable:
	//	only by a rival and the next position must to be empty in certain positions
	//  only by a rival dama and the next position must to be empty in other positions
	@Override
	public boolean isEatable() {
		int row=(this.isOwnedByUser()?-1:1);	//find the way to eat. If it's mine I have to check for upper position (less index)
		Position posNear[]= new Position[4];
		posNear[0]=getPosition().add(row, -1);
		posNear[1]=getPosition().add(row, 1);
		posNear[2]=getPosition().add(-row, 1);
		posNear[3]=getPosition().add(-row, -1);
		
		for(int i=0;i<2;i++)
			if(getBoard().areRivalsAt(this.getPosition(), posNear[i]) &&
					getBoard().isCellEmpty(posNear[i+2]))
				return true;
		for(int i=2;i<4;i++)
			if(getBoard().isADamaAt(posNear[i]) && getBoard().areRivalsAt(this.getPosition(), posNear[i]) &&
					getBoard().isCellEmpty(posNear[i-2]))
				return true;
		return false;
	}

}
