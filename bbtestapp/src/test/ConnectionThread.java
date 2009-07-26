package test;

import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class ConnectionThread extends Thread {

    private boolean start = false;

    private boolean stop = false;

    private String url;

    private String data;

    public boolean sendResult = false;

    public boolean sending = false;

    public void run() {
        while (true) {
            if (start == false && stop == false) {
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (stop) {
                return;
            } else if (start) {
                httpPost();
            }
        }
    }

    private void httpPost() {
        HttpConnection conn = null;
        OutputStream out = null;
        int responseCode;

        try {
            conn = (HttpConnection) Connector.open(url);
            conn.setRequestMethod(HttpConnection.POST);
            out = conn.openOutputStream();
            out.write(data.getBytes());
            out.flush();
            responseCode = conn.getResponseCode();

            if (responseCode != HttpConnection.HTTP_OK) {
                sendResult = false;
            } else {
                sendResult = true;
            }
            start = false;
            sending = false;

        } catch (IOException e) {
            start = false;
            sendResult = false;
            sending = false;
        }

    }

    public void post(String url, String data) {
        this.url = url;
        this.data = data;
        sendResult = false;
        sending = true;
        start = true;
    }

    public void stop() {
        stop = true;
    }

}

