package com.yifan.ruihua.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.yifan.ruihua.util.MailAuthenticator;
import com.yifan.ruihua.view.PagePdfView;
import com.yifan.ruihua.model.Salary;

@Controller
public class SalaryController {
	private static final int SERIAL_NO_START = 7;
	private static final int SERIAL_NO_POS = 8;
	
	private static String CompanyName = "";
	private static String PayrollTitle = "";
	private static String Period = "";
	private static String Currency = "";
	private static List<String> keyList = null; 
	private static Map<String, List<Salary>> salarys = null;
	private static List<Salary> salaryListAll = null;
	private static int countPeople = 0;
	private static String fromEmail = "";
	private static String fromEmailPassword = "";
	
    @ModelAttribute("salarys")
    public Map<String, List<Salary>> poplateOrder(){
        return new HashMap();
    }
	
	@RequestMapping("/salary")
	public String show(){
		
		CompanyName = "";
		PayrollTitle = "";
		Period = "";
		Currency = "";
		keyList = null; 
		salarys = null;
		salaryListAll = null;
		countPeople = 0;
		fromEmail = "";
		fromEmailPassword = "";		
		
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
            	salaryListAll = new ArrayList<Salary>();
            	keyList = new ArrayList<String>();
            	int countRowPos = SERIAL_NO_START;
            	int serialNOPos = 1;
            	
            	
            	List<Salary> salaryList = new ArrayList<Salary>();
            	Row row = null;
            	DecimalFormat formatCurrent = new DecimalFormat("#,###.00");
            	NumberFormat formatPercent = NumberFormat.getPercentInstance();     

            	Date date=new Date();
            	SimpleDateFormat formatDate=new SimpleDateFormat("yyyy-MM-dd");
            	String startDate = formatDate.format(date);
            	do {
            		row = sheet.getRow(countRowPos++);
          		
            		if(Cell.CELL_TYPE_STRING==row.getCell(1).getCellType()
            				&&"Serial No.".equals(row.getCell(1).getStringCellValue().split("\n")[0])){
            			Salary salary = new Salary();
            			//salary.setStartDate(startDate);
            			salary.setEmail(row.getCell(0).getStringCellValue());
            			
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
            			salary.setEmail(row.getCell(0).getStringCellValue());
            			salary.setPos(String.valueOf((int)row.getCell(1).getNumericCellValue()));
            			
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
            			salary.setTaxRate(formatPercent.format(row.getCell(17).getNumericCellValue()));
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
            			salaryListAll.add(salary);
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
            	countPeople = salaryListAll.size();
            	modelMap.put("countPeople", countPeople);
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
	
	@RequestMapping(value="/salary", params={"showPdf"})
	public ModelAndView createPdf(@RequestParam("pos")String pos){
		int posInt = Integer.parseInt(pos);
		Salary salary = salaryListAll.get(posInt-1);
		
        Map model = new HashMap();
        model.put("companyName", CompanyName);
        model.put("payrollTitle", PayrollTitle);
        model.put("period", Period);
        model.put("currency", Currency);
        model.put("salary", salary);
        model.put("salarys", salarys);
    	
        PagePdfView viewPdf = new PagePdfView();

        return new ModelAndView(viewPdf, model);
	}
	
	@RequestMapping(value="/salary", params={"sendEmail"})
	@ResponseBody
	public String sendEmail(@RequestParam("pos")String pos,
			HttpServletRequest request){
		int posInt = Integer.parseInt(pos);
		Salary salary = salaryListAll.get(posInt);
		String username = fromEmail;
		String password = fromEmailPassword;
				
		String smtpHostName = "smtp." + fromEmail.substring(fromEmail.split("@")[0].length() + 1);
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		props.setProperty("mail.smtp.port","25");
		
		 // 验证
		MailAuthenticator authenticator = new MailAuthenticator(username, password);
	    // 创建session
	    Session session = Session.getInstance(props, authenticator);
	    session.setDebug(true);
	    
	    // 创建mime类型邮件
	    MimeMessage message = new MimeMessage(session);
	    // 设置发信人
	    try {
			message.setFrom(new InternetAddress(fromEmail));
		    // 设置主题	    
//		    Date date = new Date();   
//	        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy",  Locale.ENGLISH);
//		    String subject = "Payroll-" + sdf.format(date);
			String periodHere = Period.split(":")[1];
			String subject = "Payroll-" + periodHere;
		    message.setSubject(subject);
		    // 设置邮件内容
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();         

            //设置邮件的文本内容
		    String usernameE = salary.getName().split("\n")[1];
		    String usernameSubject = usernameE.substring(0, 1).toUpperCase()
		    		+usernameE.substring(1);		    
		    String text =  "Dear " + usernameSubject + ", \n\n"
		    		+ "Please refer the attachment for your payroll slip of " + periodHere + ",should you have any questions please do not hesitate to contact us. \n\n"
		    		+ "Thanks.";
            
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(text);
            multipart.addBodyPart(contentPart);
            //添加附件
            String fileName = createPdf(salary, request);
            //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            
            BodyPart messageBodyPart= new MimeBodyPart();
            //String filePath = request.getSession().getServletContext().getRealPath("/upload/"+enc.encode(salary.getName().getBytes())+".pdf");
            String filePath = request.getSession().getServletContext().getRealPath("/upload/"+fileName+".pdf");
            File file = new File(filePath);
            DataSource source = new FileDataSource(file);
            //添加附件的内容
            messageBodyPart.setDataHandler(new DataHandler(source));
            //添加附件的标题
            //这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
            messageBodyPart.setFileName(salary.getName().split("\n")[1]+".pdf");
            multipart.addBodyPart(messageBodyPart);		 
            message.setContent(multipart);
		    // 设置收件人
		    String recipient = salary.getEmail();
		    message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		    message.saveChanges();
	        // 发送邮件
            Transport transport = session.getTransport("smtp");
            //连接服务器的邮箱
            transport.connect(smtpHostName, fromEmail, fromEmailPassword);
            //把邮件发送出去
            
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return pos;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return pos;
		}

        return "success";
	}
	
	
	private String createPdf(Salary salary, HttpServletRequest request){
		String fileName = salary.getPos();
		
		//取得标题
		int nowpos = Integer.valueOf(salary.getPos()) + SERIAL_NO_POS;
		Set set = salarys.keySet();
		Iterator it = set.iterator(); 
		String rightKey = "";
		int lastKey = 999;
		while(it.hasNext()){  
			String temp = (String)it.next();
			int keypos = Integer.valueOf(temp);
			if(nowpos < lastKey
					&& keypos < nowpos){
				break;
			}else{
				lastKey = keypos;
			}
		} 
		Salary salaryTitle = salarys.get(String.valueOf(lastKey)).get(0);
		
		FileOutputStream fileStream = null;
		try {
			Document document = new Document(PageSize.LEGAL);
	    	// 使用微软雅黑字体显示中文
//	    	String yaHeiFontName = 
//	    			request.getSession().getServletContext().getRealPath("/resources/fonts/msyhl.ttc");
//	    	yaHeiFontName += ",1";
//	    	Font yaHeiFont;
//
//			yaHeiFont = new Font(BaseFont.createFont(yaHeiFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));

			//中文简体
	    	// 使用宋体显示中文
	    	String songFontName = 
	    			request.getSession().getServletContext().getRealPath("/resources/fonts/simsun.ttc");
	    	songFontName += ",1";
	    	Font songFont = new Font(BaseFont.createFont(songFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));//中文简体    	
	    	
	    	//sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
	    	//fileStream = new FileOutputStream(request.getSession().getServletContext().getRealPath("/upload/"+enc.encode(salary.getName().getBytes())+".pdf"));
	    	fileStream = new FileOutputStream(request.getSession().getServletContext().getRealPath("/upload/"+fileName+".pdf"));
	    	PdfWriter.getInstance (document, fileStream);  
	    	
	        document.open();
	        document.add(new Paragraph(CompanyName));       
	        document.add(new Paragraph(PayrollTitle));
	        document.add(new Paragraph(Period));
	        document.add(new Paragraph(Currency));
	                
	        document.add(new Paragraph("\n"));
	        
	        //table
	        PdfPTable table = new PdfPTable(2);
	        table.setTotalWidth(300);
	        table.setLockedWidth(true);
	        table.setTotalWidth(new float[]{ 200, 100});
	        table.setLockedWidth(true);
	        table.setHorizontalAlignment(PdfTable.ALIGN_LEFT);
	        
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getSocialBenefitLocation(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getSocialBenefitLocation(), songFont)));
//	        table.addCell(new PdfPCell(new Phrase("Start Date")));
//	        table.addCell(new PdfPCell(new Phrase(salary.getStartDate())));       
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getName(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getName(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getBasicSalary(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getBasicSalary(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getSocialBenefitBasis(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getSocialBenefitBasis(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getPensionEmployee(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getPensionEmployee(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getMedicalEmployee(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getMedicalEmployee(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getUnemploymentEmployee(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getUnemploymentEmployee(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getSubtotalOfSocialBenefitsEmployee(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getSubtotalOfSocialBenefitsEmployee(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getHousingFundBasis(), songFont)));        
	        table.addCell(new PdfPCell(new Phrase(salary.getHousingFundBasis(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getHousingFundEmployee(), songFont)));        
	        table.addCell(new PdfPCell(new Phrase(salary.getHousingFundEmployee(), songFont)));
	        table.addCell(new PdfPCell(new Phrase("Total Amount(socail benefit,housing fund)", songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getTotalAmount(), songFont)));        
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getTaxDeduction(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getTaxDeduction(), songFont)));        
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getTaxableIncome(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getTaxableIncome(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getTaxRate(), songFont)));        
	        table.addCell(new PdfPCell(new Phrase(salary.getTaxRate(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getQuickReckon(), songFont)));        
	        table.addCell(new PdfPCell(new Phrase(salary.getQuickReckon(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getIIT(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getIIT(), songFont)));        
	        table.addCell(new PdfPCell(new Phrase(salaryTitle.getNetPay(), songFont)));
	        table.addCell(new PdfPCell(new Phrase(salary.getNetPay(), songFont)));   
	        
	        document.add(table);
	        
//	        document.add(new Paragraph("Note"));
//	        document.add(new Paragraph("(1)  The celling of housing fund  basis in Shanghai has been adjusted to RMB16,353 from " + Period + "."));
//	        document.add(new Paragraph("(2) Aaron's monthly gross salary has been adjusted to RMB 27,025.00 from 1st July."));
	        
	        document.close();
        
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fileStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fileName;
	}
	
	@RequestMapping(value="/salary", params={"testEmail"})
	@ResponseBody
	public String testEmail(@RequestBody String formInfo){
		String[] formInfos = formInfo.split(",");
		String email = formInfos[0].substring(10, formInfos[0].length()-1);
		String password = formInfos[1].substring(12, formInfos[1].length()-2);
		
		fromEmail = email;
		fromEmailPassword = password;

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.port","25");
		String smtpHostName = "smtp." + email.substring(email.split("@")[0].length() + 1);
		props.put("mail.smtp.host", smtpHostName);	
		 // 验证
		MailAuthenticator authenticator = new MailAuthenticator(email, password);
	    // 创建session
	    Session session = Session.getInstance(props, authenticator);
	    session.setDebug(true);
	    
	    // 创建mime类型邮件
	    MimeMessage message = new MimeMessage(session);
	    // 设置发信人
	    try {
			message.setFrom(new InternetAddress("107247222@qq.com"));
		    // 设置主题	    
		    message.setSubject("Test Email");
		    // 设置邮件内容
		    message.setText("Test successfully!-From RSM China Consulting Payroll Auto Distribution System");
		    // 设置收件人
		    message.setRecipient(RecipientType.TO, new InternetAddress(email));
		    message.saveChanges();
	        // 发送邮件
            Transport transport = session.getTransport("smtp");
            //连接服务器的邮箱
            transport.connect(smtpHostName, email, password);
            //把邮件发送出去  
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
		} catch (AddressException e) {
			return "error";
		} catch (MessagingException e) {
			return "error";
		}		
	    
		return "success";
	}
	
	@RequestMapping(value="/salary", params={"batchSendEmail"})
	@ResponseBody
	public String batchSendEmail(HttpServletRequest request){
		for(int i=0; i<countPeople; i++){
			String status = sendEmail(String.valueOf(i), request);
			if(!"success".equals(status)){
				return status;
			}		
		}
		return "success";
	}
	
	
    @RequestMapping(value="/salary", params={"downloadFile"})
    public void downloadFile(HttpServletRequest request, 
    		HttpServletResponse response) throws IOException {
    	String path = request.getSession().getServletContext().getRealPath("/upload/demo.xlsx");
    	File file = new File(path);
    	
    	//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
        response.setContentType("multipart/form-data");  
        //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)  
        response.setHeader("Content-Disposition", "attachment;fileName=demo.xlsx");
        ServletOutputStream out;
        
        try {  
            FileInputStream inputStream = new FileInputStream(file);  
  
            //3.通过response获取ServletOutputStream对象(out)  
            out = response.getOutputStream();  
  
            int b = 0;  
            byte[] buffer = new byte[512];  
            b = inputStream.read(buffer); 
            while (b != -1){   
                //4.写到输出流(out)中  
                out.write(buffer,0,b);  
                
                b = inputStream.read(buffer); 
            }  
            inputStream.close();  
            out.close();  
            out.flush();  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
}
