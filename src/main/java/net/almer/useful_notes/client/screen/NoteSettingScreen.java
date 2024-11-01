package net.almer.useful_notes.client.screen;


import net.almer.useful_notes.UsefulNotes;
import net.almer.useful_notes.UsefulNotesClient;
import net.almer.useful_notes.UsefulNotesEventHandler;
import net.almer.useful_notes.client.widget.DraggablePanel;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class NoteSettingScreen extends Screen {
    private static final Text RETURN_TO_GAME_TEXT = Text.literal("x");
    private static final Text SETTINGS_TEXT = Text.translatable("gui.note_settings");
    private final List<DraggablePanel> notes = new ArrayList<>();
    public DraggablePanel selectedNote;
    private TextIconButtonWidget itemChoose = TextIconButtonWidget.builder(Text.literal("📦"), (button) -> {
        MinecraftClient.getInstance().setScreen(new ItemSelectionScreen());
    }, true).width(20).texture(Identifier.of(UsefulNotes.MOD_ID, "icon/item_choose"), 16, 16).build();
    private final ButtonWidget plus = ButtonWidget.builder(Text.literal("+")
            , (button) -> {
        if(selectedNote != null) {
            selectedNote.count++;
        }
    }).width(20).build();
    private ButtonWidget minus = ButtonWidget.builder(Text.literal("-"), (button) -> {
        if(selectedNote != null) {
            selectedNote.count--;
        }
    }).width(20).build();
    private ButtonWidget color = ButtonWidget.builder(Text.of("color"), (button) -> {
        selectedNote.getNextColor();
    }).width(20).build();
    private TextIconButtonWidget remove = TextIconButtonWidget.builder(RETURN_TO_GAME_TEXT, (button) -> {
                for(ClickableWidget widget : selectedNote.getWidgets()){
                    remove(widget);
                }
                this.remove(selectedNote);
                notes.remove(selectedNote);
            },
            true).width(20).texture(Identifier.of(UsefulNotes.MOD_ID, "icon/delete_note"), 15, 15).build();

    public NoteSettingScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);
        GridWidget gridWidget1 = new GridWidget();
        gridWidget1.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder colorAdder = gridWidget1.createAdder(1);
        adder.add(itemChoose, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(plus, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(minus, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(color, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(remove, 1, gridWidget.copyPositioner().alignRight().alignTop());
        adder.add(TextIconButtonWidget.builder(RETURN_TO_GAME_TEXT, (button) -> {
                    DraggablePanel note = new DraggablePanel(0,0,60,80);
                    notes.add(note);
                    addDrawableChild(note);
                },
                true).width(20).texture(Identifier.of(UsefulNotes.MOD_ID, "icon/add_note"), 15, 15).build(), 1, gridWidget.copyPositioner().alignRight().alignBottom());
        adder.add(TextIconButtonWidget.builder(RETURN_TO_GAME_TEXT, (button) -> {
                for(DraggablePanel note : notes){
                    for(ClickableWidget widget : note.getWidgets()){
                        remove(widget);
                    }
                    this.remove(note);
                }
                },
                true).width(20).texture(Identifier.of(UsefulNotes.MOD_ID,"icon/remove_notes"), 13, 13).build(), 1, gridWidget.copyPositioner().alignRight().alignBottom());
        adder.add(ButtonWidget.builder(RETURN_TO_GAME_TEXT, (button) -> {
            this.client.setScreen((Screen)null);
            this.client.mouse.lockCursor();
        }).width(20).build(), 1, gridWidget.copyPositioner().alignRight().alignBottom());


        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.999F, 0.5F);
        gridWidget.forEachChild(this::addDrawableChild);
        gridWidget1.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget1, 0, 0, this.width, this.height, 0.89F, 0.5F);
        gridWidget1.forEachChild(this::addDrawableChild);

        super.init();
    }

}
