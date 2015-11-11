# Coercers #

coerceers conver one value type to enother.

There is couple of system coercers, you can add your own.

The class `Coercions` manipulates with coercers, its method

```
Coercions.addCoercer(Coercer)
```

allows to add it. You can use `AbstractCoercer` class, which checks for empty values (and returns null in such case), then lets you to make conversion. Example is here:

```
Coercer<Byte, String> coercer = new AbstractCoercer<Byte, String>() {

	@Override
	protected String convertImpl(Byte source) {
		return Byte.toString(source);
	}
	
};
Coercions.addCoercer(coercer);
```