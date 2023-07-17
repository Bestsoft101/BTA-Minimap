package b100.minimap.gui.waypoint;

import org.lwjgl.input.Keyboard;

import b100.minimap.gui.ColorListener;
import b100.minimap.gui.GuiButton;
import b100.minimap.gui.GuiButtonNavigation;
import b100.minimap.gui.GuiColorSelectScreen;
import b100.minimap.gui.GuiContainerBox;
import b100.minimap.gui.GuiElement;
import b100.minimap.gui.GuiNavigationContainer;
import b100.minimap.gui.GuiNavigationContainer.Position;
import b100.minimap.gui.GuiScreen;
import b100.minimap.gui.GuiTextComponent;
import b100.minimap.gui.GuiTextComponentInteger;
import b100.minimap.gui.GuiTextElement;
import b100.minimap.gui.GuiTextField;
import b100.minimap.gui.TextComponentListener;
import b100.minimap.mc.Player;
import b100.minimap.waypoint.Waypoint;
import net.minecraft.core.util.helper.MathHelper;

public abstract class GuiEditWaypointBase extends GuiScreen implements TextComponentListener, ColorListener {
	
	public String title;
	
	public Waypoint waypoint;
	
	public GuiNavigationContainer navTop;
	public GuiNavigationContainer navBottom;
	
	public GuiContainerBox container;

	public GuiTextField textFieldName;
	public GuiTextField textFieldPosX;
	public GuiTextField textFieldPosY;
	public GuiTextField textFieldPosZ;
	
	public GuiTextComponentInteger textComponentX;
	public GuiTextComponentInteger textComponentY;
	public GuiTextComponentInteger textComponentZ;

	public GuiTextElement textName;
	public GuiTextElement textOffset;
	public GuiTextElement textX;
	public GuiTextElement textY;
	public GuiTextElement textZ;
	public GuiTextElement textColor;
	
	public GuiButton colorButton;
	
	public int playerOffsetX;
	public int playerOffsetY;
	public int playerOffsetZ;
	
	public GuiEditWaypointBase(GuiScreen parentScreen) {
		super(parentScreen);
		
		setPlayerOffset();
	}
	
	public void setPlayerOffset() {
		Player player = minimap.minecraftHelper.getThePlayer();
		
		this.playerOffsetX = MathHelper.floor_double(player.getPosX(1.0f));
		this.playerOffsetY = MathHelper.floor_double(player.getPosY(1.0f));
		this.playerOffsetZ = MathHelper.floor_double(player.getPosZ(1.0f));
	}

	@Override
	public void onInit() {
		this.container = add(new GuiContainerBox());
		
		this.navTop = add(new GuiNavigationContainer(this, container, Position.TOP));
		this.navBottom = add(new GuiNavigationContainer(this, container, Position.BOTTOM));
		
		this.navTop.add(new GuiButtonNavigation(this, title, container));
		this.navBottom.add(new GuiButtonNavigation(this, "Cancel", container).addActionListener((e) -> cancel()));
		this.navBottom.add(new GuiButtonNavigation(this, "OK", container).addActionListener((e) -> ok()));
		
		this.textComponentX = new GuiTextComponentInteger(waypoint.x - playerOffsetX);
		this.textComponentY = new GuiTextComponentInteger(waypoint.y - playerOffsetY);
		this.textComponentZ = new GuiTextComponentInteger(waypoint.z - playerOffsetZ);
		
		this.textFieldName = container.add(new GuiTextField(this, waypoint.name));
		this.textFieldPosX = container.add(new GuiTextField(this, textComponentX));
		this.textFieldPosY = container.add(new GuiTextField(this, textComponentY));
		this.textFieldPosZ = container.add(new GuiTextField(this, textComponentZ));
		
		this.textName = add(new GuiTextElement("Name"));
		this.textOffset = add(new GuiTextElement("Offset"));
		this.textX = add(new GuiTextElement("x"));
		this.textY = add(new GuiTextElement("y"));
		this.textZ = add(new GuiTextElement("z"));
		this.textColor = add(new GuiTextElement("Color"));
		
		this.textFieldName.textComponent.addTextComponentListener(this);
		this.textFieldPosX.textComponent.addTextComponentListener(this);
		this.textFieldPosY.textComponent.addTextComponentListener(this);
		this.textFieldPosZ.textComponent.addTextComponentListener(this);
		
		this.textFieldName.textComponent.setFocused(true);
		
		this.colorButton = add(new GuiWaypointColorButton(this, waypoint)).addActionListener((e) -> utils.displayGui(new GuiColorSelectScreen(this, waypoint.color, this)));
	}
	
