package org.sunfish.icsp.common.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SessionStorage {


    private static final Logger log = LogManager.getLogger(SessionStorage.class);


    private static SessionStorage instance;
    private Map<String, UserSession> sessionStore;
    private SecureRandom random = new SecureRandom();
    private Date lastCleanup;


    private static final int SESSION_TIMEOUT_MINUTES = 20;


    private SessionStorage() {

        sessionStore = new HashMap<>();
    }

    public static SessionStorage getInstance()
    {
        if(instance == null) {
            instance = new SessionStorage();
        }

        return instance;
    }


    public String generateUserSession(String firstName, String lastName, String birthday, List<String> userRoles, String bpk, String issuingNation) {

        doCleanup();

        UserSession userSession = new UserSession(firstName, lastName, birthday, userRoles, bpk, issuingNation);

        String session = nextSessionId();

        sessionStore.put(session, userSession);

//        writeToFile();

        return session;
    }


    public UserSession getUserSession(String session) {

//        readFromFile();

        doCleanup();

        return sessionStore.get(session);

    }


    private String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }


    private void doCleanup() {

        Date now = new Date();

        if(lastCleanup == null) {
            lastCleanup = new Date();
        }

        if (now.getTime() - lastCleanup.getTime() >= SESSION_TIMEOUT_MINUTES *60*1000) {
            sessionStore.clear();
            lastCleanup = new Date();
        }

    }


    private void writeToFile() {

        try {
            FileOutputStream fileOut = new FileOutputStream("/tmp/sessions.sunfish");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.sessionStore);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/sessions.sunfish");

        } catch(IOException e) {
            log.error("Could not store sessions!",e);
        }

    }

    private void readFromFile() {

        try {
            FileInputStream fileIn = new FileInputStream("/tmp/sessions.sunfish");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.sessionStore = (HashMap<String, UserSession>) in.readObject();
            in.close();
            fileIn.close();

        } catch(IOException e) {
            log.error("Could not retrieve sessions!", e);
        } catch(ClassNotFoundException e) {
            log.error("Class not found!", e);
        }

    }

}
