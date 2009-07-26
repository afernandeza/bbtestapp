package test;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.AutoTextEditField;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.MainScreen;


public class DataCaptureScreen extends MainScreen {

    protected boolean sending = false;

    private ConnectionThread connThread;

    private DateField dateTimeField = new DateField("Date: ", System
            .currentTimeMillis(), DateField.DATE_TIME);

    private ObjectChoiceField category = new ObjectChoiceField("Category:",
            new String[] { "Hardware", "Software", "Network" });

    private AutoTextEditField user = new AutoTextEditField("User:", "");

    private BasicEditField phone = new BasicEditField("Contact #: ", "", 10,
            BasicEditField.FILTER_PHONE);

    private AutoTextEditField notes = new AutoTextEditField("Notes:", "");

    private ButtonField sendButton = new ButtonField("Send");

    public DataCaptureScreen(ConnectionThread connThread) {
        this.connThread = connThread;
        setTitle("Data Capture");
        add(dateTimeField);
        add(category);
        add(user);
        add(phone);
        add(notes);
        add(new SeparatorField());
        FieldListener sendListener = new FieldListener();
        sendButton.setChangeListener(sendListener);
        add(sendButton);
    }

    public boolean onClose() {
        connThread.stop();
        close();
        return true;
    }

    private String getFieldData() {
        StringBuffer sb = new StringBuffer();
        sb.append("dateTime=");
        sb.append(dateTimeField.toString() + "&");
        sb.append("category=");
        sb.append(category.getChoice(category.getSelectedIndex()) + "&");
        sb.append("user=");
        sb.append(user.getText() + "&");
        sb.append("phone=");
        sb.append(phone.getText() + "&");
        sb.append("notes=");
        sb.append(user.getText());

        return sb.toString();
    }

    class FieldListener implements FieldChangeListener {
        public void fieldChanged(Field field, int context) {
            StringBuffer sb = new StringBuffer("Sending...");
            connThread
                    .post(
                            "http://localhost:8080/servlet/ReceiveData",
                            getFieldData());
            while (connThread.sending) {
                try {
                    Status.show( sb.append(".").toString() );
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (connThread.sendResult) {
                Status.show("Transmission Successful");
            } else {
                Status.show("Transmission Failed");
            }

        }
    }
}
