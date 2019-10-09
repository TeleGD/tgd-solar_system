package solar_system;

import app.AppLoader;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class Explosion {

	private float aspectRatio;
	private int x;
	private int y;
	private Animation animation; // animation
	private int spriteWidth;
	private int spriteHeight;
	protected boolean side; // true : Regarde vers la droite, false : Regarde vers la gauche
	private int frameDuration;
	private Solsys solsys;
	private int duration;
	private double correctionRatio = 1.5;  // Facteur multiplicatif de correction de la taille de l'affichage du sprite de l'explosion

	/**
	 * @param aspectRatio
	 * @param spritePath chemin vers l'image contenant les sprites d'animation
	 * @param spriteWidth largeur d'un sprite à l'affichage
	 * @param spriteHeight hauteur d'un sprite à l'affichage
	 * @param spriteNaturalWidth largeur d'un sprite dans le spriteSheet des ressources du jeu
	 * @param spriteNaturalHeight hauteur d'un sprite dans le spriteSheet des ressources du jeu
	 * @param x
	 * @param y
	 * @param nbFramesOnX
	 * @param duration durée de l'explosion en milisecondes
	 */
	public Explosion(Solsys solsys, float aspectRatio, String spritePath, int spriteWidth, int spriteHeight, int spriteNaturalWidth, int spriteNaturalHeight, int x, int y, int nbFramesOnX, int nbFramesOnY, int animLineToLoad, int duration){
		this.aspectRatio = aspectRatio;
		this.spriteWidth = (int) (spriteWidth * correctionRatio);
		this.spriteHeight = (int) (spriteHeight * correctionRatio);
		this.x = x;
		this.y = y;
		this.solsys = solsys;
		this.duration = duration;

		this.frameDuration = this.duration / (nbFramesOnX * nbFramesOnY);

		// Chargements des sprites dans les animations :
		SpriteSheet spriteSheet = null;
		Image spriteImage = AppLoader.loadPicture(spritePath);
		spriteSheet = new SpriteSheet(spriteImage, spriteNaturalWidth, spriteNaturalHeight);

		animation = new Animation();

		if (nbFramesOnY == 1) {
			loadAnimation(spriteSheet,0, nbFramesOnX, animLineToLoad);
		}
		else {
			loadAnimationOnMultipleLines(spriteSheet,0, nbFramesOnX, nbFramesOnY);
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		this.duration -= delta;
		if (this.duration <= 0 ){
			solsys.removeExplosion(this);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		context.drawAnimation(animation, this.x , this.y);
	}

	public void loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int animLineToLoad) {
		for (int i = startX; i < endX; i++) {
			animation.addFrame(spriteSheet.getSprite(i, animLineToLoad).getScaledCopy((int) (spriteWidth * this.aspectRatio), (int) (spriteHeight * this.aspectRatio)), this.frameDuration);
		}
	}

	public void loadAnimationOnMultipleLines(SpriteSheet spriteSheet, int startX, int endX, int endY) {
		for (int i = startX; i < endX; i++) {
			for (int j = 0; j < endY; j++) {
				animation.addFrame(spriteSheet.getSprite(i, j).getScaledCopy((int) (spriteWidth * this.aspectRatio), (int) (spriteHeight * this.aspectRatio)), this.frameDuration);
			}
		}
	}
}
