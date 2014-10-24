DROP DATABASE CapstoneJeremyMalasics;
CREATE DATABASE CapstoneJeremyMalasics;
USE CapstoneJeremyMalasics;
CREATE TABLE Employee (id INT PRIMARY KEY AUTO_INCREMENT, 
						firstName VARCHAR(25) NOT NULL, 
						lastName VARCHAR(35) NOT NULL, 
						dateHired DATE NOT NULL, 
						department VARCHAR(50) NOT NULL, 
						jobTitle VARCHAR(50) NOT NULL
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
								