	@Override
	public void onResize() {
		final int paddingOuter = 2;
		final int paddingInner = 1;

		final int lineHeight = 10;
		final int lineHeightPad = lineHeight + paddingInner;
		
		int w = 150;
		int h = 6 * lineHeight + 5 * paddingInner + 2 * paddingOuter;
		
		container.setSize(w, h);
		container.setPosition((this.width - w) / 2, (this.height - h) / 2);
		
		int innerWidth = w - 2 * paddingOuter;
		
		int posXInner = container.posX + paddingOuter;
		int posYInner = container.posY + paddingOuter;
		
		textName.setPosition(posXInner, posYInner).setSize(innerWidth, lineHeight);
		textFieldName.setPosition(posXInner, posYInner + 1 * lineHeightPad).setSize(innerWidth, lineHeight);

		int colorBoxWidth = 3 * lineHeight + 2 * paddingInner;
		int offsetBoxX = posXInner;
		int offsetBoxY = posYInner + 2 * lineHeightPad;
		int offsetBoxWidth = innerWidth - colorBoxWidth - paddingInner;
		int colorBoxX = offsetBoxX + offsetBoxWidth + paddingInner;
		
		textOffset.setPosition(offsetBoxX, offsetBoxY).setSize(offsetBoxWidth, lineHeight);
		textX.setPosition(offsetBoxX, offsetBoxY + 1 * lineHeightPad).setSize(lineHeight, lineHeight);
		textY.setPosition(offsetBoxX, offsetBoxY + 2 * lineHeightPad).setSize(lineHeight, lineHeight);
		textZ.setPosition(offsetBoxX, offsetBoxY + 3 * lineHeightPad).setSize(lineHeight, lineHeight);
		
		int positionTextFieldWidth = innerWidth - lineHeightPad - colorBoxWidth - paddingInner;
		textFieldPosX.setPosition(posXInner + lineHeightPad, posYInner + 3 * lineHeightPad).setSize(positionTextFieldWidth, lineHeight);
		textFieldPosY.setPosition(posXInner + lineHeightPad, posYInner + 4 * lineHeightPad).setSize(positionTextFieldWidth, lineHeight);
		textFieldPosZ.setPosition(posXInner + lineHeightPad, posYInner + 5 * lineHeightPad).setSize(positionTextFieldWidth, lineHeight);

		textColor.setPosition(colorBoxX, offsetBoxY).setSize(colorBoxWidth, lineHeight);
		colorButton.setPosition(colorBoxX, offsetBoxY + lineHeightPad).setSize(colorBoxWidth, colorBoxWidth);
		
		super.onResize();
	}
	
	@Override
	public void keyEvent(int key, char c, boolean pressed, boolean repeat, int mouseX, int mouseY) {
		if(key == Keyboard.KEY_RETURN) {
			ok();
		}
		super.keyEvent(key, c, pressed, repeat, mouseX, mouseY);
	}
	
	@Override
	public void onTextComponentChanged(GuiTextComponent textComponent) {
		this.waypoint.name = this.textFieldName.getText();
		this.waypoint.x = this.textComponentX.getValue() + playerOffsetX;
		this.waypoint.y = this.textComponentY.getValue() + playerOffsetY;
		this.waypoint.z = this.textComponentZ.getValue() + playerOffsetZ;
	}
	
	@Override
	public void onColorChanged(GuiElement source, int color) {
		this.waypoint.color = color;
	}
	
	public abstract void ok();
	
	public abstract void cancel();
	

}
