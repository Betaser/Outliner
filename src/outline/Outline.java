package outline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import miscDataStructures.Mut;

public class Outline {
	
	public String contents;
	public List<Outline> points;
	
	public Outline() {
		points = new ArrayList<>();
	}
	
	public Outline(String contents) {
		this();
		this.contents = contents;
	}
	
	public Outline(Iterator<String> pointsData) {
		this();
		
		Outline curr = this;
		Stack<Outline> parents = new Stack<>();
		int tabs = 0;
		while (pointsData.hasNext()) {
			final String point = pointsData.next();
			final int countedTabs = countTabs(point);
			if (countedTabs > tabs) {
				Outline subOutline = new Outline();
				curr.points.add(subOutline);
				parents.push(curr);
				curr = subOutline;
			} else if (countedTabs < tabs) {
				for (int i = 0; i < tabs - countedTabs; i++)
					parents.pop();
			}
			if (curr.contents == null) {
				curr.contents = processLine(point);
			} else {
				Outline subOutline = new Outline();
				subOutline.contents = processLine(point);
				parents.peek().points.add(subOutline);
				curr = subOutline;
			}
			tabs = countedTabs;
		}
	}
	
	public Outline remove(Outline outline) {
		Mut<Outline> ret = new Mut<>();
		traverse(
			(o, d) -> {
				if (o.points.contains(outline)) {
					ret.val = outline;
					o.points.remove(outline);
				}
			},
			this,
			0
		);
		return ret.val;
	}
	
	public Outline get(int index) {
		Mut<Integer> c = new Mut<>(0);
		Mut<Outline> get = new Mut<>();
		Outline.traverse(
			(o, d) -> {
				if (c.val == index)
					get.val = o;
				c.val++;
			},
			this,
			0
		);
		return get.val;
	}
	
	public static void traverse(OutlineTraverser traverser, Outline outline, int depth) {
		traverser.traverse(outline, depth);
		for (Outline point : outline.points)
			traverse(traverser, point, depth + 1);
	}
	
	public static <T> T reduce(OutlineReducer<T> reducer, T seed, Outline outline, int depth) {
		seed = reducer.reduce(seed, outline, depth);
		for (Outline point : outline.points)
			seed = reduce(reducer, seed, point, depth + 1);
		return seed;
	}
	
	public static int countTabs(String line) {
		return (int) line.chars().filter(c -> c == '\t').count();
	}
	
	static String processLine(String line) {
		int index = line.lastIndexOf(".");
		if (index == -1)
			return line;
		return line.substring(index + 2);
	}
	
	public String toString() {
		String ret = reduce(
			(r, o, d) -> r +
					Stream.iterate(0, i -> i < d, i -> i + 1)
						.map(i -> "    ")
						.collect(Collectors.joining()) + 
					o.contents +
					'\n',
			"", 
			this, 
			0
		);
		return ret.length() > 0 ? ret.substring(0, ret.length() - 1) : ret;
	}

}
