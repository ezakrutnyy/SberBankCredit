CREATE TABLE "TACCOUNT"
(
	"ID" BIGINT IDENTITY NOT NULL ,
	"ACCNUMBER" VARCHAR(255) UNIQUE NOT NULL,
	"BALANCE" DECIMAL NOT NULL, 
	CONSTRAINT "TACCOUNT_PK" PRIMARY KEY ("ID")
	
);



INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES ('40817810340000701301', '100000');
INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES ('40817810340000701302', '120000');
INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES ('40817810340000701303', '7500.45');
INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES ('40817810340000701304', '9200');
INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES ('40817810340000701305', '50000');
INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES ('40817810340000701306', '10000');
INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES ('40817810340000701307', '80000');
INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES ('40817810340000701308', '90000');
INSERT INTO TACCOUNT (ACCNUMBER, BALANCE) VALUES ('40817810340000701309', '50000');