package com.cmig.future.csportal.api.common.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.Base64Utils;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.fastdfs.FastDFSClient;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping(value = "${commonPath}/file")
public class FileUploadApiController extends BaseExtendController {

    private static Logger log = LoggerFactory.getLogger(FileUploadApiController.class);
    
    private static final int max=50;
    // 上传文件的最大文件大小
    private static final long MAX_FILE_SIZE = 1024 * 1024 * max;
    // 系统默认建立和使用的以时间字符串作为文件名称的时间格式
    private static final String DEFAULT_SUB_FOLDER_FORMAT_AUTO = "yyyyMMdd";
    private static final String DEFAULT_SUB_FOLDER_FORMAT_NO_AUTO = "yyyy-MM-dd";
    private static final String DEFAULT_SUB_FOLDER_FORMAT_SECOND = "yyyyMMdd_HHmmss_SSS";

    @Value("${api.maxUploadSize}")
    private String maxUploadSize;

    private static Map<String, String> imageTypes = new HashMap<String, String>();
    static {
        imageTypes.put("JPG", "JPG");
        imageTypes.put("JPEG", "JPEG");
        imageTypes.put("PNG", "PNG");
        imageTypes.put("BMP", "BMP");
    }

    /**
     * 文件上传
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/upload", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp upload(HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        JSONArray jsonArray=new JSONArray();
        try{
            //创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //判断 request 是否有文件上传,即多部分请求
            if(multipartResolver.isMultipart(request)){
                //转换成多部分request
                MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    // 记录上传过程起始时的时间，用来计算上传时间
                    int pre = (int) System.currentTimeMillis();
                    // 取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        // 取得当前上传文件的文件名称
                        String myFileName = file.getOriginalFilename();
                        // 获取上传文件大小
                        Long size = file.getSize();
                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if (myFileName.trim() != "") {
                            if (new Double(MAX_FILE_SIZE) < file.getSize()) {
                                throw new DataWarnningException("上传文件不能超过" + max+ "M");
                            }
                            Map<String, String> metaList = new HashMap<String, String>();
                            // metaList.put("width","60");
                            // metaList.put("height","60");
                            // metaList.put("author","张涛");
                            metaList.put("uploadDate", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
                            String fid = FastDFSClient.uploadFile(file.getBytes(), myFileName, metaList);
                            logger.debug("upload local file " + myFileName + " ok, fileid=" + fid);

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("fastdfsImageServer", Global.getImageServerPath());
                            jsonObject.put("attachUrl", fid);
                            jsonObject.put("showAttachUrl", Global.getFullImagePath(fid));
                            jsonObject.put("fileName", myFileName);
                            jsonObject.put("size", size);
                            jsonArray.add(jsonObject);
                        }
                    }
                    // 记录上传该文件后的时间
                    int finaltime = (int) System.currentTimeMillis();
                    logger.debug(new Integer(finaltime - pre).toString());
                }
            }
            retApp.setTotall(new Long(jsonArray.size()));
            retApp.setData(jsonArray);
            retApp.setStatus(OK);
            retApp.setMessage("上传成功");
        } catch (DataWarnningException e) {
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("上传失败");
        }
        return retApp;
    }

    /**
     * 文件上传
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/uploadImage", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp uploadImage(HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        JSONArray jsonArray = new JSONArray();
        try {
            // 创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    // 记录上传过程起始时的时间，用来计算上传时间
                    int pre = (int) System.currentTimeMillis();
                    // 取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        // 取得当前上传文件的文件名称
                        String myFileName = file.getOriginalFilename();
                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if (!StringUtils.isEmpty(myFileName.trim())) {
                            // 文件格式
                            String fileType = myFileName.substring(myFileName.lastIndexOf(".") + 1).toUpperCase();
                            if (!imageTypes.containsKey(fileType)) {
                                throw new DataWarnningException("请上传jpg,jpeg,png,bmp格式的图片文件！");
                            }
                            // 获取上传文件大小
                            Long size = file.getSize();
                            if (size == 0) {
                                throw new DataWarnningException("图片不存在！");
                            }

                            String checkImageWidthHeightFlagStr = getParam(request, "checkImageWidthHeightFlag",
                                    "false");
                            String mWidthStr = getParam(request, "mWidth", "1366");
                            String mHeightStr = getParam(request, "mHeight", "768");
                            String mSizeStr = getParam(request, "mSize", "2");
                            boolean checkImageWidthHeightFlag = Boolean.valueOf(checkImageWidthHeightFlagStr);
                            int mWidth = Integer.valueOf(mWidthStr);
                            int mHeight = Integer.valueOf(mHeightStr);
                            Double mSize = Double.valueOf(mSizeStr) * 1024 * 1024;

                            if (MAX_FILE_SIZE < file.getSize()) {
                                throw new DataWarnningException("上传文件不能超过" + max+ "M");
                            }

                            Map<String, String> metaList = new HashMap<String, String>();
                            metaList.put("width", mWidthStr);
                            metaList.put("height", mHeightStr);
                            // metaList.put("author","张涛");
                            metaList.put("uploadDate", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));

                            if (checkImageWidthHeightFlag) {
                                log.info("uploadApk................获取图片尺寸");
                                Iterator<?> readers = ImageIO.getImageReadersByFormatName(fileType);
                                ImageReader reader = (ImageReader) readers.next();
                                ImageInputStream iis = ImageIO.createImageInputStream(file.getInputStream());
                                reader.setInput(iis, true);
                                int width = reader.getWidth(0);
                                int height = reader.getHeight(0);
                                DecimalFormat df = new DecimalFormat("###");// 这样为保持4位
                                String s1 = df.format((width * 10000) / height);
                                String s2 = df.format((mWidth * 10000) / mHeight);
                                if (!s1.equals(s2) || (width < mWidth || height < mHeight)) {
                                    throw new DataWarnningException(
                                            "请上传(宽X高)像素不小于（" + mWidth + "X" + mHeight + "）像素的等比例图片！");
                                }
                                log.info("uploadApk................获取图片尺寸width=" + width + "height=" + height);
                            }

                            String fid = FastDFSClient.uploadFile(file.getBytes(), myFileName, metaList);
                            logger.debug("upload local file " + myFileName + " ok, fileid=" + fid);

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("fastdfsImageServer", Global.getImageServerPath());
                            jsonObject.put("attachUrl", fid);
                            jsonObject.put("showAttachUrl", Global.getFullImagePath(fid));
                            jsonObject.put("fileName", myFileName);
                            jsonObject.put("size", size);
                            jsonArray.add(jsonObject);
                        }
                    } else {
                        throw new DataWarnningException("图片不存在！");
                    }
                    // 记录上传该文件后的时间
                    int finaltime = (int) System.currentTimeMillis();
                    logger.debug(new Integer(finaltime - pre).toString());
                }
            }
            retApp.setTotall(new Long(jsonArray.size()));
            retApp.setData(jsonArray);
            retApp.setStatus(OK);
            retApp.setMessage("上传成功");
        } catch (DataWarnningException e) {
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("上传失败");
        }
        return retApp;
    }

    /**
     * 文件删除
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/delete", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp delete(HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        try {
            String fileId = getParam(request, "fileId", "");
            if (StringUtils.isEmpty(fileId)) {
                throw new DataWarnningException("文件id不能为空");
            }
            int result = FastDFSClient.deleteFile(fileId);
            if (-1 == result) {
                retApp.setStatus(FAIL);
                retApp.setMessage("删除失败");
            } else {
                retApp.setStatus(OK);
                retApp.setMessage("删除成功");
            }
        } catch (DataWarnningException e) {
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("删除失败");
        }
        return retApp;
    }

    /**
     * 获取文件元数据
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getMetadata", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp getMetadata(HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        try {
            String fileId = getParam(request, "fileId", "");
            if (StringUtils.isEmpty(fileId)) {
                throw new DataWarnningException("文件id不能为空");
            }
            Map<String, String> map = FastDFSClient.getFileMetadata(fileId);
            retApp.setData(map);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功");
        } catch (DataWarnningException e) {
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("查询失败");
        }
        return retApp;
    }

    @RequestMapping(value = "/editorImage", method = RequestMethod.POST)
    public RetApp uploadEditorImage(HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        JSONArray jsonArray=new JSONArray();
//        String mSizeStr = getParam(request,"mSize","");
//        Double mSize = Double.valueOf(mSizeStr)*1024*1024;
//        if(mSizeStr!=""){
//            maxUploadSize = String.valueOf(mSize);
//        }
        try{
            //创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //判断 request 是否有文件上传,即多部分请求
            if(multipartResolver.isMultipart(request)){
                //转换成多部分request
                MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    // 记录上传过程起始时的时间，用来计算上传时间
                    int pre = (int) System.currentTimeMillis();
                    // 取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        // 取得当前上传文件的文件名称
                        String myFileName = file.getOriginalFilename();
                        // 获取上传文件大小
                        Long size = file.getSize();
                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if (myFileName.trim() != "") {
//                            if (new Double(maxUploadSize) < file.getSize()) {
//                                throw new DataWarnningException("上传文件不能超过"
//                                        + String.format("%f", Double.valueOf(maxUploadSize) / (1024.0 * 1024.0)) + "M");
//                            }
                            try (PrintWriter out = response.getWriter()) {
                                response.setContentType("text/html; charset=UTF-8");
                                response.setHeader("Cache-Control", "no-cache");
                                Map<String, String> metaList = new HashMap<String, String>();
                                // metaList.put("width","60");
                                // metaList.put("height","60");
                                // metaList.put("author","张涛");
                                metaList.put("uploadDate", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
                                String fid = FastDFSClient.uploadFile(file.getBytes(), myFileName, metaList);
                                logger.debug("upload local file " + myFileName + " ok, fileid=" + fid);

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("fastdfsImageServer", Global.getImageServerPath());
                                jsonObject.put("attachUrl", fid);
                                jsonObject.put("showAttachUrl", Global.getFullImagePath(fid));
                                jsonObject.put("fileName", myFileName);
                                jsonObject.put("size", size);
                                jsonArray.add(jsonObject);
                                
                                String callback = request.getParameter("CKEditorFuncNum");
                                out.println("<script type=\"text/javascript\">");
                                out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + Global.getFullImagePath(fid) + "',''" + ")");
                                out.println("</script>");
                            }catch (Exception e) {
                                if (logger.isErrorEnabled()) {
                                    logger.error(e.getMessage(), e);
                                }
                            }
                        }
                    }
                    // 记录上传该文件后的时间
                    int finaltime = (int) System.currentTimeMillis();
                    logger.debug(new Integer(finaltime - pre).toString());
                }
            }
            retApp.setTotall(new Long(jsonArray.size()));
            retApp.setData(jsonArray);
            retApp.setStatus(OK);
            retApp.setMessage("上传成功");
        } catch (DataWarnningException e) {
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("上传失败");
        }
        return retApp;
    }

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/base64/upload", produces = { "application/json" }, method = RequestMethod.POST)
	public RetApp base64Upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String base64Code=getParam(request,"base64Code","");
		String fileType=getParam(request,"fileType","");
		JSONArray jsonArray=new JSONArray();
		if (!StringUtils.isEmpty(base64Code)&&!StringUtils.isEmpty(fileType)) {
			String basePath = Global.getUserfilesTempDir() + "base64File"+ File.separator + "temp" + File.separator;
			File baseDir = new File(basePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			String tempFilePath=basePath+ IdGen.uuid()+"."+fileType;
			Base64Utils.decodeToFile(tempFilePath,base64Code);

			File file=new File(tempFilePath);
			if (file != null) {
				// 取得当前上传文件的文件名称
				String myFileName = file.getName();
				// 获取上传文件大小
				Long size = file.length();
				// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
				if (myFileName.trim() != "") {
					if (new Double(maxUploadSize) < file.length()) {
						throw new DataWarnningException("上传文件不能超过"+ String.format("%f", Double.valueOf(maxUploadSize) / (1024.0 * 1024.0)) + "M");
					}
					Map<String, String> metaList = new HashMap<String, String>();
					// metaList.put("width","60");
					// metaList.put("height","60");
					// metaList.put("author","张涛");
					metaList.put("uploadDate", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
					String fid = FastDFSClient.uploadFile(FileUtils.readFileToByteArray(file), myFileName, metaList);
					logger.debug("upload local file " + myFileName + " ok, fileid=" + fid);

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("fastdfsImageServer", Global.getImageServerPath());
					jsonObject.put("attachUrl", fid);
					jsonObject.put("showAttachUrl", Global.getFullImagePath(fid));
					jsonObject.put("fileName", myFileName);
					jsonObject.put("size", size);
					jsonArray.add(jsonObject);
				}
				file.delete();
			}
		}
		return RetAppUtil.success(jsonArray,"上传成功！");
	}
}
