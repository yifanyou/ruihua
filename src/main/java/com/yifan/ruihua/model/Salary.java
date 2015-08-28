package com.yifan.ruihua.model;

public class Salary {
	//Employee portion
	private String socialBenefitLocation;
	private String name;
	private String position;
	private String basicSalary;
	private String bonus;
	private String totalGross;
	private String socialBenefitBasis;
	private String pensionEmployee;
	private String medicalEmployee;
	private String unemploymentEmployee;
	private String subtotalOfSocialBenefitsEmployee;
	private String housingFundBasis;
	private String housingFundEmployee;
	private String taxDeduction;
	private String taxableIncome;
	private String taxRate;
	private String quickReckon;
	private String iIT;
	private String netPay;
	private String totalAmount; //subtotalOfSocialBenefitsEmployee + housingFundEmployee
	
	//Employer portion
	private String pensionEmployer;
	private String medicalEmployer;
	private String unemploymentEmployer;
	private String maternity;
	private String workRelatedInjury;
	private String subtotalOfSocialBenefitsEmployer;
	private String housingFundEmployer;
	private String totalAmountPersonal;
	private String totalAmountCompany;
	
	//others
	private String startDate;
	private String email;
	
	public String getSocialBenefitLocation() {
		return socialBenefitLocation;
	}
	public void setSocialBenefitLocation(String socialBenefitLocation) {
		this.socialBenefitLocation = socialBenefitLocation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getBasicSalary() {
		return basicSalary;
	}
	public void setBasicSalary(String basicSalary) {
		this.basicSalary = basicSalary;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	public String getTotalGross() {
		return totalGross;
	}
	public void setTotalGross(String totalGross) {
		this.totalGross = totalGross;
	}
	public String getSocialBenefitBasis() {
		return socialBenefitBasis;
	}
	public void setSocialBenefitBasis(String socialBenefitBasis) {
		this.socialBenefitBasis = socialBenefitBasis;
	}
	public String getPensionEmployee() {
		return pensionEmployee;
	}
	public void setPensionEmployee(String pensionEmployee) {
		this.pensionEmployee = pensionEmployee;
	}
	public String getMedicalEmployee() {
		return medicalEmployee;
	}
	public void setMedicalEmployee(String medicalEmployee) {
		this.medicalEmployee = medicalEmployee;
	}
	public String getUnemploymentEmployee() {
		return unemploymentEmployee;
	}
	public void setUnemploymentEmployee(String unemploymentEmployee) {
		this.unemploymentEmployee = unemploymentEmployee;
	}
	public String getSubtotalOfSocialBenefitsEmployee() {
		return subtotalOfSocialBenefitsEmployee;
	}
	public void setSubtotalOfSocialBenefitsEmployee(
			String subtotalOfSocialBenefitsEmployee) {
		this.subtotalOfSocialBenefitsEmployee = subtotalOfSocialBenefitsEmployee;
	}
	public String getHousingFundBasis() {
		return housingFundBasis;
	}
	public void setHousingFundBasis(String housingFundBasis) {
		this.housingFundBasis = housingFundBasis;
	}
	public String getHousingFundEmployee() {
		return housingFundEmployee;
	}
	public void setHousingFundEmployee(String housingFundEmployee) {
		this.housingFundEmployee = housingFundEmployee;
	}
	public String getTaxDeduction() {
		return taxDeduction;
	}
	public void setTaxDeduction(String taxDeduction) {
		this.taxDeduction = taxDeduction;
	}
	public String getTaxableIncome() {
		return taxableIncome;
	}
	public void setTaxableIncome(String taxableIncome) {
		this.taxableIncome = taxableIncome;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getQuickReckon() {
		return quickReckon;
	}
	public void setQuickReckon(String quickReckon) {
		this.quickReckon = quickReckon;
	}
	public String getIIT() {
		return iIT;
	}
	public void setIIT(String iIT) {
		this.iIT = iIT;
	}
	public String getNetPay() {
		return netPay;
	}
	public void setNetPay(String netPay) {
		this.netPay = netPay;
	}
	public String getPensionEmployer() {
		return pensionEmployer;
	}
	public void setPensionEmployer(String pensionEmployer) {
		this.pensionEmployer = pensionEmployer;
	}
	public String getMedicalEmployer() {
		return medicalEmployer;
	}
	public void setMedicalEmployer(String medicalEmployer) {
		this.medicalEmployer = medicalEmployer;
	}
	public String getUnemploymentEmployer() {
		return unemploymentEmployer;
	}
	public void setUnemploymentEmployer(String unemploymentEmployer) {
		this.unemploymentEmployer = unemploymentEmployer;
	}
	public String getMaternity() {
		return maternity;
	}
	public void setMaternity(String maternity) {
		this.maternity = maternity;
	}
	public String getWorkRelatedInjury() {
		return workRelatedInjury;
	}
	public void setWorkRelatedInjury(String workRelatedInjury) {
		this.workRelatedInjury = workRelatedInjury;
	}
	public String getSubtotalOfSocialBenefitsEmployer() {
		return subtotalOfSocialBenefitsEmployer;
	}
	public void setSubtotalOfSocialBenefitsEmployer(
			String subtotalOfSocialBenefitsEmployer) {
		this.subtotalOfSocialBenefitsEmployer = subtotalOfSocialBenefitsEmployer;
	}
	public String getHousingFundEmployer() {
		return housingFundEmployer;
	}
	public void setHousingFundEmployer(String housingFundEmployer) {
		this.housingFundEmployer = housingFundEmployer;
	}
	public String getTotalAmountPersonal() {
		return totalAmountPersonal;
	}
	public void setTotalAmountPersonal(String totalAmountPersonal) {
		this.totalAmountPersonal = totalAmountPersonal;
	}
	public String getTotalAmountCompany() {
		return totalAmountCompany;
	}
	public void setTotalAmountCompany(String totalAmountCompany) {
		this.totalAmountCompany = totalAmountCompany;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
