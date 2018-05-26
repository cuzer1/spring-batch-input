package com.cuzer.springbatchinput.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;

public class StatelessItemReader implements ItemReader<String> {

	public final Iterator<String> data;

	public StatelessItemReader(List<String> data) {
		this.data = data.iterator();
	}

	@Override
	public String read() throws Exception {

		if (this.data.hasNext()) {
			return this.data.next();
		} else {
			return null;
		}
	}

}
