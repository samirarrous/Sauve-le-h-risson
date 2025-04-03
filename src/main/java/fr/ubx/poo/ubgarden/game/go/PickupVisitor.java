package fr.ubx.poo.ubgarden.game.go;

import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;

public interface PickupVisitor {
    /**
     * Called when visiting and picking up an {@link EnergyBoost}.
     *
     * @param energyBoost the energy boost to be picked up
     */
    default void pickUp(EnergyBoost energyBoost) {
    }

}
