package pret.dama.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import pret.dama.controller.AI;
import pret.dama.controller.Move;
import pret.dama.model.Board;
import pret.dama.model.Piece;
import pret.dama.model.Position;

public class Game extends JFrame{

	private final int HEIGHT=600;
	private final int WIDTH=600;
	private Board mBoard;
	private boolean mOnSelected=false;	//Tells if a tile is selected
	private boolean mUnderMultipleEaten=false;	//Tells if a piece is under multiple eaten
	private Piece mCurrentPiece;	//Contain the piece selected
	private Move mThisMove;			//Contain the current move
	private AI mIntelligence;
	
	public Game(Board board){
		super("La Dama Italiana");
		mBoard=board;
		mIntelligence=new AI(mBoard);
		
		this.setLayout(new GridLayout(8, 8));		

		initMatrixPosition();
		voidPaint();
		
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);	//center the Game
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		//If the user is black, the computer will start
		if(!mBoard.userIsWhite())
			execAi();
	}
	
	private Color[] col={Color.decode("#aa7e44"), Color.WHITE};
	private Position[][] mMatrixPosition= new Position[8][8];
	
	private void initMatrixPosition() {
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				mMatrixPosition[i][j]= new Position(i, j);
	}
	
	private void voidPaint(){
		PieceTile pt;

		for(int i=0;i<8; i++)
			for(int j=0; j<8; j++){
				if(mBoard.isCellEmpty(mMatrixPosition[i][j]))
					this.addToBoard(new EmptyTile(col[(j+i)%2], new Position(i,j)));
				else{
					pt=new PieceTile(col[(j+i)%2], new Position(i,j), mBoard.isOwnedByUserAt(mMatrixPosition[i][j])==mBoard.userIsWhite());
					if(mBoard.isADamaAt(mMatrixPosition[i][j]))
						pt.toDama();
					if(mBoard.getPieceAt(mMatrixPosition[i][j]).isSelectable())
						pt.canMoved();
					addToBoard(pt);
				}
			}
	}
	
	private void finalPaint(int winner){
		String msg=winner>0?"Complimenti hai vinto!":"Complimenti hai perso!";
		getContentPane().removeAll();
		
		JLabel j=new JLabel(msg, SwingConstants.CENTER);
		j.setSize(WIDTH, HEIGHT);
		j.setFont(new Font("Arial", Font.PLAIN, 50));
		this.add(j);
		
		invalidate();
		validate();
	}
	
	private void reDraw(){
		getContentPane().removeAll();
		paintScreen();
		invalidate();
		validate();
	}
	
	private void execAi() {
		mIntelligence.getTheStep();
		
		try {
			TimeUnit.MILLISECONDS.sleep(120);
		}catch(InterruptedException ex) {
		    System.out.println(ex);
		}
		
		if(mBoard.whoWin()==0){
			mBoard.changeTurn();
			getContentPane().removeAll();
			voidPaint();
			invalidate();
			validate();
		}else
			finalPaint(mBoard.whoWin());
	}
	
	private void addToBoard(final Tile tile){
		this.add(tile);
		tile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//update the current piece only if I pressed on a Piece Tile and there isn't a multiple eaten in progress
				if(!mBoard.isCellEmpty(tile.getPosition()) && !mUnderMultipleEaten){
					mCurrentPiece=mBoard.getPieceAt(tile.getPosition());

					//Select the piece only if it's selectable
					if(mCurrentPiece.isSelectable())
						mOnSelected=true;
					else{
						mOnSelected=false;
						mCurrentPiece=null;
					}
					reDraw();

				}else if(mOnSelected){
					mThisMove= new Move(mCurrentPiece, tile.getPosition());
					if(mThisMove.isLegal()){
						mThisMove.exec();

						//Check if there is a possibility to have multiple eaten. If there is, enable the mUnderMultipleEaten "flag"
						if(!mThisMove.isAnEat() || !mCurrentPiece.couldEat()){
							mBoard.changeTurn();
							mOnSelected=false;
							mCurrentPiece=null;
							mUnderMultipleEaten=false;
						}else
							mUnderMultipleEaten=true;

						//Check if the game is ended
						if(mBoard.whoWin()==0){
							reDraw();
							if(!mUnderMultipleEaten)
								execAi();	//If the user is not under multiple eaten, the Opponent do its moves
						}else
							finalPaint(mBoard.whoWin());	//the match is ended. Print the Final Screen
					}
				}
			}//end actionPerformed method
		});//end Actionlistener implementation
	}
	
	private void paintScreen(){
		PieceTile pt;
		System.out.println(mCurrentPiece);
		if(mCurrentPiece!=null){		
			for(int i=0;i<8; i++)
				for(int j=0; j<8; j++){
					if(mBoard.isCellEmpty(mMatrixPosition[i][j])){
						if(mCurrentPiece.getLegalPositions(mBoard).contains(mMatrixPosition[i][j]))
							this.addToBoard(new EmptyTile(Color.decode("#e95a5a"), new Position(i,j)));
						else
							this.addToBoard(new EmptyTile(col[(j+i)%2], new Position(i,j)));
					}else{
						pt=new PieceTile(col[(j+i)%2], new Position(i,j), mBoard.isOwnedByUserAt(mMatrixPosition[i][j])==mBoard.userIsWhite());
						//green if it is the position of the currentPiece
						if(mCurrentPiece.getPosition().equals(mMatrixPosition[i][j]))
							pt.select();
						if(mBoard.isADamaAt(mMatrixPosition[i][j]))
							pt.toDama();
						//If a piece is selectable and there isn't multiple eaten in progress, check it "movable"
						if(mBoard.getPieceAt(mMatrixPosition[i][j]).isSelectable() && !mUnderMultipleEaten)
							pt.canMoved();
						addToBoard(pt);
					}
				}
		}else
			voidPaint();
	}
	
	public static void main(String[] args) {
		
		int color=new SelectColor().color();
		boolean userIsWhite=color==0?true:false;
		
		if(color>-1)
			new Game(new Board(userIsWhite));	//if userIsWhite is true the game is started by the user
		else
			System.exit(ERROR);
	}
}
