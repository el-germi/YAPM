package com.germimonte.yapm.util;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class SafeAL<E> extends ArrayList<E> {
	public SafeAL(int i) {
		super(i);
	}

	public Optional<E> safeGet(int index) {
		try {
			E out = get(index);
			if (out != null)
				return Optional.of(out);
		} catch (Throwable t) {
		}
		return Optional.empty();
	}

	public E safeGet(int index, E alt) {
		return safeGet(index).orElse(alt);
	}
}
