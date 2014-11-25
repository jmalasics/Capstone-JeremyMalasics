DROP DATABASE CapstoneJeremyMalasics;
CREATE DATABASE CapstoneJeremyMalasics;
USE CapstoneJeremyMalasics;
CREATE TABLE Department (id INT PRIMARY KEY AUTO_INCREMENT,
							department VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE Employee (id INT PRIMARY KEY AUTO_INCREMENT, 
						firstName VARCHAR(25) NOT NULL, 
						lastName VARCHAR(35) NOT NULL, 
						dateHired DATE NOT NULL, 
						department VARCHAR(50) NOT NULL, 
						jobTitle VARCHAR(50) NOT NULL,
						UNIQUE (firstName, lastName),
						CONSTRAINT employee_foreign_key_1 FOREIGN KEY (department) REFERENCES Department (department)
);
CREATE TABLE RFIDCard (id INT PRIMARY KEY AUTO_INCREMENT,
						rfidCode CHAR(10) NOT NULL UNIQUE
);
CREATE TABLE EmployeeRFIDCard (id INT PRIMARY KEY AUTO_INCREMENT, 
								rfid CHAR(10) UNIQUE NOT NULL,
								empId INT UNIQUE,
								UNIQUE (rfid, empId),
								CONSTRAINT employeeRFIDCard_foreign_key_1 FOREIGN KEY (rfid) REFERENCES RFIDCard (rfidCode),
								CONSTRAINT employeeRFIDCard_foreign_key_2 FOREIGN KEY (empId) REFERENCES Employee (id)
);
CREATE TABLE Device (id INT PRIMARY KEY AUTO_INCREMENT,
						device VARCHAR(25) NOT NULL,
						totalUsage DECIMAL NOT NULL
);
CREATE TABLE DeviceUsageHistory (id INT PRIMARY KEY AUTO_INCREMENT,
									deviceId INT NOT NULL,
									timeStored DATETIME NOT NULL,
									recentUsage DECIMAL NOT NULL
);
CREATE TABLE DeviceActivationTimes (id INT PRIMARY KEY AUTO_INCREMENT, 
					deviceID INT NOT NULL,
					activationTime DATETIME NOT NULL,
					disableTime DATETIME NOT NULL,
					UNIQUE (deviceID, activationTime),
					CONSTRAINT deviceActivationTimes_foreign_1 FOREIGN KEY (deviceID) REFERENCES Device (id)
);
INSERT INTO Department (department) VALUES ("Management");
INSERT INTO Department (department) VALUES ("Computer Science");
INSERT INTO Department (department) VALUES ("Game Development");
INSERT INTO Department (department) VALUES ("Information Systems");
INSERT INTO Department (department) VALUES ("Web Development");
INSERT INTO Department (department) VALUES ("Business");
INSERT INTO Department (department) VALUES ("Student Services");
INSERT INTO RFIDCard (rfidCode) VALUES ("010077A2A7");
INSERT INTO RFIDCard (rfidCode) VALUES ("01007687A5");
INSERT INTO RFIDCard (rfidCode) VALUES ("0100763FC5");
INSERT INTO Employee (firstName, lastName, dateHired, department, jobTitle) VALUES ("Jeremy", "Malasics", CURDATE(), "Computer Science", "Student");
INSERT INTO Employee (firstName, lastName, dateHired, department, jobTitle) VALUES ("Steve", "Halladay", CURDATE(), "Computer Science", "Program Chair");
INSERT INTO Employee (firstName, lastName, dateHired, department, jobTitle) VALUES ("Jamie", "King", CURDATE(), "Game Development", "Program Chair");
INSERT INTO Employee (firstName, lastName, dateHired, department, jobTitle) VALUES ("Ben", "Fletcher", CURDATE(), "Information Systems", "Program Chair");
INSERT INTO Employee (firstName, lastName, dateHired, department, jobTitle) VALUES ("Josh", "Krebs", CURDATE(), "Computer Science", "Faculty");
INSERT INTO Employee (firstName, lastName, dateHired, department, jobTitle) VALUES ("Tim", "Clark", CURDATE(), "Web Development", "Program Chair");
INSERT INTO Employee (firstName, lastName, dateHired, department, jobTitle) VALUES ("Jonathan", "Pearl", CURDATE(), "Game Development", "Student");
INSERT INTO EmployeeRFIDCard (rfid, empId) VALUES ("010077A2A7", 1);
INSERT INTO EmployeeRFIDCard (rfid, empId) VALUES ("01007687A5", 2);
INSERT INTO EmployeeRFIDCard (rfid, empId) VALUES ("0100763FC5", 6);
INSERT INTO Device (device, totalUsage) VALUES ("ROOM202_RFID", 0);
INSERT INTO Device (device, totalUsage) VALUES ("ROOM202_PIR", 0);
INSERT INTO Device (device, totalUsage) VALUES ("ROOM202_LIGHTS", 0);
INSERT INTO DeviceActivationTimes (deviceID, activationTime, disableTime) VALUES (1, CURDATE(), "2014-11-24 15:00:00");
INSERT INTO DeviceActivationTimes (deviceID, activationTime, disableTime) VALUES (2, CURDATE(), "2014-11-24 15:00:00");
INSERT INTO DeviceActivationTimes (deviceID, activationTime, disableTime) VALUES (3, CURDATE(), "2014-11-24 15:00:00");
INSERT INTO DeviceActivationTimes (deviceID, activationTime, disableTime) VALUES (1, "2014-11-25 12:00:00", "2014-11-25 15:00:00");
INSERT INTO DeviceActivationTimes (deviceID, activationTime, disableTime) VALUES (2, "2014-11-25 12:00:00", "2014-11-25 15:00:00");
INSERT INTO DeviceActivationTimes (deviceID, activationTime, disableTime) VALUES (3, "2014-11-25 12:00:00", "2014-11-25 15:00:00");
INSERT INTO DeviceActivationTimes (deviceID, activationTime, disableTime) VALUES (1, "2014-11-26 12:00:00", "2014-11-26 15:00:00");
INSERT INTO DeviceActivationTimes (deviceID, activationTime, disableTime) VALUES (2, "2014-11-26 12:00:00", "2014-11-26 15:00:00");
INSERT INTO DeviceActivationTimes (deviceID, activationTime, disableTime) VALUES (3, "2014-11-26 12:00:00", "2014-11-26 15:00:00");
								