package org.sunfish.icsp.common.session;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class UserSession implements Serializable {

    private String firstName;
    private String lastName;
    private String birthday;
    private List<String> userRoles;
    private String bpk;
    private String issuingNation;

    public UserSession() {
    }

    public UserSession(String firstName, String lastName, String birthday, List<String> userRoles, String bpk, String issuingNation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.userRoles = userRoles;
        this.bpk = bpk;
        this.issuingNation = issuingNation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public String getBpk() {
        return bpk;
    }

    public void setBpk(String bpk) {
        this.bpk = bpk;
    }

    public String getIssuingNation() {
        return issuingNation;
    }

    public void setIssuingNation(String issuingNation) {
        this.issuingNation = issuingNation;
    }
}
