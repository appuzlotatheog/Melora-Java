package com.discord.musicbot.commands.admin;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import com.discord.musicbot.data.GuildSettingsManager;
import com.discord.musicbot.data.model.GuildSettings;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class DJCommand extends SlashCommand {

    @Override
    public String getName() {
        return "djmode";
    }

    @Override
    public void execute(CommandContext ctx) {
        String sub = ctx.getEvent().getSubcommandName();
        if (sub == null) { ctx.replyError("Invalid subcommand."); return; }

        if (sub.equals("grant") || sub.equals("revoke")) {
            if (!com.discord.musicbot.commands.framework.CommandRegistry.checkDjRole(ctx.getGuild(), ctx.getMember(), ctx.getMusicManager(), null)) {
                ctx.replyError("You need to be a DJ to use this command.");
                return;
            }
        } else {
            if (!ctx.getMember().hasPermission(net.dv8tion.jda.api.Permission.MANAGE_SERVER)) {
                ctx.replyError("You need `Manage Server` permission to configure DJ Mode.");
                return;
            }
        }

        GuildSettings settings = GuildSettingsManager.getInstance().getSettings(ctx.getGuild().getId());

        switch (sub) {
            case "on":
                settings.setDjMode(true);
                GuildSettingsManager.getInstance().markDirty();
                ctx.replySuccess("DJ Mode has been **enabled**.");
                break;
            case "off":
                settings.setDjMode(false);
                GuildSettingsManager.getInstance().markDirty();
                ctx.replySuccess("DJ Mode has been **disabled**.");
                break;
            case "role":
                net.dv8tion.jda.api.entities.Role role = ctx.getOption("role").getAsRole();
                settings.setDjRole(role.getId());
                GuildSettingsManager.getInstance().markDirty();
                ctx.replySuccess("DJ Role has been set to: " + role.getAsMention());
                break;
            case "grant":
                if (ctx.getMusicManager() == null) {
                    ctx.replyError("I am not actively playing music in this server.");
                    return;
                }
                net.dv8tion.jda.api.entities.User targetGrant = ctx.getOption("user").getAsUser();
                ctx.getMusicManager().grantTempDj(targetGrant.getId());
                ctx.replySuccess("Granted temporary DJ access to " + targetGrant.getAsMention() + " for this session.");
                break;
            case "revoke":
                if (ctx.getMusicManager() == null) {
                    ctx.replyError("I am not actively playing music in this server.");
                    return;
                }
                net.dv8tion.jda.api.entities.User targetRevoke = ctx.getOption("user").getAsUser();
                ctx.getMusicManager().revokeTempDj(targetRevoke.getId());
                ctx.replySuccess("Revoked temporary DJ access from " + targetRevoke.getAsMention() + ".");
                break;
            default:
                ctx.replyError("Unknown subcommand.");
        }
    }

    @Override
    public boolean requiresDj() { return false; } // Handled manually by permissions

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Configure DJ Mode settings and temporary access")
                .addSubcommands(
                        new SubcommandData("on", "Enable DJ Mode"),
                        new SubcommandData("off", "Disable DJ Mode"),
                        new SubcommandData("role", "Set the DJ Role")
                                .addOption(OptionType.ROLE, "role", "The role to set as DJ", true),
                        new SubcommandData("grant", "Grant temporary DJ access to a user")
                                .addOption(OptionType.USER, "user", "The user to grant access to", true),
                        new SubcommandData("revoke", "Revoke temporary DJ access from a user")
                                .addOption(OptionType.USER, "user", "The user to revoke access from", true)
                );
    }
}
