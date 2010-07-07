package org.jequals;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class ConstructorBasedVerifier {

	private static final Function<ConstructorParam, Object> DEFAULT_VALUE_FUNCTION = new Function<ConstructorParam, Object>() {
		@Override
		public Object apply(ConstructorParam constructorParam) {
			return constructorParam.getDefaultValue();
		}
	};

	public boolean equals(Class classUnderTest) throws InvocationTargetException, IllegalAccessException, InstantiationException {

		Constructor constructor = findConstructor(classUnderTest);

		if(constructor == null) {
			throw new NoEligibleConstructorFoundException();	
		}

		List<ConstructorParam> constructorParams = getConstructorParams(constructor);

		for (int i = 0; i < constructorParams.size(); i++) {
			boolean result = false;

			ConstructorParam param = constructorParams.get(i);

			if (!param.isAnnotatedWithEquals)
				continue;

			Object[] paramsForObjectA = getDefaultValuesForParams(constructorParams);
			Object[] paramsForObjectB = Arrays.copyOf(paramsForObjectA, paramsForObjectA.length);

			Object instanceA = createNewInstance(constructor, paramsForObjectA);
			Object instanceB = createNewInstance(constructor, paramsForObjectB);

			result = instanceA.equals(instanceB);

			paramsForObjectA[i] = param.getNewValue();
			instanceA = createNewInstance(constructor, paramsForObjectA);

			result = result && !instanceA.equals(instanceB);

			if(!result) {
				return false;
			}
		}

		return true;
	}


	private Object createNewInstance(Constructor constructor, Object[] params) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		return constructor.newInstance(params);
	}

	private Object[] getDefaultValuesForParams(List<ConstructorParam> constructorParams) {
		return Lists.newArrayList(Iterables.transform(constructorParams, DEFAULT_VALUE_FUNCTION)).toArray();

	}

	private Constructor findConstructor(Class classUnderTest) {
		for (Constructor constructor : classUnderTest.getDeclaredConstructors()) {
			for (Annotation[] annotations : constructor.getParameterAnnotations()) {
				if (annotations.length > 0 && annotations[0].annotationType() == Equals.class) {
					return constructor;
				}
			}
		}

		return null;  
	}


	private List<ConstructorParam> getConstructorParams(Constructor constructor) {

		List<ConstructorParam> args = Lists.newArrayList();

		Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
		for (int i = 0; i < constructor.getParameterTypes().length; i++) {
			Class parameterClass = constructor.getParameterTypes()[i];
			if (parameterAnnotations[i].length > 0 && parameterAnnotations[i][0].annotationType() == Equals.class) {
				args.add(new ConstructorParam(parameterClass, true));
			} else {
				args.add(new ConstructorParam(parameterClass, false));
			}

		}

		return args;
	}
}