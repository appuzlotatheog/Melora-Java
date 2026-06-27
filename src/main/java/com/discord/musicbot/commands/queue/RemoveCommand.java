package com.discord.musicbot.commands.queue;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class RemoveCommand extends SlashCommand {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void execute(CommandContext ctx) {
        int pos = ctx.getOption("position").getAsInt() - 1;
        var queue = ctx.getScheduler().getQueue();
        if (pos < 0 || pos >= queue.size()) {
            ctx.replyError("Invalid position.");
            return;
        }
        var track = queue.get(pos);
        
        // DJ Check Exception: Users can remove their own tracks
        com.discord.musicbot.data.model.GuildSettings settings = com.discord.musicbot.data.GuildSettingsManager.getInstance().getSettings(ctx.getGuild().getId());
        if (settings.isDjMode()) {
            boolean isOwner = false;
            Object ud = track.getUserData();
            if (ud instanceof net.dv8tion.jda.api.entities.User u) isOwner = u.getId().equals(ctx.getUser().getId());
            else if (ud instanceof String s) {
                if (s.contains("\"requester\":\"")) isOwner = s.split("\"requester\":\"")[1].split("\"")[0].equals(ctx.getUser().getId());
                else isOwner = s.equals(ctx.getUser().getId());
            }
            if (!isOwner) {
                boolean hasDj = ctx.getMember().hasPermission(net.dv8tion.jda.api.Permission.MANAGE_SERVER) || ctx.getMember().hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
                if (!hasDj) {
                    String djRoleId = settings.getDjRole();
                    if (djRoleId != null) hasDj = ctx.getMember().getRoles().stream().anyMatch(r -> r.getId().equals(djRoleId));
                    else hasDj = ctx.getMember().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("DJ"));
                }
                if (!hasDj) {
                    ctx.replyError("DJ mode is enabled. You can only remove your own tracks, unless you have the DJ role or Manage Server permission.");
                    return;
                }
            }
        }

        track = ctx.getScheduler().remove(pos);
        if (track != null) {
            ctx.replySuccess("Removed: " + track.getInfo().title);
        } else {
            ctx.replyError("Invalid position.");
        }
    }

    @Override
    public boolean requiresDj() { return false; }

    @Override
    public boolean requiresVoice() { return true; }

    @Override
    public boolean requiresBotInVoice() { return true; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Remove track").addOption(OptionType.INTEGER, "position", "Queue position", true, true);
    }
}

