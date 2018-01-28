package org.siqisource.stone.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializeTool {

	private static final Logger logger = LoggerFactory.getLogger(SerializeTool.class);

	public static <T extends Serializable> T fromString(String s, Class<T> clazz) {
		try {
			byte[] data = Base64.decode(s);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			return (T) o;
		} catch (Exception e) {
			logger.error("从字符串读取为对象时失败", e);
		}
		return null;
	}

	public static String toString(Serializable o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			return Base64.encodeToString(baos.toByteArray());
		} catch (Exception e) {
			logger.error("从对象序列化为字符串时出错", e);
		}
		return "";
	}

}