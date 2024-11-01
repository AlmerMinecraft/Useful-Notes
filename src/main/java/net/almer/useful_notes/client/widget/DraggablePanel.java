package net.almer.useful_notes.client.widget;

import net.almer.useful_notes.UsefulNotes;
import net.almer.useful_notes.client.screen.NoteSettingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DraggablePanel extends ClickableWidget {
    private int x, y, width, height, deltaX, deltaY, edge;
    private List<ClickableWidget> widgets = new ArrayList<>();
    private Map<ClickableWidget, float[]> originalCoordinates = new HashMap<>();
    public boolean visible;
    public int count;
    private TextWidget textWidget = new TextWidget(40,20,Text.literal("sefsef"),MinecraftClient.getInstance().textRenderer);
    // В другом моем моде это отвечало за изменение размера панели
    private boolean ctrl = false;

    public DraggablePanel(int x, int y, int width, int height) {
        super(x,y,width,height, Text.empty());
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true; // По умолчанию панель видима
    }

    public DraggablePanel(int x, int y, int width, int height, boolean visible) {
        this(x, y, width, height);
        this.visible = visible;
    }

    public void add(ClickableWidget widget) {
        this.widgets.add(widget);
        updateOriginalCoordinates(widget);
    }

    public void addAll(List<ClickableWidget> widgets) {
        for (ClickableWidget widget : widgets) {
            add(widget);
        }
    }

    public void addAll(ClickableWidget... widgets) {
        for (ClickableWidget widget : widgets) {
            add(widget);
        }
    }

    private void updateOriginalCoordinates(ClickableWidget widget) {
        int relativeX = widget.getX() - this.x;
        int relativeY = widget.getY() - this.y;
        float coefficientX = (float) relativeX / this.width;
        float coefficientY = (float) relativeY / this.height;
        int widthRatio = this.width / widget.getWidth();
        int heightRatio = this.height / widget.getHeight();
        this.originalCoordinates.put(widget, new float[]{widthRatio, heightRatio, coefficientX, coefficientY});
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public List<ClickableWidget> getWidgets() {
        return widgets;
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {
        for (ClickableWidget widget : widgets) {
            consumer.accept(widget);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        deltaX = (int) mouseX - this.x;
        deltaY = (int) mouseY - this.y;
        if(isMouseOver(mouseX,mouseY)){
            ((NoteSettingScreen) MinecraftClient.getInstance().currentScreen).selectedNote = this;
        }
        for(ClickableWidget widget : widgets){
            widget.mouseClicked(mouseX,mouseY,button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isMouseOverPanel(mouseX, mouseY) && !ctrl) {
            this.x = (int) (mouseX - this.deltaX);
            this.y = (int) (mouseY - this.deltaY);
            updateWidgetsPosition();
        }
        for(ClickableWidget widget : widgets){
            widget.mouseDragged(mouseX, mouseY, button,deltaX,deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void updateWidgetsPosition() {
        for (Map.Entry<ClickableWidget, float[]> entry : originalCoordinates.entrySet()) {
            ClickableWidget widget = entry.getKey();
            float[] coords = entry.getValue();
            widget.setX((int) (this.x + coords[2] * this.width));
            widget.setY((int) (this.y + coords[3] * this.height));
        }
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return super.getNavigationPath(navigation);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return isMouseOverPanel(mouseX, mouseY);
    }

    @Override
    public void setFocused(boolean focused) {
        // Установка фокуса на панель
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Nullable
    @Override
    public GuiNavigationPath getFocusedPath() {
        return super.getFocusedPath();
    }

    @Override
    public int getNavigationOrder() {
        return super.getNavigationOrder();
    }

    private boolean isMouseOverPanel(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        for(ClickableWidget widget : widgets){
            widget.render(context, mouseX, mouseY, delta);
        }
        context.drawTexture(Identifier.of(UsefulNotes.MOD_ID,"textures/gui/sprites/white_note.png"),x,y,0,0,width,height,width,height);
        textWidget.setPosition(x,y + this.height - textWidget.getHeight());
        textWidget.setMessage(Text.literal(String.valueOf(count)));
        textWidget.renderWidget(context, mouseX, mouseY, delta);
    }

}