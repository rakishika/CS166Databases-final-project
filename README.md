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



