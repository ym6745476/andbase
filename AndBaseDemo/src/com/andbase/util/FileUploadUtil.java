/*package com.andbase.util;
import java.io.File;import java.io.UnsupportedEncodingException;import java.util.ArrayList;import java.util.Calendar;import java.util.HashMap;import java.util.Iterator;import java.util.List;import java.util.Map;import java.util.TreeMap;import javax.servlet.http.HttpServletRequest;import org.apache.commons.fileupload.FileItem;import org.apache.commons.fileupload.FileUpload;import org.apache.commons.fileupload.FileUploadException;import org.apache.commons.fileupload.disk.DiskFileItemFactory;import org.apache.commons.fileupload.servlet.ServletFileUpload;import com.ab.util.AbDateUtil;import com.my.global.Constant;public class FileUploadUtil {
	private Map fileField = new TreeMap(); 
	private Map formField = new TreeMap(); 	//文件的保存后的可网络访问的地址，文件名和路径	private HashMap<String,String> filePaths = null; 
	private int memoryBlock = 2048; 
	private File saveFolder = null; 
	private boolean multipart = false; 
	private HttpServletRequest request = null; 
	private final int maxSize = Constant.UPLOAD_MAX_FILESIZE; 	private int fileCount = 0;
		public FileUploadUtil(File saveFolder) {
		this.saveFolder = saveFolder;		filePaths = new HashMap<String,String>(); 		if(!saveFolder.exists()){			saveFolder.mkdirs();		}	}
		public FileUploadUtil() {		filePaths = new HashMap<String,String>(); 		
	}	
	public HashMap<String,String> download(HttpServletRequest request, String charset) throws FileUploadException {
		this.request = request;		filePaths.clear();
		multipart = FileUpload.isMultipartContent(request);
		if (multipart) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(memoryBlock);			//缓冲区
			factory.setRepository(saveFolder);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxSize);
			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {
					processFormField(item, charset);
				} else {
					processUploadedFile(item);
				}
			}
		}		return filePaths;
	}	
	private void processFormField(FileItem item, String charset) {
		try {
			String name = item.getFieldName();
			String value = item.getString(charset);
			Object objv = formField.get(name);
			if (objv == null) {
				formField.put(name, value);
			} else {
				List values = null;
				if (objv instanceof List) {
					values = (List) objv;
					values.add(value);
				} else {
					String strv = (String) objv;
					values = new ArrayList();
					values.add(strv);
					values.add(value);
				}
				formField.put(name, values);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("the argument \"charset\" missing!");
		}
	}		*//**	 * 描述：上传文件	 * @param item	 * @date：2012-9-4 上午8:42:17	 * @version v1.0	 *//*
	private String processUploadedFile(FileItem item) {
		String fieldName = item.getFieldName();		String fileName = item.getName();
		fileField.put(fieldName, item);		fileCount++;		try {						//要保证名字唯一,加一秒			String str = AbDateUtil.getCurrentDateByFormat("yyyyMMddHHmmss", Calendar.SECOND, fileCount);			int start = fileName.lastIndexOf(".");			if(start!=-1){				String fileNewName = str+fileName.substring(start);				String newFilePath = saveFolder +Constant.SEPARATOR+ fileNewName;				File newFile = new File(newFilePath);								if (!newFile.exists()) {					newFile.createNewFile();				}				String newFilePathRet =  write2file(item,newFile);				//保存文件的网络地址				filePaths.put(fileNewName, newFilePathRet);				return newFilePathRet;			}		} catch (Exception e) {			e.printStackTrace();		}		return null;
	}	
	public static String write2file(FileItem item, File file) {
		try {
			item.write(file);			return file.getPath();
		} catch (Exception e) {			e.printStackTrace();
		}
		return null;
	}	
	public FileItem getFileItem(String fieldName) {
		if (multipart) {
			return (FileItem) fileField.get(fieldName);
		} else {
			return null;
		}
	}	
	public String getParameter(String fieldName) {
		String value = null;
		if (multipart) {
			Object obj = formField.get(fieldName);
			if (obj != null && obj instanceof String) {
				value = (String) obj;
			}
		} else if (request != null) {
			value = request.getParameter(fieldName);
		}
		return value;
	}	
	public String[] getParameterValues(String fieldName) {
		String[] values = null;
		if (multipart) {
			Object obj = formField.get(fieldName);
			if (obj != null) {
				if (obj instanceof List) {
					values = (String[]) ((List) obj).toArray(new String[0]);
				} else {
					values = new String[] {(String) obj};
				}
			}
		} else if (request != null) {
			values = request.getParameterValues(fieldName);
		}
		return values;
	}	
	public File getRepository() {
		return this.saveFolder;
	}	
	public int getSizeThreshold() {
		return this.memoryBlock;
	}	
	public boolean isMultipart() {
		return this.multipart;
	}	
	public int getMaxSize() {
		return maxSize;
	}
}*/