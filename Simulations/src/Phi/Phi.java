package Phi;

import java.awt.Color;
import java.util.Random;

public class Phi extends Render {

	private float domainA, domainB, pixelValue, spacingX, spacingY, range;
	private float[] x;
	private int r = 0, g = 0, b = 0, animateTimer;

	public Phi(int width, int height, float domainA, float domainB) {
		super(width, height);
		this.domainA = domainA;
		this.domainB = domainB;
		this.pixelValue = ((domainB - domainA)) / (width * 1.0f);
		spacingX = (width * 1.0f) / (domainB - domainA);
		spacingY = (height * 1.0f) / 10.0f;
		range = domainB - domainA;
		x = new float[]{10, 10000f, .001f,1.0f, 2.234f, 4.435f, 1.234f, -1.34f, -200.34f, -1/1.61803398874989484f};
	}
	
	public float f(float x) {
		return 1.0f + 1.0f / x;
	}

	public void render() {
		if(animateTimer % 100 == 0) {
			clear();
			graphLayout();
			graph();
			animate();
			//print();
		}
		animateTimer++;
	}

	public void graphLayout() {
		// axis
		setColor(0);
		drawLine(0, (int) spacingY, width, (int) spacingY);
		drawLine(0, (int) (height - spacingY), width, (int) (height - spacingY));
		for (float x = 0; x < width; x += spacingX) {
			if (!(width / 2 + spacingX > x && x > width / 2 - spacingX)) {
				setColor(0);
			} else {// x == 0
				setColor(16711680);
			}
			drawLine((int) x, (int) (spacingY + spacingX / range), (int) x, (int) (spacingY - spacingX / range));
			drawLine((int) x, (int) (height - spacingY + spacingX / range), (int) x, (int) (height - spacingY - spacingX / range));
		}
	}
	
	public void graph() {
		for(float x = domainA, sampling = 0; x < domainB; x+= pixelValue, sampling++) {
			setColor(colorShifter());
			if(sampling % 1 == 0) {
				float y = f(x);
				drawLine((int)(((x * (float)width) / range + width/2.0f)), (int)spacingY, (int)(((y * (float)width) / range + width/2.0f)), (int)(height - spacingY));	
			}
		}
	}
	
	public void animate() {
		for(int i = 0; i < x.length; i++) {
			setColor(255);
			float y = f(x[i]);
			drawLine((int)(((x[i] * (float)width) / range + width/2.0f)), (int)spacingY, (int)(((y * (float)width) / range + width/2.0f)), (int)(height - spacingY));			
			x[i] = f(x[i]);
		}
	}
	
	public float map(float x, float rangeA, float rangeB) {
		return x * rangeB / rangeA;
	}
	
	public int colorShifter() {	
		r = 250;
		g = 0;
		if(b == 255) {
			b = 0;
		} else {
			b++;
		}
		return new Color(r,g,b).getRGB();
	}
	
	public void print() {
		for(int i = 0; i < x.length; i++) {
			System.out.print(x[i] + " ");
		}
		System.out.println();
	}
	
	public int[] getPixels() {
		return super.pixels;
	}
}