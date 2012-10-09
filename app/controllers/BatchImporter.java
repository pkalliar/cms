package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import models.Folder;
import models.Protocol;
import models.User;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class BatchImporter {
	
	public static String dateFormat = "dd-MM-yyyy";
	
	static String curDir = System.getProperty("user.dir") + System.getProperty("file.separator");
	
	public static boolean readFromExcel() throws InvalidFormatException, IOException, ParseException{
		
		
		
	    InputStream inp = new FileInputStream(curDir + "Test1.xls");
	    //InputStream inp = new FileInputStream("workbook.xlsx");

	    Workbook wb = WorkbookFactory.create(inp);
	    Sheet sheet = wb.getSheetAt(0);
	    Iterator<Row> itr = sheet.rowIterator();
	    while(itr.hasNext()) {
	    	Row row = itr.next();
	    	
	    	if(row.getRowNum() == 0)
	    		continue;
	    	
	    	int as = 		(int) row.getCell(0).getNumericCellValue();
	    	int folderS = 	(int)row.getCell(1).getNumericCellValue();
	    	Date createdOn = 	row.getCell(2).getDateCellValue();
	    	String creatorS = 	row.getCell(3).getStringCellValue();
	    	String subject = 	row.getCell(4).getStringCellValue();
	    	
	    	//System.out.println("Looking for folder: --" + folderS + "--");
	    	Folder folder = Folder.findByCode(folderS + "");
	    	//System.out.println("Found folder: --" + folder.code + "--");
	    	User creator = User.findByName(creatorS);
	    	
	    	Protocol prot = Protocol.findByAS(as);
	    	if(prot == null){
	    		prot = new Protocol();
	    		prot.alfasigma = as;
	    	}
	    	
	    	prot.folder = folder;
	    	prot.creator = creator;
	    	
	    	//DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        //Date date = (Date)formatter.parse(createdOn);
	    	prot.createdon = createdOn;
	    	
	    	prot.name = subject;
	    	
	    	Protocol.create(prot);
	    			
	    			
//	    	Iterator<Cell> citr = row.cellIterator();
//	    	while(citr.hasNext()) {
//	    		Cell cell = citr.next();
//	    		System.out.println(cell.getRowIndex() + "|" + cell.getColumnIndex() + "" + cell.getStringCellValue());
//	    		
//	    		
//	    	    int ctype = cell.getCellType();
//	    	    if( ctype == Cell.CELL_TYPE_NUMERIC){
//	    	    	System.out.println("Found numeric type: " + cell.getNumericCellValue());
//	    	    }else if( ctype == Cell.CELL_TYPE_STRING){
//	    	    	System.out.println("Found String type: " + cell.getStringCellValue());
//	    	    }
//	    	}
	    }
	    

		
		return true;
	}
	
	public static boolean saveToExcel() throws IOException{
		
	    Workbook wb = new HSSFWorkbook();
	    FileOutputStream fileOut = new FileOutputStream(curDir + "exported.xls");
	    
	    CreationHelper createHelper = wb.getCreationHelper();
	    
	    Sheet sheet = wb.createSheet("ekserxomena");
	    
	    Row row = sheet.createRow((short)0);
	    Cell cell = row.createCell(0);
	    cell.setCellValue("AS");
	    
	    cell = row.createCell(1);
	    cell.setCellValue("FAKELOS");
	    
	    cell = row.createCell(2);
	    cell.setCellValue("IMEROMINIA");
	    
	    cell = row.createCell(3);
	    cell.setCellValue("DIMIOURGOS");
	    
	    cell = row.createCell(4);
	    cell.setCellValue("THEMA");
	    
	    short rownum = 0;
	    List<Protocol> results = Protocol.all();
	    for(Protocol result : results){
	    	rownum++;
	    	Row datarow = sheet.createRow(rownum);
	    	
		    cell = datarow.createCell(0);
		    cell.setCellValue(result.alfasigma);
		    
		    cell = datarow.createCell(1);
		    cell.setCellValue(result.folder.code);
		    
		    CellStyle dateStyle = wb.createCellStyle();
		    dateStyle.setDataFormat(
		        createHelper.createDataFormat().getFormat(dateFormat));
		    cell = datarow.createCell(2);
		    cell.setCellValue(result.createdon);
		    cell.setCellStyle(dateStyle);
		    
		    cell = datarow.createCell(3);
		    cell.setCellValue(result.creator.name);
		    
		    cell = datarow.createCell(4);
		    cell.setCellValue(result.name);
	    	
	    }
	    

	    
	    Sheet folderSheet = wb.createSheet("folders");
	    Row frow = folderSheet.createRow((short)0);
	    Cell fcell = frow.createCell(0);
	    fcell.setCellValue("id");
	    
	    fcell = frow.createCell(1);
	    fcell.setCellValue("folder code");
	    
	    fcell = frow.createCell(2);
	    fcell.setCellValue("folder name");
	    
	    fcell = frow.createCell(3);
	    fcell.setCellValue("folder category");
	    
	    rownum = 0;
	    List<Folder> fresults = Folder.findAll();
	    for(Folder result : fresults){
	    	rownum++;
	    	Row datarow = folderSheet.createRow(rownum);
	    	
	    	fcell = datarow.createCell(0);
	    	fcell.setCellValue(result.id);
	    	
	    	fcell = datarow.createCell(1);
	    	fcell.setCellValue(result.code);
	    	
	    	fcell = datarow.createCell(2);
	    	fcell.setCellValue(result.name);
	    	
//	    	fcell = datarow.createCell(3);
//	    	fcell.setCellValue(result);
	    	
	    }
	    
	    wb.write(fileOut);
	    fileOut.close();
	    
		
		
		return true;
	}
	
	public static boolean synchronizeFromExcel() throws IOException, InvalidFormatException {
		InputStream inp = new FileInputStream(curDir + "exported.xls");
		
	    Workbook wb = WorkbookFactory.create(inp);
	    Sheet sheet = wb.getSheetAt(0);
	    Iterator<Row> itr = sheet.rowIterator();
	    
	    while(itr.hasNext()) {
	    	Row row = itr.next();
	    	
	    	//System.out.println("We are in line: " + row.getRowNum() + ".");
	    	
	    	if(row.getRowNum() == 0)
	    		continue;
	    	
	    	Cell asCell = row.getCell(0);
	    	int as = (int) asCell.getNumericCellValue();
	    	
	    	String folderS = null;
	    	Cell folderCell = row.getCell(1);
	    	if(folderCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
	    		folderS = 	(int)folderCell.getNumericCellValue() + "";
	    	else if(folderCell.getCellType() == Cell.CELL_TYPE_STRING)
	    		folderS = folderCell.getStringCellValue();
	    	
	    	Date createdOn = 	row.getCell(2).getDateCellValue();
	    	
	    	String creatorS = 	row.getCell(3).getStringCellValue();
	    	
	    	String subject = 	row.getCell(4).getStringCellValue();
	    	
	    	
	    	
	    	Protocol prot = Protocol.findByAS(as);
	    	if(prot == null){
	    		//System.out.println(row.getRowNum() + "..Creating new as");
	    		prot = new Protocol();
	    		prot.alfasigma = as;
	    	}
	    	else{
	    		//System.out.println(row.getRowNum() + "..Updating existing as");
	    	}
	    	
	    	prot.createdon = createdOn;
	    	User creator = User.findByName(creatorS);
	    	prot.creator = creator;
	    	Folder folder = Folder.findByCode(folderS);
	    	if(folder == null){
	    		System.out.println(row.getRowNum() + "..Did not find folder: " + folderS);
	    	}
	    	prot.folder = folder;
	    	prot.name = subject; 
	    	
	    	Protocol.create(prot);
	    	
	    }
		
	    return true;
	}
	
	public static boolean importExistingAS() throws IOException, InvalidFormatException{
		
	    InputStream inp = new FileInputStream(curDir + "ALFASIGMA-TRIPOLIS-2011.xls");
	    //InputStream inp = new FileInputStream("workbook.xlsx");

	    Workbook wb = WorkbookFactory.create(inp);
	    Sheet sheet = wb.getSheetAt(1);
	    Iterator<Row> itr = sheet.rowIterator();
	    
	    List<String> foldersList = new ArrayList();
	    List<String> creatorsList = new ArrayList();
	    
	    while(itr.hasNext()) {
	    	Row row = itr.next();
	    	
	    	System.out.println("We are in line: " + row.getRowNum() + ".");
	    	
	    	if(row.getRowNum() == 0)
	    		continue;
	    	
	    	String as = null;
	    	Cell asCell = row.getCell(0);
	    	if(asCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
	    		as = 	(int)asCell.getNumericCellValue() + "";
	    	else if(asCell.getCellType() == Cell.CELL_TYPE_STRING)
	    		as = asCell.getStringCellValue();
	    	
	    	String folderS = null;
	    	Cell folderCell = row.getCell(2);
	    	if(folderCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
	    		folderS = 	(int)folderCell.getNumericCellValue() + "";
	    	else if(folderCell.getCellType() == Cell.CELL_TYPE_STRING)
	    		folderS = folderCell.getStringCellValue();
	    	
	    	if(!foldersList.contains(folderS))
	    		foldersList.add(folderS);
	    	
	    	
	    	Date createdOn = 	row.getCell(1).getDateCellValue();
	    	
	    	String creatorS = 	row.getCell(3).getStringCellValue();
	    	if(!creatorsList.contains(creatorS))
	    		creatorsList.add(creatorS);	    
	    	
	    	String subject = 	row.getCell(4).getStringCellValue();
	    	
	    	Protocol prot = new Protocol();
	    	prot.alfasigma = Integer.parseInt(as);
	    	prot.createdon = createdOn;
	    	User creator = User.findByName(creatorS);
	    	prot.creator = creator;
	    	Folder folder = Folder.findByCode(folderS);
	    	if(folder == null){
	    		folder = new Folder();
	    		folder.code = folderS;
	    		folder.name = "TO DO";
	    		Folder.create(folder);
	    	}
	    	prot.folder = folder;
	    	prot.name = subject;
	    	
	    	Protocol.create(prot);
	    	
	    }
	    
//	    System.out.println("Found " + foldersList.size() + " different folders.");
//	    for(String foldercode : foldersList){
//	    	//System.out.println("Found " + foldercode);
//	    	Folder folder = Folder.findByCode(foldercode);
//	    	if(folder == null){
//	    		folder = new Folder();
//	    		folder.code = foldercode;
//	    		folder.name = "TO DO";
//	    		Folder.create(folder);
//	    	}
//	    		
//	    }
	    
//	    System.out.println("Found " + creatorsList.size() + " different creators.");
//	    Workbook cwb = new HSSFWorkbook();
//	    FileOutputStream fileOut = new FileOutputStream(curDir + "creators.xls");
//	    Sheet csheet = cwb.createSheet("creators");
//	    
//	    short rownum = 0;
//	    for(String creator : creatorsList){
//	    	System.out.println("Found " + creator);
//	    	//Folder folder = Folder.findByCode(creator);
//		    Row row = csheet.createRow(rownum);
//		    Cell cell = row.createCell(0);
//		    cell.setCellValue(creator);
//		    rownum++;
//	    		
//	    }
//	    cwb.write(fileOut);
//	    fileOut.close();
	    
	    return true;
		
		
	}
	
		

}
