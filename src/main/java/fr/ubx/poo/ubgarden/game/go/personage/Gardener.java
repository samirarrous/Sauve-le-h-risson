/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.PickupVisitor;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;

public class Gardener extends GameObject implements Movable, PickupVisitor, WalkVisitor {

    private int actualLevel=1;
    private final int energy;
    private Direction direction;
    private boolean moveRequested = false;

    public Gardener(Game game, Position position) {

        super(game, position);
        this.direction = Direction.DOWN;
        this.energy = game.configuration().gardenerEnergy();
    }

    @Override
    public void pickUp(EnergyBoost energyBoost) {
        System.out.println("The Player has pickup an item at the "+energyBoost.getPosition() + "Position ");


    }


    public int getEnergy() {
        return this.energy;
    }


    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    @Override
    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        // Interdire de sortir de la map
        if (!game.world().getGrid().inside(nextPos)) {
            return false;
        }

        // Récupérer le décor sur la case cible
        Decor next = game.world().getGrid().get(nextPos);

        // Vérifier si le décor est un des types autorisés
        if (next instanceof Hedgehog ||next instanceof Grass) {
            return true;
        }

        // Si ce n'est pas un décor autorisé, on bloque le mouvement
        return false;
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);
        if (next != null)
            next.pickUpBy(this);
        return nextPos;
    }

    public void update(long now) {

        if(game.getStatus()==Game.GameStatus.RUNNING){//si le jeu n'est pas perdu/gagné


        if (moveRequested) {
            if (canMove(direction)) {
                move(direction);
            }
        }
        moveRequested = false;
        Decor decor = game.world().getGrid().get(getPosition());

        // Check if game is WON


        if (decor instanceof fr.ubx.poo.ubgarden.game.go.decor.Hedgehog) {
            System.out.println("Victoire ! Vous avez retrouvé le hérisson siuuuuuu!");
            game.setStatus(Game.GameStatus.VICTORY);
            System.out.println("Valeur actuelle de GameStatus : " + game.getStatus());
            actualLevel++;
            ChangeLevel(actualLevel);

        }

        //Check if Game Is Loose
        if(getEnergy()<=0){
            game.setStatus(Game.GameStatus.DEFEAT);
            System.out.println("Game is Loose :(");
        }
        }
    }

    public void hurt(int damage) {
    }

    public void hurt() {
        hurt(1);
    }

    public Direction getDirection() {
        return direction;
    }

    public void ChangeLevel(int levelToChange){
        game.setStatus(Game.GameStatus.RUNNING);
        //ajouter le changement de niveau
        System.out.println("Vous accédez au niveau "+levelToChange);
    }

}
