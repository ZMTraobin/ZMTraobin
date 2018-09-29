package com.cmig.future.csportal.common.utils.fastdfs;

import org.apache.commons.io.FileUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhangtao107@126.com on 2017/3/31.
 */
public class FastDFSClient {

    private static Logger logger= LoggerFactory.getLogger(FastDFSClient.class);
    private static final String CONFIG_FILENAME = "fdfs_client.conf";

    private static StorageClient1 storageClient1 = null;


    // 初始化FastDFS Client
    static {
        try {

            //获取classpath路径下配置文件"fdfs_client.conf"的路径
            //conf直接写相对于classpath的位置，不需要写classpath:
            String configPath = getClassPath()+File.separator+CONFIG_FILENAME;
            logger.debug(configPath);


            ClientGlobal.init(configPath);
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                throw new IllegalStateException("getConnection return null");
            }

            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null) {
                throw new IllegalStateException("getStoreStorage return null");
            }

            storageClient1 = new StorageClient1(trackerServer,storageServer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getClassPath() {
        String path = new Object() {
            public String getPath() {
                return this.getClass().getClassLoader().getResource("").getFile();
            }
        }.getPath();
        return path;
    }

    /** * 上传文件 * @param file 文件对象 * @param fileName 文件名 * @return */
    public static String uploadFile(File file, String fileName)throws Exception {
        return uploadFile(file,fileName,null);
    }

    /** * 上传文件 * @param file 文件对象 * @param fileName 文件名 * @param metaList 文件元数据 * @return */
    public static String uploadFile(File file, String fileName, Map<String,String> metaList) throws IOException, MyException {
        byte[] buff = FileUtils.readFileToByteArray(file);
        NameValuePair[] nameValuePairs = null;
        if (metaList != null) {
            nameValuePairs = new NameValuePair[metaList.size()];
            int index = 0;
            for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<String,String> entry = iterator.next();
                String name = entry.getKey();
                String value = entry.getValue();
                nameValuePairs[index++] = new NameValuePair(name,value);
            }
        }
        String extension=fileName.substring(fileName.lastIndexOf(".")+1);
        return storageClient1.upload_file1(buff, extension,nameValuePairs);
    }

    /** * 上传文件 * @param file 文件对象 * @param fileName 文件名 * @param metaList 文件元数据 * @return */
    public static String uploadFile(byte[] buff, String fileName, Map<String,String> metaList) throws IOException, MyException {
        NameValuePair[] nameValuePairs = null;
        if (metaList != null) {
            nameValuePairs = new NameValuePair[metaList.size()];
            int index = 0;
            for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<String,String> entry = iterator.next();
                String name = entry.getKey();
                String value = entry.getValue();
                nameValuePairs[index++] = new NameValuePair(name,value);
            }
        }
        String extension=fileName.substring(fileName.lastIndexOf(".")+1);
        return storageClient1.upload_file1(buff, extension,nameValuePairs);
    }

    /** * 获取文件元数据 * @param fileId 文件ID * @return */
    public static Map<String,String> getFileMetadata(String fileId) throws IOException, MyException {
        NameValuePair[] metaList = storageClient1.get_metadata1(fileId);
        if (metaList != null) {
            HashMap<String,String> map = new HashMap<String, String>();
            for (NameValuePair metaItem : metaList) {
                map.put(metaItem.getName(),metaItem.getValue());
            }
            return map;
        }
        return null;
    }

    /** * 删除文件 * @param fileId 文件ID * @return 删除失败返回-1，否则返回0 */
    public static int deleteFile(String fileId) throws IOException, MyException {
        return storageClient1.delete_file1(fileId);
    }

    /** * 下载文件 * @param fileId 文件ID（上传文件成功后返回的ID） * @param outFile 文件下载保存位置 * @return */
    public static int downloadFile(String fileId, File outFile) throws IOException, MyException {
        byte[] content = storageClient1.download_file1(fileId);
        FileUtils.writeByteArrayToFile(outFile,content);
        return 0;
    }
}
