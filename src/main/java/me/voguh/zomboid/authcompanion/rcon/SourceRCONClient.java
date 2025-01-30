package me.voguh.zomboid.authcompanion.rcon;

import me.voguh.zomboid.authcompanion.rcon.exception.AuhenticationException;
import me.voguh.zomboid.authcompanion.rcon.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class SourceRCONClient implements Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceRCONClient.class);

    private static final int SERVERDATA_AUTH = 3;
    private static final int SERVERDATA_AUTH_RESPONSE = 2;
    private static final int SERVERDATA_EXECCOMMAND = 2;
    private static final int SERVERDATA_RESPONSE_VALUE = 0;

    private final String password;
    private final Socket socket;
    private int requestId;

    public SourceRCONClient(String host, int port, String password) throws IOException, AuhenticationException {
        this.password = password;
        this.socket = new Socket(host, port);
        this.requestId = 0;

        if (!authenticate(password)) {
            throw new AuhenticationException("Authentication failed, invalid Source RCON password");
        }
    }

    private boolean authenticate(String password) throws IOException {
        requestId = (int) (Math.random() * Integer.MAX_VALUE);
        sendPacket(requestId, SERVERDATA_AUTH, password);

        // By following the Source RCON docs at https://developer.valvesoftware.com/wiki/Source_RCON_Protocol#Requests_and_Responses,
        // Server will respond with SERVERDATA_RESPONSE_VALUE followed by SERVERDATA_AUTH_RESPONSE

        Packet resPacket = readPacket();
        if (resPacket.getType() == SERVERDATA_RESPONSE_VALUE && resPacket.getRequestId() == requestId) {
            Packet authResPacket = readPacket();
            return authResPacket.getType() == SERVERDATA_AUTH_RESPONSE && authResPacket.getRequestId() == requestId;
        }

        return false;
    }

    public String command(String command) throws IOException {
        Packet packet = commandRaw(command);
        if (packet != null) {
            return new String(packet.getPayload(), StandardCharsets.US_ASCII);
        }

        return null;
    }

    public Packet commandRaw(String command) throws IOException {
        if (socket.isClosed()) {
            authenticate(password);
        }

        sendPacket(requestId, SERVERDATA_EXECCOMMAND, command);
        Packet packet = readPacket();
        if (packet.getType() == SERVERDATA_RESPONSE_VALUE && packet.getRequestId() == requestId) {
            return packet;
        }

        return null;
    }

    /* ============================================================================================================== */

    private void sendPacket(int requestId, int type, String body) throws IOException {
        byte[] payload = body.getBytes(StandardCharsets.US_ASCII);
        int payloadSize = 4 + 4 + payload.length + 2;
        int packetSize = 4 + payloadSize;

        ByteBuffer buffer = ByteBuffer.allocate(packetSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(payloadSize);
        buffer.putInt(requestId);
        buffer.putInt(type);
        buffer.put(payload);
        buffer.put((byte) 0x00);
        buffer.put((byte) 0x00);

        socket.getOutputStream().write(buffer.array());
        socket.getOutputStream().flush();
    }

    private Packet readPacket() throws IOException {
        InputStream inputStream = socket.getInputStream();

        byte[] header = new byte[4 * 3];
        int read = inputStream.read(header);
        if (read != header.length) {
            throw new IOException("An error occurred on read packet");
        }


        ByteBuffer buffer = ByteBuffer.wrap(header);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int payloadSize = buffer.getInt();
        int requestId = buffer.getInt();
        int type = buffer.getInt();

        byte[] payload = new byte[payloadSize - 4 - 4 - 2];
        read = inputStream.read(payload);
        if (read != payload.length) {
            throw new IOException("An error occurred on read packet");
        }

        read = inputStream.read(new byte[2]);
        if (read != 2) {
            throw new IOException("An error occurred on read packet");
        }

        return new Packet(requestId, type, payload);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

}
