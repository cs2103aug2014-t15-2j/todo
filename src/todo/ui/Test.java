package todo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Test extends JFrame {


	private JPanel jp;
	private JButton jb;
	private JScrollPane jsp;
	
	public static void main(String[] args) {
		new Test();

	}
	
	public Test() {

		
		
		jp = new JPanel(new GridLayout(100,1));
		//我偷懒直接上限100个了哈
				
		jp.setBackground(Color.GREEN);
		jsp = new JScrollPane(jp);

		jb = new JButton();
		jb.setText("baka fish");
		jb.setPreferredSize(new Dimension(300, 100));
		jb.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JPanel temp = new JPanel();
				temp.setBackground(Color.blue);
				temp.setBorder(BorderFactory.createTitledBorder("喵喵喵"));
				temp.setPreferredSize(new Dimension(350, 50));
				jp.add(temp);
				
//				jp.repaint();
				jp.revalidate();
//				jp.updateUI();
//				
			}
		});
 

		this.add(jsp, BorderLayout.CENTER);
		this.add(jb, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 600);
		this.setVisible(true);
		this.setTitle("哦呵呵呵呵");
		setLocationRelativeTo(null);
		
	}

}

