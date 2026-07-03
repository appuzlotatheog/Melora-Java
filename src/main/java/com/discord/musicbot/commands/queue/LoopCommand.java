package com.discord.musicbot.commands.queue;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class LoopCommand extends SlashCommand {
    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public void execute(CommandContext ctx) {
        var modeOpt = ctx.getOption("mode");
        com.discord.musicbot.audio.TrackScheduler.LoopMode newMode;
        if (modeOpt == null) {
            newMode = ctx.getScheduler().cycleLoopMode();
        } else {
            String m = modeOpt.getAsString().toUpperCase();
            try {
                newMode = com.discord.musicbot.audio.TrackScheduler.LoopMode.valueOf(m);
                ctx.getScheduler().setLoopMode(newMode);
            } catch (Exception e) {
                ctx.replyError("Invalid mode. Use: off, track, or queue.");
                return;
            }
        }

        if (ctx.getScheduler().getCurrentTrack() == null) {
            ctx.replySuccess("Loop mode set to: **" + newMode.name() + "** (Will apply when songs start playing)");
        } else {
            ctx.replySuccess("Loop mode set to: **" + newMode.name() + "**");
            ctx.getMusicManager().updateNowPlayingMessage();
        }
    }

    @Override
    public boolean requiresDj() { return true; }

    @Override
    public boolean requiresVoice() { return true; }

    @Override
    public boolean requiresBotInVoice() { return true; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Set loop mode").addOption(OptionType.STRING, "mode", "off/track/queue", false);
    }
}

