package b100.minimap.gui.waypoint;

import b100.minimap.Minimap;
import b100.minimap.gui.GuiButtonNavigation;
import b100.minimap.gui.GuiContainerBox;
import b100.minimap.gui.GuiNavigationContainer;
import b100.minimap.gui.GuiNavigationContainer.Position;
import b100.minimap.gui.GuiScreen;
import b100.minimap.gui.GuiTextComponent;
import b100.minimap.gui.GuiTextComponentInteger;
import b100.minimap.gui.GuiTextElement;
import b100.minimap.gui.GuiTextField;
import b100.minimap.gui.TextComponentListener;
import b100.minimap.waypoint.Waypoint;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;

public abstract class GuiEditWaypointBase extends GuiScreen implements TextComponentListener {
	
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
	public GuiTextElement textPosition;
	public GuiTextElement textX;
	public GuiTextElement textY;
	public GuiTextElement textZ;
	
	public int playerOffsetX;
	public int playerOffsetY;
	public int playerOffsetZ;
	
	public GuiEditWaypointBase(GuiScreen parentScreen) {
		super(parentScreen);
		
		setPlayerOffset();
	}
	
	public void setPlayerOffset() {
		EntityPlayer player = Minimap.instance.minecraftHelper.getThePlayer();
		
		this.playerOffsetX = MathHelper.floor_double(player.posX);
		this.playerOffsetY = MathHelper.floor_double(player.posY);
		this.playerOffsetZ = MathHelper.floor_double(player.posZ);
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
		this.textPosition = add(new GuiTextElement("Offset"));
		this.textX = add(new GuiTextElement("x"));
		this.textY = add(new GuiTextElement("y"));
		this.textZ = add(new GuiTextElement("z"));
		
		this.textFieldName.textComponent.addTextComponentListener(this);
		this.textFieldPosX.textComponent.addTextComponentListener(this);
		this.textFieldPosY.textComponent.addTextComponentListener(this);
		this.textFieldPosZ.textComponent.addTextComponentListener(this);
		
		this.textFieldName.textComponent.setFocused(true);
	}
	
	@Override
	public void onResize() {
		final int paddingOuter = 2;
		final int paddingInner = 1;

		final int lineHeight = 10;
		final int h1 = lineHeight + paddingInner;
		
		int w = 150;
		int h = 6 * lineHeight + 5 * paddingInner + 2 * paddingOuter;
		
		container.setSize(w, h);
		container.setPosition((this.width - w) / 2, (this.height - h) / 2);
		
		int innerWidth = w - 2 * paddingOuter;
		
		int x1 = container.posX + paddingOuter;
		int y1 = container.posY + paddingOuter;
		
		textName.setPosition(x1, y1).setSize(innerWidth, lineHeight);
		textFieldName.setPosition(x1, y1 + 1 * h1).setSize(innerWidth, lineHeight);
		
		textPosition.setPosition(x1, y1 + 2 * h1).setSize(innerWidth, lineHeight);
		textX.setPosition(x1, y1 + 3 * h1).setSize(lineHeight, lineHeight);
		textY.setPosition(x1, y1 + 4 * h1).setSize(lineHeight, lineHeight);
		textZ.setPosition(x1, y1 + 5 * h1).setSize(lineHeight, lineHeight);

		textFieldPosX.setPosition(x1 + h1, y1 + 3 * h1).setSize(innerWidth - h1, lineHeight);
		textFieldPosY.setPosition(x1 + h1, y1 + 4 * h1).setSize(innerWidth - h1, lineHeight);
		textFieldPosZ.setPosition(x1 + h1, y1 + 5 * h1).setSize(innerWidth - h1, lineHeight);
		
		super.onResize();
	}
	
	@Override
	public void onTextComponentChanged(GuiTextComponent textComponent) {
		this.waypoint.name = this.textFieldName.getText();
		this.waypoint.x = this.textComponentX.getValue() + playerOffsetX;
		this.waypoint.y = this.textComponentY.getValue() + playerOffsetY;
		this.waypoint.z = this.textComponentZ.getValue() + playerOffsetZ;
	}
	
	public abstract void ok();
	
	public abstract void cancel();
	

}
