package main;

import java.util.Scanner;
import kot.*;

public class Main {
	static Scanner scan = new Scanner(System.in);

public static void main(String[] args) {
	Welcome();
	getUser();
}
public static void Welcome() {

	System.out.println(
			"-----------------------------------------------------------------------------------------------------");
	System.out.println(
			"-----------------------------------|                    |--------------------------------------------");
	System.out.println(
			"-----------------------------------| WelCome to MAC Res |--------------------------------------------");
	System.out.println(
			"-----------------------------------|                    |--------------------------------------------");
	System.out.println(
			"-----------------------------------------------------------------------------------------------------");

}
public static void getUser() {
	
	
	System.out.println();
	System.out.println(
			"-----------------------------------------------------------------------------------------------------");
	System.out.println("-| Hi Sir..!  User Type ? |-");
	System.out.println("\t1 - Waiter \n\t2 - KitchenStaff \n\t3 - ManagementStaff \n\t0 - Close");
	System.out.println(
			"-----------------------------------------------------------------------------------------------------");
	String type = scan.next();
	
	switch (type) {
	case "1":
		Waiter waiter = new Waiter();
		waiter.start();
		break;
	case "2":
		Kitchen k = new Kitchen();
		k.start();
		break;
	case "3":
		Manager m = new Manager();
		m.start();
		break;
	case "0":
		System.exit(0);
		break;
	default:
		System.err.println("----------------------------------------------");
		System.err.println("|XXXXX| Please Select Correct Option ! |XXXXX|");
		System.err.println("----------------------------------------------");

		getUser();
		break;
	}
}

}
