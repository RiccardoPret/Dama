package pret.dama.model;


public class Board {
	private Piece[][] mMatrix= new Piece[8][8];
	private int mUserPiecesRemained=12;		//User= human
	private int mOpponentPiecesRemained=12; //Opponent= computer
	private boolean mUserTurn;
	private boolean mUserIsWhite;
	
	public Board(boolean userColorIsWhite){
		mUserIsWhite=userColorIsWhite;
		mUserTurn=userColorIsWhite;
		//Initialize Opponent Pawn Array
		for(int row=0; row<3; row++){
			for(int col=(row%2==0)?0:1; col<8; col+=2)		//If the row is even then the first coloumn is 0 otherwise is 1
				mMatrix[row][col]=new Pawn(this, false, new Position(row, col));
		}
		//Initialize User Pawn Array
		for(int row=5; row<8; row++){
			for(int col=(row%2==0)?0:1; col<8; col+=2)
				mMatrix[row][col]=new Pawn(this, true, new Position(row, col));
		}
	}
	
	public Board(Board b){
		this.mUserIsWhite=b.mUserIsWhite;
		this.mUserTurn=b.mUserTurn;
		this.mUserPiecesRemained=b.mUserPiecesRemained;
		this.mOpponentPiecesRemained=b.mOpponentPiecesRemained;
		for(int i=0; i<8;i++)
			for(int j=0;j<8;j++){
				if(b.mMatrix[i][j] instanceof Pawn)
					this.mMatrix[i][j]=new Pawn(this, b.mMatrix[i][j].isOwnedByUser(), 
							new Position(b.mMatrix[i][j].getPosition().getRow(), b.mMatrix[i][j].getPosition().getCol()));
				else if(b.mMatrix[i][j] instanceof Dama)
					this.mMatrix[i][j]=new Dama(this, b.mMatrix[i][j].isOwnedByUser(), 
							new Position(b.mMatrix[i][j].getPosition().getRow(), b.mMatrix[i][j].getPosition().getCol()));
			}
	}
	
	//Return true if the current player can't do anything --> he lose
	private boolean isBlocked(){
		for(Piece pArray[]:mMatrix)
			for(Piece p: pArray)
				if (p!=null && (p.couldEat() || p.couldMove()))	//N.B. couldEat and couldMove selects only piece of the current player
					return false;	//someone isn't blocked
		
		return true;
	}
	
	//Convert a Pawn in a Dama
	private void PawnToDamaAt(Piece pawn, Position newPos){
		mMatrix[newPos.getRow()][newPos.getCol()]=new Dama(pawn);
	}
	
	//Return the Matrix that contains the Piece reference
	public Piece[][] getMatrix(){
		return mMatrix;
	}
	
	public boolean userIsWhite(){
		return mUserIsWhite;
	}
	
	//return true if it's the user turn
	public boolean isUserTurn(){
		return mUserTurn;
	}
	
	public void changeTurn(){
		mUserTurn=!mUserTurn;
	}
	
	/*Return 1 -->The winner is the user
	  Return -1-->The winner is the user
	  Return 0 -->The game is not ended*/
	public int whoWin(){
		if(mUserPiecesRemained==0 || (isUserTurn() && isBlocked()))
			return -1;
		if(mOpponentPiecesRemained==0 || (!isUserTurn() && isBlocked()))
			return 1;
		return 0;
	}
	
	//Return true if the piece at the two positions are rivals
	public boolean areRivalsAt(Position p1, Position p2){
		return p1.canExist() && p2.canExist() && !isCellEmpty(p1) && !isCellEmpty(p2) && isOwnedByUserAt(p1)!=isOwnedByUserAt(p2);
	}
	
	//Return true if in that position there isn't a piece
	public boolean isCellEmpty(Position pos){
		//It's important that canExist() is the first method. Prevent IndexOutOfBoundException()
		return pos.canExist() && mMatrix[pos.getRow()][pos.getCol()]==null;
	}
	
	//Return true if in that position there is a User Piece
	public boolean isOwnedByUserAt(Position pos){
		return pos.canExist() && !isCellEmpty(pos) && mMatrix[pos.getRow()][pos.getCol()].isOwnedByUser();
	}
	
	//Return true if in that position there is a Dama
	public boolean isADamaAt(Position pos){
		return pos.canExist() && !isCellEmpty(pos) && mMatrix[pos.getRow()][pos.getCol()].isADama();
	}
	
	//Return true if no pieces of the current player can eat
	public boolean noneCanEat(){
		for(Piece pArray[]:mMatrix)
			for(Piece p: pArray)
				if (p!=null && p.couldEat())	//N.B. couldEat selects only piece of the current player
					return false;	//someone can eat
		
		return true;
	}
	
	//Return the Piece at that position
	public Piece getPieceAt(Position pos){
		if(pos.canExist())
			return mMatrix[pos.getRow()][pos.getCol()];
		System.out.println("POSIZIONE INESISTENTE");
		return null;
	}
	
	//Remove piece at that position and decrement the value of the piece remained
	public void removePieceAt(Position pos){
		if(mMatrix[pos.getRow()][pos.getCol()].isOwnedByUser())
			mUserPiecesRemained--;
		else
			mOpponentPiecesRemained--;
		System.out.println("Pezzi rimasti user: "+mUserPiecesRemained);
		System.out.println("Pezzi rimasti opponent: "+mOpponentPiecesRemained);
		mMatrix[pos.getRow()][pos.getCol()]=null;
	}
	
	//Change the position, and eventually 
	//							"cast" the Pawn in Dama
	//							remove a piece
	public void changePosition(Piece piece, Position newPos){
		//START MAPPING
		mMatrix[newPos.getRow()][newPos.getCol()]=mMatrix[piece.getPosition().getRow()][piece.getPosition().getCol()];
		mMatrix[piece.getPosition().getRow()][piece.getPosition().getCol()]=null;
		
		if(piece instanceof Pawn && newPos.isABorder())
			PawnToDamaAt(piece, newPos);
		
		//Formula: calculate the middle position --> (p1+p2)/2
		if(Math.abs(piece.getPosition().getRow()-newPos.getRow())==2)
			removePieceAt(new Position((piece.getPosition().getRow()+newPos.getRow())/2, (piece.getPosition().getCol()+newPos.getCol())/2));
		//END MAPPING
		
		//Set the piece position only at the end so I can calculate the position of the piece to remove
		mMatrix[newPos.getRow()][newPos.getCol()].setPosition(newPos);
	}
	
	public String toString(){
		String s="";
		for(Piece[] rows: mMatrix){
			for(Piece cell:rows){
				s+=(cell!=null)?String.format("%s", ""+cell):String.format("%17s", " ");
			}
			s+="\n";
		}
		return s;
	}
}
