package eu.sunfishproject.icsp.pip.database;

/**
 * Created by dziegler on 18/10/2016.
 */
public interface DatabaseService {

    public String getHostForServiceId(String id);

    public String getZoneForServiceId(String id);

    public String getPEPForZone(String id);


}
