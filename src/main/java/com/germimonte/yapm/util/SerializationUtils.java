package com.germimonte.yapm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.SerializationException;

/**
 * This is a blunt rip-off of the Apache commons lang3 SerializationUtils class,
 * I copied(and updated) it to not use a huge library just for 2 small functions
 */
public class SerializationUtils {

	/** Deep clone an {@code Object} using serialization. */
	@Nullable
	public static <T extends Serializable> T clone(@Nullable final T object) {
		if (object == null) {
			return null;
		}
		final byte[] objectData = serialize(object);
		final ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
		try (ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais,
				object.getClass().getClassLoader())) {
			return (T) in.readObject();
		} catch (final ClassNotFoundException ex) {
			throw new RuntimeException("ClassNotFoundException while reading cloned object data", ex);
		} catch (final IOException ex) {
			throw new RuntimeException("IOException while reading cloned object data", ex);
		}
	}

	/** Tests the serialization and deserialization methods */
	public static <T extends Serializable> T roundtrip(final T msg) {
		return (T) SerializationUtils.deserialize(SerializationUtils.serialize(msg));
	}

	public static void serialize(final Serializable obj, final OutputStream outputStream) {
		if (outputStream == null) {
			throw new IllegalArgumentException("The OutputStream must not be null");
		}
		try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
			out.writeObject(obj);
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static byte[] serialize(final Serializable obj) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
		serialize(obj, baos);
		return baos.toByteArray();
	}

	public static <T> T deserialize(final InputStream inputStream) {
		if (inputStream == null) {
			throw new IllegalArgumentException("The InputStream must not be null");
		}
		try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
			return (T) in.readObject();
		} catch (final ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static <T> T deserialize(final byte[] objectData) {
		if (objectData == null) {
			throw new IllegalArgumentException("The byte[] must not be null");
		}
		return SerializationUtils.<T>deserialize(new ByteArrayInputStream(objectData));
	}

	static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
		private static final Map<String, Class<?>> primitiveTypes = new HashMap<String, Class<?>>();

		static {
			primitiveTypes.put("byte", byte.class);
			primitiveTypes.put("short", short.class);
			primitiveTypes.put("int", int.class);
			primitiveTypes.put("long", long.class);
			primitiveTypes.put("float", float.class);
			primitiveTypes.put("double", double.class);
			primitiveTypes.put("boolean", boolean.class);
			primitiveTypes.put("char", char.class);
			primitiveTypes.put("void", void.class);
		}

		private final ClassLoader classLoader;

		public ClassLoaderAwareObjectInputStream(final InputStream in, final ClassLoader classLoader)
				throws IOException {
			super(in);
			this.classLoader = classLoader;
		}

		@Override
		protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
			final String name = desc.getName();
			try {
				return Class.forName(name, false, classLoader);
			} catch (final ClassNotFoundException ex) {
				try {
					return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
				} catch (final ClassNotFoundException cnfe) {
					final Class<?> cls = primitiveTypes.get(name);
					if (cls != null) {
						return cls;
					}
					throw cnfe;
				}
			}
		}
	}
}
