package outline;

public interface OutlineReducer<R> {
	
	R reduce(R reduced, Outline outline, Integer depth);

}
