package b100.minimap.gui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import b100.minimap.Minimap;
import b100.minimap.gui.GuiNavigationContainer.Position;
import b100.minimap.gui.GuiTextElement.Align;
import b100.minimap.utils.Utils;
import net.minecraft.src.GLAllocation;

public class GuiColorSelectScreen extends GuiScreen implements TextComponentListener {
	
	protected int colorPickerTexture1;
	protected int colorPickerTexture2;
	
	protected GuiColorBrightnessSaturationElement colorElement1;
	protected GuiColorHueElement colorElement2;
	protected GuiColorPreviewElement colorPreviewElement;
	
	protected final int previousColor;
	
	protected int color;
	
	protected int red;		// 0 - 255
	protected int green;	// 0 - 255
	protected int blue;		// 0 - 255
	
	protected float hue;			// 0.0f - 1.0f
	protected float saturation;		// 0.0f - 1.0f
	protected float brightness;		// 0.0f - 1.0f
	
	protected float[] hsb = new float[3];
	
	public GuiContainerBox container;
	
	public GuiNavigationContainer navTop;
	public GuiNavigationContainer navBottom;
	
	public List<ColorListener> colorListeners = new ArrayList<>();
	
	public String title = "Choose Color";
	
	public GuiTextElement textRed;
	public GuiTextElement textGreen;
	public GuiTextElement textBlue;
	
	public GuiTextElement textHue;
	public GuiTextElement textSat;
	public GuiTextElement textVal;
	
	public GuiTextElement textHex;
	
	public GuiTextComponentInteger textComponentRed;
	public GuiTextComponentInteger textComponentGreen;
	public GuiTextComponentInteger textComponentBlue;
	
	public GuiTextComponentInteger textComponentHue;
	public GuiTextComponentInteger textComponentSat;
	public GuiTextComponentInteger textComponentVal;
	
	public GuiTextComponentColor textComponentHex;
	
	public GuiTextField inputRed;
	public GuiTextField inputGreen;
	public GuiTextField inputBlue;
	
	public GuiTextField inputHue;
	public GuiTextField inputSat;
	public GuiTextField inputVal;
	
	public GuiTextField inputHex;
	
	private boolean updatingColor = false;
	
	public GuiColorSelectScreen(GuiScreen parentScreen, int color, ColorListener colorListener) {
		super(parentScreen);
		colorListeners.add(colorListener);
		
		this.previousColor = this.color = color;
		
		this.red = color >> 16 & 0xFF;
		this.green = color >> 8 & 0xFF;
		this.blue = color & 0xFF;
		
		Color.RGBtoHSB(red, green, blue, hsb);
		
		this.hue = hsb[0];
		this.saturation = hsb[1];
		this.brightness = hsb[2];
	}

