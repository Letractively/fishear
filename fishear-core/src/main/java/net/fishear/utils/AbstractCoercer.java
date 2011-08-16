package net.fishear.utils;

import java.lang.reflect.ParameterizedType;

import net.fishear.exceptions.AppException;

public abstract class AbstractCoercer<S, T> implements Coercer<S, T> {

	private Class<S> sourceType;
	
	private Class<T> targetType;
	
	protected abstract T convertImpl(S source);

	public AbstractCoercer() {
		setTypes();
	}

	@Override public T convert(S source) {
		if(source == null || (source instanceof CharSequence && ((CharSequence)source).length() == 0)) {
			return null;
		}
		return convertImpl(source);
	}

	@Override
	public Class<S> getSourceType() {
		return sourceType;
	}

	@Override
	public Class<T> getTargetType() {
		return targetType;
	}

	@SuppressWarnings("unchecked")
	private void setTypes() {
		Class<?> clazz = this.getClass();
		while(clazz != Object.class) {
			Object gscl = clazz.getGenericSuperclass();
			if(ParameterizedType.class.isAssignableFrom(gscl.getClass())) {
				ParameterizedType pt = (ParameterizedType)gscl;
				Object[] oa = pt.getActualTypeArguments();
				if(oa != null && oa.length > 1) {
					this.sourceType = (Class<S>)oa[0];
					this.targetType = (Class<T>)oa[1];
					return;
				}
			}
			clazz = clazz.getSuperclass();
		}
		throw new AppException("Subclass does not parametrize generic superclass.");
	}
}
