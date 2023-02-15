package outline;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class OutlineFormats {
	
	public static final List<Function<Integer, String>> DEF_POINT_SYMBOLS = List.of(
		OutlineFormats::outlineHeader,
		OutlineFormats::numbers,
		OutlineFormats::lowercaseLetters,
		OutlineFormats::lowercaseRomanNumerals,
		OutlineFormats::uppercaseLetters
	);
	
	static TreeMap<Integer, String> valuesOfSymbols = new TreeMap<>();
	static {
		valuesOfSymbols.putAll(Map.ofEntries(
			Map.entry(1000, "M"),
			Map.entry(900, "CM"),
			Map.entry(500, "D"),
			Map.entry(400, "CD"),
			Map.entry(100, "C"),
			Map.entry(90, "XC"),
			Map.entry(50, "L"),
			Map.entry(40, "XL"),
			Map.entry(10, "X"),
			Map.entry(9, "IX"),
			Map.entry(5, "V"),
			Map.entry(4, "IV"),
			Map.entry(1, "I")
		));
	}
	
	public static Function<Integer, String> GET_DEF_POINT_SYMBOL(int depth) {
		if (depth < DEF_POINT_SYMBOLS.size())
			return DEF_POINT_SYMBOLS.get(depth);
		String wrapCount = "(" + (depth / DEF_POINT_SYMBOLS.size()) + ")";
		return (c) -> wrapCount + DEF_POINT_SYMBOLS.get(depth % DEF_POINT_SYMBOLS.size()).apply(c);
	}
	
	static String uppercaseLetters(Integer i) {
		return (char) ('A' + i) + ". ";
	}
	
	static String outlineHeader(Integer i) {
		return "";
	}
	
	static String lowercaseRomanNumerals(Integer i) {
		return lowercaseRomanNumerals_recur(i + 1) + ". ";
	}
	
	private static String lowercaseRomanNumerals_recur(Integer i) {
		int num = valuesOfSymbols.floorKey(i);
		if (i == num)
			return valuesOfSymbols.get(i).toLowerCase();
		return (valuesOfSymbols.get(num) + lowercaseRomanNumerals_recur(i - num)).toLowerCase();
	}
	
	static String lowercaseLetters(Integer i) {
		return (char) ('a' + i) + ". ";
	}
	
	static String numbers(Integer i) {
		return (i + 1) + ". ";
	}

}
