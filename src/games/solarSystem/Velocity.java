package games.solarSystem;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Velocity {
	private double norm; // Vitesse représentée
	private double maxNorm;	// Norme de vitesse maximale
	private double angle;
	private int x; // Origine du vecteur
	private int y;
	private int x2; // Pointe de la flèche
	private int y2;
	private int x3; // Premier côté de la flèche
	private int y3;
	private int x4; // Deuxième côté de la flèche
	private int y4;
	private ArrayList<ArrayList<Integer>> simulation;
	private float deltaAlpha; // Varie de 0 à 1 pour rendre la trajectoire de plus en plus transparente
	private float deltaDash;  // Permet de mettre les tirets de la flèche du vecteur en mouvement
	private int len;	// Longueur des tirets de la flèche (en pixels)
	private Solsys solsys;

	public Velocity(double norm, double maxNorm, double angle, Solsys solsys) {
		this.norm = norm;
		this.maxNorm = maxNorm;
		this.angle = angle;
		this.len = 16;
		this.solsys = solsys;
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setVelocity(double norm, double angle) {
		// Réduction de la norme si elle dépasse la valeur max
		if (norm > maxNorm) norm = maxNorm;
		// On la fixe à 0 si elle est négative
		if (norm < 0) norm = 0;
		this.norm = norm;
		this.angle = angle;
	}

	public void moveAngle(double deltaAngle) {
		this.angle += deltaAngle;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public double getNorm() {
		return this.norm;
	}

	public double getAngle() {
		return this.angle;
	}

	public ArrayList<ArrayList<Integer>> getSimulation() {
		return this.simulation;
	}

	public void makeSimulation() {
		// Attention : cette gestion d'une liste de positions est exceptionnellement sale.
		// Si vous trouvez le moyen de faire contenir les positions dans une liste du genre
		// ArrayList<int[]>, vous êtes le/la bienvenu(e) !
		ArrayList<ArrayList<Integer>> posList = new ArrayList<>();
		double delta = 1;
		double x = this.x;
		double y = this.y;
		ArrayList<Integer> pos = new ArrayList<>();
		pos.add((int)x);
		pos.add((int)y);
		posList.add(pos);
		double vx = this.norm*Math.cos(this.angle);
		double vy = this.norm*Math.sin(this.angle);
		double rx, ry;
		double r3;
		for (int i = 0; i < 3000; i++) {
			rx = x;
			ry = y;
			r3 = Math.pow(rx*rx+ry*ry, 1.5);
			vx -= delta*40*rx/r3;	// Variation de la vitesse = accélération
			vy -= delta*40*ry/r3;
			x += delta*vx;
			y += delta*vy;
			pos = new ArrayList<>();
			pos.add((int)x);
			pos.add((int)y);
			posList.add(pos);
		}
		this.deltaAlpha = 0;
		this.simulation = posList;
	}

	public void resetSimulation() {
		this.simulation = null;
	}

	public void update (GameContainer container, StateBasedGame game, int delta) {
		// Calcul de la position de la pointe du vecteur (2), du premier côté de la flèche (3), et du deuxième côté (4)
		x2 = (int)(x+norm*Math.cos(angle)*400);
		y2 = (int)(y+norm*Math.sin(angle)*400);
		double theta;
		if (norm >= 0) theta = 5*Math.PI/6;
		else theta = Math.PI/6;
		x3 = (int)(x2+20*Math.cos(angle+theta));
		y3 = (int)(y2+20*Math.sin(angle+theta));
		x4 = (int)(x2+20*Math.cos(angle-theta));
		y4 = (int)(y2+20*Math.sin(angle-theta));

		deltaDash = (deltaDash+(float)delta/50)%(2*len);
		if (this.simulation != null) this.deltaAlpha += (float)delta/5000;
		if (this.deltaAlpha > 1) this.simulation = null;
	}

	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		context.setColor(Color.white);
		context.setLineWidth(1+2*solsys.getZoom());
		double aX = (double)(x2-x)/(norm*400);
		double aY = (double)(y2-y)/(norm*400);
		for (int i = (int)deltaDash; i < this.norm*400-len; i += 2*len) {
			int[] start = solsys.toScreenPosition(x+i*aX, y+i*aY);
			int[] end = solsys.toScreenPosition(x+(i+len)*aX, y+(i+len)*aY);
			context.drawLine(start[0], start[1], end[0], end[1]);
		}
		int[] pos2 = solsys.toScreenPosition(x2, y2);
		int[] pos3 = solsys.toScreenPosition(x3, y3);
		int[] pos4 = solsys.toScreenPosition(x4, y4);
		context.drawLine(pos2[0], pos2[1], pos3[0], pos3[1]);
		context.drawLine(pos2[0], pos2[1], pos4[0], pos4[1]);

		// Tracé des pointillés de la simulation si elle existe
		context.setLineWidth(1+solsys.getZoom());
		if (this.simulation != null) {
			int n = this.simulation.size();
			if (n > 30) {
				ArrayList<Integer> pos0, pos1;
				for (int i = 30; i < n; i += 60) {
					pos0 = simulation.get(i-30);
					pos1 = simulation.get(i);
					int start[] = solsys.toScreenPosition(pos0.get(0), pos0.get(1));
					int end[] = solsys.toScreenPosition(pos1.get(0), pos1.get(1));
					context.drawGradientLine(start[0], start[1], 1, 1, 1, -deltaAlpha+(float)(n-i+30)/n, end[0], end[1], 1, 1, 1, -deltaAlpha+(float)(n-i)/n);
				}
			}
		}
	}
}
