package net.almer.useful_notes.client.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.util.Identifier;

public class Texture implements Drawable {
    private int x;
    private int y;
    private int width;
    private int height;
    private final Identifier texture;
    public boolean visible=true;
    public Texture(Identifier texture,int x,int y,int width,int height){
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.x = x - width/2;
        this.y = y - height/2;
    }
    public Texture(Identifier texture,int width,int height){
        this.texture = texture;
        this.width = width;
        this.height = height;
    }
    public void setPosition(int x,int y){
        this.setX(x);
        this.setY(y);
    }
    public void setX(int x){
        this.x=x-this.width/2;
    }
    public  void setY(int y){
        this.y=y-this.height/2;
    }
    public void setWidth(int width){
        this.width=width;
    }
    public void setHeight(int height){
        this.height=height;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public boolean isMouseOver(double mouseX,double mouseY){
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(visible) {
            context.drawTexture(texture,x,y,0,0,width,height);
        }
    }
}
