package me.voguh.zomboid.authcompanion.discord;

import me.voguh.source.rcon.SourceRCONClient;
import me.voguh.zomboid.authcompanion.discord.adapter.GuildMemberListenerAdapter;
import me.voguh.zomboid.authcompanion.util.EnvProperty;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Set;

public class DiscordManager {

    private static JDA client;

    public static void startClient(SourceRCONClient rconClient) {
        Set<GatewayIntent> intents = Set.of(GatewayIntent.GUILD_MEMBERS);
        client = JDABuilder.createLight(EnvProperty.DISCORD_TOKEN, intents)
            .addEventListeners(new GuildMemberListenerAdapter(rconClient))
            .build();
    }

    public static void stopClient() throws InterruptedException {
        client.shutdown();
        client.awaitShutdown();
    }

}
