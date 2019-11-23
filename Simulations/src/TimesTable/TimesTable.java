package TimesTable;

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

public class TimesTable extends Canvas implements Runnable {

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;
	public static final String TITLE = "Times Table";
	
	
	private Thread thread;
	private boolean running = false;
	private int Nx, Mx, radius,radiusP, Cx, Cy;
	private ArrayList<Point> points;
	
	public TimesTable(int Nx, int Mx) {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		this.Nx = Nx;
		this.Mx = Mx;
		radius = WIDTH/2;
		radiusP = 5;
		Cx = WIDTH / 2;
		Cy = HEIGHT / 2;
		points = new ArrayList<Point>();
		float angle = (float) (2 * Math.PI / Nx);
		for(int i = 0; i < Nx; i++){
			Point temp = new Point((int) (Cx + (radius * Math.cos(Math.PI - angle)) - radiusP),(int) (Cy + (radius * Math.sin(Math.PI - angle)) - radiusP)); 
			points.add(temp);
			angle += (float) (2 * Math.PI / Nx);
		}
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
	int time;
	private void tick() {
		time++;
		if(time%20 == 0){
			//Mx++;
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}		
		Graphics g = bs.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.black);
		g.fillOval(Cx - radius, Cy - radius, radius*2, radius*2);
		
		g.setColor(Color.blue);
		for(int i = 0; i < Nx; i++){
			g.fillOval(points.get(i).x , points.get(i).y, radiusP*2, radiusP*2);
		}
		for(int i = 0; i < Nx; i++){
			g.drawLine(points.get(i).x, points.get(i).y, points.get((i * Mx)%Nx).x, points.get((i * Mx)%Nx).y);
		}
		
		g.setColor(Color.black);
		g.drawOval(Cx - radius, Cy - radius, radius*2,radius*2);

		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		TimesTable game = new TimesTable(400, 2);
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