package com.complementarycode.icecrusher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.complementarycode.icecrusher.GameView.screenRatioX;
import static com.complementarycode.icecrusher.GameView.screenRatioY;

public class Animal extends GameElement {

    static int index = 0;
    Resources res;
    private int [] btmAnimals = {R.drawable.polarbear, R.drawable.polar1, R.drawable.polar2,
            R.drawable.polar3};
    public int speed = 5;
    public boolean wasShot = true;
    Bitmap animal;

    Animal(Resources res) {

        this.res = res;

        x = 0;

        processOneAnimal();

        y = -height;
    }

    public void processOneAnimal() {
        animal = BitmapFactory.decodeResource(res, btmAnimals[index]);

        width = animal.getWidth();
        height = animal.getHeight();

        width /= 11;
        height /= 11;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        animal = Bitmap.createScaledBitmap(animal, width, height, false);
        index++;
        if (index > 3) {
            index = 0;
        }
    }

    Bitmap getGameElement() {
        return animal;
    }
}

