package games.solarSystem;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class ResourceIcon {
	private int x;
	private int y;
	private Image img;
	private String cost;
	private int size = 48;

	public ResourceIcon(int x, int y, Image img, double cost) {
		this.x = x;
		this.y = y;
		this.img = img;
		this.cost = String.valueOf(cost);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		context.drawImage(img, x, y, x+size, y+size, 0, 0, img.getWidth(), img.getHeight());

		// Placement du coût en bas à droite
		context.setColor(Color.white);
		int largeur = context.getFont().getWidth(this.cost);
		context.drawString(this.cost, x+48-largeur, y+32);

	}

	public void moveY(int dY) {
		this.y += dY;
	}

	public Image getImg() {
		return img;
	}

	public int getSize() {
		return size;
	}
}
