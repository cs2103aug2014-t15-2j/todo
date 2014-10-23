package todo.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUInew extends JFrame{
	JLabel jl1, jl2, jl3;
	JPanel jp1, jp2, jp3;

	public GUInew(){

		//testComps.setAlignment(FlowLayout.TRAILING);
		
		setTitle("JustDidIt");
		setSize(700, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
		
		jl1 = new JLabel("JLabel1");
		jl2 = new JLabel("JLable2");
		jl3 = new JLabel("JLable3");
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		
		jp1.add(jl1);
		jp2.add(jl2);
		jp3.add(jl3);
		
		add(jp1, BorderLayout.NORTH);
		add(jp2, BorderLayout.CENTER);
		add(jp3, BorderLayout.SOUTH);

	}
	
	public static void main(String[] ars){
		GUInew gui = new GUInew();
	
	}
}
