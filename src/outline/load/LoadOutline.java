package outline.load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import outline.Outline;

public class LoadOutline {
	
	public static Outline loadOutlineFromFile(File file) {
		Outline outline = null;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			List<String> points = new ArrayList<>();
			StringBuilder point = new StringBuilder();
			String rawData = br.lines().collect(Collectors.joining("\n"));
			char[] rawDatas = rawData.toCharArray();
			
			point.append(rawDatas[0]);
			
			for (int i = 1; i < rawDatas.length; i++) {
				final char ch = rawDatas[i];
				final char prev = rawDatas[i - 1];
				
				if (ch == '\t' && prev == '\n') {
					points.add(point.toString().substring(0, point.toString().length() - 1));
					point.delete(0, point.toString().length());
				}
				point.append(ch);
			}
			points.add(point.toString());
			
			outline = new Outline(points.iterator());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outline;
	}

}
