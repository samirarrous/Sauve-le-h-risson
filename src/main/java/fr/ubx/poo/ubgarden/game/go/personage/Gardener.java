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
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.decor.*;

public class Gardener extends GameObject implements Movable, PickupVisitor, WalkVisitor {

    private int actualLevel = 1;
    private int energy;
    private int diseaseLevel ;
    private Direction direction;
    private boolean moveRequested = false;
    private long lastMoveTime = 0; // Moment du dernier mouvement
    private long diseaseEndTime = 0; // Moment où la maladie diminue
    private int timer = 1200000;

    public Gardener(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.energy = game.configuration().gardenerEnergy();
        this.diseaseLevel = 1;
    }

    @Override
    public void pickUp(EnergyBoost energyBoost) {
        this.energy = Math.min(Math.max(this.energy + 50, 0), this.game.configuration().gardenerEnergy());

        // Vérifier si le jeu est perdu en cas d'énergie nulle ou négative
        if (this.energy <= 0) {
            game.setStatus(Game.GameStatus.DEFEAT);
            System.out.println("Game is Loose :(");
        }
        System.out.println("The Player has picked up an item at the " + energyBoost.getPosition() + " Position ");
    }

    @Override
    public void pickUp(Hedgehog hedgehog) {
        game.setStatus(Game.GameStatus.VICTORY);
        System.out.println("The Player has won the game");
    }

    @Override
    public void pickUp(PoisonedApple poisonedApple) {
        // Augmenter la maladie de 1
        this.diseaseLevel += 1;

        // Calculer le moment où la maladie diminue
        long diseaseDurationMillis = game.configuration().diseaseDuration() * 1000L; // Convertir en millisecondes
        this.diseaseEndTime = System.currentTimeMillis() + diseaseDurationMillis;

        System.out.println("The Player has picked up a Poisoned Apple. Disease level: " + this.diseaseLevel);
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
        this.energy -= next.energyConsumptionWalk()*diseaseLevel;

        // Vérifier si le décor contient un bonus
        if (next != null) {
            next.pickUpBy(this); // Ramasser le bonus
            if (next.getBonus() != null) {
                Bonus bonus = next.getBonus();
                System.out.println("Found a bonus: " + bonus.getClass().getSimpleName());
                bonus.pickUpBy(this); // Ramasser le bonus
                bonus.remove();       // Supprimer le bonus après ramassage
            }
        }

        return nextPos;
    }

    public void update(long now) {
        if (game.getStatus() == Game.GameStatus.RUNNING) {

            // Gestion des mouvements
            if (moveRequested) {
                if (canMove(direction)) {
                    move(direction);
                    lastMoveTime = now; // Mettre à jour le dernier mouvement
                }
            } else {
                // Récupération d'énergie lorsqu'il n'y a pas de mouvement
                if (now - lastMoveTime >= game.configuration().energyRecoverDuration() * timer && energy < game.configuration().gardenerEnergy()) {
                    energy = Math.min(energy + 1, game.configuration().gardenerEnergy());
                    lastMoveTime = now;
                }
            }
            moveRequested = false;

            // Gestion de la fin de la maladie
            if (diseaseLevel > 0 && now >= diseaseEndTime) {
                diseaseLevel = Math.max(diseaseLevel - 1, 1); // Réduire la maladie de 1
                if (diseaseLevel == 1) {
                    System.out.println("The disease has ended.");
                } else {
                    // Recalculer le moment où la maladie diminue pour le niveau restant
                    diseaseEndTime = now + game.configuration().diseaseDuration() * timer;
                }
                System.out.println("The disease level has decreased. Current disease level: " + diseaseLevel);
            }
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

    public int getDiseaseLevel() {
        return diseaseLevel;
    }

    public void ChangeLevel(int levelToChange) {
        game.setStatus(Game.GameStatus.RUNNING);
        // Ajouter le changement de niveau
        System.out.println("Vous accédez au niveau " + levelToChange);
    }
}