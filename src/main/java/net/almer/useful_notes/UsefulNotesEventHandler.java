package net.almer.useful_notes;

import net.almer.useful_notes.client.screen.NoteSettingScreen;
import net.almer.useful_notes.client.widget.NoteWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class UsefulNotesEventHandler {
    private static final KeyBinding OPEN_SETTINGS_KEY;
    private static final KeyBinding TOGGLE_NOTES_KEY;
    private static DefaultedList<NoteWidget> notes;
    static {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            OPEN_SETTINGS_KEY = Util.make(() -> {
                KeyBinding keybind = new KeyBinding("key.useful-notes.open_settings", GLFW.GLFW_KEY_N, "key.category.useful-notes.notes");
                KeyBindingHelper.registerKeyBinding(keybind);
                return keybind;
            });
            TOGGLE_NOTES_KEY = Util.make(() -> {
                KeyBinding keybind = new KeyBinding("key.useful-notes.toggle_notes", GLFW.GLFW_KEY_F4, "key.category.useful-notes.notes");
                KeyBindingHelper.registerKeyBinding(keybind);
                return keybind;
            });
        }
        else{
            OPEN_SETTINGS_KEY = null;
            TOGGLE_NOTES_KEY = null;
        }
    }
    @Environment(EnvType.CLIENT)
    public static void registerClientEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(UsefulNotesEventHandler::onClientTick);
    }
    @Environment(EnvType.CLIENT)
    private static void onClientTick(MinecraftClient client){
        if(client.world == null || client.player == null) return;
        if(OPEN_SETTINGS_KEY.wasPressed() && client.currentScreen == null){
            client.setScreen(new NoteSettingScreen(Text.empty()));
        }
    }
    public static void setNotes(DefaultedList<NoteWidget> note){
        notes = note;
    }
    public static DefaultedList<NoteWidget> getNotes(){
        return notes;
    }
}
