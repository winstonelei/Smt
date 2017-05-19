package com.github.sshClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
/**
 * Created by winstone on 2016/12/6.
 * 命令行执行器
 *
 */
public class CliCommandExecutor {


    private String remoteHost;
    private int port;
    private String remoteUser;
    private String remotePwd;
    private int remoteTimeoutSeconds = 3600;

    public CliCommandExecutor() {
    }

    public void setRunAtRemote(String host, int port, String user, String pwd) {
        this.remoteHost = host;
        this.port = port;
        this.remoteUser = user;
        this.remotePwd = pwd;
    }

    public void setRunAtLocal() {
        this.remoteHost = null;
        this.remoteUser = null;
        this.remotePwd = null;
    }

    /**
     * copy 文件从本地到目的地
     * @param localFile
     * @param destDir
     * @throws IOException
     */
    public void copyFile(String localFile, String destDir) throws IOException {
        if (remoteHost == null)
            copyNative(localFile, destDir);
        else
            copyRemote(localFile, destDir);
    }

    private void copyNative(String localFile, String destDir) throws IOException {
        File src = new File(localFile);
        File dest = new File(destDir, src.getName());
        FileUtils.copyFile(src, dest);
    }

    private void copyRemote(String localFile, String destDir) throws IOException {
        SSHClient ssh = new SSHClient(remoteHost, port, remoteUser, remotePwd);
        try {
            ssh.scpFileToRemote(localFile, destDir);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public Pair<Integer, String> execute(String command) throws IOException {
        return execute(command, new SoutLogger());
    }

    /**
     * 远程执行命令
     * @param command
     * @param logAppender
     * @return
     * @throws IOException
     */
    public Pair<Integer, String> execute(String command, Logger logAppender) throws IOException {
        Pair<Integer, String> r;
        if (remoteHost == null) {
            r = runNativeCommand(command, logAppender);
        } else {
            r = runRemoteCommand(command, logAppender);
        }

        if (r.getFirst() != 0)
            throw new IOException("OS command error exit with " + r.getFirst() //
                    + (remoteHost == null ? "" : " (remoteHost:" + remoteHost + ")") //
                    + " -- " + command + "\n" + r.getSecond());

        return r;
    }

    private Pair<Integer, String> runRemoteCommand(String command, Logger logAppender) throws IOException {
        SSHClient ssh = new SSHClient(remoteHost, port, remoteUser, remotePwd);

        SSHClientOutput sshOutput;
        try {
            sshOutput = ssh.execCommand(command, remoteTimeoutSeconds, logAppender);
            int exitCode = sshOutput.getExitCode();
            String output = sshOutput.getText();
            return Pair.newPair(exitCode, output);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private Pair<Integer, String> runNativeCommand(String command, Logger logAppender) throws IOException {
        String[] cmd = new String[3];
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            cmd[0] = "cmd.exe";
            cmd[1] = "/C";
        } else {
            cmd[0] = "/bin/bash";
            cmd[1] = "-c";
        }
        cmd[2] = command;

        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectErrorStream(true);
        Process proc = builder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            result.append(line).append('\n');
            if (logAppender != null) {
                logAppender.log(line);
            }
        }

        try {
            int exitCode = proc.waitFor();
            return Pair.newPair(exitCode, result.toString());
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

}
