package ArduinoConnection;

/**
 * Created by jmalasics on 10/22/2014.
 */
public interface IRawDataObersvable {

    public void registerObserver(IRawDataOberserver oberserver);

    public void removeOberserver(IRawDataOberserver oberserver);

    public void notifyObeservers();

}