	@Override
	public void onInit() {
		container = add(new GuiContainerBox());
		
		colorElement1 = add(new GuiColorBrightnessSaturationElement(this));
		colorElement2 = add(new GuiColorHueElement(this));
		colorPreviewElement = add(new GuiColorPreviewElement(this));
		
		navTop = add(new GuiNavigationContainer(this, container, Position.TOP));
		navBottom = add(new GuiNavigationContainer(this, container, Position.BOTTOM));

		navTop.add(new GuiButtonNavigation(this, title, container));
		navBottom.add(new GuiButtonNavigation(this, "Cancel", container).addActionListener((e) -> back()));
		navBottom.add(new GuiButtonNavigation(this, "OK", container).addActionListener((e) -> ok()));
		
		textRed = add(new GuiTextElement("R", Align.CENTER, Align.CENTER));
		textGreen = add(new GuiTextElement("G", Align.CENTER, Align.CENTER));
		textBlue = add(new GuiTextElement("B", Align.CENTER, Align.CENTER));
		
		textHue = add(new GuiTextElement("H", Align.CENTER, Align.CENTER));
		textSat = add(new GuiTextElement("S", Align.CENTER, Align.CENTER));
		textVal = add(new GuiTextElement("V", Align.CENTER, Align.CENTER));
		
		textHex = add(new GuiTextElement("#", Align.CENTER, Align.CENTER));
		
		textComponentRed = (GuiTextComponentInteger) new GuiTextComponentInteger(0, 0, 255).addTextComponentListener(this);
		textComponentGreen = (GuiTextComponentInteger) new GuiTextComponentInteger(0, 0, 255).addTextComponentListener(this);
		textComponentBlue = (GuiTextComponentInteger) new GuiTextComponentInteger(0, 0, 255).addTextComponentListener(this);
		
		textComponentHue = (GuiTextComponentInteger) new GuiTextComponentInteger(0, 0, 360).addTextComponentListener(this);
		textComponentSat = (GuiTextComponentInteger) new GuiTextComponentInteger(0, 0, 100).addTextComponentListener(this);
		textComponentVal = (GuiTextComponentInteger) new GuiTextComponentInteger(0, 0, 100).addTextComponentListener(this);
		
		textComponentHex = (GuiTextComponentColor) new GuiTextComponentColor(0, false).addTextComponentListener(this);
		
		inputRed = add(new GuiTextField(this, textComponentRed));
		inputGreen = add(new GuiTextField(this, textComponentGreen));
		inputBlue = add(new GuiTextField(this, textComponentBlue));
		
		inputHue = add(new GuiTextField(this, textComponentHue));
		inputSat = add(new GuiTextField(this, textComponentSat));
		inputVal = add(new GuiTextField(this, textComponentVal));
		
		inputHex = add(new GuiTextField(this, textComponentHex));
		
		updateColorUiElements(true, true, true, true, true);
	}
	
	public void setColorFromHSV() {
		this.color = Color.HSBtoRGB(hue, saturation, brightness);
		this.red = color >> 16 & 0xFF;
		this.green = color >> 8 & 0xFF;
		this.blue = color & 0xFF;
	}
	
	public void setColorFromRGB() {
		this.red = Utils.clamp(red, 0, 255);
		this.green = Utils.clamp(green, 0, 255);
		this.blue = Utils.clamp(blue, 0, 255);
		this.color = red << 16 | green << 8 | blue;
		Color.RGBtoHSB(red, green, blue, hsb);
		this.hue = hsb[0];
		this.saturation = hsb[1];
		this.brightness = hsb[2];
	}
	
	public void setColorFromARGB() {
		this.red = this.color >> 16 & 0xFF;
		this.green = this.color >> 8 & 0xFF;
		this.blue = this.color & 0xFF;
		Color.RGBtoHSB(red, green, blue, hsb);
		this.hue = hsb[0];
		this.saturation = hsb[1];
		this.brightness = hsb[2];
	}
	
	public void updateColorUiElements(boolean updateRgbInput, boolean updateHsvInput, boolean updateBrightnessSaturationPicker, boolean updateHuePicker, boolean updateHexInput) {
		updatingColor = true;
		if(updateRgbInput) {
			textComponentRed.setValue(red);
			textComponentGreen.setValue(green);
			textComponentBlue.setValue(blue);
		}
		if(updateHsvInput) {
			textComponentHue.setValue((int) (hue * 360));
			textComponentSat.setValue((int) (saturation * 100));
			textComponentVal.setValue((int) (brightness * 100));
		}
		if(updateBrightnessSaturationPicker || updateHuePicker) {
			updateColorPickerTextures();
		}
		if(updateHexInput) {
			textComponentHex.setColor(color);
		}
		updatingColor = false;
	}
	
