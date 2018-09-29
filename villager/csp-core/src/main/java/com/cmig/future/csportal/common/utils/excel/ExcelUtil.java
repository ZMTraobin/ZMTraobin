package com.cmig.future.csportal.common.utils.excel;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.Reflections;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.*;

public class ExcelUtil {
	private static Log log = LogFactory.getLog(ExcelUtil.class);
	public static final String SETTER_PREFIX = "set";

	public static final String GETTER_PREFIX = "get";

	public static ExcelField getExcelField(Object entity, String column) {
		String getterMethodName = ExcelUtil.GETTER_PREFIX + StringUtils.capitalize(column);
		Method method = Reflections.getAccessibleMethod(entity, getterMethodName, new Class[] {});
		ExcelField ef = method.getAnnotation(ExcelField.class);
		return ef;
	}
	
	/**
	 * 判断excel中的某几个字段数据是否重复，并将错误信息添加到errorList
	 * 
	 * @param dataList
	 * @param errorList
	 * @param columnNames
	 * @param cls
	 */
	public static <E> void duplicateMutiColumsValidator(String sheetName,int dataRowNum, List<E> dataList, List errorList, String[] columnNames, Class<E> cls) {
		Set set = new HashSet();
		List list = new ArrayList();
		String errorDataTitle = "";
		Map tempMap = new LinkedHashMap();// tempMap key:单元格的内容 value:单元格
		Map errorRowMap = new LinkedHashMap();// 存放错误（重复）单元格的行信息 key:内容 value:存放重复行的set

		Map errorMap = new LinkedHashMap();// 存放错误提示信息 key:内容 value:存放重复单元格的List
		// 遍历excel处理当期行数据
		for (int i = 0; i < dataList.size(); i++) {
			E e = (E) dataList.get(i);
			Map cellMap = getMutiColumsCombinationValue(e, columnNames);
			String combinationvalue = (String) cellMap.get("combinationvalue");
			if(StringUtils.isEmpty(combinationvalue)){
				continue;
			}
			if (i == 0) {
				errorDataTitle = cellMap.get("errorDataTitle").toString();
			}
			CellData cellData = new CellData();
			cellData.setRow(dataRowNum +1+i);
			cellData.setTitle(errorDataTitle);

			// 重复情况下：将单元格放入errorMap
			if (tempMap.containsKey(combinationvalue)) {
				// 在errorRowMap中存在该单元格（即多次重复情况），则获取与之重复的单元格的集合
				if (errorRowMap.containsKey(combinationvalue)) {
					set = (Set) errorRowMap.get(combinationvalue);
					list = (List) errorMap.get(combinationvalue);

				} else {// 如果不存在 ，set初始化，（即第一次出现重复值的情况）
					set = new HashSet();
					// 存储 key：单元格内容 ，set：null
					errorRowMap.put(combinationvalue, set);
					list = new ArrayList();
					// 存储 key：单元格内容 ，list：null
					errorMap.put(combinationvalue, list);
				}
				// 向集合中添加第一个出现错误的单元格信息，并保证不重复添加和有序
				if (set.add(tempMap.get(combinationvalue))) {
					list.add(tempMap.get(combinationvalue));
				}
				// 添加当前出现错误的单元格信息
				list.add(cellData);

			} else {
				// 未重复情况下：将单元格数据置入tempMap key:内容 value:单元格
				tempMap.put(combinationvalue, cellData);
			}
		}

		// 获取errorMap的所有key，即重复的cellData的内容
		Set keySet = errorMap.keySet();

		Iterator it = keySet.iterator();
		// 将重复单元格的信息添加到errorList
		while (it.hasNext()) {
			Object key = it.next();
			List rowlist = (List) errorMap.get(key);
			String errorMsg = "";
			Iterator i = rowlist.iterator();
			// 迭代行提示信息，拼接获取所有有重复的单元格的行信息的字符串
			while (i.hasNext()) {
				CellData cellData = (CellData) i.next();
				errorMsg += cellData.getRow() + "行、";
			}
			errorMsg = errorMsg.substring(0, errorMsg.lastIndexOf("、")) + "重复。";
			// 迭代行提示信息，将errorMsg添加到excel错误提示列表
			i = rowlist.iterator();
			while (i.hasNext()) {
				CellData cellData = (CellData) i.next();
				ExcelError error = new ExcelError(sheetName, cellData.getRow(), errorDataTitle, errorMsg);
				errorList.add(error);
			}

		}
	}

	/**
	 * 根据组合列获得对应值的组合，通过&连接
	 * 
	 * @param obj
	 * @param columnNames
	 * @return Map
	 */
	private static Map getMutiColumsCombinationValue(final Object obj, String[] columnNames) {
		Map cellMap = new HashMap();
		StringBuffer sbf = new StringBuffer();
		StringBuffer errorDataTitle = new StringBuffer();// 存储组合重复的列名，用+连接。
		for (int i = 0; i < columnNames.length; i++) {
			String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(columnNames[i]);
			Method method = Reflections.getAccessibleMethod(obj, getterMethodName, new Class[] {});
			ExcelField ef = method.getAnnotation(ExcelField.class);
			Object object = Reflections.invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
			String val;
			try {
				if (object instanceof String) {
					String s = String.valueOf(object.toString());
					if (StringUtils.endsWith(s, ".0")) {
						val = StringUtils.substringBefore(s, ".0");
					} else {
						val = String.valueOf(s.toString());
					}
				} else if (object instanceof Integer) {
					val = object.toString();
				} else if (object instanceof Long) {
					val = object.toString();
				} else if (object instanceof Double) {
					val = object.toString();
				} else if (object instanceof Float) {
					val = object.toString();
				} else if (object instanceof Date) {
					val = DateUtils.formatDate((Date) object, "yyyy-MM-dd HH:mm:ss");
				} else {
					if(null==object) val="";
					else {
						val = new Integer(object.hashCode()).toString();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				val = "";
			}

			errorDataTitle.append(ef.title());// 第一个字段需要带出sheet名
			errorDataTitle.append("+");
			if (i == columnNames.length - 1) {
				errorDataTitle.deleteCharAt(errorDataTitle.length() - 1);
			}
			sbf.append(val);
			sbf.append("&");
			if (i == columnNames.length - 1) {
				sbf.deleteCharAt(sbf.length() - 1);
			}
		}

		cellMap.put("errorDataTitle", errorDataTitle.toString());
		cellMap.put("combinationvalue", sbf.toString());
		return cellMap;
	}

}
