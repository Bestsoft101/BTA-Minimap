package b100.minimap.render;

import net.minecraft.src.Tessellator;

public class RenderHelper {
	
	public void drawRectangle(Tessellator tessellator, double x, double y, double w, double h, float u0, float v0, float u1, float v1, int zLevel) {
		double x0 = x;
		double y0 = y;
		
		double x1 = x + w;
		double y1 = y + h;

		tessellator.addVertexWithUV(x0, y0, zLevel, u0, v0);
		tessellator.addVertexWithUV(x0, y1, zLevel, u0, v1);
		tessellator.addVertexWithUV(x1, y1, zLevel, u1, v1);
		tessellator.addVertexWithUV(x1, y0, zLevel, u1, v0);
	}
	
	public void drawRectangleInt(Tessellator tessellator, int x, int y, int w, int h, float u0, float v0, float u1, float v1, int zLevel) {
		int x0 = x;
		int y0 = y;
		
		int x1 = x + w;
		int y1 = y + h;

		tessellator.addVertexWithUV(x0, y0, zLevel, u0, v0);
		tessellator.addVertexWithUV(x0, y1, zLevel, u0, v1);
		tessellator.addVertexWithUV(x1, y1, zLevel, u1, v1);
		tessellator.addVertexWithUV(x1, y0, zLevel, u1, v0);
	}
	
	/**
	 * pain 
	 */
	public void drawRotatedRectangle(Tessellator tessellator, double x, double y, double w, double h, float u0, float v0, float u1, float v1, int zLevel, double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		
		double originX = x + w / 2;
		double originY = y + h / 2;
		
		double p0x = x;
		double p0y = y;
		
		double p1x = x;
		double p1y = y + h;
		
		double p2x = x + w;
		double p2y = y + w;
		
		double p3x = x + w;
		double p3y = y;
		
		p0x -= originX;
		p0y -= originY;
		
		p1x -= originX;
		p1y -= originY;
		
		p2x -= originX;
		p2y -= originY;
		
		p3x -= originX;
		p3y -= originY;
		
		double p0xNew = p0x * sin - p0y * cos;
		double p0yNew = p0x * cos + p0y * sin;
		
		double p1xNew = p1x * sin - p1y * cos;
		double p1yNew = p1x * cos + p1y * sin;
		
		double p2xNew = p2x * sin - p2y * cos;
		double p2yNew = p2x * cos + p2y * sin;
		
		double p3xNew = p3x * sin - p3y * cos;
		double p3yNew = p3x * cos + p3y * sin;
		
		p0x = p0xNew + originX;
		p0y = p0yNew + originY;
		
		p1x = p1xNew + originX;
		p1y = p1yNew + originY;
		
		p2x = p2xNew + originX;
		p2y = p2yNew + originY;
		
		p3x = p3xNew + originX;
		p3y = p3yNew + originY;
		
		tessellator.addVertexWithUV(p0x, p0y, zLevel, u0, v0);
		tessellator.addVertexWithUV(p1x, p1y, zLevel, u0, v1);
		tessellator.addVertexWithUV(p2x, p2y, zLevel, u1, v1);
		tessellator.addVertexWithUV(p3x, p3y, zLevel, u1, v0);
	}

}
