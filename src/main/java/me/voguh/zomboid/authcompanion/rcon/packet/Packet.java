package me.voguh.zomboid.authcompanion.rcon.packet;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Packet {

    private int requestId;
    private int type;
    private byte[] payload;

}
