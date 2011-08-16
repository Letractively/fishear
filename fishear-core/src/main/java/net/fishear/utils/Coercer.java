package net.fishear.utils;

public interface Coercer<S, T> {
	public T convert(S source);

	Class<S> getSourceType();

	Class<T> getTargetType();
} 

