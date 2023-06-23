package b100.minimap.render.style;

import java.awt.image.BufferedImage;

public interface MapStyle {
	
	public BufferedImage getMapTexture();
	
	public BufferedImage getMaskTexture();
	
	public boolean isRound();
	
	public boolean useLinearFiltering();
	
}
