package com.cmig.future.csportal.common.utils.fastdfs;


import org.csource.common.MyException;
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
public class FastDFSClientTest {
    private Logger logger= LoggerFactory.getLogger(FastDFSClientTest.class);

    /** * 文件上传测试 */
    public void testUpload() throws IOException, MyException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\装修.png");
        Map<String,String> metaList = new HashMap<String, String>();
        metaList.put("width","60");
        metaList.put("height","60");
        metaList.put("author","张涛");
        metaList.put("date","20170331");
        String fid = FastDFSClient.uploadFile(file,file.getName(),metaList);
        logger.debug("upload local file " + file.getPath() + " ok, fileid=" + fid);
        //上传成功返回的文件ID： group1/M00/00/00/wKgAyVgFk9aAB8hwAA-8Q6_7tHw351.jpg
    }

    /** * 文件下载测试 */
    public void testDownload() throws IOException, MyException {
        int r = FastDFSClient.downloadFile("G01/M00/00/00/ChHIB1jeCTCAP6_NAAAM-pDs1uY901.png", new File("DownloadFile_fid.jpg"));
        logger.debug(r == 0 ? "下载成功" : "下载失败");
    }

    /** * 获取文件元数据测试 */
    public void testGetFileMetadata() throws IOException, MyException {
        Map<String,String> metaList = FastDFSClient.getFileMetadata("G01/M00/00/00/ChHIB1jeCTCAP6_NAAAM-pDs1uY901.png");
        for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String,String> entry = iterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            logger.debug(name + " = " + value );
        }
    }

    /** * 文件删除测试 */
    public void testDelete() throws IOException, MyException {
        int r = FastDFSClient.deleteFile("G01/M00/00/00/ChHIB1jeCTCAP6_NAAAM-pDs1uY901.png");
        logger.debug(r == 0 ? "删除成功" : "删除失败");
    }

    public static void main(String[] args) throws IOException, MyException {
        FastDFSClientTest a=new FastDFSClientTest();
        a.testUpload();
        a.testDownload();
        a.testGetFileMetadata();
        a.testDelete();
    }
}
