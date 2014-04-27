package pret.dama.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class SelectColor{
	
	private int color;
	
	public SelectColor(){
		color=chooser();
	}

	public int color(){
		return color;
	}
	
	public int chooser(){
		Object[] options = {"Bianche", "Nere"};
		return JOptionPane.showOptionDialog(new JFrame(), "Scegli il colore delle tue pedine",
				"Colore Pedine", JOptionPane.YES_NO_OPTION,	JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title
	}
}
