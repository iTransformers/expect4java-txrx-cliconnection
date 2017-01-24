package net.itransformers.expect4java.cliconnection.rxtx;

import gnu.io.*;
import net.itransformers.expect4java.cliconnection.CLIConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by vasko on 1/24/2017.
 */
public class RxTxCommCliConnection implements CLIConnection {
    protected SerialPort serialPort;
    protected InputStream in;
    protected OutputStream out;

    public static final int DATABITS_5 = 5;
    public static final int DATABITS_6 = 6;
    public static final int DATABITS_7 = 7;
    public static final int DATABITS_8 = 8;
    public static final int PARITY_NONE = 0;
    public static final int PARITY_ODD = 1;
    public static final int PARITY_EVEN = 2;
    public static final int PARITY_MARK = 3;
    public static final int PARITY_SPACE = 4;
    public static final int STOPBITS_1 = 1;
    public static final int STOPBITS_2 = 2;
    public static final int STOPBITS_1_5 = 3;
    public static final int FLOWCONTROL_NONE = 0;
    public static final int FLOWCONTROL_RTSCTS_IN = 1;
    public static final int FLOWCONTROL_RTSCTS_OUT = 2;
    public static final int FLOWCONTROL_XONXOFF_IN = 4;
    public static final int FLOWCONTROL_XONXOFF_OUT = 8;


    public void connect(Map<String, Object> map) throws IOException {
        String port;
        if (map.get("port") == null){
            throw new RuntimeException("missing parameter 'port'. Try using COM1, COM2 etc.");
        } else {
            port = (String) map.get("port");
        }
        Integer baudrate;
        if (map.get("baudrate") == null){
            throw new IllegalArgumentException("Missing parameter 'boudrate'. Try using baudrate like: 9600,...");
        } else {
            baudrate = (Integer) map.get("baudrate");
        }
        Integer databits;
        if (map.get("databits") == null){
            throw new IllegalArgumentException("Missing parameter 'databits'. Try using baudrate like: 8,...");
        } else {
            databits = (Integer) map.get("databits");
        }
        Integer stopbits;
        if (map.get("stopbits") == null){
            throw new IllegalArgumentException("Missing parameter 'stopbits'. Try using baudrate like: 1,...");
        } else {
            stopbits = (Integer) map.get("stopbits");
        }
        Integer parity;
        if (map.get("parity") == null){
            throw new IllegalArgumentException("Missing parameter 'parity'. Try using baudrate like: 0 (NONE),...");
        } else {
            parity = (Integer) map.get("parity");
        }
        Integer timeout;
        if (map.get("timeout") == null){
            throw new IllegalArgumentException("Missing parameter 'timeout'. Try using baudrate like: 20000,...");
        } else {
            timeout = (Integer) map.get("timeout");
        }
        CommPortIdentifier portIdentifier;
        try {
            portIdentifier = CommPortIdentifier.getPortIdentifier(port);
        } catch (NoSuchPortException e) {
            throw new RuntimeException(e);
        }
        if ( portIdentifier.isCurrentlyOwned() )
        {
            throw new RuntimeException("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = null;
            try {
                commPort = portIdentifier.open(this.getClass().getName(),timeout);
            } catch (PortInUseException e) {
                throw new RuntimeException(e);
            }

            if ( commPort instanceof SerialPort)
            {
                SerialPort serialPort = (SerialPort) commPort;
                try {
                    serialPort.setSerialPortParams(baudrate, databits, stopbits, parity);
                } catch (UnsupportedCommOperationException e) {
                    throw new RuntimeException(e);
                }
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
            }
            else
            {
                throw new RuntimeException("Error: Only serial ports are handled by this example.");
            }
        }

    }

    public void disconnect() throws IOException {
        inputStream().close();
        outputStream().close();
        serialPort.close();
    }

    public InputStream inputStream() {
        return in;
    }

    public OutputStream outputStream() {
        return out;
    }
}
