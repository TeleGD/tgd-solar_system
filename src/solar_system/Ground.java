package solar_system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import app.ui.ButtonV2;
import solar_system.constructions.Ferme;
import solar_system.constructions.Mine;
import solar_system.constructions.Mine2;
import solar_system.constructions.TNCY;
import solar_system.constructions.Scierie;
import solar_system.constructions.CabaneBucheron;

import java.util.concurrent.ThreadLocalRandom;

public class Ground {
	
	private int sizeCase = 80;
	private Planet planet;
	private World world;
	private Case[][] cases;
	private int x_origin;	//Abscisse du coin supérieur gauche
	private int y_origin;	//Ordonnée du coin supérieur gauche
	private Image image;
	private Image imageBack; // image du bouton pour retourner au système
	private int radius;
	private Case selectedCase;
	private int padding; // Décalage des cases par rapport au point supérieur gauche de l'image de la planère (global car nécessaire pour la sélection des cases).
	private float facteur_magique;
	private boolean menuConstructionBoolean;
	private int coinMenuX; // Pour le menu des constructions
	private int coinMenuY;
	private int coinInfoX; // Pour les informations sur les constructions
	private int coinInfoY;
	private int imageConstructSize; // Hauteur des images des constructions, dans le menu des constructions
	private List<String> constructionsPossibles;
	private List<Image> imagesConstructions;
	private List<ButtonV2> boutonsConstructions = new ArrayList<>();
	private int hauteurTextMenuConstruct;
	private int coinBoutonDestruct; // position verticale du bouton pour détruire un batiment
	protected boolean constructionFailed; // Vaut true si le joueur a demandé la construction d'un batiment pour lequel il n'a pas assez de ressources
	private MenuConstruction menuConstruction;
	
