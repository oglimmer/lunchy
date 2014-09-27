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

	public static void copyAllAttributes(String objectName, JsonObject targetObject) {
		try {
			ObjectName objName = new ObjectName(objectName);
			copyAllAttributes(objName, targetObject);
		} catch (MalformedObjectNameException e) {
			log.error("Failed to query mbean", e);
		}
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
					targetObject.addProperty("OBJECT:" + mbai.getName(), attValue.toString());
				}
			}
		} catch (InstanceNotFoundException | IntrospectionException | ReflectionException | AttributeNotFoundException | MBeanException e) {
			log.error("Failed to query mbean", e);
		}
	}

}
