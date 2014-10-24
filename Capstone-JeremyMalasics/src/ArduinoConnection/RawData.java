package ArduinoConnection;

import org.zu.ardulink.RawDataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmalasics on 10/22/2014.
 */
public class RawData implements RawDataListener, IRawDataObersvable {

    private String message;
    private List<IRawDataOberserver> oberservers = new ArrayList<IRawDataOberserver>();

    @Override
    public void parseInput(String s, int i, int[] ints) {
        StringBuilder builder = new StringBuilder(i + 1);
        for(int j = 0; j < i; j++) {
            builder.append((char) ints[j]);
        }
        message = builder.toString();
        notifyObeservers();
    }

    @Override
    public void registerObserver(IRawDataOberserver oberserver) {
        oberservers.add(oberserver);
    }

    @Override
    public void removeOberserver(IRawDataOberserver oberserver) {
        oberservers.remove(oberserver);
    }

    @Override
    public void notifyObeservers() {
        for(IRawDataOberserver oberserver : oberservers) {
            oberserver.update(message);
        }
    }

}
