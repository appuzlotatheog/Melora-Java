package com.discord.musicbot.commands.user;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import com.discord.musicbot.data.StatsManager;
import com.discord.musicbot.data.model.UserStats;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;



public class WrappedCommand extends SlashCommand {

    @Override
    public String getName() {
        return "wrapped";
    }

    @Override
    public void execute(CommandContext ctx) {
        User user = ctx.getUser();
        UserStats stats = StatsManager.getInstance().getStats(user.getId());

        String botName = ctx.getEvent().getJDA().getSelfUser().getName();
        byte[] image = com.discord.musicbot.utils.CanvasHelper.generateWrappedImage(user, stats, botName);
        ctx.getEvent().replyFiles(net.dv8tion.jda.api.utils.FileUpload.fromData(image, "wrapped.png")).queue();
    }

    @Override
    public boolean requiresDj() { return false; }

    @Override
    public boolean requiresVoice() { return false; }

    @Override
    public boolean requiresBotInVoice() { return false; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "View your personalized Wrapped stats");
    }
}
