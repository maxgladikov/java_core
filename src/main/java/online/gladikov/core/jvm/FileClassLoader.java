package online.gladikov.core.jvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FileClassLoader extends ClassLoader {
	
	public FileClassLoader(ClassLoader parent) {
        super(parent);
    }

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException{
		var file=new File(nameToPath(name));
		byte[] bytes;
		try {
			bytes = loadClassFromFile(file);
			assert(Optional.of(bytes).isPresent());
		} catch (IOException e) {
			throw new ClassNotFoundException();
		}
		Class<?> clazz=defineClass(name, bytes, 0, bytes.length);
				resolveClass(clazz);
		return clazz;
	}

	private byte[] loadClassFromFile(File file) throws IOException {
		byte[] bytes;
		try(InputStream in=new FileInputStream(file)){
			bytes=new byte[in.available()];
			in.read(bytes);
		} 
		return bytes;
	}
	
	private String nameToPath(String name) {
		return System.getProperty("os.name").equals("Linux")? 
					"/"+name.replace('.',File.separatorChar)+".class"
						:name.replace('.',File.separatorChar)+".class";
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		ClassLoader loader=new FileClassLoader(FileClassLoader.class.getClassLoader());
		Class<?> clazz=loader.loadClass("home.max.dev.java.core.ClassFile");
		Object instance=clazz.getConstructor().newInstance();
		Method method=clazz.getMethod("say");
		method.invoke(null, null);
	}
}



