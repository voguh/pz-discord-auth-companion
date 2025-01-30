package me.voguh.zomboid.authcompanion;

import me.voguh.source.rcon.SourceRCONClient;
import me.voguh.zomboid.authcompanion.discord.DiscordManager;
import me.voguh.zomboid.authcompanion.util.EnvProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static SourceRCONClient rconClient;

    public static void main(String[] args) {
        try {
            EnvProperty.startupValidate();

            String[] rconAddress = EnvProperty.RCON_ADDRESS.split(":");
            if (rconAddress.length != 2) {
                throw new IllegalArgumentException("Source RCON address must have <IP_ADDRESS>:<PORT>.");
            }

            rconClient = new SourceRCONClient(rconAddress[0], Integer.parseInt(rconAddress[1]), EnvProperty.RCON_PASSWORD);

            DiscordManager.startClient(rconClient);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    if (rconClient != null) {
                        rconClient.close();
                    }

                    DiscordManager.stopClient();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });
    }

}
