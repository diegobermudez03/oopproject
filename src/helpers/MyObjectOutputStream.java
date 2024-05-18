package helpers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyObjectOutputStream extends ObjectOutputStream{
	
	public MyObjectOutputStream(OutputStream out) throws IOException{
        super(out);
    
	}
	protected void writeStreamHeader() throws IOException {}
}
