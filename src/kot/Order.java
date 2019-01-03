package kot;

public class Order {
	String id;
	String OrderID;
	String FoodID;
	String FoodName;
	int Quantity;
	double Price;
	String Type;
	String TableName;
	String Date;
	public Order(String id, String orderID, String foodID, String foodName, int quantity, double price, String type,
			String tableName, String date) {
		
		this.id = id;
		this.OrderID = orderID;
		this.FoodID = foodID;
		this.FoodName = foodName;
		this.Quantity = quantity;
		this.Price = price;
		this.Type = type;
		this.TableName = tableName;
		this.Date = date;
	}
	
	
}
