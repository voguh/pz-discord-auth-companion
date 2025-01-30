package me.voguh.zomboid.authcompanion.discord.adapter;

import me.voguh.source.rcon.SourceRCONClient;
import me.voguh.zomboid.authcompanion.util.EnvProperty;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GuildMemberListenerAdapter extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuildMemberListenerAdapter.class);

    private final SourceRCONClient rconClient;

    public GuildMemberListenerAdapter(SourceRCONClient rconClient) {
        this.rconClient = rconClient;
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        String username = event.getUser().getAsTag().split("#")[0];

        try {
            rconClient.command("setaccesslevel " + username + " none");
            rconClient.command("banuser " + username);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String username = event.getUser().getAsTag().split("#")[0];
        List<Role> roles = event.getMember().getRoles();

        try {
            if (isPlayer(roles)) {
                rconClient.command("unbanuser \"" + username + "\"");

                String accessLevel = isAdmin(roles) ? "admin" : isModerator(roles) ? "moderator" : "none";
                rconClient.command("setaccesslevel \"" + username + "\" \"" + accessLevel + "\"");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void onGuildMemberUpdate(@NotNull GuildMemberUpdateEvent event) {
        String username = event.getUser().getAsTag().split("#")[0];
        List<Role> roles = event.getMember().getRoles();

        try {
            if (isPlayer(roles)) {
                rconClient.command("unbanuser \"" + username + "\"");

                String accessLevel = isAdmin(roles) ? "admin" : isModerator(roles) ? "moderator" : "none";
                rconClient.command("setaccesslevel \"" + username + "\" \"" + accessLevel + "\"");
            } else {
                rconClient.command("setaccesslevel " + username + " none");
                rconClient.command("banuser " + username);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private boolean isAdmin(List<Role> roles) {
        return roles.stream().anyMatch(role -> role.getId().equals(EnvProperty.DISCORD_ADMIN_ROLE));
    }

    private boolean isModerator(List<Role> roles) {
        return roles.stream().anyMatch(role -> role.getId().equals(EnvProperty.DISCORD_MODERATOR_ROLE));
    }

    private boolean isPlayer(List<Role> roles) {
        return roles.stream().anyMatch(role -> role.getId().equals(EnvProperty.DISCORD_PLAYER_ROLE));
    }

}
