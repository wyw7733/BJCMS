package util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Play;

public class Ueditor {

	public static String config() {
		String result = "";

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();

		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) Play
				.application().configuration().getObject("ueditor_conf");

		for (Entry<String, Object> entry : map.entrySet()) {
			String k = entry.getKey();
			Object v = entry.getValue();

			if (v instanceof String) {
				node.put(k, v.toString());
			} else if (v instanceof Long) {
				node.put(k, ((Long) v).longValue());
			} else if (v instanceof Integer) {
				node.put(k, ((Integer) v).longValue());
			}else if (v instanceof ArrayList) {
				ArrayNode ndArray = mapper.createArrayNode();

				@SuppressWarnings("unchecked")
				List<String> l = (ArrayList<String>) v;
				for (int i = 0; i < l.size(); i++) {
					ndArray.add(l.get(i));
				}
				node.put(k, ndArray);
			}
		}
		try {
			result = mapper.writeValueAsString(node);
		} catch (JsonProcessingException e) {

		}
		return result;
	}

	public static String uploadFileDeal(File file, String fileName, String type) {
		String result = "";

		String fileType = "upload";

		HashMap<String, Object> config = new HashMap<String, Object>();

		if (type.equals("image")) {
			config.put("savePath", Play.application().configuration()
					.getString("ueditor_conf.imageSavePath"));
			config.put("pathFormat", Play.application().configuration()
					.getString("ueditor_conf.imagePathFormat"));
			config.put(
					"maxSize",
					Play.application().configuration()
							.getLong("ueditor_conf.imageMaxSize"));
			config.put("allowFiles", Play.application().configuration()
					.getList("ueditor_conf.imageAllowFiles"));

		} else if (type.equals("video")) {
			config.put("savePath", Play.application().configuration()
					.getString("ueditor_conf.videoSavePath"));
			config.put("pathFormat", Play.application().configuration()
					.getString("ueditor_conf.videoPathFormat"));
			config.put(
					"maxSize",
					Play.application().configuration()
							.getLong("ueditor_conf.videoMaxSize"));
			config.put("allowFiles", Play.application().configuration()
					.getList("ueditor_conf.videoAllowFiles"));
			
			

		} else if (type.equals("file")) {
			config.put("savePath", Play.application().configuration()
					.getString("ueditor_conf.fileSavePath"));
			config.put("pathFormat", Play.application().configuration()
					.getString("ueditor_conf.filePathFormat"));
			config.put(
					"maxSize",
					Play.application().configuration()
							.getLong("ueditor_conf.fileMaxSize"));
			config.put("allowFiles", Play.application().configuration()
					.getList("ueditor_conf.fileAllowFiles"));

		}

		UeUploader up = new UeUploader(file, fileName, config, fileType);
		result = up.upload();

		return result;

	}
	
	public static String catchRemote(List<String> source){
		String result = "";
		
		HashMap<String, Object> config = new HashMap<String, Object>();
		config.put(
				"savePath",
				Play.application().configuration()
						.getString("ueditor_conf.catcherSavePath"));
		config.put(
				"pathFormat",
				Play.application().configuration()
						.getString("ueditor_conf.catcherPathFormat"));
		config.put(
				"maxSize",
				Play.application().configuration()
						.getLong("ueditor_conf.catcherMaxSize"));
		config.put(
				"allowFiles",
				Play.application().configuration()
						.getList("ueditor_conf.catcherAllowFiles"));
		
		String fileType = "remote";
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode ndArray = mapper.createArrayNode();
		for(int i = 0; i< source.size(); i++){
			ObjectNode node  = mapper.createObjectNode();
			String url = source.get(i);
			UeUploader up = new UeUploader(url, "remote.png", config, fileType);
			up.remoteUpload(node);
			ndArray.add(node);
		}
		
		ObjectNode nodeResult  = mapper.createObjectNode();
		nodeResult.put("list", ndArray);

		try {
			result = mapper.writeValueAsString(nodeResult);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	public static String uploadBase64Deal(String content) {
		String result = "";

		HashMap<String, Object> config = new HashMap<String, Object>();
		config.put(
				"savePath",
				Play.application().configuration()
						.getString("ueditor_conf.scrawlSavePath"));
		config.put(
				"pathFormat",
				Play.application().configuration()
						.getString("ueditor_conf.scrawlPathFormat"));
		config.put(
				"maxSize",
				Play.application().configuration()
						.getLong("ueditor_conf.scrawlMaxSize"));
		config.put(
				"allowFiles",
				Play.application().configuration()
						.getList("ueditor_conf.scrawlAllowFiles"));
		
		String fileType = "base64";
		
		UeUploader up = new UeUploader(content, "scrawl.png", config, fileType);
		result = up.upload();

		return result;
	}

	public static String listManager(String url, int start, int listSize) {
		String result = "";

		String imgActionName = Play.application().configuration()
				.getString("ueditor_conf.imageManagerActionName");

		int end = 0;

		List<Object> allowFiles = new ArrayList<Object>();
		String path = "";
		String urlPath = "";

		if (url.indexOf(imgActionName) == 0) {
			
			// 列出指定目录下的图片
			if (url.equals(imgActionName)) {
				
				listSize = Play.application().configuration()
						.getInt("ueditor_conf.imageManagerListSize");
				allowFiles = Play.application().configuration()
						.getList("ueditor_conf.imageManagerAllowFiles");
				path = Play.application().configuration()
						.getString("ueditor_conf.imageManagerListPath");
				urlPath = Play.application().configuration()
						.getString("ueditor_conf.imageManagerUrlPath");
			}

		} else if (url.indexOf(Play.application().configuration()
				.getString("ueditor_conf.fileManagerActionName")) == 0) {
			// 列出指定目录下的文件
			listSize = Play.application().configuration()
					.getInt("ueditor_conf.fileManagerListSize");
			allowFiles = Play.application().configuration()
					.getList("ueditor_conf.fileManagerAllowFiles");
			path = Play.application().configuration()
					.getString("ueditor_conf.fileManagerListPath");
			urlPath = Play.application().configuration()
					.getString("ueditor_conf.fileManagerUrlPath");

		}

		end = start + listSize;

		List<HashMap<String, String>> files = new ArrayList<HashMap<String, String>>();

		
		fileList(new File(path), path, urlPath, allowFiles, files);

		int filesCount = files.size();
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		ArrayNode ndArray = mapper.createArrayNode();
		
		if (filesCount == 0) {
			node.put("state", "no match file");
		} else {

			if (end >= filesCount){
				end = filesCount - 1;
			}else{
				end = end - 1;
			}
			
			List<HashMap<String, String>> resultFiles = new ArrayList<HashMap<String, String>>();
			
			for (int i = start; i <= end; i++) {
				resultFiles.add(files.get(i));
				
				ObjectNode nd = mapper.createObjectNode();
				
				for (Entry<String, String> entry : files.get(i).entrySet()) {
					String k = entry.getKey();
					String v = entry.getValue();
					nd.put(k, v);
				}
				ndArray.add(nd);
			}
			
			node.put("state", "SUCCESS");
			
		}
		node.put("list", ndArray);
		node.put("start", start);
		node.put("total", filesCount);
		
		try {
			result = mapper.writeValueAsString(node);
		} catch (JsonProcessingException e) {

		}
		
		return result;
	}

	private static List<HashMap<String, String>> fileList(File file,
			String path, String urlPath, List<Object> allowFiles,
			List<HashMap<String, String>> resultFiles) {

		String filePath;
		String modifyTime;
		String fileExt;

		File[] files = file.listFiles();
		if (files == null) {
			return resultFiles;// 判断目录下是不是空的
		}
		for (File f : files) {
			if (f.isDirectory()) {// 判断是否文件夹
				fileList(f, path, urlPath, allowFiles, resultFiles);// 调用自身,查找子目录
			} else {

				filePath = f.getAbsolutePath().replace("\\", "/");
				fileExt = filePath.substring(filePath.lastIndexOf("."));
				
				filePath = filePath.replaceFirst(path, urlPath);

				if (allowFiles.contains(fileExt.toLowerCase())) {
					Calendar cal = Calendar.getInstance();
					long time = f.lastModified();
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					cal.setTimeInMillis(time);
					modifyTime = formatter.format(cal.getTime());

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("url", filePath);
					map.put("mtime", modifyTime);

					resultFiles.add(map);
				}
			}
		}
		return resultFiles;
	}

}
