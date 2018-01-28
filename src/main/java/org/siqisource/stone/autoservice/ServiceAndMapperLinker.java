package org.siqisource.stone.autoservice;

import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.siqisource.stone.mybatis.dialect.Dialect;
import org.siqisource.stone.mybatis.model.Model;
import org.siqisource.stone.mybatis.plugins.UUIDTypeHandler;
import org.siqisource.stone.mybatis.utils.ModelClazzReader;
import org.siqisource.stone.mybatis.utils.TypeResolver;
import org.siqisource.stone.mybatis.utils.XMLBuilder;

public class ServiceAndMapperLinker {

	public static final Log logger = LogFactory.getLog(ServiceAndMapperLinker.class);

	public static void linkMapperAndService(Map<String, AutoService> autoServiceMap,
			Map<String, AutoMapper> autoMapperMap, SqlSessionFactory sqlSessionFactory, Dialect dialect) {

		// 为Mapper生成XML
		linkMapperAndXml(autoMapperMap, sqlSessionFactory, dialect);

		// 将Mapper注入到Service
		Map<Class<?>, AutoService<?>> modelServiceMap = prepareModelServiceMap(autoServiceMap);

		for (Map.Entry<String, AutoMapper> entry : autoMapperMap.entrySet()) {
			AutoMapper<?> autoMapper = entry.getValue();
			Class<?>[] arguments = TypeResolver.resolveRawArguments(AutoMapper.class, autoMapper.getClass());
			Class<?> modelClazz = arguments[0];
			AutoService<?> autoService = modelServiceMap.get(modelClazz);
			if (autoService != null) {
				autoService.setAutoMapper(autoMapper);
			}
		}

	}

	private static Map<Class<?>, AutoService<?>> prepareModelServiceMap(Map<String, AutoService> autoServiceMap) {
		Map<Class<?>, AutoService<?>> result = new HashMap<Class<?>, AutoService<?>>();

		for (Map.Entry<String, AutoService> entry : autoServiceMap.entrySet()) {
			AutoService<?> autoService = entry.getValue();

			Class<?>[] arguments = TypeResolver.resolveRawArguments(AutoService.class, autoService.getClass());
			Class<?> modelClazz = arguments[0];

			if (modelClazz.getName().indexOf("Unknow") != -1) {
				continue;
			}
			result.put(modelClazz, autoService);
		}

		return result;
	}

	public static void linkMapperAndXml(Map<String, AutoMapper> autoMapperMap, SqlSessionFactory sqlSessionFactory,
			Dialect dialect) {
		Configuration configuration = sqlSessionFactory.getConfiguration();
		// 下划线转驼峰
		configuration.setMapUnderscoreToCamelCase(true);
		// 注册类型处理
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		typeHandlerRegistry.register(UUIDTypeHandler.class);
		// typeHandlerRegistry.register(BooleanTypeHandler.class);

		SqlSession session = sqlSessionFactory.openSession();
		DatabaseMetaData metaData;
		try {
			metaData = session.getConnection().getMetaData();

			for (Map.Entry<String, AutoMapper> entry : autoMapperMap.entrySet()) {
				AutoMapper<?> autoMapper = entry.getValue();
				Class<?>[] arguments = TypeResolver.resolveRawArguments(AutoMapper.class, autoMapper.getClass());
				Class<?> modelClazz = arguments[0];

				boolean isSingleKey = SingleKeyMapper.class.isAssignableFrom(autoMapper.getClass()) ? true : false;

				Class<?> mapperClazz = null;
				Class<?> interfaces[] = autoMapper.getClass().getInterfaces();
				for (Class<?> interfaceClazz : interfaces) {
					if (interfaceClazz.getName().equals(SingleKeyMapper.class.getName())) {
						continue;
					}
					if (interfaceClazz.getName().equals(GeneralMapper.class.getName())) {
						continue;
					}
					if (interfaceClazz.getName().equals(AutoMapper.class.getName())) {
						continue;
					}
					mapperClazz = interfaceClazz;
				}
				if (mapperClazz == null) {
					continue;
				}
				Model model = ModelClazzReader.readModel(mapperClazz, modelClazz, metaData, isSingleKey, dialect);

				XMLBuilder xmlBuilder = new XMLBuilder(model, dialect);
				InputStream xml = xmlBuilder.genXml();
				XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(xml, configuration,
						"autogen." + model.getName(), configuration.getSqlFragments());
				xmlMapperBuilder.parse();
			}

		} catch (SQLException e) {
			logger.error("获得数据库meta数据时出错。", e);
			throw new RuntimeException(e);
		}

		session.close();

	}

}
