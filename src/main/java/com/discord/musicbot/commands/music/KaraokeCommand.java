package com.discord.musicbot.commands.music;

import com.discord.musicbot.audio.MusicManager;
import com.discord.musicbot.audio.PlayerManager;
import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class KaraokeCommand extends SlashCommand {

    @Override
    public String getName() {
        return "karaoke";
    }

    @Override
    public void execute(CommandContext ctx) {
        MusicManager manager = PlayerManager.getInstance().getMusicManager(ctx.getGuild().getIdLong());
        if (manager == null || manager.getPlayer().getPlayingTrack() == null) {
            ctx.replyError("I am not playing any music right now.");
            return;
        }

        boolean currentMode = manager.isKaraokeMode();
        manager.setKaraokeMode(!currentMode);

        if (!currentMode) {
            ctx.replySuccess("**Karaoke Mode ENABLED!** If synced lyrics are available, they will appear in the Now Playing embed.");
        } else {
            ctx.replySuccess("**Karaoke Mode DISABLED.**");
        }
    }

    @Override
    public boolean requiresDj() {
        return true; // Requires DJ to toggle karaoke
    }

    @Override
    public boolean requiresVoice() {
        return true;
    }

    @Override
    public boolean requiresBotInVoice() {
        return true;
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Toggle Live Karaoke mode for the current session");
    }
}