	@Override
	public void onResize() {
		final int paddingOuter = 3;
		final int paddingInner = 3;

		int size1 = 128;	// Width and height of the brightness and saturation picker, and also the area right of the hue picker
		int size2 = 8;		// Width of the hue picker
		int size3 = 32;		// Width and height of the preview element
		
		int innerWidth = size1 * 2 + size2 + 2 * paddingInner;
		int innerHeight = size1;
		
		int width = innerWidth + 2 * paddingOuter;
		int height = innerHeight + 2 * paddingOuter;
		
		container.setPosition((this.width - width) / 2, (this.height - height) / 2).setSize(width, height);
		
		int innerPosX = container.posX + paddingOuter;
		int innerPosY = container.posY + paddingOuter;
		
		colorElement1.setPosition(innerPosX, innerPosY).setSize(size1, size1);
		colorElement2.setPosition(innerPosX + size1 + paddingInner, innerPosY).setSize(size2, size1);
		
		int x1 = innerPosX + size1 + size2 + 2 * paddingInner;	// X Position of the right area
		int x2 = x1 + size1 / 2;								// X Position of the second column in the right area
		int y1 = innerPosY + size3 + paddingInner;				// Y Position below the color preview
		
		colorPreviewElement.setPosition(x1 + (size1 - size3) / 2, innerPosY).setSize(size3, size3);
		
		int lineHeight = 10;
		int lineHeightPad = lineHeight + 1;
		int w1 = size1 - lineHeight - paddingInner; 	// Text field width
		
		textHue.setPosition(x1, y1 + 1 * lineHeightPad).setSize(lineHeight, lineHeight);
		textSat.setPosition(x1, y1 + 2 * lineHeightPad).setSize(lineHeight, lineHeight);
		textVal.setPosition(x1, y1 + 3 * lineHeightPad).setSize(lineHeight, lineHeight);
		
		inputHue.setPosition(x1 + lineHeight + paddingInner, y1 + 1 * lineHeightPad).setSize(32, lineHeight);
		inputSat.setPosition(x1 + lineHeight + paddingInner, y1 + 2 * lineHeightPad).setSize(32, lineHeight);
		inputVal.setPosition(x1 + lineHeight + paddingInner, y1 + 3 * lineHeightPad).setSize(32, lineHeight);
		
		textRed.setPosition(x2, y1 + 1 * lineHeightPad).setSize(lineHeight, lineHeight);
		textGreen.setPosition(x2, y1 + 2 * lineHeightPad).setSize(lineHeight, lineHeight);
		textBlue.setPosition(x2, y1 + 3 * lineHeightPad).setSize(lineHeight, lineHeight);
		
		inputRed.setPosition(x2 + lineHeight + paddingInner, y1 + 1 * lineHeightPad).setSize(32, lineHeight);
		inputGreen.setPosition(x2 + lineHeight + paddingInner, y1 + 2 * lineHeightPad).setSize(32, lineHeight);
		inputBlue.setPosition(x2 + lineHeight + paddingInner, y1 + 3 * lineHeightPad).setSize(32, lineHeight);
		
		textHex.setPosition(x1, y1 + 5 * lineHeightPad).setSize(lineHeight, lineHeight);
		inputHex.setPosition(x1 + lineHeight + paddingInner, y1 + 5 * lineHeightPad).setSize(64, lineHeight);
		
		super.onResize();
	}
	
	public void ok() {
		for(int i=0; i < colorListeners.size(); i++) {
			try{
				colorListeners.get(i).onColorChanged(this, color);
			}catch (CancelEventException e) {}
		}
		back();
	}
	
	@Override
	public void onGuiOpened() {
		createTextures();
	}
	
	@Override
	public void onGuiClosed() {
		deleteTextures();
	}
	
