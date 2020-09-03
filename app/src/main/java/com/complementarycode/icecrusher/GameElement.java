package com.complementarycode.icecrusher;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class GameElement {

    protected int x, y, width, height;
    private Bitmap gameElement;

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getGameElement() {
        return gameElement;
    }
}
