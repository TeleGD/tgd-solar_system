package solar_system;

public class Construc_Mine extends Construction {

	
	public Construc_Mine (){
		this.lifeMax=100;
		this.life=lifeMax;
		this.productions.add(new Production(3, "fer", player));
	}
	
	
}

