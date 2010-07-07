package org.jequals;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.Assert.fail;

public class JEquals {

	private ConstructorBasedVerifier constructorBasedVerifier = new ConstructorBasedVerifier();

	private EqualsFilter filter = new EqualsFilter();

	private static final Predicate<Field> FIELDS_WITH_EQUALS = new Predicate<Field>() {
		@Override
		public boolean apply(Field o) {
			return o.isAnnotationPresent(Equals.class);
		}
	};

	public static void main(String args[]) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
		JEquals jequals = new JEquals();

		jequals.run(Lists.<Class>newArrayList(Sample.class));

	}

	private void run(List<Class> classesUnderTest) throws InvocationTargetException, InstantiationException, IllegalAccessException {
		classesUnderTest = filter.filter(classesUnderTest);
		if (classesUnderTest.isEmpty()) {
			return;
		}

		List<Class> constructorBasedClasses = Lists.newArrayList();
		List<Class> setterBasedClasses = Lists.newArrayList();

		for (Class constructorBasedClass : classesUnderTest) {
			if (!constructorBasedVerifier.equals(constructorBasedClass)) {
				fail("Failed Constructor based verification for Class : " + constructorBasedClass.getName());
			}
		}
		
	}




	


}
