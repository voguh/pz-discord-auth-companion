package me.voguh.zomboid.authcompanion.settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Settings {

    private String playersRoleId;
    private String moderatorsRoleId;
    private String adminsRoleId;

}
