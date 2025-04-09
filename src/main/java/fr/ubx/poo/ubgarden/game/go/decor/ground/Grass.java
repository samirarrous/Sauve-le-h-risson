/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Hedgehog;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Grass extends Land {
    public Grass(Position position) {
        super(position);
    }




    public boolean walkableBy(Gardener gardener) {
        return gardener.canWalkOn(this);
    }

    @Override
    public int energyConsumptionWalk() {
        return 1;
    }

}
