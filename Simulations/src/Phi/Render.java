package Phi;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Render {
	
	protected int width, height, color;
	protected int[] pixels;
	
	public Render(int width, int height){
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 16777215;
		}
		color = 0;
	}
	
	public void drawPoint(int x, int y, int color){
		if(x > 0 && x < width && y > 0 && y < height){
			pixels[x + y * width] = color;
		}
	}
	
	/**
	 * @return from (x1, y1) to (x2, y2) draws line
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		if(x1 != x2){
			float slope = ((float)(y1 - y2) / (x1 - x2));
			for(int x = Math.min(x1, x2); x < Math.max(x1, x2); x++){
				int y = (int) ((float)(slope * (x - x1) + y1)); 
				if(x > 0 && x < width && y > 0 && y < height){
					pixels[x + y * width] = color;
				}
			}
			for(int y = Math.min(y1, y2); y < Math.max(y1, y2); y++){
				int x = (int) ((float)((y - y1) / slope)) + x1; 
				if(x > 0 && x < width && y > 0 && y < height){
					pixels[x + y * width] = color;
				}
			}
		} else {
			for(int y = Math.min(y1, y2); y < Math.max(y1, y2); y++){
				if(x1 > 0 && x1 < width && y > 0 && y < height){
					pixels[x1 + y * width] = color;
				}
			}
		}
	}
	
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3){
		if(x1 != x2){
			float slope = ((float)(y1 - y2) / (x1 - x2));
				for(int x = Math.min(x1, x2); x < Math.max(x1, x2); x++){
					int y = (int) ((float)(slope * (x - x1) + y1)); 
					if(x > 0 && x < width && y > 0 && y < height){
						drawLine(x,y, x3,y3);
					}
				}
			for(int y = Math.min(y1, y2); y < Math.max(y1, y2); y++){
				int x = (int) ((float)((y - y1) / slope)) + x1; 
				if(x > 0 && x < width && y > 0 && y < height){
					drawLine(x,y, x3,y3);
				}
			}
		} else {
			for(int y = Math.min(y1, y2); y < Math.max(y1, y2); y++){
				if(x1 > 0 && x1 < width && y > 0 && y < height){
					drawLine(x1,y, x3,y3);
				}
			}
		}
	}
	
	public void fillTriangle(float[] xx, float[] yy){
		if(xx.length == 3 && yy.length == 3){
			fillTriangle((int)xx[0],(int)yy[0],(int)xx[1],(int)yy[1],(int)xx[2],(int)yy[2]);
		}
		
	}
	
	public void drawRectangle(int x1, int y1, int x2, int y2){
		drawLine(x1,y1, x1,y2);
		drawLine(x1,y1, x2,y1);
		drawLine(x2,y1, x2,y2);
		drawLine(x1,y2, x2+1,y2);
	}
	
	public void fillRectangle(int x1, int y1, int x2, int y2){
		for(int x = Math.min(x1, x2); x < Math.max(x1, x2); x++){
			drawLine(x, y1,x, y2);
		}
	}
	
	/**
	 * @param x has length of 3 x-components of the triangle
	 * @param y has length of 3 y-components of the triangle
	 */
	public void drawTriangle(float[] x, float[] y){
		if(x.length == 3 && y.length == 3){
			drawLine((int) x[0], (int) y[0], (int) x[1], (int) y[1]);
			drawLine((int) x[2], (int) y[2], (int) x[1], (int) y[1]);
			drawLine((int) x[0], (int) y[0], (int) x[2], (int) y[2]);
		}
	}
	
	public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3){
		drawTriangle(new float[]{x1,x2,x3},new float[]{y1,y2,y3});
	}
	
	public void drawEllipse(int centerX, int centerY, int a, int b){
		for(int x = centerX - a; x < centerX + a; x++){
			int y = (int) (float)(Math.sqrt((float)b*b * (1f - (float)((x - centerX)*(x - centerX))/(a*a))) + centerY);
			if(x > 0 && x < width && y > 0 && y < height){
				pixels[x + y * width] = color;
			}
			y = (int) (float)(-Math.sqrt((float)b*b * (1f - (float)((x - centerX)*(x - centerX))/(a*a))) + centerY);
			if(x > 0 && x < width && y > 0 && y < height){
				pixels[x + y * width] = color;
			}
		}
		for(int y = centerY - b; y < centerY + b; y++){
			int x = (int) ((float)(Math.sqrt((float)a*a * (1f - (float)(y - centerY)*(y - centerY)/(b*b)))) + centerX);
			if(x > 0 && x < width && y > 0 && y < height){
				pixels[x + y * width] = color;
			}
			x = (int) ((float)(-Math.sqrt((float)a*a * (1f - (float)(y - centerY)*(y - centerY)/(b*b)))) + centerX);
			if(x > 0 && x < width && y > 0 && y < height){
				pixels[x + y * width] = color;
			}
		}
	}
	
	public float lineY(float x, float x1, float y1, float x2, float y2){
		if(x1 != x2){
			return ((float)(((float)(y1 - y2) / (x1 - x2)) * (x - x1) + y1)); 
		} else {
			return x1;
		}
	}
	
	public float lineX(float y, float x1, float y1, float x2, float y2){
		if(x1 != x2){
			return (int) ((float)((y - y1) / ((float)(y1 - y2) / (x1 - x2)))) + x1; 
		} else {
			return x1;
		}
	}
	
	public float distance(float x1, float y1, float x2, float y2){
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	
	public void addImage(BufferedImage a, int offsetX, int offSetY){
		int[] image = getPixelArray(a);
		for(int x = 0; x < a.getWidth(); x++){
			for(int y = 0; y < a.getHeight(); y++){
				pixels[(x + offsetX) + (y + offSetY) * width] = image[x + y * a.getWidth()];
			}
		}
	}
	
	public static int[] getPixelArray(BufferedImage a){
		int[] imagePixels = new int[a.getHeight() * a.getWidth()];
		for(int r = 0; r < a.getWidth(); r++){
			for(int c = 0;c < a.getHeight(); c++){
				imagePixels[c + r * a.getWidth()] = a.getRGB(c, r);
			}
		}
		return imagePixels;
	}
	
	public static Color getINRGBTORGB(int intRGB) {
		int r = (intRGB >> 16) &  0xFF;
		int g = (intRGB >> 8) & 0xFF;
		int b = (intRGB >> 0) & 0xFF;
		return new Color(r,g,b);
	}
	
	
	public int getRandom(int a, int b){
		return (int) ((b-a + 1) * Math.random() + a);
	}
	
	public void clear(){
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = 16777215;
		}
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
}