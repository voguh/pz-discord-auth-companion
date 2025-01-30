package me.voguh.zomboid.authcompanion.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class EnvProperty {

    @Required
    public static final String DISCORD_TOKEN = getEnvOrProperty("DISCORD_TOKEN");

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
            if (field.isAnnotationPresent(Required.class) && Strings.isNullOrEmpty((String) field.get(null))) {
                throw new Exception("Missing or empty required property '" + field.getName() + "'");
            }
        }
    }

    private static String getEnvOrProperty(String name) {
        String env = System.getenv(name);
        if (env != null) {
            return env;
        }

        return System.getProperty(name);
    }

}
