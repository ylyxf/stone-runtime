package org.siqisource.stone.utils;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.Key;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

public class SecurityUtil {

	private final static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	private static Key key = null;

	private static AesCipherService aesCipherService = new AesCipherService();

	public static Key getKey() {
		if (key == null) {
			try {
				Resource resource = new ClassPathResource("security/security.key");
				InputStream resourceInputStream = resource.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(resourceInputStream);
				key = (Key) ois.readObject();
				ois.close();
			} catch (Exception e) {
				logger.error("无法读取加密秘钥", e);
			}
		}
		return key;
	}

	public static String encode(String source) {
		if (StringUtils.isEmpty(source)) {
			return "";
		}
		String dist = aesCipherService.encrypt(source.getBytes(), getKey().getEncoded()).toHex();
		return dist;
	}

	public static String decode(String source) {
		if (StringUtils.isEmpty(source)) {
			return "";
		}
		String dist = new String(aesCipherService.decrypt(Hex.decode(source), getKey().getEncoded()).getBytes());
		return dist;

	}
}
