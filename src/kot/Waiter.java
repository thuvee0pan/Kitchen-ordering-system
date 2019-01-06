package kot;

import java.sql.*;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
//import com.mysql.jdbc.PreparedStatement;

import javax.swing.text.TableView;

import TableView.DBTablePrinter;
import db.DB_Connection;
import main.Main;

public class Waiter {
	DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	LocalDateTime today = LocalDateTime.now(); 
	private String tableName = null;
	static Scanner scan = new Scanner(System.in);
	private String Oid = null;
	Random rand = new Random(); 
    ArrayList<Order> Forder = new ArrayList<>();

	public String getOid() {
		return Oid;
	}
	public void setOid(String oid) {
		Oid = oid;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	

	public void start() {

		System.out.println();
		System.out.println(
				"-----------------------------------------------------------------------------------------------------");
		System.out.println("-| Hi Sir..! Waiter Dashboard |-");
		System.out.println("\t1 - get New Order \n\t2 - View Order Status \n\t3 - Edit  or Deleten Order  \n\t4 - Make Invoice \n\t5 - Print Bill \n\t0 main Manu");
		System.out.println(
				"-----------------------------------------------------------------------------------------------------");
		String type = scan.next();
		
		switch (type) {
		case "1":
			listfoodItems();
			setOid(getTableName()+ rand.nextInt(100));
			getOrder();

			break;
		case "2":
			orderStatus();
			start();
			break;
		case "3":
			UpdateOrder();
			start();
			break;
		case "4":
			createInvoice();
			break;
		case "5":
			printBill();
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
	private void orderStatus() {
		Connection conn = null;

		try {

			conn = DB_Connection.getConnection();
			System.err.println("-----------------|Pending Food Orders |------------------------");
			DBTablePrinter.printPendingOrderWithID(conn, "kot.`orders`");
			System.err.println("-----------------|Cooking Food Orders |---------------------------------------");
			DBTablePrinter.printActionOrderWithID(conn, "kot.`orders`");
			System.err.println("-----------------|Cooked Food Orders  |-------------------------------");
			DBTablePrinter.printDoneOrderWithID(conn, "kot.`orders`");     

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
	private void UpdateOrder() {
		System.out.println("-| What you want to do?  |- \n\t1 - Edit Order \n\t2 - Delete Order \n\t0 - Back to Manu");
		String inp = scan.next();
		
		switch (inp) {
		case "1":
			editOrder();
			break;
		case "2":
			deleteOrder();	
			break;
		case "3":
			start();
			break;

		default:
			System.err.println("----------------------------------------------");
			System.err.println("|XXXXX| Please Select Correct Option ! |XXXXX|");
			System.err.println("----------------------------------------------");
			UpdateOrder();
			break;
		}

	}
	private void deleteOrder() {
		
		System.out.println("-| Order ID : | "+getOid() );
		System.out.println("-| Table Name: | "+getTableName());
		ResultSet rsc;
		PreparedStatement psc;
		Connection conn = null;
		
		try {
			
			conn =DB_Connection.getConnection();
			System.err.println("-----------------|Pending Food Orders |------------------------");
			DBTablePrinter.printPendingOrderWithID(conn, "kot.`orders`");
			String query = "Select * FROM orders where id = ?";
			System.out.println("-| What order Do you want to cancel? |- \n\t ID :-   \n\t0 - Back to Manu");

			String ID = scan.next();
			if (ID == "0") {
				start();
			}else {
			psc = (PreparedStatement) conn.prepareStatement(query);
			psc.setString(1, ID);
			rsc = psc.executeQuery(); 
			if (!rsc.next()) {

				System.err.println(
						"----------------------------------------------\n|XXXXX| Please Select Correct Option ! |XXXXX|\n----------------------------------------------");

				System.out.println("-| Please Select Available Order Items ! |-");

				deleteOrder();
			}else {
				System.out.print("-|Are you sure do you want to cancel this order? |- \n\t1 - yes \n\t2 - no \n\t");
				int option = scan.nextInt();
				 System.out.println(option == 1 );
				if (option == 1) {
					
					 String sql = "DELETE FROM `orders` WHERE `id` = "+ID;
					 try {
						 Statement stmt = conn.createStatement();
						 stmt.executeUpdate(sql);
					     System.out.println("Order deleted successfully");
					     
					     
//					     Update the food quantity
						String fId = rsc.getString("foodid");
						int quantity = rsc.getInt("quantity");
						String sqlF = "SELECT stock FROM foods WHERE id = ?";
						
						PreparedStatement getfoodq;
						PreparedStatement updateFoodq;
						ResultSet getfoodquantity;
						
						getfoodq = (PreparedStatement) conn.prepareStatement(sqlF);
						getfoodq.setString(1, fId);
						getfoodquantity = getfoodq.executeQuery();
						
						while (getfoodquantity.next()) {
						int stock = getfoodquantity.getInt("stock");
						
						String ustock = "Update foods set stock = ? where id = ? ";
						updateFoodq = (PreparedStatement) conn.prepareStatement(ustock);
						updateFoodq.setInt(1, stock + quantity);
						updateFoodq.setString(2, fId);
						updateFoodq.executeUpdate();
						}
						conn.close();
						

					} catch (Exception e) {
						 e.printStackTrace();
					      System.out.println("Order deleted Failed");

					}
				}else {
					start();
				}
			}

			}

		}catch (Exception e) {
			 e.printStackTrace();
		}
		
		
		
	}

	private void getOrder() {
		order();
//		System.out.println("-| Order ID : | "+getOid() );
//		System.out.println("-| Table Name: | "+getTableName());

		System.out.println("-| Are You Want to Buy Another Food Item ? |- \n\t1 - yes \n\t2 - No ");
		String s = scan.next();
		switch (s) {
		case "1":
			order();
			break;
		case "2":
			ShowOrders();
			start();
			break;
		default:
			System.err.println("----------------------------------------------");
			System.err.println("|XXXXX| Please Select Correct Option ! |XXXXX|");
			System.err.println("----------------------------------------------");
			getOrder();
			break;
		}
	}
	private void order() {

//		System.out.println("-| Order ID : | "+getOid() );
//		System.out.println("-| Table Name: | "+getTableName());
		System.out.println();
		System.out.print("-| Food ID :- ");
		String id = scan.next();
		
		ResultSet rsc;
		PreparedStatement psc;
		Connection connc = null;

		try {
			connc = DB_Connection.getConnection();

			String query = "Select * from foods where id = ?";

			psc = (PreparedStatement) connc.prepareStatement(query);

			psc.setString(1, id);

			rsc = psc.executeQuery(); 
			if (!rsc.next()) {

				System.err.println(
						"----------------------------------------------\n|XXXXX| Please Select Correct Option ! |XXXXX|\n----------------------------------------------");

				System.out.println("-| Please Select Available Food Items ! |-");

				order();

			} else {

				System.out.print("-| How Many You Want ");
				int oqty = scan.nextInt();

				ResultSet rs;

				PreparedStatement ps;
				PreparedStatement pso;
				Connection conn = null;

				try {
					conn = DB_Connection.getConnection();
					ps = (PreparedStatement) conn.prepareStatement(query);
					ps.setString(1, id);
					rs = ps.executeQuery();

					while (rs.next()) {
						String OrderFoodId = rs.getString("id");
						String OrderFoodName = rs.getString("name");
						int OrderStock = rs.getInt("stock");
						Double OrderPrice = rs.getDouble("price");
						String Catergories = rs.getString("type");

						String Order = "INSERT INTO `orders`(`OrderId`, `foodId`, `foodName`, `quantity`, `price`, `type`, `tableName`, `status`, `Odate`) VALUES (?,?,?,?,?,?,?,?,?)";

						pso = (PreparedStatement) conn.prepareStatement(Order);
						pso.setString(1, getOid());
						pso.setString(2, OrderFoodId);
						pso.setString(3, OrderFoodName);
						pso.setInt(4, oqty);
						pso.setDouble(5, (oqty * OrderPrice));
						pso.setString(6, Catergories);
						pso.setString(7, getTableName());
						pso.setString(8, "pending");
						pso.setString(9, date.format(today));
						pso.executeUpdate();

						Connection connset = DB_Connection.getConnection();

						String ustock = "Update foods set stock = ? where id = ? ";
						PreparedStatement psupdate = (PreparedStatement) connset.prepareStatement(ustock);
						psupdate.setInt(1, OrderStock - oqty);
						psupdate.setString(2, id);
						psupdate.executeUpdate();
						connset.close();
					}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {

					try {
						conn.close();
					} catch (SQLException e) {
	
						e.printStackTrace();
					}

				}

			} 
			}	catch (SQLException e) {
				e.printStackTrace();
			}

}
	private void ShowOrders() {
		System.out.println("-| Order ID : | "+getOid() );
		System.out.println("-| Table Name: | "+getTableName());
		Connection conn = null;

		try {

			conn = DB_Connection.getConnection();
			DBTablePrinter.printOrderWithID(conn, "kot.`orders`" ,getOid());
			
         

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
	private void listfoodItems() {


		Connection conn = null;
		
		try {
			Table t = new Table();
			
			t.tableList();
			
			setTableName(t.getTableId());
			
			System.out.println("-| Hi Sir Select Foods From this List ! |- \n");
			conn = DB_Connection.getConnection();
			DBTablePrinter.printTable(conn, "foods");

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
    private void editOrder() {

    	ResultSet order;
		PreparedStatement getOrder;
		ResultSet Food;
		PreparedStatement getFood;
		PreparedStatement updateFood;
		PreparedStatement updateOrder;
		Connection conn = null;
		
		try {
			
			conn =DB_Connection.getConnection();
			System.err.println("-----------------|Pending Food Orders |------------------------");
			DBTablePrinter.printPendingOrderWithID(conn, "kot.`orders`");
			String query = "Select * FROM orders where id = ?";
			System.out.println("-| What order Do you want to edit? |- \n\t ID :-   \n\t0 - Back to Manu");
			String ID = scan.next();

			if (ID == "0") {
				start();
			}else {
				getOrder = (PreparedStatement) conn.prepareStatement(query);
				getOrder.setString(1, ID);
				order = getOrder.executeQuery(); 
			if (!order.next()) {

				System.err.println(
						"----------------------------------------------\n|XXXXX| Please Select Correct Option ! |XXXXX|\n----------------------------------------------");

				System.out.println("-| Please Select Available Order Items ! |-");

				editOrder();
			}else {
				String fid = order.getString("foodid");
				String foodName = order.getString("foodName");
				int foodQua = order.getInt("quantity");
				System.out.print("-| How Many "+foodName+" You Want ?");
				int newoqty = scan.nextInt();
				
				String sqlF = "SELECT stock FROM foods WHERE id = ?";
				
				getFood = (PreparedStatement) conn.prepareStatement(sqlF);
				getFood.setString(1, fid);
				Food = getFood.executeQuery();
				
				while (Food.next()) {
					int stock = Food.getInt("stock");
					
					String ustock = "Update foods set stock = ? where id = ? ";
					updateFood = (PreparedStatement) conn.prepareStatement(ustock);
					updateFood.setInt(1, (stock + foodQua)-newoqty);
					updateFood.setString(2, fid);
					updateFood.executeUpdate();
					
					String orderUp = "Update orders set quantity = ? where id = ? ";
					updateOrder = (PreparedStatement) conn.prepareStatement(orderUp);
					updateOrder.setInt(1, newoqty);
					updateOrder.setString(2, ID);
					updateOrder.executeUpdate();
					}
			     System.out.println("Order deleted successfully");
					conn.close();

				
			}
			}
		}catch (Exception e) {
			 e.printStackTrace();
		}
   }
    private void createInvoice() {
    	Connection conn = null;
    	PreparedStatement getOrder;
    	PreparedStatement createInvoice;
    	ResultSet order;
    	double TotalAmount;
    	double ServiceTax = 80;
    	
    	
    	try {
			conn = DB_Connection.getConnection();
			DBTablePrinter.printDoneOrderWithID(conn, "kot.`orders`");     

		} catch (Exception e) {
			// TODO: handle exception
		}
    	System.out.println("Enter The orderID for the invoice ?");
    	String orderID = scan.next();
    	
    	String orderSQL = "SELECT * FROM `orders` WHERE `OrderId` = ?";
    	String invoiceSql = "INSERT INTO `invoice`(`tableName`, `orderId`, `totalAmount`, `serviceTax`,`date`) VALUES (?,?,?,?,?)";
    	try {
			getOrder = (PreparedStatement) conn.prepareStatement(orderSQL);
			getOrder.setString(1, orderID);
			order = getOrder.executeQuery(); 
			if(!order.next()) {
				System.err.println(
						"----------------------------------------------\n|XXXXX| Please Select Correct Option ! |XXXXX|\n----------------------------------------------");

				System.out.println("-| Please Select Available OrderID Items ! |-");

				createInvoice();
			}else {
				TotalAmount= addOrder(order);
				
				createInvoice = (PreparedStatement) conn.prepareStatement(invoiceSql);
				createInvoice.setString(1, getTableName());
				createInvoice.setString(2, orderID);
				createInvoice.setDouble(3, TotalAmount);
				createInvoice.setDouble(4, ServiceTax);
				createInvoice.setString(5, date.format(today));
				createInvoice.executeUpdate(); 
				
				setTableName(null);
				System.out.println("Invoice Created successfully ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	start();
    	
    	
    }
    private double addOrder(ResultSet order) {
    	double totalAmount = 0;
    	Forder.clear();
    	try {
			while (order.next()) {
				String id = order.getString("id");
			
				String OrderID = order.getString("orderId");
				String FoodID= order.getString("foodId");
				String FoodName= order.getString("foodName");
				int Quantity= order.getInt("quantity");
				double Price= order.getDouble("price");
				String Type= order.getString("type");
				setTableName(order.getString("tableName"));
				String TableName= order.getString("tableName");
				String Date= order.getString("Odate");
				Order o =new Order(id, OrderID, FoodID, FoodName, Quantity, Price, Type, TableName, Date);
				Forder.add(o);
				
				totalAmount +=Price;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return totalAmount;
	}
    private void printBill() {
    	Connection conn = null;
    	String orderID = null;
    	String tableName = null;
    	String invoiceID = null;
    	String discount= null;
    	String totalAmount= null;
    	String serviceTax= null;
    	String billAmount= null;
    	String date= null;
    	try {
			conn = DB_Connection.getConnection();
			DBTablePrinter.printconfirmedBill(conn, "kot.`invoice`");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	System.out.println("Enter The invoice ID: ");
    	String ID = scan.next();
    	PreparedStatement getinvoice;
    	ResultSet invoice;
    	String InvoiceSQL="SELECT * FROM `invoice` WHERE `id` = ?";
    	
    	try {
    		getinvoice = (PreparedStatement) conn.prepareStatement(InvoiceSQL);
    		getinvoice.setString(1, ID);
    		invoice = getinvoice.executeQuery();
    		
    		if (!invoice.next()) {
    			System.err.println(
						"----------------------------------------------\n|XXXXX| Please Select Correct Option ! |XXXXX|\n----------------------------------------------");

				System.out.println("-| Please Select Available Invoice ID Items ! |-");
				printBill();
    		}else {
//    			while (invoice.next()) {
					orderID = invoice.getString("orderID");
					tableName = invoice.getString("tableName");
					invoiceID = invoice.getString("id");
					discount =invoice.getString("discount");
					totalAmount =invoice.getString("totalAmount");
					serviceTax =invoice.getString("serviceTax");
					billAmount = invoice.getString("billAmount");
					date =invoice.getString("date");
//    		}
    			System.out.println();
    			System.out.println("Invoice No : "+invoiceID);
    			System.out.println("Table No : "+tableName);
    			System.out.println("Date : " +date);
    			DBTablePrinter.printbillFoods(conn, orderID);
    			System.out.println("Total Price : " +totalAmount);
    			System.out.println("Service Tax : " +serviceTax);
    			System.out.println("Discount : " +discount +"%");
    			System.out.println("Bill Amount: " +billAmount);




			}
    		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
	}
}


