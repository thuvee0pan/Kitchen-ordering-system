package kot;

import java.util.Scanner;

public class Table {

	private String tableId = null;

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	
	public void tableList() {
		Scanner scan = new Scanner(System.in);

		System.out.println(
				"-| Please Select The Table For Order ? \n\t1 - Table:01 \t2 - Table:02 \n\t3 - Table:03 \t4 - Table:04 \n\t5 - Table:05 \n\t\t\t0 - Close");
		
		String tid = scan.nextLine();
		switch (tid) {
		case "1":
			setTableId("Table:01");
			break;

		case "2":
			setTableId("Table:02");
			break;
		case "3":
			setTableId("Table:03");
			break;
		case "4":
			setTableId("Table:04");
			break;
		case "5":
			setTableId("Table:05");
			break;
		case "0":
			System.exit(0);
			break;
		default:
			System.err.println("----------------------------------------------");
			System.err.println("|XXXXX| Please Select Correct Option ! |XXXXX|");
			System.err.println("----------------------------------------------");
			
			tableList();
			break;
		}
	}
	
}
