package GUI;

import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel{
	
	public Main() {
		
	}
	
	public void run() {
		while(true) {
			repaint();
		}
	}
	
	public void paint(Graphics g) {
		render(g);
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.drawRect(0, 0, 190, 545);
	}
		
	public static void main(String[] args) {
		Main main = new Main();
		JFrame frame = new JFrame("TITLE");
		frame.add(main);
		frame.pack();
		frame.setSize(800, 600);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}

}
