package net.almer.useful_notes.yaml;

import org.yaml.snakeyaml.constructor.Constructor;

public class Note {
    private String item;
    private int count;
    private int color;
    private int x;
    private int y;
    private boolean selected;

    public Note() {
    }

    public Note(String item, int count, int color,int x,int y,boolean selected) {
        this.item = item;
        this.count = count;
        this.color = color;
        this.x = x;
        this.y = y;
        this.selected = selected;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
