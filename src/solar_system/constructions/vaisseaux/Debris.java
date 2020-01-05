package solar_system.constructions.vaisseaux;

import solar_system.Player;
import solar_system.Solsys;
import solar_system.World;
import solar_system.constructions.Vaisseau;

public class Debris extends Vaisseau{
	
	public Debris(Player player, Solsys solsys) {
		super(player,solsys);
		this.name = "Débris";
		cout.put("Fer",100.0);
		cout.put("Bois",20.0);
		this.v0Max = 0.5;
	}
}
