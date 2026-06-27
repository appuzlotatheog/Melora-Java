package com.discord.musicbot.commands.music;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import com.discord.musicbot.commands.framework.EmbedHelper;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class ForwardCommand extends SlashCommand {
    @Override
    public String getName() { return "forward"; }

    @Override
    public void execute(CommandContext ctx) {
        AudioTrack track = ctx.getMusicManager().getPlayer().getPlayingTrack();
        if (track == null) {
            ctx.replyError("Nothing is currently playing!");
            return;
        }
        
        int seconds = ctx.getOption("seconds").getAsInt();
        long newPosition = track.getPosition() + (seconds * 1000L);
        if (newPosition > track.getDuration()) {
            newPosition = track.getDuration();
        }
        track.setPosition(newPosition);
        
        ctx.reply(EmbedHelper.MSG_SUCCESS + " Skipped forward by `" + seconds + "` seconds. (" + EmbedHelper.formatDuration(track.getPosition()) + " / " + EmbedHelper.formatDuration(track.getDuration()) + ")");
        ctx.getMusicManager().updateNowPlayingMessage();
    }

    @Override public boolean requiresDj() { return true; }
    @Override public boolean requiresVoice() { return true; }
    @Override public boolean requiresBotInVoice() { return true; }
    @Override public boolean requiresSameChannel() { return true; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Skips forward by a specific amount of seconds")
            .addOption(OptionType.INTEGER, "seconds", "Number of seconds to skip forward", true);
    }
}
