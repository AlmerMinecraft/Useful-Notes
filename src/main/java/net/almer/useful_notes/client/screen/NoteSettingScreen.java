package net.almer.useful_notes.client.screen;


import net.almer.useful_notes.UsefulNotes;
import net.almer.useful_notes.UsefulNotesClient;
import net.almer.useful_notes.UsefulNotesEventHandler;
import net.almer.useful_notes.client.widget.NoteWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class NoteSettingScreen extends Screen {
    private static final Text RETURN_TO_GAME_TEXT = Text.literal("x");
    private static final Text SETTINGS_TEXT = Text.translatable("gui.note_settings");
    private Text colorText = Text.literal("⬛");
    private final boolean showSets;
    private DefaultedList<NoteWidget> notes = DefaultedList.of();
    public GridWidget noteGrid;
    public GridWidget.Adder noteAdder;
    public NoteWidget selected;
    private boolean colorChoosing = false;
    private DefaultedList noteSettings = DefaultedList.ofSize(6);
    private DefaultedList colors = DefaultedList.ofSize(9);
    private TextFieldWidget text = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, this.width - 26, this.height / 2, 40, 20, Text.literal("0"));
    public final DefaultedList<ItemStack> itemList = DefaultedList.of();
    private TextIconButtonWidget itemChoose = TextIconButtonWidget.builder(Text.literal("📦"), (button) -> {
        this.client.setScreen((Screen)null);
        this.client.mouse.lockCursor();
    }, true).width(20).texture(Identifier.of(UsefulNotes.MOD_ID, "icon/item_choose"), 16, 16).build();
    private ButtonWidget plus = ButtonWidget.builder(Text.literal("+"), (button) -> {
        this.selected.size++;
    }).width(20).build();
    private ButtonWidget minus = ButtonWidget.builder(Text.literal("-"), (button) -> {
        this.selected.size--;
    }).width(20).build();
    private ButtonWidget color = ButtonWidget.builder(this.colorText, (button) -> {
        this.toggleColorChoose();
    }).width(20).build();

    private ButtonWidget white = ButtonWidget.builder(colorText.copy().withColor(Colors.WHITE), (button) -> {
        this.selected.color = Colors.WHITE;
    }).size(15, 15).build();
    private ButtonWidget gray = ButtonWidget.builder(colorText.copy().withColor(Colors.GRAY), (button) -> {
        this.selected.color = Colors.GRAY;
    }).size(15, 15).build();
    private ButtonWidget light_gray = ButtonWidget.builder(colorText.copy().withColor(Colors.LIGHT_GRAY), (button) -> {
        this.selected.color = Colors.LIGHT_GRAY;
    }).size(15, 15).build();
    private ButtonWidget red = ButtonWidget.builder(colorText.copy().withColor(Colors.RED), (button) -> {
        this.selected.color = Colors.RED;
    }).size(15, 15).build();
    private ButtonWidget blue = ButtonWidget.builder(colorText.copy().withColor(Colors.BLUE), (button) -> {
        this.selected.color = Colors.BLUE;
    }).size(15, 15).build();
    private ButtonWidget green = ButtonWidget.builder(colorText.copy().withColor(Colors.GREEN), (button) -> {
        this.selected.color = Colors.GREEN;
    }).size(15, 15).build();
    private ButtonWidget light_yellow = ButtonWidget.builder(colorText.copy().withColor(Colors.LIGHT_YELLOW), (button) -> {
        this.selected.color = Colors.LIGHT_YELLOW;
    }).size(15, 15).build();
    private ButtonWidget light_red = ButtonWidget.builder(colorText.copy().withColor(Colors.LIGHT_RED), (button) -> {
        this.selected.color = Colors.LIGHT_RED;
    }).size(15, 15).build();
    private ButtonWidget yellow = ButtonWidget.builder(colorText.copy().withColor(Colors.YELLOW), (button) -> {
        this.selected.color = Colors.YELLOW;
    }).size(15, 15).build();
    private TextIconButtonWidget remove = TextIconButtonWidget.builder(RETURN_TO_GAME_TEXT, (button) -> {
                this.selected.visible = false;
                for(int i = 0; i < this.notes.size(); i++){
                    if(this.notes.get(i) == this.selected){
                        this.notes.remove(i);
                    }
                }
                this.colorChoosing = false;
                this.selected = null;
            },
            true).width(20).texture(Identifier.of(UsefulNotes.MOD_ID, "icon/delete_note"), 15, 15).build();
    public NoteSettingScreen(boolean showSets) {
        super(SETTINGS_TEXT);
        this.showSets = showSets;
    }
    @Override
    protected void init() {
        super.init();
        if(showSets){
            this.initWidgets();
        }
        int var10004 = 10;
        int var10005 = this.width;
        Objects.requireNonNull(this.textRenderer);
        this.addDrawableChild(new TextWidget(0, var10004, var10005, 9, this.title, this.textRenderer));
        if(UsefulNotesEventHandler.getNotes() != null){
            this.notes = UsefulNotesEventHandler.getNotes();
            for(int i = 0; i < this.notes.size(); i++){
                this.notes.get(i).selected = false;
            }
        }
        this.itemList.addAll(ItemGroups.getSearchGroup().getDisplayStacks());
    }
    private void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);
        GridWidget gridWidget1 = new GridWidget();
        gridWidget1.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder colorAdder = gridWidget1.createAdder(1);
        noteGrid = new GridWidget();
        noteGrid.getMainPositioner().margin(4, 4, 4, 0);
        noteAdder = noteGrid.createAdder(10);
        adder.add(itemChoose, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(plus, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(minus, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(color, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(text);
        adder.add(remove, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(TextIconButtonWidget.builder(RETURN_TO_GAME_TEXT, (button) -> {
            NoteWidget note = new NoteWidget(this.width / 2 - 24, this.height / 2 - 32, 4, Colors.WHITE);
            noteAdder.add(note, 1, noteGrid.copyPositioner());
            this.notes.addFirst(note);
                },
                true).width(20).texture(Identifier.of(UsefulNotes.MOD_ID, "icon/add_note"), 15, 15).build(), 1, gridWidget.copyPositioner().alignRight().alignBottom());
        adder.add(TextIconButtonWidget.builder(RETURN_TO_GAME_TEXT, (button) -> {
            for(int i = 0; i < this.notes.size(); i++){
                if(this.notes.get(i) != null){
                    this.notes.get(i).visible = false;
                    this.notes.get(i).active = false;
                }
            }
            this.notes.clear();
            this.colorChoosing = false;
            this.selected = null;
                },
                true).width(20).texture(Identifier.of(UsefulNotes.MOD_ID,"icon/remove_notes"), 13, 13).build(), 1, gridWidget.copyPositioner().alignRight().alignBottom());
        adder.add(ButtonWidget.builder(RETURN_TO_GAME_TEXT, (button) -> {
            this.client.setScreen((Screen)null);
            this.client.mouse.lockCursor();
        }).width(20).build(), 1, gridWidget.copyPositioner().alignRight().alignBottom());

        colorAdder.add(white, 1, gridWidget1.copyPositioner());
        colorAdder.add(light_gray, 1, gridWidget1.copyPositioner());
        colorAdder.add(gray, 1, gridWidget1.copyPositioner());
        colorAdder.add(red, 1, gridWidget1.copyPositioner());
        colorAdder.add(light_red, 1, gridWidget1.copyPositioner());
        colorAdder.add(yellow, 1, gridWidget1.copyPositioner());
        colorAdder.add(light_yellow, 1, gridWidget1.copyPositioner());
        colorAdder.add(green, 1, gridWidget1.copyPositioner());
        colorAdder.add(blue, 1, gridWidget1.copyPositioner());

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.999F, 0.5F);
        gridWidget.forEachChild(this::addDrawableChild);
        gridWidget1.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget1, 0, 0, this.width, this.height, 0.89F, 0.5F);
        gridWidget1.forEachChild(this::addDrawableChild);

        this.noteSettings.addFirst(this.remove);
        this.noteSettings.addFirst(this.text);
        this.noteSettings.addFirst(this.minus);
        this.noteSettings.addFirst(this.plus);
        this.noteSettings.addFirst(this.color);
        this.noteSettings.addFirst(this.itemChoose);

        this.colors.addFirst(this.white);
        this.colors.addFirst(this.light_gray);
        this.colors.addFirst(this.gray);
        this.colors.addFirst(this.blue);
        this.colors.addFirst(this.green);
        this.colors.addFirst(this.red);
        this.colors.addFirst(this.light_red);
        this.colors.addFirst(this.yellow);
        this.colors.addFirst(this.light_yellow);
    }
    public void tick() {
        super.tick();
        UsefulNotesEventHandler.setNotes(this.notes);
        if(this.selected != null){
            for(int i = 0; i < this.noteSettings.size(); i++){
                if(this.noteSettings.get(i) instanceof ButtonWidget){
                    ((ButtonWidget)this.noteSettings.get(i)).active = true;
                }
                else if(this.noteSettings.get(i) instanceof TextIconButtonWidget){
                    ((TextIconButtonWidget)this.noteSettings.get(i)).active = true;
                }
                else if(this.noteSettings.get(i) instanceof TextFieldWidget){
                    ((TextFieldWidget)this.noteSettings.get(i)).active = true;
                }
            }
            if(!(this.selected.size < 6)){
                this.plus.active = false;
            }
            else{
                this.plus.active = true;
            }
            if(!(this.selected.size > 1)){
                this.minus.active = false;
            }
            else{
                this.minus.active = true;
            }
            int selectedCount = 0;
            for(int i = 0; i < this.notes.size(); i++){
                if(this.notes.get(i).selected){
                    selectedCount++;
                }
            }
            if(selectedCount > 1){
                for(int i = 0; i < this.notes.size(); i++){
                    if(this.notes.get(i).selected && this.notes.get(i) != this.selected){
                        this.notes.get(i).selected = false;
                    }
                }
            }
            if(isInteger(this.text.getText())){
                this.selected.count = Integer.parseInt(this.text.getText());
            }
        }
        else{
            for(int i = 0; i < this.noteSettings.size(); i++){
                if(this.noteSettings.get(i) instanceof ButtonWidget){
                    ((ButtonWidget)this.noteSettings.get(i)).active = false;
                }
                else if(this.noteSettings.get(i) instanceof TextIconButtonWidget){
                    ((TextIconButtonWidget)this.noteSettings.get(i)).active = false;
                }
                else if(this.noteSettings.get(i) instanceof TextFieldWidget){
                    ((TextFieldWidget)this.noteSettings.get(i)).active = false;
                }
            }
            for(int i = 0; i < this.colors.size(); i++){
                ((ButtonWidget)this.colors.get(i)).active = false;
                ((ButtonWidget)this.colors.get(i)).visible = false;
            }
        }
        if(this.colorChoosing){
            for(int i = 0; i < this.colors.size(); i++){
                ((ButtonWidget)this.colors.get(i)).active = true;
                ((ButtonWidget)this.colors.get(i)).visible = true;
            }
        }
        else{
            for(int i = 0; i < this.colors.size(); i++){
                ((ButtonWidget)this.colors.get(i)).active = false;
                ((ButtonWidget)this.colors.get(i)).visible = false;
            }
        }
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        for(int i = 0; i < this.notes.size(); i++){
            if(this.notes.get(i) != null){
                if(this.notes.get(i).visible) {
                    this.notes.get(i).renderWidget(context, mouseX, mouseY, delta);
                    noteAdder.add(this.notes.get(i), 1, noteGrid.copyPositioner());
                }
            }
        }
        noteGrid.refreshPositions();
        SimplePositioningWidget.setPos(noteGrid, 0, 0, this.width, this.height, 0.0F, 0.0F);
        noteGrid.forEachChild(this::addDrawableChild);
    }
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.showSets) {
            super.renderBackground(context, mouseX, mouseY, delta);
        }
    }
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    private void toggleColorChoose(){
        this.colorChoosing = !this.colorChoosing;
    }
}
