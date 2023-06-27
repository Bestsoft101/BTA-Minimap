package b100.minimap.render.style;

import static b100.minimap.utils.Utils.*;
import static java.lang.Math.*;

import java.awt.image.BufferedImage;

public class MapStyleGenerated implements MapStyle {

	public int resolution = 1024;
	public int color;
	public float borderWidth = 8.0f;

	private boolean round;

	public MapStyleGenerated(boolean round, int color) {
		this.round = round;
		this.color = color;
	}

	@Override
	public BufferedImage getMapTexture() {
		int w = resolution;
		int h = resolution;
		
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		if(round) {
			float centerX = w / 2.0f - 0.5f;
			float centerY = h / 2.0f - 0.5f;
			
			float maxDistance = distance(0.0f, 0.0f, centerX, centerY);
			float circleSize = resolution / (4.6f);
			
			for(int x=0; x < w; x++) {
				for(int y=0; y < h; y++) {
					int rgb = color & 0xFFFFFF;
					
					float distance = maxDistance - distance(x, y, centerX, centerY);
					float alpha = borderWidth - abs(distance - circleSize);
					
					int a = clamp((int)(alpha * 255.0f), 0, 255);
					image.setRGB(x, y, (a << 24) | rgb);
				}
			}
		}else {
			float bw = borderWidth * 2.0f;
			
			for(int x=0; x < image.getWidth(); x++) {
				for(int y=0; y < image.getHeight(); y++) {
					int rgb = color & 0xFFFFFF;
					
					float alpha = 0.0f;
					if(x < bw || y < bw || x >= w - bw || y >= h - bw) {
						alpha = 1.0f;
					}
					
					int a = clamp((int)(alpha * 255.0f), 0, 255);
					image.setRGB(x, y, (a << 24) | rgb);
				}
			}
		}
		
		return image;
	}

	@Override
	public BufferedImage getMaskTexture() {
		int w = resolution;
		int h = resolution;
		
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		if(round) {
			float bw = borderWidth / 2.0f;
			
			float centerX = w / 2.0f - 0.5f;
			float centerY = h / 2.0f - 0.5f;
			
			float maxDistance = distance(0.0f, 0.0f, centerX, centerY);
			float circleSize = resolution / 4.6f;
			
			for(int x=0; x < w; x++) {
				for(int y=0; y < h; y++) {
					int rgb = color & 0xFFFFFF;
					
					float distance = maxDistance - distance(x, y, centerX, centerY);
					float alpha = bw - (distance - circleSize);
					
					int a = clamp((int)(alpha * 255.0f), 0, 255);
					image.setRGB(x, y, (a << 24) | rgb);
				}
			}
		}
		
		return image;
	}

	@Override
	public boolean isRound() {
		return round;
	}

	@Override
	public boolean useLinearFiltering() {
		return true;
	}

	@Override
	public void closeStreams() {
		
	}

}
