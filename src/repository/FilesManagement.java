package repository;

import java.util.List;

import java.util.LinkedList;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import entities.Cliente;
import entities.Cuenta;
import helpers.MyObjectOutputStream;


public class FilesManagement {
	
	public static <T> Map<Long, T> leerFile(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		Map<Long, T> response = new HashMap<>();
		File file = getFile(fileName);
		try {
			if (!(file.exists() && file.length() > 0)) return response;
			ObjectInputStream objRead = new ObjectInputStream(new FileInputStream(file)); //here is thrown the exception
			T object;
			do {
				object = (T) objRead.readObject();
				if(object != null) {
					long key = object instanceof Cliente ? ((Cliente)object).getNumeroId() : ((Cuenta)object).getNumCuenta();
					response.put(key, object);
				}	
			} while (object != null);
			objRead.close();
		} catch (EOFException eof) {}
		return  response;
	}
	
	public static <T> boolean writeFile(LinkedList<T> objetos, String fileName) throws IOException {
		if (objetos.size() > 0) {
			File file = getFile(fileName);
			ObjectOutputStream objOut;
			if (file.exists() ) file.delete();
			file.createNewFile();
			objOut = new ObjectOutputStream(new FileOutputStream(file));
			for (T object : objetos) {
				objOut.writeObject(object);
			}
			objOut.close();
		}
		return true;
	}
	
	private static File getFile(String name) {
		Path paths = Paths.get("");
		String path = paths.toAbsolutePath().toString();
		File file = new File(path + File.separator + "src" + File.separator + name + ".dat");
		return file;
	}

}