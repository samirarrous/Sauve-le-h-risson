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
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;

public class Gardener extends GameObject implements Movable, PickupVisitor, WalkVisitor {

    private int actualLevel=1;
    private int energy ;
    private Direction direction;
    private boolean moveRequested = false;
    private long lastMoveTime = 0; // moment du dernier mouvement
    private long lastRegenTime = 0; // moment de la dernière régénération


    public Gardener(Game game, Position position) {

        super(game, position);
        this.direction = Direction.DOWN;
        this.energy = game.configuration().gardenerEnergy();
    }

    @Override
    public void pickUp(EnergyBoost energyBoost) {
        this.energy = Math.min(Math.max(this.energy + 20, 0), this.game.configuration().gardenerEnergy());

        // Vérifier si le jeu est perdu en cas d'énergie nulle ou négative
        if (this.energy <= 0) {
            game.setStatus(Game.GameStatus.DEFEAT);
            System.out.println("Game is Loose :(");
        }
        System.out.println("The Player has pickup an item at the "+energyBoost.getPosition() + "Position ");


    }
    @Override
    public void pickUp(Hedgehog hedgehog) {
            System.out.println("The Player has pickup an item at the "+hedgehog.getPosition() + "Position ");
            game.setStatus(Game.GameStatus.VICTORY);
            System.out.println("Game is won :(");
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

        // Vérifier si le jardinier peut marcher sur ce décor
        if (next != null && !next.walkableBy(this) && this.energy >= next.energyConsumptionWalk()) {
            return false;
        }

        // Si c'est un décor autorisé, on permet le mouvement
        return true;
    }

    @Override
    public Position move(Direction direction) {

        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);
        this.energy -= next.energyConsumptionWalk();


        // Vérifier si le décor contient un bonus
        if (next != null ) {
            System.out.println("Found a bonus: " + next.getClass().getSimpleName());
            next.pickUpBy(this); // Ramasser le bonus
            if( next.getBonus() != null){
                Bonus bonus = next.getBonus();
                bonus.remove();
            }
                   // Supprimer le bonus après ramassage
        }

        return nextPos;
    }

    public void update(long now) {
        if (game.getStatus() == Game.GameStatus.RUNNING) {

            if (moveRequested) {
                if (canMove(direction)) {
                    move(direction);
                    lastMoveTime = now; // Mettre à jour le dernier mouvement
                }
            } else {
                if(energy<100){
                    // Si le joueur ne bouge pas depuis 1 seconde, régénère l'énergie toutes les secondes
                    if (now - lastMoveTime >= 1_000_000_000L && now - lastRegenTime >= 1_000_000_000L) {
                        regainEnergy();
                        lastRegenTime = now;
                    }
                }

            }

            moveRequested = false;




        }
    }


    public void hurt(int damage) {
        this.energy -= damage;
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

    private void regainEnergy() {
        int recoveryPerSeconds = 5;
        this.energy = Math.min(this.energy + recoveryPerSeconds, game.configuration().gardenerEnergy());
        System.out.println("Le joueur récupère de l'énergie : " + this.energy);
    }


}
