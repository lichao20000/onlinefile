package cn.flying.rest.onlinefile.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import cn.flying.rest.onlinefile.entity.DataMap;
import cn.flying.rest.onlinefile.entity.Header;
import cn.flying.rest.onlinefile.entity.Record;
import cn.flying.rest.onlinefile.entity.ValueCell;


/**
 * 解析excel工具
 *
 */
public class DataTable implements Serializable {
  private static final long serialVersionUID = 1L;

  private String name;

  private boolean isEmpty = true;

  private List<Header> headers = new ArrayList<Header>();

  private List<Record> records = new ArrayList<Record>();

  private boolean validateResult;

  private int buildedNo = 1;
  public static final int pageCount = 100;

  public boolean isValidateResult() {
    return validateResult;
  }

  public void setValidateResult(boolean validateResult) {
    this.validateResult = validateResult;
  }

  public List<Header> getHeaders() {
    return headers;
  }

  public List<Record> getRecords() {
    return records;
  }

  /** xiaoxiong 20130402 为了解析实时进度条 添加HttpSession参数 **/
  public void initHeaderByExcel(File excelFile, HttpSession session) {
    try {
      new DataTableParseExcel(excelFile, session).initDataTableHeader();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** xiaoxiong 20130402 为了解析实时进度条 添加HttpSession参数 **/
  public void initByExcel(File excelFile, HttpSession session) {
    try {
      System.out.println("开始解析XLS文件！！！");
      long startTime = System.currentTimeMillis();
      new DataTableParseExcel(excelFile, session).initDataTable();
      System.out.println("解析XLS文件总耗时：" + (System.currentTimeMillis() - startTime));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class DataTableParseExcel{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** xiaoxiong 20130402 为了解析实时进度条 **/
    private HttpSession session;

    private POIFSFileSystem poiFileSystem;

    private HSSFWorkbook workBook;


    private HSSFSheet sheet;

    private int columnNumber = -1;

    private int rowNumber = -1;
    
    
    // huying 20110917 将hh改为HH
    public final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /** xiaoxiong 20130402 为了解析实时进度条 添加HttpSession参数 **/
    @SuppressWarnings("unchecked")
    public DataTableParseExcel(File excelFile, HttpSession session) throws FileNotFoundException,
        IOException {
      this.session = session;
      poiFileSystem = new POIFSFileSystem(new FileInputStream(excelFile));
      workBook = new HSSFWorkbook(poiFileSystem);
      sheet = workBook.getSheetAt(0);
      rowNumber = sheet.getLastRowNum();
      /** xiaoxiong 20130402 为了解析实时进度条 **/
      if (null != session.getAttribute("dataTablesMsg"))
        ((HashMap) session.getAttribute("dataTablesMsg")).put("allCount", rowNumber);
      if (null != session.getAttribute("dataTablesMsg"))
        ((HashMap) session.getAttribute("dataTablesMsg")).put("buildedNo", 1);
      if (rowNumber > -1) {
        columnNumber = sheet.getRow(0).getPhysicalNumberOfCells();
      }
    }

    @SuppressWarnings("unchecked")
    public void initDataTable() {
      if (rowNumber > -1) {
        for (int i = 0; i <= rowNumber; i++) {
          HSSFRow row = sheet.getRow(i);
          Record record = new Record();
          for (int j = 0; j < columnNumber; j++) {
            HSSFCell cell = row.getCell(j);
            ValueCell value = new ValueCell(getCellValue(cell, i, j));
            if (i == 0) {
              headers.add(new Header(value.getValue()));
            } else {
              Header header = headers.get(j);
              header.resetIsNull(value.isNull());
              if (!value.isEmpty()) {
                header.resetLength(value.getLength());
                header.resetType(value.getType());
                header.resetIsContainsSpecialChar(value);
                record.setValidateResult(!value.isContainsSpecialChar());
              }
              // record.getValues().add(new Pair(header, value));
              record.getValues().put(header.getName(), value);
              isEmpty = false;
            }
          }
          if (i != 0) {
            records.add(record);
          }
          /** xiaoxiong 20130402 为了解析实时进度条 **/
          if (null != session.getAttribute("dataTablesMsg"))
            ((HashMap) session.getAttribute("dataTablesMsg")).put("buildedNo", i);
        }
      }
    }

    public void initDataTableHeader() {
      if (rowNumber > -1) {
        for (int i = 0; i < 1; i++) {
          HSSFRow row = sheet.getRow(i);
          for (int j = 0; j < columnNumber; j++) {
            HSSFCell cell = row.getCell(j);
            ValueCell value = new ValueCell(getCellValue(cell, i, j));
            headers.add(new Header(value.getValue()));
          }
        }
      }
    }

    private String getCellValue(HSSFCell cell, int i, int j) {
      if (cell == null) {
        return "";
      }
      int cellType = cell.getCellType();
      switch (cellType) {
        case HSSFCell.CELL_TYPE_NUMERIC:
          try { // wuxing 20120424
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
              Date dateValue = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
              // int index = dateValue.toString().indexOf("CST") ;
              // long yearLong = Long.parseLong(dateValue.toString().substring(index + 4).trim()) ;
              if (timeFormat.format(dateValue).indexOf("1899-12-31") != -1) {
                return timeFormat.format(dateValue).replaceFirst("1899-12-31 ", "");
              } else {
                return dateFormat.format(dateValue);
              }

            } else {
              Double dbl = new Double(cell.getNumericCellValue());
              if (dbl.doubleValue() == dbl.intValue()) {
                return "" + dbl.intValue();
              } else {
                return "" + dbl.doubleValue();
              }
            }
          } catch (Exception e) {
            return "";
          }
        case HSSFCell.CELL_TYPE_FORMULA:
          return cell.getCellFormula();
        case HSSFCell.CELL_TYPE_BOOLEAN:
          return new Boolean(cell.getBooleanCellValue()).toString();
        case HSSFCell.CELL_TYPE_ERROR:
          return new Byte(cell.getErrorCellValue()).toString();
        default:
          return cell.getStringCellValue().trim();
      }
    }
  }

  public boolean isEmpty() {
    return isEmpty;
  }
  
  public List<Map<String,String>> getDataMapList(long start, long limit){
    List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    if (!isEmpty) {
      int j = 0;
      for (Record record : records) {
        if (start == 0 && limit == 0) {
          list.add(record.getData());
        } else {
          if (j >= start && j < start + limit) {
            list.add(record.getData());
          }else{
            break;
          }
        }
        j++;

      }
    }
    return list;
  }

  public List<DataMap> getDataMap(long start, long limit) {
    List<DataMap> list = new ArrayList<DataMap>();
    if (!isEmpty) {
      int j = 0;
      for (Record record : records) {
        if (start == 0 && limit == 0) {
          list.add(record.getDataMap());
        } else {
          if (j >= start && j < start + limit) {
            list.add(record.getDataMap());
          }
        }
        j++;

      }
    }
    return list;
  }

  public List<DataMap> getValidatedDataMap(HashMap<String, Header> hm) {
    List<DataMap> list = new ArrayList<DataMap>();
    if (!isEmpty) {
      for (Record record : records) {
        if (record.isValidateResult()) {
          list.add(record.getDataMap(hm));
        }
      }
    }
    return list;
  }

  public List<Record> getNoValidatedRecord() {
    List<Record> list = new ArrayList<Record>();
    if (!isEmpty) {
      for (Record record : records) {
        if (!record.isValidateResult()) {
          list.add(record);
        }
      }
    }
    return list;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /*****************************************************************************************************************************/
  /*************************************************** 解析XLSX文件 start ************************************/
  /*****************************************************************************************************************************/
  public void initByXlsxExcel(String excelFilePath, HttpSession session) {
    try {
      System.out.println("开始解析XLSX文件！！！");
      long startTime = System.currentTimeMillis();
      new DataTableParseXlsxExcel(excelFilePath, session).initDataTable();
      System.out.println("解析XLSX文件总耗时：" + (System.currentTimeMillis() - startTime));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class DataTableParseXlsxExcel {
    /** 文件路径 **/
    private String excelFilePath = null;
    /** 时间、日期格式模型 **/
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /** 数据类型Map **/
    private HashMap<Integer, Integer> styleMap = new HashMap<Integer, Integer>();
    /** 行数据集合 **/
    private Record record = null;

    private HttpSession session = null;

    /** 构造方法 **/
    public DataTableParseXlsxExcel(String excelFilePath, HttpSession session)
        throws FileNotFoundException, IOException {
      this.excelFilePath = excelFilePath;
      this.session = session;
    }

    @SuppressWarnings("unchecked")
    public void initDataTable() {
      try {
        OPCPackage pkg = OPCPackage.open(this.excelFilePath);
        XSSFReader r = new XSSFReader(pkg);

        StylesTable st = r.getStylesTable();
        XMLReader stylesParser = fetchStyleParser(st);
        InputStream style = r.getStylesData();
        InputSource styleSource = new InputSource(style);
        stylesParser.parse(styleSource);
        style.close();

        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
          record = null;
          InputStream sheet = sheets.next();
          InputSource sheetSource = new InputSource(sheet);
          parser.parse(sheetSource);
          sheet.close();
          if (record != null && !record.isNull()) {
            records.add(record);
          }
          buildedNo++;
          if (null != session.getAttribute("dataTablesMsg"))
            ((HashMap) session.getAttribute("dataTablesMsg")).put("buildedNo", buildedNo);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public XMLReader fetchStyleParser(StylesTable sst) throws SAXException {
      XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
      ContentHandler handler = new StyleHandler(sst);
      parser.setContentHandler(handler);
      return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private class StyleHandler extends DefaultHandler {
      private StylesTable st;
      private int no = 0;

      private StyleHandler(StylesTable st) {
        this.st = st;
      }

      public void startElement(String uri, String localName, String name, Attributes attributes)
          throws SAXException {
        if (name.equals("xf")) {
          String xfId = attributes.getValue("xfId");
          if (xfId != null) {
            styleMap.put(no, Integer.parseInt(attributes.getValue("numFmtId")));
            no++;
          }
        }
      }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
      XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
      ContentHandler handler = new SheetHandler(sst);
      parser.setContentHandler(handler);
      return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private class SheetHandler extends DefaultHandler {
      private SharedStringsTable sst;
      private String lastContents;
      private boolean nextIsString;
      private int cellTypeInt;
      private int rowNo = 0;
      private int colNo = 0;
      private String columnName;
      HashMap<String, Integer> fields = new HashMap<String, Integer>();

      private SheetHandler(SharedStringsTable sst) {
        this.sst = sst;
      }

      private String getColumnName(String columnName) {
        int i = 0;
        while (columnName.charAt(i) > 64) {
          i++;
        }
        columnName = columnName.substring(0, i);
        return columnName;
      }

      @SuppressWarnings("unchecked")
      public void startElement(String uri, String localName, String name, Attributes attributes)
          throws SAXException {
        if (name.equals("c")) {
          String cellType = attributes.getValue("t");
          if (cellType != null && cellType.equals("s")) {
            nextIsString = true;
          } else {
            String s = attributes.getValue("s");
            if (s != null && !"".equals(s)) {
              cellTypeInt = Integer.parseInt(s);
              cellTypeInt = styleMap.get(cellTypeInt);
            }
            nextIsString = false;
          }
          columnName = getColumnName(attributes.getValue("r"));
        } else if (name.equals("row")) {
          if (rowNo > 1) {
            if (!record.isNull()) {
              records.add(record);
            }
            buildedNo++;
            if (null != session.getAttribute("dataTablesMsg"))
              ((HashMap) session.getAttribute("dataTablesMsg")).put("buildedNo", buildedNo);
            // mapList.add(map) ;
          } else {
            rowNo++;
          }
          colNo = 0;
          record = new Record();
        } else if (name.equals("dimension")) {
          String ref = attributes.getValue("ref");
          if (ref.indexOf(":") > -1) {
            ref = ref.split(":")[1];
            int i = 0;
            while (ref.charAt(i) > 57) {
              i++;
            }
            buildedNo = 1;
            ref = ref.substring(i);
            if (null != session.getAttribute("dataTablesMsg"))
              ((HashMap) session.getAttribute("dataTablesMsg")).put("allCount",
                  Integer.parseInt(ref));
            if (null != session.getAttribute("dataTablesMsg"))
              ((HashMap) session.getAttribute("dataTablesMsg")).put("buildedNo", 1);
          } else {
            buildedNo = 1;
            if (null != session.getAttribute("dataTablesMsg"))
              ((HashMap) session.getAttribute("dataTablesMsg")).put("buildedNo", 1);
            if (null != session.getAttribute("dataTablesMsg"))
              ((HashMap) session.getAttribute("dataTablesMsg")).put("allCount", 1);
          }
        }
        lastContents = "";
      }

      public void endElement(String uri, String localName, String name) throws SAXException {
        if (!name.equals("v") || (rowNo > 1 && headers.size() < colNo + 1) || rowNo == 0) {
          return;
        }
        String valueStr = "";
        if (nextIsString) {
          if (!lastContents.equals("")) {
            int idx = Integer.parseInt(lastContents);
            if (name.equals("v")) {
              valueStr = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            }
          }
        } else {
          if (name.equals("v")) {
            valueStr = lastContents;
            Date dateValue = null;
            switch (cellTypeInt) {
              case 14:
                /** 日期 **/
                dateValue = HSSFDateUtil.getJavaDate(Double.parseDouble(lastContents));
                valueStr = dateFormat.format(dateValue);
                break;
              case 176:
                /** 时间 **/
                dateValue = HSSFDateUtil.getJavaDate(Double.parseDouble(lastContents));
                valueStr = timeFormat.format(dateValue).replaceFirst("1899-12-31 ", "");
                break;
              default:

            }
          }
        }
        if (rowNo == 1) {
          if (!"".equals(valueStr.trim())) {
            headers.add(new Header(valueStr.trim()));
            fields.put(columnName, colNo);
          }
        } else {
          ValueCell value = new ValueCell(valueStr);
          Header header = headers.get(fields.get(columnName));
          if (!value.isNull()) {
            if (header.getIsNull()) {
              header.resetIsNull(value.isNull());
            }
            header.resetLength(value.getLength());
            header.resetType(value.getType());
            if (!header.isContainsSpecialChar()) {
              header.resetIsContainsSpecialChar(value);
            }
            if (record.isValidateResult()) {
              record.setValidateResult(!value.isContainsSpecialChar());
            }
            record.setIsNull(false);
          }
          record.getValues().put(header.getName(), value);
          if (isEmpty) {
            isEmpty = false;
          }
        }
        colNo++;
      }

      public void characters(char[] ch, int start, int length) throws SAXException {
        lastContents += new String(ch, start, length);
      }
    }
  }
  /*****************************************************************************************************************************/
  /*************************************************** 解析XLSX文件 end **************************************/
  /*****************************************************************************************************************************/

	public List<Map<String, String>> getDataMapListStep2(long start, long limit) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < records.size(); i++) {
			if(i>=start-1 && i<=limit-1){
				list.add(records.get(i).getData());
			}
		}
		return list;
	}
}
