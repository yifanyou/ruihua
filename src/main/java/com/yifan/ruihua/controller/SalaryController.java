package com.yifan.ruihua.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.yifan.ruihua.model.Salary;

@Controller
public class SalaryController {
	private static final int SERIAL_NO_START = 7;
	
	private static String CompanyName = "";
	private static String PayrollTitle = "";
	private static String Period = "";
	private static String Currency = "";
	private static List<String> keyList = null; 
	private static Map<String, List<Salary>> salarys = null;
	
    @ModelAttribute("salarys")
    public Map<String, List<Salary>> poplateOrder(){
        return new HashMap();
    }
	
	@RequestMapping("/salary")
	public String show(){
		return "views/salary";
	}
	
	@RequestMapping(value="/salary", params={"importExcel"})
	public String importExcel(HttpServletRequest request,HttpServletResponse response,
            @RequestParam("file") MultipartFile file,
            ModelMap modelMap) throws EncryptedDocumentException, InvalidFormatException{
    	String filePath = "";

    	if (!file.isEmpty()) {  
            try {
                filePath = request.getSession().getServletContext().getRealPath("/upload");

                String tempPath = filePath + file.getOriginalFilename();
                File tempFile = new File(tempPath);
                if(tempFile.exists()){
                	tempFile.delete();
                }

                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(filePath, file.getOriginalFilename()));  
            	File saveFile = new File(filePath, file.getOriginalFilename());
            	
            	Workbook wb = WorkbookFactory.create(saveFile);
            	Sheet sheet = wb.getSheet("Payroll schedule");
            	
            	CompanyName = sheet.getRow(1).getCell(1).getStringCellValue();
            	PayrollTitle = sheet.getRow(2).getCell(1).getStringCellValue();
            	Period = sheet.getRow(3).getCell(1).getStringCellValue();
            	Currency = sheet.getRow(4).getCell(1).getStringCellValue();
            	

            	salarys = new HashMap<String, List<Salary>>();
            	keyList = new ArrayList<String>();
            	int countRowPos = SERIAL_NO_START;
            	int serialNOPos = 1;
            	
            	
            	List<Salary> salaryList = new ArrayList<Salary>();
            	Row row = null;
            	DecimalFormat formatCurrent=new DecimalFormat("#,###.00");
            	Date date=new Date();
            	SimpleDateFormat formatDate=new SimpleDateFormat("yyyy-MM-dd");
            	String startDate = formatDate.format(date);
            	do {
            		row = sheet.getRow(countRowPos++);
          		
            		if(Cell.CELL_TYPE_STRING==row.getCell(1).getCellType()
            				&&"Serial No.".equals(row.getCell(1).getStringCellValue().split("\n")[0])){
            			Salary salary = new Salary();
            			//salary.setStartDate(startDate);
            			//salary.setEmail("10747222@qq.com");
            			
            			salary.setSocialBenefitLocation(row.getCell(2).getStringCellValue());
            			salary.setName(row.getCell(3).getStringCellValue());
            			salary.setPosition(row.getCell(4).getStringCellValue());
            			salary.setBasicSalary(row.getCell(5).getStringCellValue());
            			salary.setBonus(row.getCell(6).getStringCellValue());
            			salary.setTotalGross(row.getCell(7).getStringCellValue());
            			salary.setSocialBenefitBasis(row.getCell(8).getStringCellValue());
            			salary.setPensionEmployee(row.getCell(9).getStringCellValue());
            			salary.setMedicalEmployee(row.getCell(10).getStringCellValue());
            			salary.setUnemploymentEmployee(row.getCell(11).getStringCellValue());
            			salary.setSubtotalOfSocialBenefitsEmployee(row.getCell(12).getStringCellValue());
            			salary.setHousingFundBasis(row.getCell(13).getStringCellValue());
            			salary.setHousingFundEmployee(row.getCell(14).getStringCellValue());
            			salary.setTaxDeduction(row.getCell(15).getStringCellValue());
            			salary.setTaxableIncome(row.getCell(16).getStringCellValue());
            			salary.setTaxRate(row.getCell(17).getStringCellValue());
            			salary.setQuickReckon(row.getCell(18).getStringCellValue());
            			salary.setIIT(row.getCell(19).getStringCellValue());
            			salary.setNetPay(row.getCell(20).getStringCellValue());
            			//subtotalOfSocialBenefitsEmployee + housingFundEmployee
            			salary.setTotalAmount("Total Amount\n(social benefit,housing fund)");    
            			
            			salary.setPensionEmployer(row.getCell(22).getStringCellValue());
            			salary.setMedicalEmployer(row.getCell(23).getStringCellValue());
            			salary.setUnemploymentEmployer(row.getCell(24).getStringCellValue());
            			salary.setMaternity(row.getCell(25).getStringCellValue());
            			salary.setWorkRelatedInjury(row.getCell(26).getStringCellValue());
            			salary.setSubtotalOfSocialBenefitsEmployer(row.getCell(27).getStringCellValue());
            			salary.setHousingFundEmployer(row.getCell(28).getStringCellValue());
            			salary.setTotalAmountPersonal(row.getCell(29).getStringCellValue());
            			salary.setTotalAmountCompany(row.getCell(30).getStringCellValue()); 
            			salaryList.add(salary);
            			continue;
            		}
            		
            		if(Cell.CELL_TYPE_NUMERIC==row.getCell(1).getCellType()){
            			Salary salary = new Salary();
            			salary.setStartDate(startDate);
            			salary.setEmail("10747222@qq.com");
            			
            			salary.setSocialBenefitLocation(row.getCell(2).getStringCellValue());
            			salary.setName(row.getCell(3).getStringCellValue());
            			salary.setPosition(row.getCell(4).getStringCellValue());
            			salary.setBasicSalary(formatCurrent.format(row.getCell(5).getNumericCellValue()));
            			salary.setBonus(formatCurrent.format(row.getCell(6).getNumericCellValue()));
            			salary.setTotalGross(formatCurrent.format(row.getCell(7).getNumericCellValue()));
            			salary.setSocialBenefitBasis(formatCurrent.format(row.getCell(8).getNumericCellValue()));
            			salary.setPensionEmployee(formatCurrent.format(row.getCell(9).getNumericCellValue()));
            			salary.setMedicalEmployee(formatCurrent.format(row.getCell(10).getNumericCellValue()));
            			salary.setUnemploymentEmployee(formatCurrent.format(row.getCell(11).getNumericCellValue()));
            			salary.setSubtotalOfSocialBenefitsEmployee(formatCurrent.format(row.getCell(12).getNumericCellValue()));
            			salary.setHousingFundBasis(formatCurrent.format(row.getCell(13).getNumericCellValue()));
            			salary.setHousingFundEmployee(formatCurrent.format(row.getCell(14).getNumericCellValue()));
            			salary.setTaxDeduction(formatCurrent.format(row.getCell(15).getNumericCellValue()));
            			salary.setTaxableIncome(formatCurrent.format(row.getCell(16).getNumericCellValue()));
            			salary.setTaxRate(formatCurrent.format(row.getCell(17).getNumericCellValue()));
            			salary.setQuickReckon(formatCurrent.format(row.getCell(18).getNumericCellValue()));
            			salary.setIIT(formatCurrent.format(row.getCell(19).getNumericCellValue()));
            			salary.setNetPay(formatCurrent.format(row.getCell(20).getNumericCellValue()));
            			
            			//subtotalOfSocialBenefitsEmployee + housingFundEmployee
            			salary.setTotalAmount(formatCurrent.format(
            					row.getCell(12).getNumericCellValue()+row.getCell(12).getNumericCellValue()));
            			
            			salary.setPensionEmployer(formatCurrent.format(row.getCell(22).getNumericCellValue()));
            			salary.setMedicalEmployer(formatCurrent.format(row.getCell(23).getNumericCellValue()));
            			salary.setUnemploymentEmployer(formatCurrent.format(row.getCell(24).getNumericCellValue()));
            			salary.setMaternity(formatCurrent.format(row.getCell(25).getNumericCellValue()));
            			salary.setWorkRelatedInjury(formatCurrent.format(row.getCell(26).getNumericCellValue()));
            			salary.setSubtotalOfSocialBenefitsEmployer(formatCurrent.format(row.getCell(27).getNumericCellValue()));
            			salary.setHousingFundEmployer(formatCurrent.format(row.getCell(28).getNumericCellValue()));
            			salary.setTotalAmountPersonal(formatCurrent.format(row.getCell(29).getNumericCellValue()));
            			salary.setTotalAmountCompany(formatCurrent.format(row.getCell(30).getNumericCellValue()));            			
            			salaryList.add(salary);
            			
                		++serialNOPos;
            			continue;
            		}
            		
            		if(Cell.CELL_TYPE_BLANK==row.getCell(1).getCellType()
            				&& Cell.CELL_TYPE_BLANK==row.getCell(3).getCellType()){
//            			String location = sheet.getRow(countRowPos - 2).getCell(2).getStringCellValue();
//            			location = location.split("\n")[1];
            			String location = String.valueOf(countRowPos);
            			keyList.add(location);
            			salarys.put(location, salaryList);
            			salaryList = new ArrayList<Salary>();
            		}
            		
            	} while(!"Total".equals(row.getCell(3).getStringCellValue()));	

            	modelMap.put("keys", keyList);
            	modelMap.put("salarys", salarys);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "error";
	        }
	    }else{
	    	return "fail";
	    }
    	
	    return "views/fragments::salarys";
	}
}
