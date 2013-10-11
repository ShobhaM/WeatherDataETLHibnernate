package JingTest;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import antlr.collections.List;

import com.sjsu.cmpe226.mesonet.util.HibernateUtil;


public class testDuplicateHandler {

	public static void main(String[] args) {
		try{
			Connection connection = null;
			connection = DriverManager.getConnection(
					  "jdbc:postgresql://192.168.56.102:5432/testdb","postgres","sjsu123");
			
			Session session = HibernateUtil.getMetaSessionFactory().openSession();	        
	        session.beginTransaction();
	        Transaction txn = session.getTransaction();
	        
	        String deletesql= "select deleteduplicate(" + "\'"+ "priID" + "\',"+ "\'"+"secID" 
	        		 + "\',"+ "\'"+ "country" + "\',"+ "\'"+"state" + "\',"+ "\'"+ "station"+ "\')" ;
	        System.out.println(deletesql);
	        Query query = session.createSQLQuery(deletesql);
	        List result = (List) query.list();
	        String x = query.list().get(0).toString();
	        System.out.println("-------- end ------------");
		
		} catch (SQLException e){
			System.out.println("-------- SQLException ------------");
		
		}
	}
}
