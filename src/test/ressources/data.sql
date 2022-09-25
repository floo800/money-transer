CREATE TABLE e_account (
	id INT auto_increment NOT NULL,
	currency varchar(25) NOT NULL,
	balance decimal(10,2) NOT NULL,
	CONSTRAINT e_account_pk PRIMARY KEY (id)
);
