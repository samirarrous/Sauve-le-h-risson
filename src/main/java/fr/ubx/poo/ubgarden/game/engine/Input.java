/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.engine;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.BitSet;

import static javafx.scene.input.KeyCode.*;

public final class Input {

    /**
     * Bitset which registers if any {@link KeyCode} keeps being pressed or if it is
     * released.
     */
    private final BitSet keyboardBitSet = new BitSet();

    public Input(Scene scene) {
        // register key down
        EventHandler<KeyEvent> keyPressedEventHandler = event -> keyboardBitSet.set(event.getCode().ordinal(), true);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);

        // register key up
        EventHandler<KeyEvent> keyReleasedEventHandler = event -> keyboardBitSet.set(event.getCode().ordinal(), false);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
    }

    public void clear() {
        keyboardBitSet.clear();
    }

    private boolean is(KeyCode key) {
        return keyboardBitSet.get(key.ordinal());
    }

    // -------------------------------------------------
    // Evaluate bitset of pressed keys and return the gardener input.
    // If direction and its opposite direction are pressed simultaneously, then the
    // direction isn't handled.
    // -------------------------------------------------

    public boolean isMoveUp() {
        return is(UP) && !is(DOWN);
    }

    public boolean isMoveDown() {
        return is(DOWN) && !is(UP);
    }

    public boolean isMoveLeft() {
        return is(LEFT) && !is(RIGHT);
    }

    public boolean isMoveRight() {
        return is(RIGHT) && !is(LEFT);
    }

    public boolean isExit() {
        return is(ESCAPE);
    }
}
