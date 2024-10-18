package net.almer.useful_notes.client.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

@Environment(EnvType.CLIENT)
public class NoteHud {
    private final MinecraftClient client;
    private final TextRenderer textRenderer;
    private boolean showNotes;
    public NoteHud(MinecraftClient client){
        this.client = client;
        this.textRenderer = client.textRenderer;
    }
    public boolean shouldShowNotes() {
        return this.showNotes && !this.client.options.hudHidden;
    }
    public void toggleNotes(){
        this.showNotes = !this.showNotes;
    }
}
