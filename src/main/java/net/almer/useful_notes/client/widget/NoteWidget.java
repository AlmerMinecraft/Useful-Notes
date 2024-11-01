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
import java.util.Map;

@Environment(EnvType.CLIENT)
public class NoteWidget extends ClickableWidget {
    private static final Identifier PATH = Identifier.of(UsefulNotes.MOD_ID, "textures/gui/sprites/");
    private static final Identifier SELECTED_TEXTURE = Identifier.of(UsefulNotes.MOD_ID, "textures/gui/sprites/selection.png");

    private static final Map<Integer, String> COLOR_TEXTURE_MAP = Map.of(
            Colors.WHITE, "white_note",
            Colors.LIGHT_GRAY, "light_gray_note",
            Colors.GRAY, "gray_note",
            Colors.RED, "red_note",
            Colors.LIGHT_RED, "light_red_note",
            Colors.BLUE, "blue_note",
            Colors.GREEN, "green_note",
            Colors.YELLOW, "yellow_note",
            Colors.LIGHT_YELLOW, "light_yellow_note"
    );

    public int size;
    public int count = 0;
    private int xPos;
    private int yPos;
    private int textureWidth;
    private int textureHeight;
    private boolean dragging = false;
    public boolean selected = false;
    public int color = Colors.WHITE;
    private int posX;
    private int posY;

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
        String textureName = COLOR_TEXTURE_MAP.getOrDefault(this.color, "white_note");
        Identifier texture = PATH.withSuffixedPath(textureName + ".png");

        context.drawTexture(texture, this.xPos, this.yPos, this.textureWidth, this.textureHeight, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);

        if (this.selected) {
            context.drawTexture(SELECTED_TEXTURE, this.xPos, this.yPos, this.textureWidth, this.textureHeight, 0, 0, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
        }

        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(Integer.toString(this.count)), this.xPos, this.yPos + 12 * size, 12 * size, true);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

    public int getX() { return this.xPos; }
    public int getY() { return this.yPos; }
    int i = 0;
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.visible && this.clickInNote(mouseX, mouseY)) {
            this.dragging = true;
//            ((NoteSettingScreen) MinecraftClient.getInstance().currentScreen).selected = this;
            this.selected = true;
            posX = (int) mouseX - this.xPos;
            posY = (int) mouseY - this.yPos;
            return true;
        }
        this.selected = false;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            System.out.println(i);
            i++;
            this.dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.dragging) {
            int newXPos = (int) mouseX - posX;
            int newYPos = (int) mouseY - posY;
            this.xPos = newXPos;
            this.yPos = newYPos;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private boolean clickInNote(double mouseX, double mouseY) {
        return mouseX >= this.getX() && mouseX < this.getX() + this.textureWidth &&
                mouseY >= this.getY() && mouseY < this.getY() + this.textureHeight;
    }
}
