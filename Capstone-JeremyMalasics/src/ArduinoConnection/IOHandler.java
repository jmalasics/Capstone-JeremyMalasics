package ArduinoConnection;

import org.zu.ardulink.Link;

/**
 * Created by jmalasics on 10/22/2014.
 */
public class IOHandler implements IRawDataOberserver {

    private static final String comPort = "COM5";
    private static final int baudRate = 2400;

    private Link link = Link.getDefaultInstance();
    private boolean connected = link.connect(comPort, baudRate);
    private String latestMessage;
    private RawData rawData = new RawData();

    public void write(String message) {
        link.sendCustomMessage(message);
    }

    public String read() {
        link.addRawDataListener(rawData);
        return latestMessage;
    }

    @Override
    public void update(String message) {
        latestMessage = message;
    }

}