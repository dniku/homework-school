import java.lang.reflect.*;
import java.net.*;

public class Metainfo {
	public static String formatType(String name) {
		if (name.charAt(0) == '[') {
			switch(name.charAt(1)) {
			case 'B' : return "byte[]";
			case 'C' : return "char[]";
			case 'J' : return "long[]";
			case 'I' : return "int[]";
			case 'S' : return "short[]";
			case 'F' : return "float[]";
			case 'D' : return "double[]";
			case 'Z' : return "boolean[]";
			case 'L' : return name.substring(2, name.length() - 1) + "[]";
			default : return name;
			}
		}
		else {
			return name;
		}
	}
	
	public static void printClassInfo(Class<?> cls) {
		System.out.println(cls.toString());
		System.out.println("Constructors:");
		for (Constructor<?> constructor : cls.getConstructors()) {
			System.out.println("    " + constructor.getName());
		}
		System.out.println("Fields:");
		for (Field field : cls.getDeclaredFields()) {
			System.out.println("    " + field.getName() + " : " + formatType(field.getType().getName()));
		}
		System.out.println("Methods:");
		for (Method method : cls.getDeclaredMethods()) {
			System.out.print("    " + method.getName() + "(");
			Class<?>[] types = method.getParameterTypes();
			if (types.length > 0) {
				System.out.print(formatType(types[0].getName()));
				for (int i = 0; i < types.length; i++) {
					System.out.print(", " + formatType(types[i].getName()));
				}
			}
			System.out.print(") : ");
			System.out.println(formatType(method.getReturnType().getName()));
		}
	}
	
	public static Class<?> getClassByName(String classname) throws ClassNotFoundException {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		return loader.loadClass(classname);
	}
	
	public static Class<?> getClassByFilename(String filename) throws MalformedURLException, ClassNotFoundException {
		String curDir = System.getProperty("user.dir");
		URL classUrl = new URL("file:///" + curDir);
		URL[] classUrls = { classUrl };
        URLClassLoader ucl = new URLClassLoader(classUrls);
        Class<?> cls = ucl.loadClass(filename);
        return cls;
	}
	
	public static void main(String[] args) {
		//String classname = "java.util.Arrays";
		Class<?> cls;
		try {
			//cls = getClassByName(classname);
			cls = getClassByFilename("BatcherSort");
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		printClassInfo(cls);
	}
}
