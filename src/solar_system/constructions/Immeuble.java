package solar_system.constructions;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import solar_system.Building;
import solar_system.Case;
import solar_system.Player;
import solar_system.Resource;

public class Immeuble extends Building{
	private static HashMap<String, Resource> resourcesExploitable ;
	static 
	{
		resourcesExploitable = new HashMap<>();
        resourcesExploitable.put("Noyau Linux",new Resource("Noyau Linux"));
	}
	
	public Immeuble (Case tile, Player player){
		super(tile, player);
		this.lifeMax=100;
		this.life=lifeMax;
		this.debits.put("Noyau Linux", 0.01);
		this.name = "Maison";
		
		this.cout.put("Bois", 1000.0);
		this.cout.put("Fer", 500.0);
		this.coutPerpetuel.put("Nourriture", 0.1);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		
	}
	
	public static boolean constructPossible(Case tileConstructLocation) {
		
		boolean bool=resourcesExploitable.containsKey(tileConstructLocation.getResource().getName());
		System.out.println(bool);
		return bool;
	}

}
