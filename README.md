# CS166Databases-final-project
Creating a  LinkedIn-like networking organizer to manage users, messages, and their connections.

ERD Diagram:
<img width="1212" alt="Screenshot 2023-08-08 at 11 49 52 PM" src="https://github.com/rakishika/CS166Databases-final-project/assets/33856993/609411a9-0275-426d-94f1-b301b4fe875a">

We are pleased to present our professional network application. The application is written in Java and implements JDBC to communicate with an SQL server in order to handle large amounts of individual user data. Using the command line interface, users are able to log-in to or register new accounts, search for and make connections with other users, and send messages to other users.

New users can easily create a new account by providing information about their login ID, email address, full name and by choosing a password. Existing users can log in using their login ID and password.

Users are able to change their password from the main menu of the system.

Users are able to compose a message and send it to any user on the platform.

Users are able to view messages they have received from other users on the platform. Users are also able to delete messages they have received from their inbox.

Users are able to request to make a connection with another user given their login ID. The user receiving the connection request can later accept or reject the connection. We initially built this feature to ensure that users cannot send connection requests to other users that are more than three connections removed from one another, however this feature was not implemented.

Users are able to retrieve their list of friends/connections, including relevant information such as full name and login ID. 

Users are able to search for the names of other users on the platform, receiving information based on each person searched such as their login ID and name.


Implementation: 

Data : The data that was provided to us was in the form of a .xlsx file so we had to convert those tables into a .csv and .tsv file. We had to clean the data before we began because we encountered multiple errors while trying to run the tables. We found extra commas at the end of each line of the file in the connection’s table, so we used text edit to replace these 2 commas found at the end of each line by replacing them with a blank space. For the message table, we converted that information into a .tsv file instead of a .csv file because of the special characters along with commas that are in this table so we decided using a .tsv file would be a better fit. For the other tables we stuck with converting them to a .csv file. Overall we were able to convert the excel sheet into files necessary for our project by using the Numbers app and creating a file for each  of the 5 tables. After cleaning the data we moved on to the sql portion.

Create Tables, Bulkload Data Scripts:  In create_tables.sql, in the CREATE TABLE USR, we increased the size for names and passwords to 30, as they were 10 earlier. Then we added a foreign key to each table so that it could reference the user table. At the end, we added a message sequence to generate a number for the next message. We worked on the load_data.sql so that the files could be copied in.

After these were completed, we started up the database and ran source ./create_db.sh to generate the tables.

Java:  We combined ProfNetwork.java with Messenger.java and completed several functions to implement the needed features in the project.



