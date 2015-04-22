package com.gary.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class CSVUtils {

	public static List<String[]> reader(CsvReader reader, boolean filterHeader) throws IOException {
		List<String[]> list = new ArrayList<String[]>();
		if(filterHeader)
			reader.readHeaders();
		while (reader.readRecord()) {
			list.add(reader.getValues());
		}
		reader.close();
		return list;
	}

	public static Map<String, String> readerMap(CsvReader reader, boolean filterHeader) throws IOException {
		Map<String, String> map = new LinkedHashMap<String, String>();
		if(filterHeader)
			reader.readHeaders();
		while (reader.readRecord()) {
			String[] values = reader.getValues();
			map.put(values[0], values[values.length-1]);
		}
		reader.close();
		return map;
	}
	
	public static void writer(List<String[]> list, CsvWriter writer) throws IOException{
		for (String[] strings : list) {
			writer.writeRecord(strings);
			writer.endRecord();
		}
		writer.flush();
		writer.close();
	}
	
	public static void writer(Map<String, String> map, CsvWriter writer) throws IOException{
		for (String key : map.keySet()) {
			writer.write(key);
			writer.write(map.get(key));
			writer.endRecord();
		}
		writer.flush();
		writer.close();
	}
	
}
