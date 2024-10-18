package net.almer.useful_notes.client.widget;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.almer.useful_notes.UsefulNotes;
import net.almer.useful_notes.UsefulNotesClient;
import net.almer.useful_notes.client.screen.NoteSettingScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.item.Item;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public class NoteWidget extends ClickableWidget {
    private static final Identifier PATH = Identifier.of(UsefulNotes.MOD_ID, "textures/gui/sprites/");
    private static final Identifier SELECTED_TEXTURE = Identifier.of(UsefulNotes.MOD_ID, "textures/gui/sprites/selection.png");
    public int size;
    public int count = 0;
    private int xPos;
    private int yPos;
    private int textureWidth;
    private int textureHeight;
    private boolean dragging = false;
    public boolean selected = false;
    public int color = Colors.WHITE;
    private DefaultedList position = DefaultedList.ofSize(2, 0);
    public NoteWidget(int x, int y, int size, int color) {
        super(x, y, 12 * size, 16 * size, Text.literal(""));
        this.xPos = x;
        this.yPos = y;
        this.size = size;
        this.textureWidth = 12 * size;
        this.textureHeight = 16 * size;
        this.color = color;
    }
    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if(this.color == Colors.WHITE) {
            context.drawTexture(PATH.withSuffixedPath("white_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        else if(this.color == Colors.LIGHT_GRAY) {
            context.drawTexture(PATH.withSuffixedPath("light_gray_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        else if(this.color == Colors.GRAY) {
            context.drawTexture(PATH.withSuffixedPath("gray_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        else if(this.color == Colors.RED) {
            context.drawTexture(PATH.withSuffixedPath("red_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        else if(this.color == Colors.LIGHT_RED) {
            context.drawTexture(PATH.withSuffixedPath("light_red_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        else if(this.color == Colors.BLUE) {
            context.drawTexture(PATH.withSuffixedPath("blue_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        else if(this.color == Colors.GREEN) {
            context.drawTexture(PATH.withSuffixedPath("green_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        else if(this.color == Colors.YELLOW) {
            context.drawTexture(PATH.withSuffixedPath("yellow_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        else if(this.color == Colors.LIGHT_YELLOW) {
            context.drawTexture(PATH.withSuffixedPath("light_yellow_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        else{
            context.drawTexture(PATH.withSuffixedPath("white_note").withSuffixedPath(".png"), this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        if(this.selected){
            context.drawTexture(SELECTED_TEXTURE, this.xPos, this.yPos, 12 * size, 16 * size, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }
        TextWidget countText = new TextWidget(this.xPos, this.yPos + 12 * size, 12 * size, size, Text.literal(Integer.toString(this.count)), MinecraftClient.getInstance().textRenderer);
        countText.renderWidget(context, mouseX, mouseY, delta);
    }
    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
    public int getX(){
        return this.xPos;
    }
    public int getY(){
        return this.yPos;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0 && this.visible){
            if(this.clickInNote(mouseX, mouseY)) {
                this.dragging = true;
                ((NoteSettingScreen)MinecraftClient.getInstance().currentScreen).selected = this;
                this.selected = true;
                this.position.set(0, (int)mouseX - this.xPos);
                this.position.set(1, (int)mouseY - this.yPos);
                return true;
            }
            else{
                this.selected = false;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(button == 0) {
            this.dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(this.dragging){
            this.xPos = ((int)mouseX - ((int)this.position.get(0)));
            this.yPos = ((int)mouseY - ((int)this.position.get(1)));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    private boolean clickInNote(double mouseX, double mouseY){
        return mouseX >= this.getX() && mouseX < this.getX() + 12 * size
                && mouseY >= this.getY() && mouseY < this.getY() + 16 * size;
    }
}
