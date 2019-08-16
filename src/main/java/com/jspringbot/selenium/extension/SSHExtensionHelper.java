package com.jspringbot.selenium.extension;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.jspringbot.syntax.HighlightRobotLogger;

import java.util.Properties;

public class SSHExtensionHelper {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(SSHExtensionHelper.class);
    protected Properties config;
    protected JSch jsch;
    protected Session session;
    protected ChannelExec channel;

    private String hostname;
    private String username;
    private String password;
    private String keyPemFile;
    private String strictHostKeyChecking;
    private int port;
    private String databaseHostname;
    private int localPort;
    private int remotePort;
    private String idrsaFilename;
    private String preferredAuthentications;



    public void setKeyPemFile(String keyPemFile) {
        this.keyPemFile = keyPemFile;
    }

    public void setDatabaseHostname(String databaseHostname) {
        this.databaseHostname = databaseHostname;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public void setIdrsaFilename(String idrsaFilename) {
        this.idrsaFilename = idrsaFilename;
    }

    public void setPreferredAuthentications(String preferredAuthentications) {
        this.preferredAuthentications = preferredAuthentications;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStrictHostKeyChecking(String strictHostKeyChecking) {
        this.strictHostKeyChecking = strictHostKeyChecking;
    }
    public void setPort(int port) {
        this.port = port;
    }

    public void sshConnectByPem(){
        try{
            config = new Properties();
            config.put("StrictHostKeyChecking", strictHostKeyChecking);
            jsch = new JSch();
            jsch.addIdentity(keyPemFile);

            // Create a JSch session to connect to the server
            Session session = jsch.getSession(username, hostname, port);
            session.setConfig(config);

            // Establish the connection
            session.connect();
            LOG.info(String.format("Connected to '%s' ", hostname));
        }catch(JSchException e){
            LOG.warn(String.format("Cannot connect to '%s' ", hostname));
            LOG.warn(String.format("Error Message '%s' ", e.getMessage()));
        }
    }

    public void sshConnect(){
        try{
            config = new Properties();
            config.put("StrictHostKeyChecking", strictHostKeyChecking);
            jsch = new JSch();

            // Create a JSch session to connect to the server
            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            session.setConfig(config);

            // Establish the connection
            session.connect();
            LOG.info(String.format("Connected to '%s' ", hostname));
        }catch(JSchException e){
            LOG.warn(String.format("Cannot connect to '%s' ", hostname));
            LOG.warn(String.format("Error Message '%s' ", e.getMessage()));
        }
    }

    public void sshConnectDatabaseByRsa(){
        try{
            jsch = new JSch();
            jsch.addIdentity(idrsaFilename, password);

            // Create a JSch session to connect to the server
            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", strictHostKeyChecking);
            session.setConfig("PreferredAuthentications", preferredAuthentications);


            LOG.info("Establishing Connection...");
            session.connect(30000);
            LOG.info(String.format("Connected to '%s' ", hostname));

            session.setPortForwardingL(localPort, databaseHostname, remotePort);
            LOG.info(String.format("Success Port Forwarding to '%s' ", databaseHostname));
        }catch(JSchException e){
            LOG.warn(String.format("Cannot connect to '%s' ", hostname));
            LOG.warn(String.format("Error Message '%s' ", e.getMessage()));
        }
    }

    public void sshChannelDisconnect(){
        try{
            channel.disconnect();
            LOG.info(String.format("Disconnected to Channel", hostname));
        }catch(Exception e){
            LOG.warn(String.format("Cannot disconnect to Channel ", hostname));
            LOG.warn(String.format("Error Message Channel: ", e.getMessage()));
        }
    }

    public void ssSessionDisconnect(){
        try{
            channel.disconnect();
            session.disconnect();
            LOG.info(String.format("Disconnected to '%s' ", hostname));
        }catch(Exception e){
            LOG.warn(String.format("Cannot disconnect to '%s' ", hostname));
            LOG.warn(String.format("Error Message '%s' ", e.getMessage()));
        }
    }



}
