package solar_system;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Spaceship{
	private double x;
	private double y;
	private double vx;
	private double vy;
	private boolean hasLeft;
	private boolean crashed;
	private World world;
	
	public Spaceship(int x0, int y0, double vx0, double vy0, World world) {
		this.x = x0;
		this.y = y0;
		this.vx = vx0;
		this.vy = vy0;
		this.hasLeft = false;
		this.crashed = false;
		this.world = world;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public boolean hasLeft() {
		return this.hasLeft;
	}
	
	public void left() {
		this.hasLeft = true;
	}
	
	public boolean hasCrashed() {
		return this.crashed;
	}
	
	public void crash() {
		if (hasLeft) this.crashed = true;
	}
	
	public void nextOrbitalPosition(int delta) {
		double rx = this.x - world.getWidth()/2;
		double ry = this.y - world.getHeight()/2;
		double r3 = Math.pow(rx*rx+ry*ry, 1.5);
		vx -= delta*40*rx/r3;	// Variation de la vitesse = accélération
		vy -= delta*40*ry/r3;
		x += delta*vx;
		y += delta*vy;
	}
	
	public void update (GameContainer container, StateBasedGame game, int delta) {
		if (!crashed) {
			nextOrbitalPosition(delta);
		}
	}
	
	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		if (!crashed) {
			context.setColor(Color.green);
			context.fillOval((int)x-24, (int)y-24, 48, 48);
		}
	}
}
