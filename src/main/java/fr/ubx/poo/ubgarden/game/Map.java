package fr.ubx.poo.ubgarden.game;


import fr.ubx.poo.ubgarden.game.go.decor.Decor;

import java.util.Collection;

public interface Map {
    int width();

    int height();

    Decor get(Position position);

    Collection<Decor> values();

    boolean inside(Position nextPos);
}
