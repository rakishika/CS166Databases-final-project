/*
 *
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */

//Name: Ishika Rakesh SID: 862175848
//Name: Garrett Pedvin SID: 862085838

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Messenger {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Messenger
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws SQLException when failed to make a connection.
    */
   public Messenger(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end ProfNetwork

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
          List<String> record = new ArrayList<String>();
         for (int i=1; i<=numCol; ++i)
            record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       if(rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Messenger.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Messenger esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Messenger object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Messenger (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. Goto Friend List");
                System.out.println("2. Update Profile/Password");
                System.out.println("3. Write a new message");
                System.out.println("4. View Messages");
                System.out.println("5. Delete Messages");
                System.out.println("6. Send Friend Request");
                System.out.println("7. Accept/Reject Connection Request");
                System.out.println("8. Search a Friend");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                   case 1: FriendList(esql, authorisedUser); break;
                   case 2: UpdateProfile(esql, authorisedUser); break;
                   case 3: NewMessage(esql, authorisedUser); break;
                   case 4: ViewMessage(esql, authorisedUser); break;
                   case 5: DeleteMessage(esql, authorisedUser); break;
                   case 6: SendRequest(esql, authorisedUser); break;
                   case 7: AcceptRejectRequest(esql, authorisedUser); break;
                   case 8: FindFriend(esql, authorisedUser); break;
                   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user with privided login, passowrd and phoneNum
    * An empty block and contact list would be generated and associated with a user
    **/
   public static void CreateUser(Messenger esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         System.out.print("\tEnter user email: ");
         String email = in.readLine();
         System.out.print("\tEnter user Full Name: ");
         String name = in.readLine();

	 //Creating empty contact\block lists for a user
	 String query = String.format("INSERT INTO USR (userId, password, email, name) " +
             "VALUES ('%s', '%s','%s','%s')", login, password, email, name);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end

   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Messenger esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USR WHERE userId = '%s' AND password = '%s'", login, password);
         int userNum = esql.executeQuery(query);
	 if (userNum > 0)
		return login;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   // Rest of the functions definition go in here
   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String FriendList(Messenger esql, String authorisedUser){
      //System.out.println("Friends List for " + authorisedUser);
      try{
         String query = String.format("SELECT * FROM CONNECTION_USR " +
                 "WHERE userId = '%s'", authorisedUser);

         int userNum = esql.executeQueryAndPrintResult(query);
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   public static void UpdateProfile(Messenger esql, String authorisedUser){
      try{
         System.out.print("\tEnter new password: ");
         String password = in.readLine();

         //Creating empty contact\block lists for a user
         String query = String.format("UPDATE USR set password = '%s' " +
                 "WHERE userId = '%s'", password, authorisedUser);

         esql.executeUpdate(query);
         System.out.println ("Password successfully updated!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end

   public static String NewMessage(Messenger esql, String authorisedUser){
      try{
         System.out.print("\tEnter receiver's user id: ");
         String receiverId = in.readLine();
         System.out.print("\tEnter message: ");
         String message = in.readLine();

         String query = String.format("INSERT INTO MESSAGE " +
                 "(msgid, senderId, receiverId, contents, sendTime, deleteStatus, status) " +
                 "VALUES (NEXTVAL('MESSAGE_SEQ'), '%s', '%s', '%s', current_timestamp, 0, 'DELIVERED')",
                 authorisedUser, receiverId, message);

         esql.executeUpdate(query);
         System.out.println ("Message successfully sent!");
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   public static String ViewMessage(Messenger esql, String authorisedUser){
      //System.out.println("Friends List for " + authorisedUser);
      try{
         String query = String.format("SELECT * FROM MESSAGE " +
                 "WHERE receiverId = '%s' AND deleteStatus = 0 ", authorisedUser);

         int userNum = esql.executeQueryAndPrintResult(query);
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   public static String DeleteMessage(Messenger esql, String authorisedUser){
      try{
         System.out.print("\tEnter msg id: ");
         Integer msgId = Integer.parseInt(in.readLine());
         String query = String.format("UPDATE MESSAGE " +
                 "SET deleteStatus = 1 WHERE receiverId = '%s' AND msgId = %s ", authorisedUser, msgId);

         int userNum = esql.executeQuery(query);
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   public static String SendRequest(Messenger esql, String authorisedUser){
      try{
         System.out.print("\tEnter connection's user id: ");
         String receiverId = in.readLine();

         String query = String.format("INSERT INTO CONNECTION_USR " +
                         "(userId, connectionId, status) " +
                         "VALUES('%s', '%s', 'Request')",
                 authorisedUser, receiverId);

         esql.executeUpdate(query);
         System.out.println ("Request successfully sent!");
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   public static void AcceptRejectRequest(Messenger esql, String authorisedUser){
      try{
         System.out.print("\tEnter connection name to accept or reject the request: ");
         String connectionId = in.readLine();
         System.out.print("\tDo you like to accept the request (Enter 'Y' for yes or 'N' for no): ");
         String isAccept = in.readLine();
         String status = "";
         if (isAccept.equalsIgnoreCase("Y"))
            status = "Accept";
         else if (isAccept.equalsIgnoreCase("N"))
            status = "Reject";
         else {
            System.out.print("\tInvalid option. Try again\n");
            return;
         }


         //Creating empty contact\block lists for a user
         String query = String.format("UPDATE CONNECTION_USR set status = '%s' " +
                 "WHERE userId = '%s'", status, authorisedUser);

         esql.executeUpdate(query);
         //System.out.println ("Request " + status + "ed!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end

   public static String FindFriend(Messenger esql, String authorisedUser){
      try{
         System.out.print("\tEnter Name or User ID: ");
         String user = in.readLine();

         String query = "SELECT * FROM USR " +
                 "WHERE name LIKE '%" + user + "%'" +
                 "OR userId LIKE '%" + user + "%'";

         int userNum = esql.executeQueryAndPrintResult(query);
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

}//end ProfNetwork
