package online.gladikov.java_core.jvm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import online.gladikov.core.jvm.FileClassLoader;

@Slf4j
public class FileClassLoaderTest {
	
	@Test
	void givenPath_whenConvertToFullyQualifiedName_thenVerifyCorrect()  throws Exception{
		Class<?> clazz=Class.forName("online.gladikov.core.jvm.FileClassLoader");
		
		
		Method  method=Arrays.stream(clazz.getDeclaredMethods())
				.filter(x -> x.getName().equals("nameToPath"))
							.findFirst().orElseThrow(() -> new Exception("no such method"));
		
		
 		Constructor<?> constructor=clazz.getDeclaredConstructor(ClassLoader.class);
//		log.debug( String.valueOf(Optional.ofNullable(constructors).isPresent()));
//		int len=Optional.ofNullable(constructors).orElseThrow(() -> new Exception()).length;
//		log.debug("length is {}",len);
//		Arrays.stream(constructors)
//			.flatMap(x -> Arrays.stream(x.getParameterTypes()))
//					.forEach(x -> log.debug(x.getSimpleName()));
		FileClassLoader underTest=(FileClassLoader) constructor.newInstance(FileClassLoader.class.getClassLoader());
		method.setAccessible(true);
		String arg="home.max.dev.java.core.ClassFile";
		String expected="/home/max/dev/java/core/ClassFile.class";
		
		String result=(String)method.invoke(underTest, arg);
		
		assertEquals(expected,result) ;
	}
}
