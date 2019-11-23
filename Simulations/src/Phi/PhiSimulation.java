package Phi;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class PhiSimulation extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	static final int SCALE = 100;
	public static final int WIDTH = 16 * SCALE;
	public static final int HEIGHT = 2 * SCALE;
	public static final String TITLE = "Phi Simulation";

	private Thread thread;

	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	public Phi phi;

	public PhiSimulation() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);	
		setMaximumSize(size);

		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		phi = new Phi(WIDTH, HEIGHT, -5, 5);
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
					//System.out.println(frames + "fps");
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
		phi.render();
	}
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
			
		g.drawImage(img, 0, 0, null);
	
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = phi.getPixels()[i];
		}
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		PhiSimulation game = new PhiSimulation();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		game.start();
	}
}
