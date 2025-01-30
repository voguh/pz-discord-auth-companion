package me.voguh.zomboid.authcompanion.settings;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class SettingsManager {
    private static final Yaml YAML_PARSER = new Yaml();
    private static final Path CONFIG_PATH = Path.of("config.yaml");

    private static Settings _settings;

    public static Settings settings() {
        return _settings;
    }

    public static void load() throws IOException, URISyntaxException {
        if (!Files.exists(CONFIG_PATH)) {
            Path templateConfigFilePath = Path.of(ClassLoader.getSystemResource("config.yaml").toURI());
            Files.writeString(CONFIG_PATH, Files.readString(templateConfigFilePath), StandardOpenOption.CREATE);
        }

        String rawConfigYaml = Files.readString(CONFIG_PATH);
        _settings = YAML_PARSER.loadAs(rawConfigYaml, Settings.class);
    }


}