	public void updateColorPickerTextures() {
		if(colorPickerTexture1 == 0 || colorPickerTexture2 == 0) {
			Minimap.log("Cannot update gradient texture because texture doesnt exist!");
			return;
		}
		
		final int res = 256;
		final float resMinus1 = res - 1;
		final int size1 = res * res * 4;
		final int size2 = 256 * 4;
		final int sizeTotal = size1 + size2;
		ByteBuffer buffer = minimap.minecraftHelper.getBufferWithCapacity(sizeTotal);
		
		buffer.position(0).limit(size1);
		for(int i=0; i < res; i++) {
			float brightness = 1.0f - i / resMinus1;
			for(int j=0; j < res; j++) {
				float saturation = j / resMinus1;
				
				int color = Color.HSBtoRGB(hue, saturation, brightness);
				
				byte a = (byte) (color >> 24);
				byte r = (byte) (color >> 16);
				byte g = (byte) (color >>  8);
				byte b = (byte) (color >>  0);
				
				buffer.put(r);
				buffer.put(g);
				buffer.put(b);
				buffer.put(a);
			}
		}
		
		buffer.position(size1).limit(sizeTotal);
		for(int i=0; i < 256; i++) {
			int color = Color.HSBtoRGB(1.0f - i / 255.0f, 1.0f, 1.0f);
			
			byte a = (byte) (color >> 24);
			byte r = (byte) (color >> 16);
			byte g = (byte) (color >>  8);
			byte b = (byte) (color >>  0);
			
			buffer.put(r);
			buffer.put(g);
			buffer.put(b);
			buffer.put(a);
		}
		
		boolean filter = false;
		int filterMode = filter ? GL_LINEAR : GL_NEAREST;
		
		buffer.position(0).limit(size1);
		glBindTexture(GL_TEXTURE_2D, colorPickerTexture1);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, res, res, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filterMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filterMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		
		buffer.position(size1).limit(sizeTotal);
		glBindTexture(GL_TEXTURE_2D, colorPickerTexture2);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1, 256, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filterMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filterMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
	}
	
	public void createTextures() {
		if(colorPickerTexture1 == 0) colorPickerTexture1 = GLAllocation.generateTexture();
		if(colorPickerTexture2 == 0) colorPickerTexture2 = GLAllocation.generateTexture();
	}
	
	public void deleteTextures() {
		if(colorPickerTexture1 != 0) {
			glDeleteTextures(colorPickerTexture1);
			GLAllocation.textureNames.remove((Integer) colorPickerTexture1);
			colorPickerTexture1 = 0;
		}
		if(colorPickerTexture2 != 0) {
			glDeleteTextures(colorPickerTexture2);
			GLAllocation.textureNames.remove((Integer) colorPickerTexture2);
			colorPickerTexture2 = 0;
		}
	}
	
	public static abstract class GuiColorElement extends GuiElement {

		public GuiColorSelectScreen screen;
		
		public GuiColorElement(GuiColorSelectScreen screen) {
			this.screen = screen;
		}
		
		protected boolean dragging = false;
		
		@Override
		public void draw(float partialTicks) {
			glEnable(GL_TEXTURE_2D);
			glDisable(GL_BLEND);
			glBindTexture(GL_TEXTURE_2D, getTexture());
			utils.drawTexturedRectangle(posX, posY, width, height, 0.0f, 0.0f, 1.0f, 1.0f, 0xFFFFFFFF);
			glEnable(GL_BLEND);
			
			if(dragging) {
				updateColor(screen.cursorX, screen.cursorY);	
			}
		}
		
		@Override
		public void mouseEvent(int button, boolean pressed, int mouseX, int mouseY) {
			if(pressed && screen.getClickElementAt(mouseX, mouseY) == this) {
				dragging = true;
				updateColor(mouseX, mouseY);
			}
			if(!pressed && dragging) {
				dragging = false;
				updateColor(mouseX, mouseY);
			}
		}
		
		public abstract int getTexture();
		
		public abstract void updateColor(int mouseX, int mouseY);
		
	}
	
	public static class GuiColorBrightnessSaturationElement extends GuiColorElement {

		public GuiColorBrightnessSaturationElement(GuiColorSelectScreen screen) {
			super(screen);
		}
		
