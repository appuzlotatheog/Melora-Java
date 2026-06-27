package com.discord.musicbot.commands.admin;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.EmbedHelper;
import com.discord.musicbot.commands.framework.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class PingCommand extends SlashCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public void execute(CommandContext ctx) {
        long timeBefore = System.currentTimeMillis();
        ctx.getEvent().deferReply().queue(hook -> {
            long restPing = System.currentTimeMillis() - timeBefore;
            long gatewayPing = ctx.getEvent().getJDA().getGatewayPing();
            hook.sendMessage(
                    EmbedHelper.MSG_SUCCESS + String.format(" Gateway: `%dms` REST: `%dms`", gatewayPing, restPing))
                    .queue();
        });
    }

    @Override
    public boolean requiresDj() {
        return false;
    }

    @Override
    public boolean requiresVoice() {
        return false;
    }

    @Override
    public boolean requiresBotInVoice() {
        return false;
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Check bot latency");
    }
}
