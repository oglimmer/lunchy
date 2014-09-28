package de.oglimmer.lunchy.services;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import lombok.extern.slf4j.Slf4j;

import com.google.gson.JsonObject;

@Slf4j
public class MBeanService {

	private static MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

	public static void copyAllNodes(String domainPattern, JsonObject targetObject, String excludeString) {
		try {
			Set<ObjectName> objectNames = mbs.queryNames(new ObjectName(domainPattern), null);
			for (ObjectName objName : objectNames) {
				if (!objName.toString().contains(excludeString)) {
					JsonObject node = new JsonObject();
					copyAllAttributes(objName, node);
					targetObject.add(objName.toString(), node);
				}
			}
		} catch (MalformedObjectNameException e) {
			log.error("Failed to query mbean", e);
		}
	}

	public static ObjectName getPoolDSJMXName() {
		try {
			ObjectName registryName = new ObjectName("com.mchange.v2.c3p0:type=C3P0Registry,name=C3P0DBConnection");
			MBeanInfo registry = mbs.getMBeanInfo(registryName);
			for (MBeanAttributeInfo mba : registry.getAttributes()) {
				if ("AllIdentityTokens".equals(mba.getName())) {
					String[] identityTokens = (String[]) mbs.getAttribute(registryName, mba.getName());
					for (String identityToken : identityTokens) {

						try {
							ObjectName objName = new ObjectName("com.mchange.v2.c3p0:type=PooledDataSource,identityToken="
									+ identityToken + ",name=poolDS");
							mbs.getMBeanInfo(objName);
							return objName;
						} catch (InstanceNotFoundException e) {
							// there are not valid identityTokens in the list of the registry. as we try them all, this is
							// expected and needs to be ignored
						}
					}
				}
			}
		} catch (MalformedObjectNameException | IntrospectionException | InstanceNotFoundException | AttributeNotFoundException
				| ReflectionException | MBeanException e) {
			log.error("Failed to query mbean", e);
		}
		return null;
	}

	public static void copyAllAttributes(ObjectName on, JsonObject targetObject) {
		try {
			MBeanInfo mbi = mbs.getMBeanInfo(on);
			for (MBeanAttributeInfo mbai : mbi.getAttributes()) {
				Object attValue = mbs.getAttribute(on, mbai.getName());
				if (attValue instanceof Boolean) {
					targetObject.addProperty(mbai.getName(), (Boolean) attValue);
				} else if (attValue instanceof Character) {
					targetObject.addProperty(mbai.getName(), (Character) attValue);
				} else if (attValue instanceof Number) {
					targetObject.addProperty(mbai.getName(), (Number) attValue);
				} else if (attValue instanceof String) {
					targetObject.addProperty(mbai.getName(), (String) attValue);
				} else {
					targetObject.addProperty("OBJECT:" + mbai.getName(), attValue != null ? attValue.toString() : "null");
				}
			}
		} catch (InstanceNotFoundException | IntrospectionException | ReflectionException | AttributeNotFoundException
				| MBeanException e) {
			log.error("Failed to query mbean", e);
		}
	}

}
