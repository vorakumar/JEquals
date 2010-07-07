package org.jequals;

import java.util.HashMap;
import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.mock;


public class ConstructorParam {
	Class parameterClass;
	boolean isAnnotatedWithEquals;
	Object defaultValue;


	private Map<Class,Object> defaultValueMap = new HashMap<Class, Object>() {{
		put(boolean.class, true);
		put(String.class, "StringA");
	}};

	private Map<Class,Object> newValueMap = new HashMap<Class, Object>() {{
		put(boolean.class, false);
		put(String.class, "StringB");
	}};


	ConstructorParam(Class parameterClass, boolean annotatedWithEquals) {
		this.parameterClass = parameterClass;
		isAnnotatedWithEquals = annotatedWithEquals;
		defaultValue = buildValue(parameterClass, defaultValueMap);
	}

	private Object buildValue(Class parameterClass, Map<Class, Object> fieldTypesToValue) {
		Object value = fieldTypesToValue.get(parameterClass);

		if (parameterClass.isEnum()) {
			value = parameterClass.getEnumConstants()[0];
		}
		else if(value == null) {
			value = mock(parameterClass);
		}

		return value;
	}

	public Object getNewValue() {
		return buildValue(this.parameterClass, newValueMap);
	}

	public Object getDefaultValue() {
		return defaultValue;
	}
}

