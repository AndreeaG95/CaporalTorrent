package rmiIO;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.server.UnicastRemoteObject;

/*
 *  Wrap given input and output streams and export themselves as remote object.
 */
public class RMIOutputStreamImpl implements RMIOutputStreamInterf{

    private OutputStream out;
    
    public RMIOutputStreamImpl(OutputStream out) throws IOException {
        this.out = out;
        UnicastRemoteObject.exportObject(this, 5000);
    }
    
    public void write(int b) throws IOException {
        out.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    public void close() throws IOException {
        out.close();
    }

}
