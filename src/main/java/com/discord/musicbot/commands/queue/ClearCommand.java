package com.discord.musicbot.commands.queue;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ClearCommand extends SlashCommand {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public void execute(CommandContext ctx) {
        if (ctx.getScheduler().getQueueSize() == 0) {
            ctx.replyError("The queue is already empty.");
            return;
        }
        var userOpt = ctx.getOption("user");
        String filterUserId = userOpt != null ? userOpt.getAsString() : null;
        
        int size = ctx.getScheduler().clear(filterUserId);
        
        if (filterUserId == null) {
            ctx.replySuccess("Cleared " + size + " tracks from the queue.");
        } else {
            String mention = "<@" + filterUserId + ">";
            ctx.replySuccess("Cleared " + size + " tracks queued by " + mention + ".");
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
        return Commands.slash(getName(), "Clear the queue")
                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "user", "Clear only tracks queued by this user", false, true);
    }
}

