package pret.dama.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import pret.dama.model.Position;

public class PieceTile extends Tile {
	private boolean isWhite;
	
	public PieceTile(Color backgroundColor, Position p, boolean IsAWhitePiece){
		super(backgroundColor, p);
		isWhite=IsAWhitePiece;
		if(isWhite==true)
			this.setIcon(new ImageIcon("white.png"));
		else
			this.setIcon(new ImageIcon("black.png"));
	}

	public void select(){
		this.setBackground(Color.decode("#5ae97c"));
	}
	
	public void toDama() {
		// TODO Auto-generated method stub
		if(isWhite==true)
			this.setIcon(new ImageIcon("whiteboss.png"));
		else
			this.setIcon(new ImageIcon("blackboss.png"));
	}
	
	public void canMoved(){
		this.setBorder(BorderFactory.createLineBorder(Color.decode("#5ae97c"), 2));
	}
}
