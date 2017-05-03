package util;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import play.Play;

import java.io.*;

public class FileToPdf {
	public static String toPdf(String path) throws IOException {
		String fileSuffix = path.substring(path.lastIndexOf(".")).toLowerCase();//获取文件的后缀。
		String documentName = path.substring(0, path.lastIndexOf("."));
		if (fileSuffix.matches(".txt|.java")){//如果上传的文件是txt或java文件
			converToTxtOrOdt(path, documentName + ".odt");//将txt或java文件转为odt文件
			converToPdf(documentName + ".odt", documentName + ".pdf");//将odt格式的文件转换为pdf文件 。
			new File(documentName + ".odt").delete();//删除odt源文件。
		}else if(fileSuffix.matches(".odt|.doc|.docx|.ppt|.pptx|.xls|.xlsx|.txt")){
			converToPdf(path, documentName + ".pdf");//将odt、doc、docx、ppt或pptx格式的文件转换为pdf文件。
		}
		return documentName + ".pdf";
	}
	/**
	 * 将java或txt文件转为odt文件
	 *
	 * @author ghj
	 */
	private static boolean converToTxtOrOdt(String sourceFilePath, String odtFileSavePath){
		
        try {   
			File sourceFile = new File(sourceFilePath);
		    FileInputStream input = new FileInputStream(sourceFile);// 新建文件输入流并对它进行缓冲
			BufferedInputStream inBuffer = new BufferedInputStream(input);
			FileOutputStream output = new FileOutputStream(new File(odtFileSavePath));// 新建文件输出流并对它进行缓冲
			BufferedOutputStream outBuffer = new BufferedOutputStream(output);
			byte[] byteArray = new byte[1024 * 5];// 缓冲数组
			int length;
			while((length = inBuffer.read(byteArray)) != -1) {
				outBuffer.write(byteArray, 0, length);
			}
			outBuffer.flush();// 刷新此缓冲的输出流
			// 关闭流
			inBuffer.close();
			outBuffer.close();
			output.close();
			input.close();
			//sourceFile.delete();//删除源文件(txt或java格式的文件)。
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e){
			return false;
		}
	}
	/**
	 * 将odt、doc、docx、ppt或pptx格式的文件转换为pdf文件，如果文件后缀是pdf，则直接返回true。
	 * 
	 * fileSuffix 文件的后缀。
	 * @throws IOException 
	 */
	private static boolean converToPdf(String sourceFilePath, String pdfFileSavePath) throws IOException {
		File sourceFile = new File(sourceFilePath);
		if (sourceFile.exists()) {
			String OS = System.getProperty("os.name").toLowerCase();

			String OpenOffice_HOME = Play.application().configuration().getString("OpenOffice");// OpenOffice的安装根目录
			String[] cmdargs = new String[3];
			cmdargs[0] = OpenOffice_HOME + "/program/soffice.exe";
			cmdargs[1] = "-headless";
			cmdargs[2] = "-accept=\"socket,host=127.0.0.1,port=8100;urp;\"";

			/*try {*/
				Process process = null;
				if (OS.indexOf("windows") >= 0) {// windows环境处理
					process = Runtime.getRuntime().exec(cmdargs);// 启动OpenOffice的服务
				}
				OpenOfficeConnection connection = new SocketOpenOfficeConnection(
						"127.0.0.1", 8100);
				connection.connect();

				if (connection.isConnected()) {// 如果连接成功

					DocumentConverter converter = new OpenOfficeDocumentConverter(
							connection);
					
					converter.convert(sourceFile, new File(pdfFileSavePath));
					connection.disconnect();// close the connection

					if (OS.indexOf("windows") >= 0) {// windows环境处理
						process.destroy();// 封闭OpenOffice服务的进程
					}

					return true;
				} else {
					return false;// swf转换器异常，openoffice连接失败！
				}

		} else {
			return false;
		}
	}
}
