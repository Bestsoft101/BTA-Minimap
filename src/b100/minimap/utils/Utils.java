package b100.minimap.utils;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import b100.minimap.Minimap;

public abstract class Utils {
	
	public static void setTextureImage(int texture, BufferedImage image, boolean filter, boolean repeat) {
		glBindTexture(GL_TEXTURE_2D, texture);
		
		int w = image.getWidth();
		int h = image.getHeight();
		int size = w * h * 4;
		ByteBuffer buffer = Minimap.instance.minecraftHelper.getBufferWithCapacity(size);
		
		buffer.position(0).limit(size);
		
		for(int i = 0; i < image.getHeight(); i++) {
			for(int j = 0; j < image.getWidth(); j++) {
				int color = image.getRGB(j, i);
				
				byte a = (byte) ((color >> 24) & 0xFF);
				byte r = (byte) ((color >> 16) & 0xFF);
				byte g = (byte) ((color >>  8) & 0xFF);
				byte b = (byte) ((color >>  0) & 0xFF);
				
				buffer.put(r);
				buffer.put(g);
				buffer.put(b);
				buffer.put(a);
			}
		}
		
		buffer.position(0);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		int filterMode = filter ? GL_LINEAR : GL_NEAREST;
		int wrapMode = repeat ? GL_REPEAT : GL_CLAMP;
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filterMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filterMode);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapMode);
	}
	
	public static float distance(float x0, float y0, float x1, float y1) {
		return length(x1 - x0, y1 - y0);
	}
	
	public static float length(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public static int brightnessToColor(float brightness) {
		int r = clamp((int) (brightness * 255.0f), 0, 255);
		
		return 255 << 24 | r << 16 | r << 8 | r;
	}
	
	public static int clamp(int a, int min, int max) {
		if(a > max) return max;
		if(a < min) return min;
		return a;
	}
	
	public static float clamp(float a, float min, float max) {
		if(a > max) return max;
		if(a < min) return min;
		return a;
	}
	
	public static float mix(float a, float b, float c) {
		return a * (1.0f - c) + b * c;
	}
	
	public static int mixColor(int color0, int color1, float f) {
		float a0 = ((color0 >> 24) & 0xFF) / 255.0f;
		float r0 = ((color0 >> 16) & 0xFF) / 255.0f;
		float g0 = ((color0 >>  8) & 0xFF) / 255.0f;
		float b0 = ((color0 >>  0) & 0xFF) / 255.0f;
		
		float a1 = ((color1 >> 24) & 0xFF) / 255.0f;
		float r1 = ((color1 >> 16) & 0xFF) / 255.0f;
		float g1 = ((color1 >>  8) & 0xFF) / 255.0f;
		float b1 = ((color1 >>  0) & 0xFF) / 255.0f;
		
		float a2 = mix(a0, a1, f);
		float r2 = mix(r0, r1, f);
		float g2 = mix(g0, g1, f);
		float b2 = mix(b0, b1, f);

		int a = (int) ((a2) * 255.0f);
		int r = (int) ((r2) * 255.0f);
		int g = (int) ((g2) * 255.0f);
		int b = (int) ((b2) * 255.0f);
		
		a = clamp(a, 0, 255);
		r = clamp(r, 0, 255);
		g = clamp(g, 0, 255);
		b = clamp(b, 0, 255);

		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public static int multiplyColor(int color0, float brightness) {
		float a0 = ((color0 >> 24) & 0xFF) / 255.0f;
		float r0 = ((color0 >> 16) & 0xFF) / 255.0f;
		float g0 = ((color0 >>  8) & 0xFF) / 255.0f;
		float b0 = ((color0 >>  0) & 0xFF) / 255.0f;
		
		int a = (int) ((a0 * brightness) * 255.0f);
		int r = (int) ((r0 * brightness) * 255.0f);
		int g = (int) ((g0 * brightness) * 255.0f);
		int b = (int) ((b0 * brightness) * 255.0f);
		
		a = clamp(a, 0, 255);
		r = clamp(r, 0, 255);
		g = clamp(g, 0, 255);
		b = clamp(b, 0, 255);
		
		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public static int multiplyColor(int color0, int color1) {
		float a0 = ((color0 >> 24) & 0xFF) / 255.0f;
		float r0 = ((color0 >> 16) & 0xFF) / 255.0f;
		float g0 = ((color0 >>  8) & 0xFF) / 255.0f;
		float b0 = ((color0 >>  0) & 0xFF) / 255.0f;
		
		float a1 = ((color1 >> 24) & 0xFF) / 255.0f;
		float r1 = ((color1 >> 16) & 0xFF) / 255.0f;
		float g1 = ((color1 >>  8) & 0xFF) / 255.0f;
		float b1 = ((color1 >>  0) & 0xFF) / 255.0f;
		
		int a = (int) ((a0 * a1) * 255.0f);
		int r = (int) ((r0 * r1) * 255.0f);
		int g = (int) ((g0 * g1) * 255.0f);
		int b = (int) ((b0 * b1) * 255.0f);
		
		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public static String getValidFileName(String name) {
		final char seperator = '-';
		
		StringBuilder str = new StringBuilder();
		for(int i=0; i < name.length(); i++) {
			char c = name.charAt(i);
			
			if("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) != -1) {
				c = (char) (c + 32);
				str.append(c);
			}else if("abcdefghijklmnopqrstuvwxyz0123456789".indexOf(c) != -1) {
				str.append(c);
			}else {
				if(str.length() > 0) {
					if(str.charAt(str.length() - 1) != seperator) {
						str.append(seperator);	
					}	
				}
			}
		}
		
		if(str.length() > 0 && str.charAt(str.length() - 1) == seperator) {
			str.deleteCharAt(str.length() - 1);
		}
		if(str.length() == 0) {
			str.append(seperator);
		}
		
		return str.toString();
	}
	
	public static void copyStringToClipboard(String string) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(string), null);
	}
	
	public static String getClipboardString() {
		try {
			return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static int parseColor(String string) {
		if(string.length() > 8) {
			throw new NumberFormatException();
		}
		int color = 0;
		for(int i = string.length() - 1; i >= 0; i--) {
			char c = string.charAt(i);
			int charValue;
			if(c >= '0' && c <= '9') {
				charValue = c - '0';
			}else if(c >= 'a' && c <= 'f') {
				charValue = c - 'a' + 10;
			}else if(c >= 'A' && c <= 'F') {
				charValue = c - 'A' + 10;
			}else {
				throw new NumberFormatException("Invalid character '"+c+"' at index "+i+"!");
			}
			color |= charValue << ((string.length() - i - 1) << 2);
		}
		return color;
	}
	
	public static String toColorString(int color, boolean includeAlpha) {
		final String hexString = "0123456789abcdef";
		StringBuilder builder = new StringBuilder();
		
		final int start = includeAlpha ? 0 : 2;
		for(int i = start; i < 8; i++) {
			int j = (color >> (7 - i) * 4) & 0xF;
			
			builder.append(hexString.charAt(j));
		}
		
		return builder.toString();
	}

}
