package com.discord.musicbot;

import com.discord.musicbot.commands.framework.AutocompleteHandler;
import com.discord.musicbot.commands.framework.CommandRegistry;
import com.discord.musicbot.commands.framework.InteractionHandler;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommandHandler extends ListenerAdapter {
    private final CommandRegistry registry;

    public BotCommandHandler(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        registry.handleSlashCommand(event);
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        AutocompleteHandler.handle(event);
    }

    private final java.util.concurrent.ExecutorService interactionExecutor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        interactionExecutor.execute(() -> InteractionHandler.handleButton(event));
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        interactionExecutor.execute(() -> InteractionHandler.handleSelectMenu(event));
    }

    @Override
    public void onMessageReceived(net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser(),
                net.dv8tion.jda.api.entities.Message.MentionType.USER)) return;

        var section = net.dv8tion.jda.api.components.section.Section.of(
                net.dv8tion.jda.api.components.thumbnail.Thumbnail.fromUrl(event.getJDA().getSelfUser().getEffectiveAvatarUrl()),
                net.dv8tion.jda.api.components.textdisplay.TextDisplay.of("### Hi! I am " + event.getJDA().getSelfUser().getName()),
                net.dv8tion.jda.api.components.textdisplay.TextDisplay.of("Use `/play` or `/help` to know about me!!")
        );

        var container = net.dv8tion.jda.api.components.container.Container.of(
                section,
                net.dv8tion.jda.api.components.actionrow.ActionRow.of(
                        net.dv8tion.jda.api.components.buttons.Button.link("https://melora-info.vercel.app", "Website")
                )
        ).withAccentColor(com.discord.musicbot.commands.framework.EmbedHelper.COLOR_MAIN);

        event.getChannel().sendMessageComponents(container)
             .useComponentsV2()
             .queue();
    }
}
