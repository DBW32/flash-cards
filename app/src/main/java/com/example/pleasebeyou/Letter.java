package com.example.pleasebeyou;

import android.graphics.Bitmap;
import java.io.Serializable;

public class Letter implements Serializable {
    private String letter;
    private String word;
    private String description;
    private String imageUrl;

    Letter(String letter, String word, String description, String imageUrl) {
        this.letter = letter;
        this.word = word;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getLetter() {
        return letter;
    }

    public String getWord() {
        return word;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() { return imageUrl; }
}
