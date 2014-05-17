package net.fishear.utils;

import java.util.HashMap;
import java.util.Map;

import net.fishear.exceptions.CoercerNotFoundException;

public class Coercions
{

	
	private static final Map<Class<?>, Map<Class<?>, Coercer<?, ?>>> COERCERS = new HashMap<Class<?>, Map<Class<?>, Coercer<?, ?>>>();

	static {
		// String -> Long
		addCoercer_(new AbstractCoercer<String, Long>() {
			
			@Override public Long convertImpl(String source) {
				return Long.valueOf(source);
			}
		});

		// String -> Integer
		addCoercer_(new AbstractCoercer<String, Integer>() {
			@Override public Integer convertImpl(String source) {
				return Integer.valueOf(source);
			}
		});

		// String -> Integer
		addCoercer_(new AbstractCoercer<String, Integer>() {
			@Override public Integer convertImpl(String source) {
				return Integer.valueOf(source);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static final <S, T> Coercer<S, T> getCoercer(Class<?> srcType, Class<?> dstType) {
		Map<Class<?>, Coercer<?, ?>> map = COERCERS.get(srcType);
		if(map != null) {
			return (Coercer<S, T>) map.get(dstType);
		}
		return null;
	}

	/** adds the given coercer to coercer list.
	 * @param coercer
	 */
	private static <S, T> void addCoercer_(Coercer<S, T> coercer) {
		Map<Class<?>, Coercer<?, ?>> map = COERCERS.get(coercer.getSourceType());
		if(map == null) {
			map = new HashMap<Class<?>, Coercer<?,?>>();
			COERCERS.put(coercer.getSourceType(), map);
		}
		map.put(coercer.getTargetType(), coercer);
	}

	public static <S, T> void addCoercer(Coercer<S, T> coercer) {
		Coercer<S, T> coercer2 = Coercions.getCoercer(coercer.getSourceType(), coercer.getTargetType());
		if(coercer2 != null) {
			throw new IllegalStateException(String.format("The coercer from '%s' to '%s' is already registered", coercer.getSourceType().getName(), coercer.getTargetType().getName()));
		}
		addCoercer_(coercer);
	}

	public static final <T, S> T convertType(S source, Class<T> targetType) {
		if(source == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		Coercer<S, T> coercer = (Coercer<S, T>) Coercions.getCoercer(source.getClass(), targetType);
		if(coercer == null) {
			throw new CoercerNotFoundException(String.format("Cannot convert value '%s' from sourcetype '%s' to target type '%s', because Coercer between source type and target type is not registered.", source, source.getClass().getName(), targetType.getName()), source, targetType);
		}
		return coercer.convert(source);
	}
}
