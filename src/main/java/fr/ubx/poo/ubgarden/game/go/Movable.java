/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Position;

public interface Movable {
    /**
     * Checks whether the entity can move in the given direction.
     *
     * @param direction the direction in which the entity intends to move
     * @return true if the move is possible, false otherwise
     */
    boolean canMove(Direction direction);

    /**
     * Moves the entity in the given direction and returns its new position.
     *
     * @param direction the direction in which to move
     * @return the new position after the move
     */
    Position move(Direction direction);
}
