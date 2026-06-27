package com.discord.musicbot.commands.user;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import com.discord.musicbot.data.StatsManager;
import com.discord.musicbot.data.model.UserStats;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class StatsCommand extends SlashCommand {

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public void execute(CommandContext ctx) {
        String userId = ctx.getUser().getId();
        UserStats stats = StatsManager.getInstance().getStats(userId);

        byte[] image = com.discord.musicbot.utils.CanvasHelper.generateStatsImage(ctx.getUser(), stats);
        ctx.getEvent().replyFiles(net.dv8tion.jda.api.utils.FileUpload.fromData(image, "stats.png")).queue();
    }

    @Override
    public boolean requiresDj() { return false; }

    @Override
    public boolean requiresVoice() { return false; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "View your personal listening statistics");
    }
}
