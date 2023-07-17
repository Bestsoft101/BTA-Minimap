package b100.minimap.mc.impl;

import b100.minimap.mc.IDimension;
import net.minecraft.src.Dimension;

public class DimensionWrapper implements IDimension {

	public Dimension dimension;
	
	public DimensionWrapper(Dimension dimension) {
		this.dimension = dimension;
	}
	
	@Override
	public String getDisplayName() {
		return dimension.getName();
	}

	@Override
	public String getId() {
		return String.valueOf(dimension.dimId);
	}

	@Override
	public int compareTo(IDimension o) {
		DimensionWrapper w = (DimensionWrapper) o;
		
		return dimension.dimId - w.dimension.dimId;
	}

}
