package com.yifan.ruihua.view;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.yifan.ruihua.model.Salary;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by youyifan
 * Date:14-7-31
 * Comment:
 */
public class PagePdfView extends AbstractPdfView{
	private static final int SERIAL_NO_POS = 8;
	
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document,
                                    PdfWriter writer, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
    	
    	// 使用微软雅黑字体显示中文
//    	String yaHeiFontName = 
//    			request.getSession().getServletContext().getRealPath("/resources/fonts/msyhl.ttc");
//    	yaHeiFontName += ",1";
//    	Font yaHeiFont = new Font(BaseFont.createFont(yaHeiFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));//中文简体
    	// 使用宋体显示中文
    	String songFontName = 
    			request.getSession().getServletContext().getRealPath("/resources/fonts/simsun.ttc");
    	songFontName += ",1";
    	Font songFont = new Font(BaseFont.createFont(songFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));//中文简体    	

    	Salary salary = (Salary) model.get("salary");
    	Map<String, List<Salary>> salarys = (Map<String, List<Salary>>) model.get("salarys");
    	
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

    	document.setPageSize(PageSize.LEGAL);
        document.open();
        document.add(new Paragraph((String)model.get("companyName")));
        document.add(new Paragraph((String)model.get("payrollTitle")));
        document.add(new Paragraph((String)model.get("period")));
        document.add(new Paragraph((String)model.get("currency")));
        
        document.add(new Paragraph((String)model.get("\n")));
        
        //table
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(300);
        table.setLockedWidth(true);
        table.setTotalWidth(new float[]{ 200, 100});
        table.setLockedWidth(true);
        table.setHorizontalAlignment(PdfTable.ALIGN_LEFT);
        
        table.addCell(new PdfPCell(new Phrase(salaryTitle.getSocialBenefitLocation(), songFont)));
        table.addCell(new PdfPCell(new Phrase(salary.getSocialBenefitLocation(), songFont)));
//        table.addCell(new PdfPCell(new Phrase("Start Date")));
//        table.addCell(new PdfPCell(new Phrase(salary.getStartDate())));       
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
        
//        document.add(new Paragraph("Note"));
//        document.add(new Paragraph("(1)  The celling of housing fund  basis in Shanghai has been adjusted to RMB16,353 from July 2015."));
//        document.add(new Paragraph("(4) Aaron's monthly gross salary has been adjusted to RMB 27,025.00 from 1st July."));
//        
        document.close();
    }

}
