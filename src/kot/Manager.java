package kot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import TableView.DBTablePrinter;
import db.DB_Connection;
import main.Main;

public class Manager {
	static Scanner scan = new Scanner(System.in);
	private double discount;
	private double billAmount;

	public double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount() {
		System.out.println("Enter The Discount ? 0-100 %");
		double dis = scan.nextDouble();
		if (dis>100 ||dis< 0 ) {
			System.out.println("-| Discount Can be 0 to 100 percentage  |-");
			setDiscount();
		}else {
			this.discount = dis;
		}
	}
	public void start() {
		System.out.println();
		System.out.println(
				"-----------------------------------------------------------------------------------------------------");
		System.out.println(
				"-| Hi Sir ! What you want |- \n\t1 - Invoice Detials \n\t2 - Meanage Customer Bill Statement \n\t0 - main manu");
		System.out.println(
				"-----------------------------------------------------------------------------------------------------");
		String type = scan.next();
		
		switch (type) {
		case "1":
			Invoices();
			break;
		case "2":
		PendingInvoices();
		confirmTheBill() ;
			break;
		case "0":
			Main.getUser();
			break;
		default:
			System.err.println("----------------------------------------------");
			System.err.println("|XXXXX| Please Select Correct Option ! |XXXXX|");
			System.err.println("----------------------------------------------");

			start();
			break;
		}

	}
	 private void PendingInvoices() {
	    	Connection conn = null;
	    	try {
				conn = DB_Connection.getConnection();
				DBTablePrinter.printPendingOrderWithID(conn, "kot.`invoice`");
				
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	
		}
	 private void Invoices() {
	    	Connection conn = null;
	    	try {
				conn = DB_Connection.getConnection();
				DBTablePrinter.printTable(conn, "kot.`invoice`");
				
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	start();
	    	
		}
	 private void confirmTheBill() {

		 System.out.println("Enter The Invoice ID :");
		 String InVID = scan.next();
		 
		 Connection conn = null;
		 PreparedStatement getInvoice;
		 PreparedStatement UpdateInvoice;
		 ResultSet invoice;
		 
		 try {
			conn = DB_Connection.getConnection();
			String getSQL = "SELECT * FROM `invoice` WHERE `id` = ?";
			getInvoice = (PreparedStatement) conn.prepareStatement(getSQL);
			getInvoice.setString(1, InVID);
			invoice = getInvoice.executeQuery();
			if (!invoice.next()) {
				System.err.println(
						"----------------------------------------------\n|XXXXX| Please Select Correct Option ! |XXXXX|\n----------------------------------------------");

				System.out.println("-| Please Select Available invoice ID ! |-");
			}else {
				String upInvoice = "UPDATE `invoice` SET `discount`=? ,`billAmount`=?,`status`=? WHERE `id` = ?";
				setDiscount();
				
				double amount  = invoice.getDouble("totalAmount");
				double tax = invoice.getDouble("serviceTax");
				double billAmount;
				if (getDiscount()>0) {
					billAmount = (amount +tax) - (amount * getDiscount()/100) ; 
				}else {
					billAmount = amount + tax; 
				}
				setBillAmount(billAmount);
				System.out.println( getDiscount());
				UpdateInvoice = (PreparedStatement) conn.prepareStatement(upInvoice);
				UpdateInvoice.setDouble(1, getDiscount());
				UpdateInvoice.setDouble(2, billAmount);
				UpdateInvoice.setString(3, "confirmed");
				UpdateInvoice.setString(4,InVID);
				UpdateInvoice.executeUpdate();
				
				System.out.println("Bill Confirmed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 start();
	}
}
