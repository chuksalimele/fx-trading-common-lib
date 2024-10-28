package chuks.flatbook.fx.common.account.profile;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
public class ClientIPInfo {

    private String ip;
    private long connectionTime;
    private long disconnectionTime;   // used to calcualate last seen
    private long lastSeenSecondsAgo;
    private boolean isConnected;
    private int userID;
    private String userFullName;
    private String originHardwareInfo;

    public ClientIPInfo(String str) {

        String[] fields = str.split("|");
        for (int i = 0; i < fields.length; i++) {
            String[] token = fields[i].split("=");
            String field_name = token[0];
            String value = token[1];

            if (field_name.equals("ip")) {
                this.ip = value;
            }
            if (field_name.equals("connectionTime")) {
                this.connectionTime = Long.parseLong(value);
            }
            if (field_name.equals("disconnectionTime")) {
                this.disconnectionTime = Long.parseLong(value);
            }
            if (field_name.equals("lastSeenSecondsAgo")) {
                this.lastSeenSecondsAgo = Long.parseLong(value);
            }
            if (field_name.equals("isConnected")) {
                this.isConnected = Boolean.parseBoolean(value);
            }
            if (field_name.equals("userID")) {
                this.userID = Integer.parseInt(value);
            }
            if (field_name.equals("userFullName")) {
                this.userFullName = value;
            }
            if (field_name.equals("originHardwareInfo")) {
                this.originHardwareInfo = value;
            }

        }
    }

    public String stringify() {
        // Initialize the StringBuilder with an estimate of the final size
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("|ip=").append(ip)
                .append("|connectionTime=").append(connectionTime)
                .append("|disconnectionTime=").append(disconnectionTime)
                .append("|lastSeenSecondsAgo=").append(lastSeenSecondsAgo)
                .append("|isConnected=").append(isConnected)
                .append("|userID=").append(userID)
                .append("|userFullName=").append(userFullName)
                .append("|originHardwareInfo=").append(originHardwareInfo);

        // Convert the StringBuilder to a String and return it
        return strBuilder.toString();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(long connectionTime) {
        this.connectionTime = connectionTime;
    }

    public long getLastSeenSecondsAgo() {
        return lastSeenSecondsAgo;
    }

    /**
     * This computation should only be perform on the server end for correctness
     * @return 
     */
    public long computeLastSeenSecondsAgo() {
        return disconnectionTime > 0
                ? System.currentTimeMillis() - disconnectionTime
                : 0;
    }

    public void setLastSeenSecondsAgo(long lastSeenSecondsAgo) {
        this.lastSeenSecondsAgo = lastSeenSecondsAgo;
    }

    public long getDisconnectionTime() {
        return disconnectionTime;
    }

    public void setDisconnectionTime(long disconnectionTime) {
        this.disconnectionTime = disconnectionTime;
    }

    public boolean isIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getOriginHardwareInfo() {
        return originHardwareInfo;
    }

    public void setOriginHardwareInfo(String originHardwareInfo) {
        this.originHardwareInfo = originHardwareInfo;
    }

}
