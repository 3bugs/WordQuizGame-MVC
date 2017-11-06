package com.example.wordquizgame_mvc.model;

public class Word {

    private static final String TAG = Word.class.getName();

    public final String text;
    final String imageFilePath;

    Word(String category, String fileName) {
        this.text = fileName.toLowerCase().replace(".png", "");
        this.imageFilePath = category + "/" + fileName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Word) {
            Word word = (Word) obj;
            return word.text.equals(this.text);
        }
        return false;
    }
}
