package b100.minimap.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import b100.minimap.Minimap;

public abstract class ConfigBase {
	
	public boolean read(File file) {
		if(!file.exists()) {
			return false;
		}
		
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
			read(stream);
			return true;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			try{
				stream.close();
			}catch (Exception e) {}
		}
	}
	
	public void read(InputStream stream) {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		
		List<Option<?>> options = getAllOptions();
		
		Map<String, Option<?>> optionMap = new HashMap<>();
		for(Option<?> option : options) {
			optionMap.put(option.name, option);
		}
		
		try {
			while(true) {
				String line = br.readLine();
				if(line == null) {
					break;
				}
				
				if(line.length() == 0 || line.startsWith("#")) {
					continue;
				}
				
				int i = line.indexOf(':');
				if(i == -1) {
					Minimap.log("Invalid line: \""+line+"\"");
					continue;
				}
				
				String key = line.substring(0, i);
				String value = line.substring(i + 1);
				
				Option<?> option = optionMap.get(key);
				if(option != null) {
					try {
						parse(option, value);
					}catch (Exception e) {
						Minimap.log("Invalid value for option "+option.name+": \""+value+"\"");
					}
				}else {
					Minimap.log("Invalid option: \""+key+"\"");
				}
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			try {
				br.close();
			}catch (Exception e) {}
		}
	}
	
	public void write(File file) {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			write(stream);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			try {
				stream.close();
			}catch (Exception e) {}
		}
	}
	
	public void write(OutputStream stream) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(stream);
			
			List<Option<?>> options = getAllOptions();
			
			for(Option<?> option : options) {
				writer.write(option.name + ":" + option.getValueString() + "\n");
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			try{
				writer.close();
			}catch (Exception e) {}
		}
	}
	
	private <E> void parse(Option<E> option, String value) {
		option.value = option.parse(value);
	}
	
	public abstract List<Option<?>> getAllOptions();
	
}
