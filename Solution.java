import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Solution {

	
	public static void main(String[] args) throws IOException {
		
		Properties prop = new Properties();
		String loc = "";
		try {
		    //load a properties file from class path, inside static method
		    prop.load(Solution.class.getClassLoader().getResourceAsStream("application.properties"));

		    //get the property value and print it out
		    loc = prop.getProperty("location");
		} 
		catch (IOException ex) {
		    ex.printStackTrace();
		}
		

		File file = new File(loc);

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		Map<String, Loan> loanMap = new HashMap<String, Loan>();
		Map<String, Payment> paymentMap = new HashMap<String, Payment>();
		List<String> result = new ArrayList<String>();
		while ((st = br.readLine()) != null) {

			processInput(st, loanMap, paymentMap, result);
		}

		for (String res : result) {
			System.out.println(res);
		}
	}

	private static void processInput(String st, Map<String, Loan> loanMap, Map<String, Payment> paymentMap,
			List<String> result) {
		if (st.contains("LOAN")) {
			String[] le = st.split(" ");
			Double amt = Double.parseDouble(le[3]);
			int yr = Integer.parseInt(le[4]);
			int rate = Integer.parseInt(le[5]);
			Loan loan = new Loan(le[1], le[2], amt, yr, rate);
			
			double interest = (amt * yr * rate) / 100;
			double totalAmt = amt + interest;
			int emiAmt = (int) Math.ceil((totalAmt / (yr * 12)));
			loan.setInterestAmt(interest);
			loan.setTotalAmt(amt + interest);
			loan.setEmiAmt(emiAmt);
			loanMap.put(loan.getBank() + loan.getBorrower(), loan);
			
		} else if (st.contains("PAYMENT")) {
			String[] pe = st.split(" ");
			Payment payment = new Payment(pe[1], pe[2], Double.parseDouble(pe[3]), Integer.parseInt(pe[4]));
			
			String key = payment.getBank() + payment.getBorrower();
			if (!paymentMap.containsKey(key))
				paymentMap.put(key, payment);
			else {
				Payment oldPayment = paymentMap.get(key);
				payment.setLumSumAmt(oldPayment.getLumSumAmt() + payment.getLumSumAmt());
				paymentMap.put(key, payment);
			}
			
		} else {
			String[] be = st.split(" ");
			int emiMonth = Integer.parseInt(be[3]);
			Balance balance = new Balance(be[1], be[2], emiMonth);
			String key = balance.getBank() + balance.getBorrower();
			Loan loan = loanMap.get(key);
			Payment payment = paymentMap.get(key);
			// System.out.println("emiAmt: "+loan.getEmiAmt()+" emiMonth: "+emiMonth);
			int amount = loan.getEmiAmt() * emiMonth;
			if (null != payment) {
				if (payment.getNumOfemi() <= emiMonth) {
					amount += payment.getLumSumAmt();
				}
			}
			double amountRem = loan.getTotalAmt() - amount;
			int emiRemaining = (int) Math.ceil(amountRem / loan.getEmiAmt());
			String output = balance.getBank() + " " + balance.getBorrower() + " " + amount + " " + emiRemaining;
			result.add(output);
		}
	}

	static class Payment {
		String bank;
		String borrower;
		Double lumSumAmt;
		int numOfemi;

		public String getBank() {
			return bank;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		public String getBorrower() {
			return borrower;
		}

		public void setBorrower(String borrower) {
			this.borrower = borrower;
		}

		public Double getLumSumAmt() {
			return lumSumAmt;
		}

		public void setLumSumAmt(Double lumSumAmt) {
			this.lumSumAmt = lumSumAmt;
		}

		public int getNumOfemi() {
			return numOfemi;
		}

		public void setNumOfemi(int numOfemi) {
			this.numOfemi = numOfemi;
		}

		public Payment(String bank, String borrower, Double lumSumAmt, int numOfemi) {
			super();
			this.bank = bank;
			this.borrower = borrower;
			this.lumSumAmt = lumSumAmt;
			this.numOfemi = numOfemi;
		}

		public Payment() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public String toString() {
			return "Payment [bank=" + bank + ", borrower=" + borrower + ", lumSumAmt=" + lumSumAmt + ", numOfemi="
					+ numOfemi + "]";
		}

	}

	static class Loan {
		String bank;
		String borrower;
		Double baseAmt;
		int yr;
		int interestRate;
		double interestAmt;
		double totalAmt;
		int emiAmt;

		public String getBank() {
			return bank;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		public String getBorrower() {
			return borrower;
		}

		public void setBorrower(String borrower) {
			this.borrower = borrower;
		}

		public Double getBaseAmt() {
			return baseAmt;
		}

		public void setBaseAmt(Double baseAmt) {
			this.baseAmt = baseAmt;
		}

		public int getYr() {
			return yr;
		}

		public void setYr(int yr) {
			this.yr = yr;
		}

		public int getInterestRate() {
			return interestRate;
		}

		public void setInterestRate(int interestRate) {
			this.interestRate = interestRate;
		}

		public Loan(String bank, String borrower, Double baseAmt, int yr, int interestRate) {
			super();
			this.bank = bank;
			this.borrower = borrower;
			this.baseAmt = baseAmt;
			this.yr = yr;
			this.interestRate = interestRate;
		}

		public Loan() {
			super();
			// TODO Auto-generated constructor stub
		}

		public double getInterestAmt() {
			return interestAmt;
		}

		public void setInterestAmt(double interestAmt) {
			this.interestAmt = interestAmt;
		}

		public double getTotalAmt() {
			return totalAmt;
		}

		public void setTotalAmt(double totalAmt) {
			this.totalAmt = totalAmt;
		}

		public int getEmiAmt() {
			return emiAmt;
		}

		public void setEmiAmt(int emiAmt) {
			this.emiAmt = emiAmt;
		}

		@Override
		public String toString() {
			return "Loan [bank=" + bank + ", borrower=" + borrower + ", baseAmt=" + baseAmt + ", yr=" + yr
					+ ", interestRate=" + interestRate + ", interestAmt=" + interestAmt + ", totalAmt=" + totalAmt
					+ ", emiAmt=" + emiAmt + "]";
		}

	}

	static class Balance {
		String bank;
		String borrower;
		int numOfEmi;

		public String getBank() {
			return bank;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		public String getBorrower() {
			return borrower;
		}

		public void setBorrower(String borrower) {
			this.borrower = borrower;
		}

		public int getNumOfEmi() {
			return numOfEmi;
		}

		public void setNumOfEmi(int numOfEmi) {
			this.numOfEmi = numOfEmi;
		}

		public Balance(String bank, String borrower, int numOfEmi) {
			super();
			this.bank = bank;
			this.borrower = borrower;
			this.numOfEmi = numOfEmi;
		}

		public Balance() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public String toString() {
			return "Balance [bank=" + bank + ", borrower=" + borrower + ", numOfEmi=" + numOfEmi + "]";
		}

	}

}
