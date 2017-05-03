package util;

import models.Config;
import play.mvc.Http;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CopyRright (c)2007-2013: 陕西北佳信息技术有限责任公司<br>
 * Project:互联网项目管理系统<br>
 * Class Type:工具类<br>
 * Comments:文件工具类<br>
 * JDK version:1.7<br>
 * Namespace:com.dl<br>
 * extends：<br>
 * implements：<br>
 * --------------------------------------------------------------<br>
 * V1.0 创建 houyaohui 2016-1-5 新项目<br>
 * --------------------------------------------------------------<br>
 */
public class FileUtil {
    private List<Map<String, String>> list = new ArrayList();

    /**
     * 保存文件
     *
     * @param filePart
     */

    public static String saveFile(Http.MultipartFormData.FilePart filePart,String dataSourceType) {
        //查询配置
        Config config = Config.getConfig(dataSourceType);
        String path = "";
        if (ObjectUtil.isNotNull(config)) {
            String fileName = filePart.getFilename();
            File file = filePart.getFile();

            try {
                FileInputStream is = new FileInputStream(file);
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                String dirName = "wordFile";
                File sf = new File(config.htmlpath + "/" + dirName);
                if (!sf.exists()) {
                    sf.mkdirs();
                }
                String prefix = fileName.substring(fileName.lastIndexOf("."));
                path = sf.getPath() + "/" + UUIDUtil.getUUID() + prefix;
                OutputStream os = new FileOutputStream(path);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();
                path = path.substring(config.htmlpath.length(), path.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path;
    }

    /**
     * Description: 打开目录
     *
     * @param path 目录路径
     * @return 文件目录
     * @throws IOException IO异常
     */

    public List<Map<String, String>> searchFile(String path, String keyWords) throws IOException {

        File file = new File(path);
        // 如果是目录则展示所有子目录列表
        if (file != null) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File temp : files) {
                    // 去掉隐藏文件夹
                    if (!temp.isHidden()) {
                        Map<String, String> filesMap = new HashMap<String, String>();
                        if (ObjectUtil.isNotNull(keyWords)) {
                            if (temp.getName().contains(keyWords)) {

                                filesMap.put("path", temp.getAbsolutePath().replace("\\", "/"));
                                if (temp.isDirectory()) {
                                    filesMap.put("type", "dir");
                                    list.add(filesMap);
                                } else {
                                    String fileName = temp.getName();
                                    String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                                    if("ftl".equalsIgnoreCase(prefix)){
                                        filesMap.put("type", "file");
                                        list.add(filesMap);
                                    }
                                }
                            }
                            if (temp.isDirectory()) {
                                searchFile(temp.getAbsolutePath(), keyWords);
                            }
                        } else {
                            filesMap.put("path", temp.getAbsolutePath().replace("\\", "/"));
                            if (temp.isDirectory()) {
                                filesMap.put("type", "dir");
                                list.add(filesMap);
                                searchFile(temp.getAbsolutePath(), keyWords);
                            } else {
                                String fileName = temp.getName();
                                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                                if("ftl".equalsIgnoreCase(prefix)){
                                    filesMap.put("type", "file");
                                    list.add(filesMap);
                                }
                            }
                        }

                    }

                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
      /*  try {
            List list = searchFile("F:/ppsfile", "pps");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    /**
     * Description: 打开目录
     *
     * @param path 目录路径
     * @return 文件目录
     * @throws IOException IO异常
     */

    public static List<Map<String, String>> openDir(String path) throws IOException {
        List<Map<String, String>> list = new ArrayList();
        File file = new File(path);
        // 如果是目录则展示所有子目录列表
        if (file != null) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File temp : files) {
                    // 去掉隐藏文件夹
                    if (!temp.isHidden()) {
                        Map<String, String> filesMap = new HashMap<String, String>();
                        filesMap.put("path", temp.getCanonicalPath().replace("\\", "/"));
                        if (temp.isDirectory()) {
                            filesMap.put("type", "dir");
                            list.add(filesMap);
                        } else {
                            String fileName = temp.getName();
                            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                            if ("ftl".equalsIgnoreCase(prefix)) {
                                filesMap.put("type", "file");
                                list.add(filesMap);
                            }

                        }

                    }

                }
            }
        }
        return list;
    }

    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @return 文件内容
     * @throws IOException
     */

    public static String readFile(String path) throws IOException {
        BufferedReader reader = null;
        File file = new File(path);
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            // 每次读一行
            while ((lineTxt = bufferedReader.readLine()) != null) {
                sb.append(lineTxt).append("\r\n");
            }
            // 关闭流
            read.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * Description: 创建文件夹
     *
     * @param path
     */
    public static void createDir(String path) {
        File file = new File(path);
        file.mkdirs();

    }

    /**
     * Description: 创建文件并写人数据
     *
     * @param path
     * @param content
     */
    public static void writeFile(String path, String content) {

        File file = new File(path);

        // 创建文件
        BufferedWriter writer = null;
        try {
            file.createNewFile();
            OutputStreamWriter write = new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8");
            writer = new BufferedWriter(write);
            // 写入内容
            writer.write(content);
            // 关闭流
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * Description: 删除文件夹(包含子文件夹)
     *
     * @param delPath
     */
    public static void delete(String delPath) {
        File file = new File(delPath);
        // 如果为文件则直接删除，反之则递归删除
        if (!file.isDirectory()) {
            file.delete();
        } else if (file.isDirectory()) {
            // 获取文件列表
            String[] filelist = file.list();
            // 循环获取每个文件或文件夹
            for (int i = 0; i < filelist.length; i++) {
                File delFile = new File(delPath + "\\" + filelist[i]);
                // 如果是文件夹则递归，如果说是文件则直接删除
                if (!delFile.isDirectory()) {
                    delFile.delete();
                } else if (delFile.isDirectory()) {
                    delete(delPath + "\\" + filelist[i]);
                }
            }
            file.delete();
        }
    }

}
