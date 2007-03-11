CREATE TABLE CUSTOMER (ID INTEGER NOT NULL, NAME VARCHAR(255), ADDRESS VARCHAR(255), PHONE VARCHAR(255), AGE INTEGER, BIRTHDAY DATE, SEX INTEGER, DTYPE VARCHAR(31), DANGEON VARCHAR(255), DANGEON_LEVEL INTEGER, VERSION INTEGER, PRIMARY KEY (ID));
CREATE TABLE PRODUCT (ID INTEGER NOT NULL, NAME VARCHAR(255), CUSTOMER_ID INTEGER, VERSION INTEGER, PRIMARY KEY (ID));
ALTER TABLE PRODUCT ADD CONSTRAINT PRODUCTCUSTOMER_ID FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER (ID);
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT DECIMAL, PRIMARY KEY (SEQ_NAME));

INSERT INTO CUSTOMER VALUES(1, 'Link', 'Hyrule', '000-0000-0000', 15, '1998-1-1', 0, 'C', NULL, NULL, 0);
INSERT INTO CUSTOMER VALUES(2, 'Zelda', 'Hyrule', '000-0000-0000', 15, '1998-1-1', 1, 'C', NULL, NULL, 0);
INSERT INTO CUSTOMER VALUES(3, 'Ganon', 'Darkness', '000-0000-0000', 15, '1998-1-1', 0, 'E', 'Pyramid of Power', 10, 0);
INSERT INTO PRODUCT VALUES(1, 'MASTER SORD', 1, 0);
INSERT INTO PRODUCT VALUES(2, 'OKARINA', 2, 0);
INSERT INTO PRODUCT VALUES(3, 'TRIDENT', 3, 0);
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 3);