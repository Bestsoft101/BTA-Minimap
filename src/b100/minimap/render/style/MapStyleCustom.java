package b100.minimap.render.style;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import b100.minimap.Minimap;

public class MapStyleCustom implements MapStyle {
	
	public boolean round;
	
	public MapStyleCustom(boolean round) {
		this.round = round;
	}
	
	public InputStream getInternalResource(String path) {
		InputStream stream = Minimap.class.getResourceAsStream(path);
		if(stream == null) {
			throw new NullPointerException("Resource doesn't exist: '"+path+"'!");
		}
		return stream;
	}
	
	public InputStream getResource(String path) {
		return Minimap.instance.minecraftHelper.getResource(path);
	}
	
	public BufferedImage getImage(String path) {
		InputStream stream = null;
		try {
			stream = getResource(path);
			return ImageIO.read(stream);
		}catch (Exception e) {
			throw new RuntimeException("Error reading image: '"+path+"'", e);
		}finally {
			try {
				stream.close();
			}catch (Exception e) {}
		}
	}
	
	@Override
	public BufferedImage getMapTexture() {
		if(round) {
			return getImage("/minimap/roundmap.png");
		}else {
			return getImage("/minimap/squaremap.png");	
		}
	}

	@Override
	public BufferedImage getMaskTexture() {
		if(round) {
			return getImage("/minimap/roundmap_mask.png");
		}else {
			return getImage("/minimap/squaremap_mask.png");	
		}
	}

	@Override
	public boolean isRound() {
		return round;
	}

	@Override
	public boolean useLinearFiltering() {
		return false;
	}

	@Override
	public void closeStreams() {
		
	}
	
}
