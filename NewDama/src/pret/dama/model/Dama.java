package pret.dama.model;

import java.util.ArrayList;


public class Dama extends Piece{

	public Dama(Board board, boolean owner, Position position) {
		super(board, owner, position);
	}
	
	public Dama(Piece piece){
		super(piece);
	}

	private Position pNear[]= new Position[4];
	private Position pNext[]= new Position[4];
	
	//Init all the 4 near position and the 4 next position if the move is an eat
	private void init(){
		pNear[0]=getPosition().add(1, 1);
		pNear[1]=getPosition().add(1, -1);
		pNear[2]=getPosition().add(-1, 1);
		pNear[3]=getPosition().add(-1, -1);
		
		pNext[0]=getPosition().add(2, 2);
		pNext[1]=getPosition().add(2, -2);
		pNext[2]=getPosition().add(-2, 2);
		pNext[3]=getPosition().add(-2, -2);
	}
	
	//The position to go must to be empty,
	//the piece to eat must owned by the other player
	private boolean eatCondition(int i){
		return getBoard().isCellEmpty(pNext[i]) && 
				getBoard().areRivalsAt(this.getPosition(), pNear[i]);
	}
	
	//Could move (doesn't check eat) if the position to go is empty and it's its turn
	@Override
	public boolean couldMove() {
		init();
		for(int i=0; i<4; i++)
			if(this.getBoard().isCellEmpty(pNear[i]) && isOwnedByUser()==getBoard().isUserTurn())
				return true;
			
		return false;
	}
	
	//Could eat if respect the eat condition and it's its turn
	@Override
	public boolean couldEat() {
		init();
		for(int i=0; i<4; i++)
			if(eatCondition(i) && isOwnedByUser()==getBoard().isUserTurn())
				return true;
		
		return false;
	}
	
	//Return the legal Moves (no eat). Same condition of couldMove() method
	@Override
	public ArrayList<Position> getLegalMoves() {
		ArrayList<Position> temp= new ArrayList<Position>();
		init();
		
		for(int i=0; i<4; i++)
			if(this.getBoard().isCellEmpty(pNear[i]) && isOwnedByUser()==getBoard().isUserTurn())
				temp.add(new Position(pNear[i].getRow(), pNear[i].getCol()));
			
		return temp;
	}
	
	//Return the legal Eat. Same condition of couldEat() method
	@Override
	public ArrayList<Position> getLegalEats() {
		ArrayList<Position> temp= new ArrayList<Position>();
		init();

		for(int i=0; i<4; i++)
			if(eatCondition(i) && isOwnedByUser()==getBoard().isUserTurn())
				temp.add(new Position(pNext[i].getRow(), pNext[i].getCol()));
		
		return temp;
	}

	@Override
	public boolean isADama() {
		return true;
	}

	//A dama is eatable only by a rival dama and the next position must to be empty
	@Override
	public boolean isEatable() {
		Position posNear[]= new Position[4];
		posNear[0]=getPosition().add(1, -1);
		posNear[1]=getPosition().add(1, 1);
		posNear[2]=getPosition().add(-1, 1);
		posNear[3]=getPosition().add(-1, -1);
		
		for(int i=0;i<4;i++)
			if(getBoard().isADamaAt(posNear[i]) && getBoard().areRivalsAt(this.getPosition(), posNear[i]) &&
					getBoard().isCellEmpty(posNear[(i+2)%4]))
				return true;
		return false;
	}

}
