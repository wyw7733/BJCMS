package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;

public class UeUploader {

	private File file; // 文件上传对象
	private String base64; // 文件上传对象
	private HashMap<String, Object> config; // 配置信息
	private String oriName; // 原始文件名
	private String oriBaseName; // 原始文件名,不包含类型后缀
	private String fileName; // 新文件名
	private String fullName; // 完整文件名,即URL名

	private Long fileSize; // 文件大小
	private String fileType; // 文件类型
	private String stateInfo; // 上传状态信息,
	private String type; // 文件上传对象 upload/base64

	private final String SUCCESS = "SUCCESS";
	// private final String ERROR_TMP_FILE = "临时文件错误";
	// private final String ERROR_TMP_FILE_NOT_FOUND = "找不到临时文件";
	private final String ERROR_SIZE_EXCEED = "文件大小超出网站限制";
	private final String ERROR_TYPE_NOT_ALLOWED = "文件类型不允许";
	private final String ERROR_CREATE_DIR = "目录创建失败";
	// private final String ERROR_DIR_NOT_WRITEABLE = "目录没有写权限";
	// private final String ERROR_FILE_MOVE = "文件保存时出错";
	private final String ERROR_FILE_NOT_FOUND = "找不到上传文件";
	// private final String ERROR_WRITE_CONTENT = "写入文件内容错误";
	private final String ERROR_UNKNOWN = "未知错误";

	// private final String ERROR_DEAD_LINK = "链接不可用";
	// private final String ERROR_HTTP_LINK = "链接不是http链接";
	// private final String ERROR_HTTP_CONTENTTYPE = "链接contentType不正确";
	// private final String INVALID_URL = "非法 URL";
	// private final String INVALID_IP = "非法 IP";

	public UeUploader(File file, String fileName,
			HashMap<String, Object> config, String type) {
		this.file = file;

		this.oriName = fileName;
		this.fileType = this.oriName.substring(this.oriName.lastIndexOf("."));
		this.oriBaseName = this.oriName.replace(this.fileType, "");
		this.config = config;
		this.type = type;
	}

	public UeUploader(String content, String fileName,
			HashMap<String, Object> config, String type) {
		this.base64 = content;

		this.oriName = fileName;
		this.fileType = this.oriName.substring(this.oriName.lastIndexOf("."));
		this.oriBaseName = this.oriName.replace(this.fileType, "");
		this.config = config;
		this.type = type;
	}
	
	public ObjectNode remoteUpload(ObjectNode node){
		
		saveRemote();
		
		node.put("state", stateInfo);
		node.put("source", base64);
		node.put("url", fullName);
		
		return node;
	}
	
	
	public String upload() {
		if (type.equals("base64")) {
			upBase64();
		} else {
			upfile();
		}

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("state", stateInfo);
		node.put("url", fullName);
		node.put("title", oriName);
		node.put("original", oriName);
		node.put("type", fileType);
		node.put("size", fileSize);

		String result = "";
		try {
			result = mapper.writeValueAsString(node);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private boolean checkSize() {
		long maxSize = (long) config.get("maxSize");
		return fileSize <= maxSize ? true : false;
	}

	private boolean checkType() {
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) config.get("allowFiles");
		return list.contains(fileType.toLowerCase()) ? true : false;
	}

	private void saveRemote() {
		// 文件保存目录路径
		String savePath = (String) config.get("savePath");

		// 文件保存目录URL
		String saveUrl = (String) config.get("pathFormat");

		// 格式验证
		//if (!checkType()) {
		//	stateInfo = ERROR_TYPE_NOT_ALLOWED;
		//	return;
		//}

		// 创建文件夹
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String ymd = sdf.format(new Date());

		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);

		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				stateInfo = ERROR_CREATE_DIR;
			}
		}

		fileName = oriBaseName + "_" + ymd + "_"
				+ UUID.randomUUID().toString().replace("-", "") + fileType;

		fullName = saveUrl + fileName;
		
		
	    try
        {
           URL url = new URL(base64);
           HttpURLConnection connection = (HttpURLConnection)url.openConnection();
           DataInputStream in = new DataInputStream(connection.getInputStream());
           DataOutputStream out = new DataOutputStream(new FileOutputStream(savePath + fileName));
           byte[] buffer = new byte[1024];
           int count = 0;
           while ((count = in.read(buffer)) > 0)
           {
               out.write(buffer, 0, count);
           }
           out.close();
           in.close();
           connection.disconnect();
           stateInfo = SUCCESS;

       }
       catch (Exception e)
       {
    	   stateInfo = ERROR_UNKNOWN;
       }
	    
	}

	private void upBase64() {
		// 文件保存目录路径
		String savePath = (String) config.get("savePath");

		// 文件保存目录URL
		String saveUrl = (String) config.get("pathFormat");

		// 创建文件夹
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String ymd = sdf.format(new Date());

		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);

		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				stateInfo = ERROR_CREATE_DIR;
			}
		}

		fileName = oriBaseName + "_" + ymd + "_"
				+ UUID.randomUUID().toString().replace("-", "") + fileType;

		fullName = saveUrl + fileName;

		byte[] buf = Base64.decodeBase64(base64);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(savePath + fileName);
			stateInfo = SUCCESS;
		} catch (FileNotFoundException e) {
			stateInfo = ERROR_UNKNOWN;
		}
		try {
			out.write(buf);
		} catch (IOException e) {
			stateInfo = ERROR_UNKNOWN;
		}
		try {
			out.close();
		} catch (IOException e) {
			stateInfo = ERROR_UNKNOWN;
		}
	}

	private void upfile() {
		if (file == null) {
			stateInfo = ERROR_FILE_NOT_FOUND;
			return;
		}

		// 文件保存目录路径
		String savePath = (String) config.get("savePath");

		// 文件保存目录URL
		String saveUrl = (String) config.get("pathFormat");

		fileSize = file.length();

		// 检查文件大小
		if (!checkSize()) {
			stateInfo = ERROR_SIZE_EXCEED;
			return;
		}

		// 检查文件类型
		if (!checkType()) {
			stateInfo = ERROR_TYPE_NOT_ALLOWED;
			return;
		}

		// 创建文件夹
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String ymd = sdf.format(new Date());

		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);

		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				stateInfo = ERROR_CREATE_DIR;
			}
		}

		fileName = oriBaseName + "_" + ymd + "_"
				+ UUID.randomUUID().toString().replace("-", "") + fileType;

		fullName = saveUrl + fileName;

		try {
			FileUtils.copyFile(file, new File(dirFile, fileName));
			file.delete();
			stateInfo = SUCCESS;

		} catch (IOException e) {
			stateInfo = ERROR_UNKNOWN;
		}
		/*try {
			if (file.renameTo(new File(dirFile, fileName))) {
				stateInfo = SUCCESS;
			} else {
				stateInfo = ERROR_UNKNOWN;
			}
		} catch (Exception e) {
			stateInfo = ERROR_UNKNOWN;
		}*/

	}

}
