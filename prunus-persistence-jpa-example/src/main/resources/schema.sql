DROP SEQUENCE IF EXISTS LAPTOP_ID_SEQUENCE;

CREATE SEQUENCE LAPTOP_ID_SEQUENCE START WITH 1 INCREMENT BY 1;

DROP TABLE IF EXISTS LAPTOP;

CREATE TABLE LAPTOP (
    ID BIGINT,
    VENDOR VARCHAR2(100) NOT NULL,
    DISPLAY_SIZE BIGINT NOT NULL,
    DELETED BOOLEAN NOT NULL,
    CREATED_BY VARCHAR2(30),
    CREATED_DATE DATETIME,
    CREATED_DEPT VARCHAR2(30),
    CREATED_REMOTE_ADDR VARCHAR2(30),
    MODIFIED_BY VARCHAR2(30),
    MODIFIED_DATE DATETIME,
    MODIFIED_DEPT VARCHAR2(30),
    MODIFIED_REMOTE_ADDR VARCHAR2(30),
    PRIMARY KEY (ID)
);