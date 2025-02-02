package me.voguh.zomboid.authcompanion.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvProperty {

    private static final Map<String, String> CONFIG_FILE = readConfig();

    @Required
    public static final String DISCORD_TOKEN = getEnvOrProperty("DISCORD_TOKEN");

    @Required
    public static final String DISCORD_PLAYER_ROLE = getEnvOrProperty("DISCORD_PLAYER_ROLE");

    @Required
    public static final String DISCORD_MODERATOR_ROLE = getEnvOrProperty("DISCORD_MODERATOR_ROLE");

    @Required
    public static final String DISCORD_ADMIN_ROLE = getEnvOrProperty("DISCORD_ADMIN_ROLE");

    @Required
    public static final String RCON_ADDRESS = getEnvOrProperty("RCON_ADDRESS");

    @Required
    public static final String RCON_PASSWORD = getEnvOrProperty("RCON_PASSWORD");

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface Required {

    }

    public static void startupValidate() throws Exception {
        for (Field field : EnvProperty.class.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers()) && field.isAnnotationPresent(Required.class) && Strings.isNullOrEmpty((String) field.get(null))) {
                throw new Exception("Required property '" + field.getName() + "' can't be founded in environment variables, java properties or config.ini file.");
            }
        }
    }

    private static String getEnvOrProperty(String name) {
        String env = System.getenv(name);
        if (env != null) {
            return env;
        }

        String prop = System.getProperty(name);
        if (prop != null) {
            return prop;
        }

        return CONFIG_FILE.get(name);
    }

    private static Map<String, String> readConfig() {
        try {
            Map<String, String> result = new HashMap<>();
            List<String> lines = Files.readAllLines(Path.of("config.ini"), StandardCharsets.UTF_8);

            for (String line : lines) {
                line = line.split("#")[0];

                if (!Strings.isNullOrEmpty(line)) {
                    String[] entry = line.split("=", 2);
                    String key = entry[0].trim();
                    String value = (entry.length > 1 ? entry[1] : "").trim();
                    result.put(key, value);
                }
            }

            return result;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

}
