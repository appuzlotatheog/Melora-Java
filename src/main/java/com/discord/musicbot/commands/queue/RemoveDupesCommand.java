package com.discord.musicbot.commands.queue;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import com.discord.musicbot.commands.framework.EmbedHelper;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class RemoveDupesCommand extends SlashCommand {
    @Override
    public String getName() { return "removedupes"; }

    @Override
    public void execute(CommandContext ctx) {
        List<AudioTrack> queue = ctx.getMusicManager().getScheduler().getQueue();
        if (queue.isEmpty()) {
            ctx.replyError("The queue is currently empty.");
            return;
        }

        Set<String> seenUrls = new HashSet<>();
        List<AudioTrack> toKeep = new ArrayList<>();
        
        AudioTrack current = ctx.getMusicManager().getPlayer().getPlayingTrack();
        if (current != null) {
            seenUrls.add(current.getInfo().uri);
        }

        int removed = 0;
        for (AudioTrack track : queue) {
            if (seenUrls.contains(track.getInfo().uri)) {
                removed++;
            } else {
                seenUrls.add(track.getInfo().uri);
                toKeep.add(track);
            }
        }

        if (removed == 0) {
            ctx.reply(EmbedHelper.MSG_SUCCESS + " No duplicate tracks were found in the queue.");
            return;
        }

        ctx.getMusicManager().getScheduler().getQueueRaw().clear();
        ctx.getMusicManager().getScheduler().getQueueRaw().addAll(toKeep);
        
        ctx.reply(EmbedHelper.MSG_SUCCESS + " Removed `" + removed + "` duplicate tracks from the queue.");
    }

    @Override public boolean requiresDj() { return true; }
    @Override public boolean requiresVoice() { return true; }
    @Override public boolean requiresBotInVoice() { return true; }
    @Override public boolean requiresSameChannel() { return true; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Removes all duplicate tracks from the queue");
    }
}
