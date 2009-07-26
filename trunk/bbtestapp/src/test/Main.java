package test;

import net.rim.device.api.ui.UiApplication;

public class Main extends UiApplication {

    private DataCaptureScreen screen;

    private ConnectionThread connThread = new ConnectionThread();

    public Main() {
        connThread.start();
        screen = new DataCaptureScreen(connThread);
        pushScreen(screen);
    }

    public static void main(String[] args) {
    	Main uiApp = new Main();
        uiApp.enterEventDispatcher();
    }

}

