package com.discord.musicbot.commands.admin;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class AutoplayCommand extends SlashCommand {
    @Override
    public String getName() {
        return "autoplay";
    }

    @Override
    public void execute(CommandContext ctx) {
        boolean ap = ctx.getScheduler().toggleAutoplay();
        com.discord.musicbot.data.model.GuildSettings settings = com.discord.musicbot.data.GuildSettingsManager.getInstance().getSettings(ctx.getGuild().getId());
        settings.setAutoplay(ap);
        com.discord.musicbot.data.GuildSettingsManager.getInstance().markDirty();
        ctx.replySuccess("Autoplay is now " + (ap ? "ON" : "OFF"));
        ctx.getMusicManager().updateNowPlayingMessage();
    }

    @Override
    public boolean requiresDj() { return true; }

    @Override
    public boolean requiresVoice() { return true; }

    @Override
    public boolean requiresBotInVoice() { return true; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Autoplay command");
    }
}

