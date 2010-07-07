package org.jequals;

import java.util.Map;

public class Sample {

	private String username;
	private final Map<String, Object> data;

	public Sample(@Equals String username, @Equals Map<String, Object> data) {
		this.username = username;
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Sample sample = (Sample) o;

		if (data != null ? !data.equals(sample.data) : sample.data != null) return false;
		if (username != null ? !username.equals(sample.username) : sample.username != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = username != null ? username.hashCode() : 0;
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}
}
