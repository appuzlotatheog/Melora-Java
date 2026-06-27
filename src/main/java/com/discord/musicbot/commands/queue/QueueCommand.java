package com.discord.musicbot.commands.queue;

import com.discord.musicbot.commands.framework.CommandContext;
import com.discord.musicbot.commands.framework.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class QueueCommand extends SlashCommand {
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public void execute(CommandContext ctx) {
        String sub = ctx.getEvent().getSubcommandName();
        if (sub == null) {
            handleList(ctx);
            return;
        }

        switch (sub) {
            case "list":
                handleList(ctx);
                break;
            case "find":
                handleFind(ctx);
                break;
            case "dedupe":
                int deduped = ctx.getScheduler().dedupeQueue();
                ctx.replySuccess("Removed **" + deduped + "** duplicate tracks from the queue.");
                break;
            case "cleanup":
                int cleaned = ctx.getScheduler().cleanupQueue();
                ctx.replySuccess("Cleaned up **" + cleaned + "** tracks (duplicates/invalid) from the queue.");
                break;
            default:
                ctx.replyError("Unknown subcommand.");
        }
    }

    private void handleList(CommandContext ctx) {
        var pageOpt = ctx.getOption("page");
        int page = pageOpt != null ? Math.max(1, (int) pageOpt.getAsLong()) : 1;
        var userOpt = ctx.getOption("user");
        String filterUserId = userOpt != null ? userOpt.getAsString() : null;
        
        var embed = com.discord.musicbot.commands.framework.EmbedHelper.createQueueEmbed(ctx.getMusicManager(), page, filterUserId);
        
        long filteredSize = filterUserId == null ? ctx.getScheduler().getQueueSize() : 
            ctx.getScheduler().getQueue().stream().filter(t -> {
                Object ud = t.getUserData();
                String id = "";
                if (ud instanceof net.dv8tion.jda.api.entities.User u) id = u.getId();
                else if (ud instanceof String s) {
                    if (s.contains("\"requester\":\"")) id = s.split("\"requester\":\"")[1].split("\"")[0];
                    else id = s;
                }
                return id.equals(filterUserId);
            }).count();
            
        int maxPages = Math.max(1, (int) Math.ceil(filteredSize / 10.0));
        String prefix = filterUserId == null ? "queue" : "queue_" + filterUserId;
        var components = com.discord.musicbot.commands.framework.EmbedHelper.createPaginationButtons(prefix, page, maxPages);
        ctx.getEvent().replyEmbeds(embed).setComponents(net.dv8tion.jda.api.components.actionrow.ActionRow.of(components)).queue();
    }

    private void handleFind(CommandContext ctx) {
        String query = ctx.getOption("query").getAsString().toLowerCase();
        java.util.List<com.sedmelluq.discord.lavaplayer.track.AudioTrack> queue = ctx.getScheduler().getQueue();
        
        net.dv8tion.jda.api.EmbedBuilder eb = new net.dv8tion.jda.api.EmbedBuilder();
        eb.setTitle("Search Results in Queue");
        eb.setColor(java.awt.Color.DARK_GRAY);
        
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0; i < queue.size(); i++) {
            com.sedmelluq.discord.lavaplayer.track.AudioTrack t = queue.get(i);
            if (t.getInfo().title.toLowerCase().contains(query) || t.getInfo().author.toLowerCase().contains(query)) {
                sb.append("`").append(i + 1).append(".` ").append(t.getInfo().title).append("\n");
                count++;
                if (count >= 15) break;
            }
        }
        
        if (count == 0) {
            eb.setDescription("No tracks matched your query.");
        } else {
            if (count >= 15) sb.append("*...and more*");
            eb.setDescription(sb.toString());
        }
        
        ctx.getEvent().replyEmbeds(eb.build()).queue();
    }

    @Override
    public boolean requiresDj() { return false; } // Handled per-subcommand

    @Override
    public boolean requiresVoice() { return true; }

    @Override
    public boolean requiresBotInVoice() { return true; }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Manage the music queue")
                .addSubcommands(
                        new net.dv8tion.jda.api.interactions.commands.build.SubcommandData("list", "View the current queue")
                                .addOption(OptionType.INTEGER, "page", "Page number", false)
                                .addOption(OptionType.STRING, "user", "Filter queue by user", false, true),
                        new net.dv8tion.jda.api.interactions.commands.build.SubcommandData("find", "Search for a track in the queue")
                                .addOption(OptionType.STRING, "query", "Title or artist to search", true),
                        new net.dv8tion.jda.api.interactions.commands.build.SubcommandData("dedupe", "Remove duplicate tracks from the queue"),
                        new net.dv8tion.jda.api.interactions.commands.build.SubcommandData("cleanup", "Remove invalid/dead tracks from the queue")
                );
    }
}

