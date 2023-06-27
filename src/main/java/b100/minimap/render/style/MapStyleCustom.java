package b100.minimap.render.style;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import b100.minimap.Minimap;

public class MapStyleCustom implements MapStyle {
	
	public boolean round;
	
	public final File texturePackFile;
	
	private ZipFile zipFile;
	
	public MapStyleCustom(boolean round) {
		this.round = round;
		this.texturePackFile = Minimap.instance.minecraftHelper.getCurrentTexturePackFile();
	}
	
	public InputStream getInternalResource(String path) {
		InputStream stream = Minimap.class.getResourceAsStream(path);
		if(stream == null) {
			throw new NullPointerException("Resource doesn't exist: '"+path+"'!");
		}
		return stream;
	}
	
	public InputStream getResource(String path) {
		if(texturePackFile == null) {
			return getInternalResource(path);
		}
		try {
			if(texturePackFile.isDirectory()) {
				File file = new File(texturePackFile, path);
				if(file.exists()) {
					try{
						return new FileInputStream(file);
					}catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}	
				}else {
					return getInternalResource(path);
				}
			}else {
				if(this.zipFile == null) {
					this.zipFile = new ZipFile(texturePackFile);	
				}
				String zipPath = path;
				if(zipPath.startsWith("/")) {
					zipPath = zipPath.substring(1);
				}
				ZipEntry entry = zipFile.getEntry(zipPath);
				if(entry == null) {
					return getInternalResource(path);
				}
				return zipFile.getInputStream(entry);
			}
		}catch (Exception e) {
			throw new RuntimeException("Error getting input stream '"+path+"'", e);
		}
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
		try {
			this.zipFile.close();
			this.zipFile = null;
		}catch (Exception e) {}
	}
	
}
