package b100.minimap.render.style;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import b100.minimap.Minimap;

public class MapStyleInternal implements MapStyle {

	private String path;
	private boolean round;
	private boolean filter;
	
	public MapStyleInternal(String path, boolean round, boolean filter) {
		this.path = path;
		this.round = round;
		this.filter = filter;
	}
	
	@Override
	public BufferedImage getMapTexture() {
		if(round) {
			return getImage(path + "roundmap.png");
		}else {
			return getImage(path + "squaremap.png");
		}
	}

	@Override
	public BufferedImage getMaskTexture() {
		if(round) {
			return getImage(path + "roundmap_mask.png");
		}else {
			return getImage(path + "squaremap_mask.png");
		}
	}

	@Override
	public boolean isRound() {
		return round;
	}
	
	private BufferedImage getImage(String path) {
		return readImage(Minimap.class.getResourceAsStream(path));
	}
	
	public static BufferedImage readImage(InputStream stream) {
		try {
			return ImageIO.read(stream);
		}catch (Exception e) {
			return null;
		}finally {
			try {
				stream.close();
			}catch (Exception e) {}
		}
	}

	@Override
	public boolean useLinearFiltering() {
		return filter;
	}

}
