package kot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import TableView.DBTablePrinter;
import db.DB_Connection;
import main.Main;

public class Kitchen {
	private String orderID = null;
	static Scanner scan = new Scanner(System.in);

	public void start() {
		// TODO Auto-generated method stubSystem.out.println();
		ShowOrders();
		System.out.println(
				"-----------------------------------------------------------------------------------------------------");
		System.out.println(
				"-| Hi Sir ! What you want |- \n\t1 - Take Order \n\t0 - main manu");
		System.out.println(
				"-----------------------------------------------------------------------------------------------------");
		String type = scan.next();
		
		switch (type) {
		case "1":
			takeTheOrder();
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
	private void ShowOrders() {
		Connection conn = null;
		try {

			conn = DB_Connection.getConnection();
			System.err.println("-----------------|Pending Food Orders |------------------------");
			DBTablePrinter.printPendingOrderWithID(conn, "kot.`orders`");
			System.err.println("-----------------|Cooking Food Orders |---------------------------------------");
			DBTablePrinter.printActionOrderWithID(conn, "kot.`orders`");			

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}
	private void ShowTakeOrders() {
		Connection conn = null;
		try {

			conn = DB_Connection.getConnection();
			DBTablePrinter.printActionOrderWithID(conn, "kot.`orders`");
			
         

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}
	private void takeTheOrder() {
		
		System.out.println("-| What order Do you want to take? |- \n\t Enter the order ID:- ");
		orderID = scan.next();
		Connection conn = null;
		PreparedStatement psc;
		ResultSet rsc;


		try {
			conn = DB_Connection.getConnection();

			String query = "SELECT * FROM `orders` WHERE `OrderId` = ? ";
			psc = (PreparedStatement) conn.prepareStatement(query);
			psc.setString(1, orderID);
			
			rsc = psc.executeQuery(); 
			if (!rsc.next()) {
				System.err.println(
						"----------------------------------------------\n|XXXXX| Please Select Correct Option ! |XXXXX|\n----------------------------------------------");

				System.out.println("-| Please Select Available Order Items ! |-");

				takeTheOrder();
			}else {
				try {
				Statement stmt = conn.createStatement();
				String sql = "UPDATE `orders` SET `status`='action' WHERE `OrderId` = '"+orderID+"'";
			    stmt.executeUpdate(sql);
			    System.out.println("Order token successfully ");
			    ShowTakeOrders();
			    foodIsReady();
				}catch (SQLException e) {
				      e.printStackTrace();
			    }
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	private void foodIsReady() {
		System.out.println("-| lets update the system when the order is "+ orderID + "ready ? |- \n\t 1 - yes ");
		String status = scan.next();
		switch (status) {
		case "1":
			Connection conn = null;
			try {
				conn = DB_Connection.getConnection();
				Statement stmt = conn.createStatement();

				String sql = "UPDATE `orders` SET `status`='done' WHERE `OrderId` = '"+orderID+"'";
			    stmt.executeUpdate(sql);
			    System.out.println("Order status updated  successfully ");
			    start();
				}catch (SQLException e) {
				      e.printStackTrace();
			    }
			break;

		default:
			System.err.println("----------------------------------------------");
			System.err.println("|XXXXX| Please Select Correct Option ! |XXXXX|");
			System.err.println("----------------------------------------------");
			foodIsReady();
			break;
		}
//		if (status == 1 ) {
//			System.out.println(status);
//
//			Connection conn = null;
//			try {
//				conn = DB_Connection.getConnection();
//				Statement stmt = conn.createStatement();
//
//				String sql = "UPDATE `orders` SET `status`='done' WHERE `OrderId` = '"+orderID+"'";
//			    stmt.executeUpdate(sql);
//			    System.out.println("Order status updated  successfully ");
//			    start();
//				}catch (SQLException e) {
//				      e.printStackTrace();
//			    }
//		}else {
//			System.err.println("----------------------------------------------");
//			System.err.println("|XXXXX| Please Select Correct Option ! |XXXXX|");
//			System.err.println("----------------------------------------------");
//			foodIsReady();
//		}
	}
}
