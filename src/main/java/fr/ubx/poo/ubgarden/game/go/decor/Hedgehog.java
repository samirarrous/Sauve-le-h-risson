package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.Pickupable;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Hedgehog extends Decor {
    public Hedgehog(Position position) {
        super(position);
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        gardener.pickUp(this);
}}
