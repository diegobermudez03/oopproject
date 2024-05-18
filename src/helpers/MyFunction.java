package helpers;

public interface MyFunction<T, U, E extends Exception> {
	
	void apply(T t, U u) throws Exception;

}
