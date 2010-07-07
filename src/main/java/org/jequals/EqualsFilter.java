package org.jequals;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class EqualsFilter {
	
	private static final Predicate<Class> EQUALS_PREDICATE = new Predicate<Class>() {
		@Override
		public boolean apply(Class input) {
			try {
				input.getDeclaredMethod("equals", Object.class);
			} catch (NoSuchMethodException e) {
				return false;
			}
			return true;
		}
	};


	public List<Class> filter(List<Class> classesUnderTest) {
		return newArrayList(Iterables.filter(classesUnderTest, EQUALS_PREDICATE));
	}

}

