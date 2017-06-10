# FTTimer
###### JavaFx Project. Time Tracking Tool.
![Alt text](images/LoginPage.png?raw=true "Optional Title")
![Alt text](images/timerView.png?raw=true "Optional Title")
![Alt text](images/mainView.png?raw=true "Optional Title")
![Alt text](images/TableView.png?raw=true "Optional Title")
## Time Tracking Tool
With help this Tool You can save Time Entries into Your DB. FTTimer don't use JDBC or DataBase connection, 
DataController.class use JSON string to send Entries to Your Backend into server.
> You need Backend into server.

And to get Time Entries, Your Backend must be send JSON array with TE. JSONParser.class will parse and then show Time Entries.
If connection failed, will be created .csv file and then You can upload this file.

# Deployment
You can use InnoSetup Compiler to create full work .exe file.

# Built With Ant
> build.xml

# Author
- Vadim Makarenko

# License
This project is licensed under the MIT License - see the LICENSE.md file for details
