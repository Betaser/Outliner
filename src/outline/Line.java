package outline;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import miscDataStructures.Mut;

public class Line {
	
	public String formatting;
	public Outline outline;
	public int xPos;
	public int yPos;
	
	public static void genLinesFromOutline(List<Line> lines, Outline outline) {
		lines.clear();
		List<Integer> countAtDepth = new ArrayList<>();
		final int N_TOTAL_LINES = Outline.reduce(
			(r, o, d) -> r + (int) o.contents.chars().filter(c -> c == '\n').count() + 1,
			0,
			outline,
			0
		);
		Mut<Integer> y = new Mut<>(0);
		Mut<Integer> at = new Mut<>(-1);
		
		Outline.traverse(
			(o, d) -> {
				if (d > at.val) {
					if (countAtDepth.size() <= d)
						countAtDepth.add(0);
					else
						countAtDepth.set(d, 0);
				}
				at.val = d;
				
				String formatting = Stream.iterate(0, i -> i < d, i -> i + 1)
					.map(i -> "        ")
					.collect(Collectors.joining()) +
					OutlineFormats.GET_DEF_POINT_SYMBOL(d).apply(countAtDepth.get(d))
				;
				
				Line line = new Line();
				line.formatting = formatting;
				line.outline = o;
				line.xPos = OutlineTab.TEXTFIELD_X_OFFSET;
				final int N_LINES = 1 + (int) line.outline.contents.chars().filter(c -> c == '\n').count();
				line.yPos = (N_TOTAL_LINES - y.val) * OutlineTab.LN_HEIGHT - OutlineTab.IMG_HEIGHT;
				
				lines.add(line);
				countAtDepth.set(d, countAtDepth.get(d) + 1);
				y.val += N_LINES;
			},
			outline,
			0
		);
	}
	
	public String toString() {
		return formatting + outline.contents;
	}
	
}
