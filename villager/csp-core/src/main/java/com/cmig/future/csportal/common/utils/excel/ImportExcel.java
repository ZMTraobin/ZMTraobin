/**
 * .
 */
package com.cmig.future.csportal.common.utils.excel;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.Reflections;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * @author ThinkGem
 * @version 2013-03-10
 */
public class ImportExcel {
	
	private static Logger log = LoggerFactory.getLogger(ImportExcel.class);
			
	
	/**
	 * 工作薄对象
	 */
	private Workbook wb;
	
	/**
	 * 工作表对象
	 */
	private Sheet sheet;
	
	/**
	 * 标题行号
	 */
	private int headerNum;
	
	/**
	* 错误信息集合
	*/
	private List<ExcelError> excelErrorList;

	/**
	 * excel数据集合
	 */
	private List excelDateList;

	public List getExcelDateList() {
		return excelDateList;
	}

	public void setExcelDateList(List excelDateList) {
		this.excelDateList = excelDateList;
	}

	/**
	 * 构造函数
	 * @param fileName 导入文件，读取第一个工作表
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(String fileName, int headerNum) 
			throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum);
	}
	
	/**
	 * 构造函数
	 * @param file 导入文件对象，读取第一个工作表
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(File file, int headerNum) 
			throws InvalidFormatException, IOException {
		this(file, headerNum, 0);
	}

	/**
	 * 构造函数
	 * @param fileName 导入文件
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(String fileName, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum, sheetIndex);
	}
	
	/**
	 * 构造函数
	 * @param file 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(File file, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
	}
	
	/**
	 * 构造函数
	 * @param multipartFile 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(MultipartFile multipartFile, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex);
	}

	/**
	 * 构造函数
	 * @param fileName 导入文件对象
	 * @param is
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		if (StringUtils.isBlank(fileName)){
			throw new RuntimeException("导入文档为空!");
		}else if(fileName.toLowerCase().endsWith("xls")){    
			this.wb = new HSSFWorkbook(is);    
        }else if(fileName.toLowerCase().endsWith("xlsx")){  
        	this.wb = new XSSFWorkbook(is);
        }else{  
        	throw new RuntimeException("文档格式不正确!");
        }  
		if (this.wb.getNumberOfSheets()<sheetIndex){
			throw new RuntimeException("文档中没有工作表!");
		}
		this.sheet = this.wb.getSheetAt(sheetIndex);
		this.headerNum = headerNum;
		this.excelErrorList=new ArrayList<ExcelError>();
		log.debug("Initialize success.");
	}
	
	/**
	 * 获取行对象
	 * @param rownum
	 * @return
	 */
	public Row getRow(int rownum){
		return this.sheet.getRow(rownum);
	}

	/**
	 * 获取数据行号
	 * @return
	 */
	public int getDataRowNum(){
		return headerNum+1;
	}
	
	/**
	 * 获取最后一个数据行号
	 * @return
	 */
	public int getLastDataRowNum(){
		return this.sheet.getLastRowNum()+1;
	}
	
	/**
	 * 获取最后一个列号
	 * @return
	 */
	public int getLastCellNum(){
		return this.getRow(headerNum).getLastCellNum();
	}
	
