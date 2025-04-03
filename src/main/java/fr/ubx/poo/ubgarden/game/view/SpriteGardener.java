/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteGardener extends Sprite {

    public SpriteGardener(Pane layer, Gardener gardener) {
        super(layer, null, gardener);
        updateImage();
    }

    @Override
    public void updateImage() {
        Gardener gardener = (Gardener) getGameObject();
        Image image = getImage(gardener.getDirection());
        setImage(image);
    }

    public Image getImage(Direction direction) {
        return ImageResourceFactory.getInstance().getGardener(direction);
    }
}
