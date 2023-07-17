package b100.minimap.gui;

import java.util.ArrayList;
import java.util.List;

import b100.minimap.config.Option;

public abstract class GuiOptionButton<E> extends GuiButton {

	public Option<E> option;
	
	public List<OptionListener> optionListeners = new ArrayList<>();
	
	public GuiOptionButton(GuiScreen screen, Option<E> option) {
		super(screen);
		this.option = option;
		
		if(option == null) {
			throw new NullPointerException();
		}
				
		updateText();
	}
	
	public void updateText() {
		this.text = getOptionValueString();
	}
	
	public String getOptionValueString() {
		return option.value.toString();
	}
	
	public void onOptionValueChanged() {
		updateText();
		
		try {
			for(int i=0; i < optionListeners.size(); i++) {
				optionListeners.get(i).onOptionValueChanged(option);
			}
		}catch (CancelEventException e) {}
	}
	
	public GuiOptionButton<E> addOptionListener(OptionListener optionListener) {
		optionListeners.add(optionListener);
		return this;
	}
	
	public GuiOptionButton<E> removeOptionListener(OptionListener optionListener) {
		optionListeners.remove(optionListener);
		return this;
	}

}
