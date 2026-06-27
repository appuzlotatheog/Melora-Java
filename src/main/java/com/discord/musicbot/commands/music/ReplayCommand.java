package com.discord.musicbot.commands.music;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import com.discord.musicbot.commands.framework.EmbedHelper;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ReplayCommand extends SlashCommand {
    @Override
    public String getName() { return "replay"; }

    @Override
    public void execute(CommandContext ctx) {
        if (ctx.getMusicManager().getPlayer().getPlayingTrack() == null) {
            ctx.replyError("Nothing is currently playing!");
            return;
        }
        
        ctx.getMusicManager().getPlayer().getPlayingTrack().setPosition(0);
        ctx.reply(EmbedHelper.MSG_SUCCESS + " Replaying the current track from the beginning!");
        ctx.getMusicManager().updateNowPlayingMessage();
    }

    @Override public boolean requiresDj() { return true; }
    @Override public boolean requiresVoice() { return true; }
    @Override public boolean requiresBotInVoice() { return true; }
    @Override public boolean requiresSameChannel() { return true; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Replays the currently playing track from the start");
    }
}
