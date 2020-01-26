package com.germimonte.yapm.init;

import com.germimonte.yapm.peripherals.PhoneGPS;
import com.germimonte.yapm.peripherals.TurtleGPS;
import com.germimonte.yapm.util.IPeripheralBase;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;

import java.util.ArrayList;
import java.util.List;

public class ModPeripherals {
	public static final List<ITurtleUpgrade> TURTLE_UPGRADES = new ArrayList<>();
	public static final List<IPocketUpgrade> POCKET_UPGRADES = new ArrayList<>();

	public static void registerInternally() {
		TURTLE_UPGRADES.add(new TurtleGPS());
		POCKET_UPGRADES.add(new PhoneGPS());
	}

	public static void registerWithComputerCraft() {
		ComputerCraftAPI.registerPeripheralProvider(new IPeripheralBase.Provider());
		for (ITurtleUpgrade upgrade : TURTLE_UPGRADES)
			ComputerCraftAPI.registerTurtleUpgrade(upgrade);
		for (IPocketUpgrade upgrade : POCKET_UPGRADES)
			ComputerCraftAPI.registerPocketUpgrade(upgrade);
	}
}