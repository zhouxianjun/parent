package com.gary.payment.db;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectFileUtil{
	private File file;
	public ObjectFileUtil(File file) {
		this.file = file;
	}
	public void write(Serializable obj) throws FileNotFoundException, IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(obj);
        out.close();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T read(Class<T> c) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
    	T o = null;
    	try {
    		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
    		o = (T)in.readObject();
    		in.close();
		} catch (EOFException e) {
			o = c.newInstance();
		}
        return o;
	}
}
