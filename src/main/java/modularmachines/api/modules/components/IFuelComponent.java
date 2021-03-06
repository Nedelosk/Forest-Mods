package modularmachines.api.modules.components;

public interface IFuelComponent extends IModuleComponent {
	int getFuel();
	
	int getFuelTotal();
	
	boolean hasFuel();
	
	void removeFuel(double modifier);
	
	void updateFuel();
}