	public List<ExcelError> getExcelErrorList(){
		return this.excelErrorList;
	}
	/**
	 * 获取单元格值
	 * @param row 获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	/*public Object getCellValue(Row row, int column){
		Object val = "";
		try{
			Cell cell = row.getCell(column);
			if (cell != null){
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
					val = String.valueOf(cell.getNumericCellValue());  
				}else if (cell.getCellType() == Cell.CELL_TYPE_STRING){
					val = cell.getStringCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
					val = cell.getCellFormula();
				}else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					val = cell.getBooleanCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
					val = cell.getErrorCellValue();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return val;
		}
		return val;
	}*/
	public Object getCellValue(Row row, int column,Class<?> valType){
		Object val = "";
		try{
			Cell cell = row.getCell(column);
			if (cell != null){
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
					val = cell.getNumericCellValue();
					if(!StringUtils.isEmpty(val)){
						if (valType == String.class){
							CellStyle style=cell.getCellStyle();
							log.debug(style.getDataFormatString());
							if(!"0.0_ ".equals(style.getDataFormatString())){
								DecimalFormat df = new DecimalFormat("#");
								val=df.format(cell.getNumericCellValue()).toString(); 
								if("General".equals(style.getDataFormatString())){
									double value=cell.getNumericCellValue(); 
									if(value-Integer.valueOf(val.toString())!=0){
										val=value;
									}
								}
							}
						}
						if (valType == Date.class){
							DecimalFormat df = new DecimalFormat("#");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            val= sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
						}
						if (valType == Integer.class||valType == Long.class){
							DecimalFormat df = new DecimalFormat("#");
							val=df.format(cell.getNumericCellValue()).toString(); 
						}
					}
				}else if (cell.getCellType() == Cell.CELL_TYPE_STRING){
					val = cell.getStringCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
					val = cell.getCellFormula();
				}else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					val = cell.getBooleanCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
					val = cell.getErrorCellValue();
				}
			}
		}catch (Exception e) {
			return val;
		}
		return val;
	}
	/**
	 * 获取导入数据列表
	 * @param cls 导入对象类型
	 * @param groups 导入分组
	 */
	public <E> List<E> getDataList(Class<E> cls, int... groups) throws InstantiationException, IllegalAccessException{
		List<Object[]> annotationList = Lists.newArrayList();
		// Get annotation field 
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs){
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==2)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, f});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, f});
				}
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms){
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==2)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, m});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, m});
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		//log.debug("Import column count:"+annotationList.size());
		// Get excel data
		List<E> dataList = Lists.newArrayList();
		for (int i = this.getDataRowNum(); i < this.getLastDataRowNum(); i++) {
			E e = (E)cls.newInstance();
			int column = 0;
			Row row = this.getRow(i);
			
			/* 判断当前行是否为空行,如果是空行则跳到下一次循环
			boolean isEndRow = this.isEmptyRow(row, annotationList.size());
			if (isEndRow) {
				continue;
			}
			*/
			
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList){
				// Get param type and type cast
				Class<?> valType = Class.class;
				if (os[1] instanceof Field){
					valType = ((Field)os[1]).getType();
				}else if (os[1] instanceof Method){
					Method method = ((Method)os[1]);
					if ("get".equals(method.getName().substring(0, 3))){
						valType = method.getReturnType();
					}else if("set".equals(method.getName().substring(0, 3))){
						valType = ((Method)os[1]).getParameterTypes()[0];
					}
				}
				Object val = this.getCellValue(row, column++,valType);
				if (!StringUtils.isEmpty(val)){
					ExcelField ef = (ExcelField)os[0];
					// If is dict type, get dict value
					if (StringUtils.isNotBlank(ef.dictType())){
						String lableTemp=val.toString();
						val = CodeUtil.getDictValue(val.toString(), ef.dictType(), "","");
						//log.debug("Dictionary type value: ["+i+","+colunm+"] " + val);
						if(!StringUtils.isEmpty(lableTemp)&&StringUtils.isEmpty(val)){
							ExcelError excelError=new ExcelError(getSheetName(), row.getRowNum()+1, ef.title(), "该值不在字典项范围内");
							this.excelErrorList.add(excelError);
						}
					}
					
					if(ef.required()&&StringUtils.isEmpty(val)){
						ExcelError excelError=new ExcelError(getSheetName(), row.getRowNum()+1, ef.title(), "必填项未填写");
						this.excelErrorList.add(excelError);
					}
					//log.debug("Import value type: ["+i+","+column+"] " + valType);
					try {
						Cell cell = row.getCell(column-1);
						if (valType == String.class){
							val = String.valueOf(val.toString()).trim();
							try {
								Length length = ((Method)os[1]).getAnnotation(Length.class);
								if(null!=length&&length.max()>0){
									if (!StringUtils.isEmpty(val) && val.toString().length() > length.max()) {
										ExcelError excelError=new ExcelError(getSheetName(), row.getRowNum()+1, ef.title(), length.message());
										this.excelErrorList.add(excelError);
									}
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
						}else if (valType == Integer.class){
							try {
								val =Integer.parseInt(val.toString().trim());
							} catch (Exception e1) {
								ExcelError excelError=new ExcelError(getSheetName(), row.getRowNum()+1, ef.title(), "数据类型错误");
								this.excelErrorList.add(excelError);
							}
						}else if (valType == Long.class){
							try {
								val =Long.parseLong(val.toString().trim());
							} catch (Exception e1) {
								ExcelError excelError=new ExcelError(getSheetName(), row.getRowNum()+1, ef.title(), "数据类型错误");
								this.excelErrorList.add(excelError);
							}
						}else if (valType == Double.class){
							try {
								val =Double.parseDouble(val.toString().trim());
							} catch (Exception e1) {
								ExcelError excelError=new ExcelError(getSheetName(), row.getRowNum()+1, ef.title(), "数据类型错误");
								this.excelErrorList.add(excelError);
							}
						}else if (valType == Float.class){
							try {
								val =Float.parseFloat(val.toString().trim());
							} catch (Exception e1) {
								ExcelError excelError=new ExcelError(getSheetName(), row.getRowNum()+1, ef.title(), "数据类型错误");
								this.excelErrorList.add(excelError);
							}
						}else if (valType == Date.class){
							try {
								try {
									if(null!=cell){
										String mat=cell.getCellStyle().getDataFormatString().replace("\\","");
										if(!"yyyy/mm/dd hh:mm:ss".equals(mat)&&!"m/d/yy".equals(mat)&&!"yyyy/m/d;@".equals(mat)&&val!=null&&StringUtils.isNotEmpty(val.toString())){
											throw new RuntimeException("日期格式不正确！");
										}
									}
								} catch (Exception e1) {
									ExcelError excelError=new ExcelError(getSheetName(), row.getRowNum()+1, ef.title(), "数据格式错误");
									this.excelErrorList.add(excelError);
								}
								if(val!=null&&StringUtils.isNotEmpty(val.toString())){
                                    log.info(val.toString().trim());
									val = DateUtils.parseDate(val.toString().trim());
								}else{
									val=null;
								}
							} catch (Exception e1) {
								ExcelError excelError=new ExcelError(getSheetName(), row.getRowNum()+1, ef.title(), "数据类型错误");
								this.excelErrorList.add(excelError);
							}
						}else{
							if (ef.fieldType() != Class.class){
								val = ef.fieldType().getMethod("getValue", String.class).invoke(null, val.toString());
							}else{
								val = Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), 
										"fieldtype."+valType.getSimpleName()+"Type")).getMethod("getValue", String.class).invoke(null, val.toString());
							}
						}
						
						// set entity value
						if (os[1] instanceof Field){
							Reflections.invokeSetter(e, ((Field)os[1]).getName(), val);
						}else if (os[1] instanceof Method){
							String mthodName = ((Method)os[1]).getName();
							if ("get".equals(mthodName.substring(0, 3))){
								mthodName = "set"+StringUtils.substringAfter(mthodName, "get");
							}
							Reflections.invokeMethod(e, mthodName, new Class[] {valType}, new Object[] {val});
						}
						
					} catch (Exception ex) {
						log.info("Get cell value ["+i+","+column+"] error: " + ex.toString());
						val = null;
					}
					
				}
				sb.append(val+", ");
			}
			dataList.add(e);
			log.debug("Read success: ["+i+"] "+sb.toString());
		}
		setExcelDateList(dataList);
		return dataList;
	}

	public String getSheetName(){
		return this.sheet==null?"":this.sheet.getSheetName();
	}
	
	/**
	 * 判断是否为空行方法 一行中所有有效数据单元格都为空时则认为为空行
	 */
	public boolean isEmptyRow(Row row, int columnNum) {
		for (int column = 0; column < columnNum;column++) {
			Object val = this.getCellValue(row, column,null);
			if (!StringUtils.isEmpty(val)) {
				return false;
			}
		}
//		// 防止数据中间一行空数据
//		if (row < (sheet.getRows() - 1)) {
//			return false;
//		}
 
		return true;
	}
	
//	/**
//	 * 导入测试
//	 */
//	public static void main(String[] args) throws Throwable {
//		
//		ImportExcel ei = new ImportExcel("target/export.xlsx", 1);
//		
//		for (int i = ei.getDataRowNum(); i < ei.getLastDataRowNum(); i++) {
//			Row row = ei.getRow(i);
//			for (int j = 0; j < ei.getLastCellNum(); j++) {
//				Object val = ei.getCellValue(row, j);
//				System.out.print(val+", ");
//			}
//			System.out.print("\n");
//		}
//		
//	}

}