		@Override
		public void draw(float partialTicks) {
			super.draw(partialTicks);
			
			int selectionX = (int) (posX + screen.saturation * (width - 1));
			int selectionY = (int) (posY + (1.0f - screen.brightness) * (height - 1));
			
			glDisable(GL_TEXTURE_2D);
			utils.drawRectangle(selectionX, selectionY, 1, 1, 0xFFFFFFFF);
			utils.drawRectangle(selectionX-1, selectionY, 1, 1, 0xFF000000);
			utils.drawRectangle(selectionX+1, selectionY, 1, 1, 0xFF000000);
			utils.drawRectangle(selectionX, selectionY-1, 1, 1, 0xFF000000);
			utils.drawRectangle(selectionX, selectionY+1, 1, 1, 0xFF000000);
		}
		
		@Override
		public int getTexture() {
			return screen.colorPickerTexture1;
		}

		@Override
		public void updateColor(int mouseX, int mouseY) {
			screen.saturation = Utils.clamp((mouseX - posX) / (float) width, 0.0f, 1.0f);
			screen.brightness = 1.0f - Utils.clamp((mouseY - posY) / (float) height, 0.0f, 1.0f);
			screen.setColorFromHSV();
			screen.updateColorUiElements(true, true, false, true, true);
		}
		
	}
	
	public static class GuiColorHueElement extends GuiColorElement {
		
		public GuiColorHueElement(GuiColorSelectScreen screen) {
			super(screen);
		}
		
		@Override
		public void draw(float partialTicks) {
			super.draw(partialTicks);
			
			int selectionY = (int) (posY + (1.0f - screen.hue) * (height - 1));
			
			glDisable(GL_TEXTURE_2D);
			utils.drawRectangle(posX, selectionY, width, 1, 0xFF000000);
		}

		@Override
		public int getTexture() {
			return screen.colorPickerTexture2;
		}

		@Override
		public void updateColor(int mouseX, int mouseY) {
			screen.hue = 1.0f - Utils.clamp((mouseY - posY) / (float) height, 0.0f, 1.0f);
			screen.setColorFromHSV();
			screen.updateColorUiElements(true, true, true, false, true);
		}
		
	}
	
	public static class GuiColorPreviewElement extends GuiElement {

		public GuiColorSelectScreen screen;
		
		public GuiColorPreviewElement(GuiColorSelectScreen screen) {
			this.screen = screen;
		}
		
		@Override
		public void draw(float partialTicks) {
			int heightHalf = height / 2;
			glDisable(GL_TEXTURE_2D);
			utils.drawRectangle(posX, posY, width, heightHalf, screen.color | 0xFF000000);
			utils.drawRectangle(posX, posY + heightHalf, width, height - heightHalf, screen.previousColor | 0xFF000000);
		}
		
	}

	@Override
	public void onTextComponentChanged(GuiTextComponent textComponent) {
		if(updatingColor) {
			return;
		}
		
		if(textComponent == textComponentRed) {
			this.red = textComponentRed.getValue();
			setColorFromRGB();
			updateColorUiElements(false, true, true, true, true);
		}
		if(textComponent == textComponentGreen) {
			this.green = textComponentGreen.getValue();
			setColorFromRGB();
			updateColorUiElements(false, true, true, true, true);
		}
		if(textComponent == textComponentBlue) {
			this.blue = textComponentBlue.getValue();
			setColorFromRGB();
			updateColorUiElements(false, true, true, true, true);
		}
		
		if(textComponent == textComponentHue) {
			this.hue = textComponentHue.getValue() / 360.0f;
			setColorFromHSV();
			updateColorUiElements(true, false, true, true, true);
		}
		if(textComponent == textComponentSat) {
			this.saturation = textComponentSat.getValue() / 100.0f;
			setColorFromHSV();
			updateColorUiElements(true, false, true, true, true);
		}
		if(textComponent == textComponentVal) {
			this.brightness = textComponentVal.getValue() / 100.0f;
			setColorFromHSV();
			updateColorUiElements(true, false, true, true, true);
		}
		if(textComponent == textComponentHex) {
			this.color = textComponentHex.getColor();
			setColorFromARGB();
			updateColorUiElements(true, true, true, true, false);
		}
	}

}
