package solar_system;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Satellite extends Orbital{
	public Satellite(int lifeMax, int cout,int posX,int posY, double speed, double size, int distance) {
		super(lifeMax, cout, posX, posY, speed, size, distance);
	}

	/*
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta, int orbitalSize , int i) {
		super.update(container, game, delta, orbitalSize, i);
	}
	*/
}
