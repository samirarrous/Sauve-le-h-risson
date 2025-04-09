/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class NestWasp extends Decor {
    public NestWasp(Position position) {
        super(position);
    }
    private long lastSpawnTime = 0;
    private final long spawnCooldown = 3_000_000_000L; // 3 secondes en nanosecondes



    }
