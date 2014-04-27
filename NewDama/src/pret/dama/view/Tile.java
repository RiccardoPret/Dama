package pret.dama.view;

import java.awt.Color;
import javax.swing.JButton;
import pret.dama.model.Position;

public abstract class Tile extends JButton{
	
	private Position mPosition;
	
	public Tile(Color backgroundColor, Position p){
		this.setBackground(backgroundColor);
		this.mPosition=p;
		this.setBorder(null);
		this.setFocusPainted(false);
	}
	
	public Position getPosition(){
		return mPosition;
	}
	
}
