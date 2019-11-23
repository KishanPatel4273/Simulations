package DoublePendulum;

import java.awt.Canvas;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class DoublePendulum extends Canvas implements Runnable {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	public static final String TITLE = "Double Pendulum";
	protected float xc, yc, x1, y1, x2, y2, r1, r2, m1, m2, a1, a2, av1, av2, aa1, aa2, g;
	protected ArrayList<Point> points;
	
	private Thread thread;
	private boolean running = false;

	
	public DoublePendulum() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
	
		xc = WIDTH/2;
		yc = HEIGHT/3;
		r1 = 75;
		r2 = 40;
		m1 = 20; 
		m2 = 10;
		a1 = (float)Math.PI / 2;
		a2 = (float)Math.PI / 3;
		g = 1;
		points = new ArrayList<Point>();
	}

	private void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			requestFocus();

			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					// System.out.println(frames + "fps");
					previousTime += 1000;
					frames = 0;
				}

			}
			if (ticked) {
				render();
				frames++;
			}
			render();
			frames++;
		}
	}

	private void tick() {
		x1 = (float) (r1 * Math.sin(a1)) + xc;
		y1 = (float) (r1 * Math.cos(a1)) + yc;
		x2 = (float) (r1 * Math.sin(a2)) + x1;
		y2 = (float) (r1 * Math.cos(a2)) + y1;
		
		points.add(new Point((int) x2, (int) y2));
		
		double num1 = (-g * (2 * m1 + m2) * Math.sin(a1));
		double num2 = (-m2 * g * Math.sin(a1 - 2 * a2));
		double num3 = (-2* Math.sin(a1-a2)*m2);
		double num4 = (av2*av2*r2+av1*av1*r1*Math.cos(a1-a2));
		double num5 = (r1 * (2*m1+m2-m2*Math.cos(2*a1-2*a2)));
		aa1 = (float) ((num1 + num2 + num3*num4) / num5);
		
		num1 = 2 * Math.sin(a1-a2);
		num2 = (av1*av1*r1*(m1+m2));
		num3 = g * (m1 + m2) * Math.cos(a1);
		num4 = av2*av2*r2*m2*Math.cos(a1-a2);
		num5 = r2 * (2*m1+m2-m2*Math.cos(2*a1-2*a2));
		aa2 = (float) ((num1*(num2+num3+num4)) / num5);
		
		av2 += aa2;
		av1 += aa1;
		a1 += av1;
		a2 += av2;
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}		
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.WHITE);
		g.fillOval((int) xc - 10/2,(int) yc - 10/2, 10, 10);
		
		g.drawLine((int)x1, (int)y1, (int)xc, (int)yc);
		g.fillOval((int) (x1 - m1/2),(int) (y1 - m1/2), (int)m1, (int)m1);
	
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
		g.fillOval((int) (x2 - m2/2),(int) (y2 - m2/2), (int)m2, (int)m2);
		
		g.setColor(Color.BLUE);
		for(int i = 0; i < points.size() - 1; i++){
			g.fillRect(points.get(i).x, points.get(i).y, 1, 1);
			g.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
		}
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		DoublePendulum game = new DoublePendulum();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);;
		game.start();
	}	
}