	public Ground(Planet planet, World world) {
		/* Créer un objet de classe Ground avec des cases pour sur la planete plt */
		this.planet = planet;
		this.world = world;
		this.radius = (int) Math.floor(this.planet.getRadius()*8.1);
		this.selectedCase = null;
		// Origine de la planète (toutes les longueurs sont multipliées par un certain facteur,
		// le facteur magique.)
		this.facteur_magique = (float)(world.getHeight())/1080;
		this.x_origin = world.getWidth()/2 - (int)(radius*facteur_magique);
		this.y_origin = world.getHeight()/2 - (int)(radius*facteur_magique);
		menuConstructionBoolean = false;

		coinMenuX = (int)(0.8*world.getWidth());
		coinMenuY = (int)(0.1*world.getHeight());
		menuConstruction = new MenuConstruction(world, coinMenuX, coinMenuY);
		
		coinMenuX = 10000; // tant que le coin du menu n'a pas été calculé, on prend une grande valeur pour que le menu soit hors du champ.
		coinMenuY = 10000;
		imageConstructSize = 150;
		hauteurTextMenuConstruct = 26;
		constructionFailed = false;
		generateCases();
		
		//this.air = new Air(5,(int)(5.0/4)*radius,this.world,planet);
		Resource resource = new Resource("Fer"); // TODO : à changer
		/////air.addOrbital(new Satellite(20,0,100,100, 50,(int)(5.0/4)*radius,resource,this.world));
		
		try{  // désormais, image correspond à l'image de la planète en arrière plan.
			this.image = new Image(planet.getNomImage());
			this.imageBack = new Image("res/images/retour.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
		
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		
		// Cette fonction contient deux fois "air.render()" : c'est normal (cf définition de air.render())
		//air.render(container, game, context, true);
		int x_bas_droite = this.x_origin+(int)(2*radius*facteur_magique);
		int y_bas_droite = this.y_origin+(int)(2*radius*facteur_magique);
		int taille_x = image.getWidth()-1;
		int taille_y = image.getHeight()-1;
		// Affichage de la planète
		context.drawImage(image, x_origin, y_origin, x_bas_droite, y_bas_droite, 0, 0, taille_x, taille_y);
		// Affichage des cases
		for (Case[] tab : cases) {
			for (Case c : tab) {
				c.render(container, game, context);
			}
		}
		context.setColor(Color.red);
		context.fillRect(0,50,20,20);

		// Affichage du 'Air'
		//air.render(container, game, context, false);

		// imageBack pour revenir
		context.drawImage(imageBack.getScaledCopy(48, 48), 0, 50);
		
		if (selectedCase != null) {
			selectedCase.renderHighlighted (container, game, context);
		}
		
	}
	
	public void update (GameContainer container, StateBasedGame game, int delta) {
		for (Case[] tab : cases) {
			for (Case c : tab) {
				c.update(container, game, delta);
			}
		}
	}
	
	
	public void generateCases() {
		// Génère le tableau de dimension 2 "heightCases" et le remplit de "Case"
		
		Resource resource;
		int resourceQuantity;
		
		// Nombre de cases en longueur :
		int n = (int) Math.floor( ((float)Math.sqrt(2) * (float) radius - 14) / (float) sizeCase);
		
		this.cases = new Case[n][n];
		// padding = marge intérieure (distance entre la grille et le bord de l'image)
		padding = (int)( (radius*2-n*sizeCase)*facteur_magique/2 );
		
		for (int i = 0; i < cases.length ; i++) {
			for (int j = 0; j < cases.length; j++ ) {
				// Choix aléatoire d'une ressource :
				resource = world.getPlayer().getResource(randomResourceName());
				resourceQuantity = resourceQuantity( resource.getName() );
				
				cases[i][j] = new Case(x_origin+padding+(int)(i*sizeCase*facteur_magique), y_origin+padding+(int)(j*sizeCase*facteur_magique), (int)(sizeCase*facteur_magique), resource, resourceQuantity);
			}
		}
	}
	
	public Construction nameToConst (String name, Case tile) {
		if(name=="Mine"){
			return new Mine(tile, world.getPlayer());
		}
		if(name=="Mine2"){
			return new Mine2(tile, world.getPlayer());
		}
		if(name=="TNCY"){
			return new TNCY(tile, world.getPlayer());
		}
		if(name=="Ferme"){
			return new Ferme(tile, world.getPlayer());
		}
		if(name=="Scierie"){
			return new Scierie(tile, world.getPlayer());
		}
		if(name=="CabaneBucheron"){
			return new CabaneBucheron(tile, world.getPlayer());
		}
		return null;
	}
	
	public String randomResourceName() {
		
		String resource;
		int rand = ThreadLocalRandom.current().nextInt(0, 17);  // Donne un nombre entier aléatoire entre 0 et 4 inclus
		
		// Pour le noyau Linux :
		if (rand==0) {
			resource = "Noyau Linux";
		} else if (rand%4==0){
			resource = "Fer";
		} else if (rand%4==1) {
			resource = "Bois";
		} else if (rand%4==2) {
			resource = "Cailloux";
		} else {
			resource = "Nourriture";
		}
		return resource;
	}
	
	public int resourceQuantity(String res) {
		switch (res) {
			case "Fer" :
				return 500;
			case "Bois" :
				return 1500;
			case "Cailloux" :
				return 3000;
			case "Nourriture" :
				return 2000;
			case "Noyau Linux" :
				return 10;
			default :
				return 0;
		}
	}
	
	public boolean mousePressed(int arg0, int x, int y) { 
		// Gère les clics sur le Ground.
		
		if (constructionFailed) { // pour faire disparaitre le message, le joueur doit cliquer n'importe où.
			constructionFailed = false;
		}
		
		// Construction d'un bâtiment :
		if (menuConstruction.mousePressed(arg0, x, y)) return false;
		
		selectedCase = selectCase(x,y); // Récupère la case sélectionnée si elle existe.
		menuConstruction.casePressed(selectedCase);
		
		Air air = planet.getAir();
		if(air.mousePressed(arg0,x, y)!=null){
			selectedCase = air.mousePressed(arg0, x, y).getCase();
			
		}
		//Mettre le selected case = mousepressed de air.
		
		// Modification de la liste des constructions à afficher dans le menu des constructions :
		
		
		
/*		if (selectedCase != null) {  // On adapte les listes du menu de constructions à la case nouvellement sélectionnée.
			Image imageTemp;
			constructionsPossibles = selectedCase.infoConstruct();
			imagesConstructions = new ArrayList<Image>();
			
			for (int i=0; i<constructionsPossibles.size(); i++) {
				try{
					imageTemp = new Image("res/images/constructions/"+constructionsPossibles.get(i)+".png");
					imagesConstructions.add( imageTemp.getScaledCopy(imageConstructSize,imageConstructSize) ); // on met toutes les images à la même taille (et carrées)
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (selectedCase != null) {
			menuConstructionBoolean = true;
		} else {
			menuConstructionBoolean = false;
		}*/
		
		if (x<48 && x>0 && y<98 && y>50) { // Clic sur l'image de retour.
			selectedCase = null; // On désélectionne la case.
			menuConstructionBoolean = false;
			return(true);
		}

		return (false);
		
	}
	
	public Case selectCase(int x , int y){     // TODO : Correction à faire : pour les cases vers le bas ou vers la gauche, il y a un débordement de la 'hitbox' sur les cases d'à côté
		// Retourne la case dans laquelle le point (x,y) est contenu. 
		int renderedSize = sizeCase * world.getHeight()/1080;
		if (y - (y_origin+padding) < 0 || (y - (y_origin+padding))/renderedSize >= cases.length || x - (x_origin+padding) < 0 || (x - (x_origin+padding))/renderedSize >= cases.length){
			return (null); // Aucune case ne contient (x,y) !
		}
		else {
			return (cases[(x - (x_origin+padding))/renderedSize][(y - (y_origin+padding))/renderedSize]);}
	}
	
	public void renderMenuConstruct (GameContainer container, StateBasedGame game, Graphics context) {
		menuConstruction.render(container, game, context);
	}
	

	 /*public void renderMenuConstruct (GameContainer container, StateBasedGame game, Graphics context) {
		
		// Affiche le menu des constructions

		coinBoutonDestruct = -100;
		if (imagesConstructions.size()!=0) {
			Image img;
			int tailleImg = 128;
			int currentHeight = coinMenuY;
			int largeur, initialHeight;
			int mX = 24;	// mX pour marginX, la marge à droite
			for(int i=0; i<constructionsPossibles.size(); i++) {
				Construction c = nameToConst(constructionsPossibles.get(i), selectedCase);
				
				*//** Affichage du nom **//*
				largeur = context.getFont().getWidth(c.getName());
				context.setColor(Color.white);
				context.drawString(c.getName(), world.getWidth()-largeur-mX, currentHeight);
				currentHeight += hauteurTextMenuConstruct;
				initialHeight = currentHeight;
				
				*//** Affichage de l'image **//*
				try {
					img = new Image("res/images/constructions/"+constructionsPossibles.get(i)+".png");
					boutonsConstructions.add(new ButtonV2(img, world.getWidth()-tailleImg-mX-50, currentHeight, tailleImg, tailleImg));
				} catch (SlickException e) {
					e.printStackTrace();
				}
				currentHeight += imageConstructSize;
				
				*//** Affichage des coûts **//*
				currentHeight = initialHeight;
				//largeur = context.getFont().getWidth("Coûts :");
				//context.drawString("Coûts :", world.getWidth()-largeur-mX, currentHeight);
				//currentHeight += hauteurTextMenuConstruct;
				int currentWidth = world.getWidth()-mX-50;
				for (String k : c.cout.keySet()) {
					try{
						img = new Image(Resource.imagePath(k));
						context.drawImage(img, currentWidth, currentHeight, currentWidth+48, currentHeight+48, 0, 0, img.getWidth(), img.getHeight());
					} catch (SlickException e) {
						e.printStackTrace();
					}
					// Tout ce qui suit sert à afficher la valeur sur la droite :
					largeur = context.getFont().getWidth(Integer.toString(c.cout.get(k).intValue()));
					context.drawString(Integer.toString(c.cout.get(k).intValue()), currentWidth+48-largeur, currentHeight+32);
					currentHeight += 50;
				}
				currentHeight = Math.max(currentHeight+10,initialHeight+imageConstructSize+10);
				
				*//** Affichage des débits **//*
				largeur = context.getFont().getWidth("Gains :");
				context.drawString("Gains :", world.getWidth()-largeur-mX, currentHeight);
				currentHeight += hauteurTextMenuConstruct;
				currentWidth = world.getWidth()-24;
				for (String k : c.debits.keySet()) {
					try{
						img = new Image(Resource.imagePath(k));
						context.drawImage(img, currentWidth, currentHeight, currentWidth+48, currentHeight+48, 0, 0, img.getWidth(), img.getHeight());
					} catch (SlickException e) {
						e.printStackTrace();
					}
					currentWidth -= 48;
					// Tout ce qui suit sert à afficher la valeur sur la droite :
					largeur = context.getFont().getWidth(Integer.toString(c.debits.get(k).intValue()));
					context.drawString(Integer.toString(c.debits.get(k).intValue()), currentWidth+48-largeur, currentHeight+32);
				}
				currentHeight += 64;

			}
		}
		
		if (constructionFailed) {
			context.setColor(Color.red);
			context.drawString("Construction impossible :", (int) (0.75*world.getWidth()), (int) (0.2*world.getHeight()));
			context.drawString("    ressources insuffisantes", (int) (0.75*world.getWidth()), (int) (0.2*world.getHeight()+24));
		}
	}*/
	
	public String construcRequested(int number) {    // Renvoie le nom du bâtiment numéro 'number' dans le menu des constructions
		if (number<constructionsPossibles.size()) {
			return constructionsPossibles.get(number);
		}
		return "";
	}
	
	public Image getConstructImage(int number) {    // Renvoie le nom du bâtiment numéro 'number' dans le menu des constructions
		if (number<imagesConstructions.size()) {
			return imagesConstructions.get(number);
		}
		return null;
	}
	

	
	
	

}
