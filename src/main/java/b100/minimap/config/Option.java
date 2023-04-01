package b100.minimap.config;

public abstract class Option<E> {
	
	public final String name;
	
	public E value;
	
	public Option(String name, E value) {
		if(name == null) {
			throw new NullPointerException();
		}
		
		this.name = name;
		this.value = value;
	}
	
	public abstract E parse(String string);
	
	public String getValueString() {
		return String.valueOf(value);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
