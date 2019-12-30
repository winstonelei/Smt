package com.github.splitMethod;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WeightRandomTest {

	@Test
	public void weightRandomTest() {
		WeightRandom<String> random = WeightRandom.create();
		random.add("A", 10);
		random.add("B", 50);
		random.add("C", 100);


		Map<String,Integer> map = new HashMap<String,Integer>();
		for(int i=0;i<100;i++){
			String result = random.next();
			if(map.containsKey(result)){
				int count = map.get(result);
				count=count+1;
				map.put(result,count);
			}else{
				map.put(result,1);
			}
		}

		Iterator<Map.Entry<String, Integer>> iter = (Iterator<Map.Entry<String, Integer>>) map.entrySet().iterator();

		while(iter.hasNext()){
			Map.Entry<String,Integer> entry = iter.next();
			System.out.println(entry.getKey()+" == "+ entry.getValue());

		}



	}
}
