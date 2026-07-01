package com.discord.musicbot.commands.music;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import com.discord.musicbot.commands.framework.EmbedHelper;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class GrabCommand extends SlashCommand {
    @Override
    public String getName() { return "grab"; }

    @Override
    public void execute(CommandContext ctx) {
        AudioTrack track = ctx.getMusicManager().getPlayer().getPlayingTrack();
        if (track == null) {
            ctx.replyError("Nothing is currently playing!");
            return;
        }

        var t1 = net.dv8tion.jda.api.components.textdisplay.TextDisplay.of("### [" + EmbedHelper.escapeMarkdown(track.getInfo().title) + "](" + track.getInfo().uri + ")");
        var t2 = net.dv8tion.jda.api.components.textdisplay.TextDisplay.of("**Author:** " + track.getInfo().author + "\n**Duration:** " + EmbedHelper.formatDuration(track.getDuration()));
        net.dv8tion.jda.api.components.container.Container container;
        if (track.getInfo().uri.contains("youtube.com") || track.getInfo().uri.contains("youtu.be")) {
            var section = net.dv8tion.jda.api.components.section.Section.of(
                net.dv8tion.jda.api.components.thumbnail.Thumbnail.fromUrl("https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg"),
                t1, t2
            );
            container = net.dv8tion.jda.api.components.container.Container.of(section);
        } else {
            container = net.dv8tion.jda.api.components.container.Container.of(t1, t2);
        }
        final var finalContainer = container.withAccentColor(EmbedHelper.COLOR_MAIN);

        ctx.getMember().getUser().openPrivateChannel().queue(
            channel -> {
                channel.sendMessageComponents(finalContainer).useComponentsV2().queue(
                    success -> ctx.reply(EmbedHelper.MSG_SUCCESS + " I've sent you a DM with the current song!"),
                    error -> ctx.replyError("I couldn't send you a DM. Please check your privacy settings.")
                );
            },
            error -> ctx.replyError("I couldn't send you a DM. Please check your privacy settings.")
        );
    }

    @Override public boolean requiresDj() { return false; }
    @Override public boolean requiresVoice() { return false; }
    @Override public boolean requiresBotInVoice() { return true; }
    @Override public boolean requiresSameChannel() { return false; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "DMs you the currently playing track to save it for later");
    }
}
