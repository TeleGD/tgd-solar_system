package games.solarSystem;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

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
	private Audio sound;


	/**
	 * @param spritePath chemin vers l'image contenant les sprites d'animation
	 * @param soundPath
	 * @param spriteWidth largeur d'un sprite à l'affichage
	 * @param spriteHeight hauteur d'un sprite à l'affichage
	 * @param spriteNaturalWidth largeur d'un sprite dans le spriteSheet des ressources du jeu
	 * @param spriteNaturalHeight hauteur d'un sprite dans le spriteSheet des ressources du jeu
	 * @param x
	 * @param y
	 * @param nbFramesOnX
	 * @param duration durée de l'explosion en milisecondes
	 */
	public Explosion(Solsys solsys, String spritePath, String soundPath, int spriteWidth, int spriteHeight, int spriteNaturalWidth, int spriteNaturalHeight, int x, int y, int nbFramesOnX, int nbFramesOnY, int animLineToLoad, int duration){
		this.aspectRatio = solsys.getWorld().getFacteurMagique()*solsys.getZoom();
		this.spriteWidth = (int) (spriteWidth * correctionRatio);
		this.spriteHeight = (int) (spriteHeight * correctionRatio);
		this.x = x;
		this.y = y;
		this.solsys = solsys;
		this.sound = AppLoader.loadAudio(soundPath);

		this.frameDuration = duration / (nbFramesOnX * nbFramesOnY);
		this.duration = frameDuration * nbFramesOnX * nbFramesOnY;

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

		this.sound.playAsSoundEffect(1, .6f, false);  // Lance le son dès la fin de l'initialisation de l'explosion
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		this.duration -= delta;
		if (this.duration <= 0 ){
			solsys.removeExplosion(this);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		int[] pos = solsys.toScreenPosition(x, y);
		context.drawAnimation(animation, pos[0], pos[1]);
	}

	public void loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int animLineToLoad) {
		for (int i = startX; i < endX; i++) {
			animation.addFrame(spriteSheet.getSprite(i, animLineToLoad).getScaledCopy((int) (spriteWidth * this.aspectRatio), (int) (spriteHeight * this.aspectRatio)), this.frameDuration);
		}
	}

	public void loadAnimationOnMultipleLines(SpriteSheet spriteSheet, int startX, int endX, int endY) {
		for (int j = 0; j < endY; j++) {
			for (int i = startX; i < endX; i++) {
				animation.addFrame(spriteSheet.getSprite(i, j).getScaledCopy((int) (spriteWidth * this.aspectRatio), (int) (spriteHeight * this.aspectRatio)), this.frameDuration);
			}
		}

	}
}
