SET SERVEROUTPUT ON;

--------------------------------------------------------
--  DROP TABLES
--------------------------------------------------------
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE PICTURE_TO_CATEGORY';
	EXECUTE IMMEDIATE 'DROP TABLE PICTURE_TO_KEYWORD';
	EXECUTE IMMEDIATE 'DROP TABLE KEYWORDS';
	EXECUTE IMMEDIATE 'DROP TABLE CATEGORIES';
	EXECUTE IMMEDIATE 'DROP TABLE COMMENTS';
	EXECUTE IMMEDIATE 'DROP TABLE RATINGS';
	EXECUTE IMMEDIATE 'DROP TABLE PICTURES';
	EXECUTE IMMEDIATE 'DROP TABLE ALBUMS';
	EXECUTE IMMEDIATE 'DROP TABLE USERS';
	EXECUTE IMMEDIATE 'DROP TABLE CITIES';
	EXECUTE IMMEDIATE 'DROP TABLE REGIONS';
	EXECUTE IMMEDIATE 'DROP TABLE COUNTRIES';
	EXECUTE IMMEDIATE 'DROP TABLE APP_LOG';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

--------------------------------------------------------
--  DROP SEQUENCES
--------------------------------------------------------
BEGIN
     EXECUTE IMMEDIATE 'DROP SEQUENCE  USERS_INC';
	 EXECUTE IMMEDIATE 'DROP SEQUENCE  PICTURES_INC';
	 EXECUTE IMMEDIATE 'DROP SEQUENCE  ALBUMS_INC';
	 EXECUTE IMMEDIATE 'DROP SEQUENCE  COMMENTS_INC';
	 EXECUTE IMMEDIATE 'DROP SEQUENCE  CITIES_INC';  
	 EXECUTE IMMEDIATE 'DROP SEQUENCE  REGIONS_INC';
	 EXECUTE IMMEDIATE 'DROP SEQUENCE  COUNTRIES_INC';
	 EXECUTE IMMEDIATE 'DROP SEQUENCE  CATEGORIES_INC';
	 EXECUTE IMMEDIATE 'DROP SEQUENCE  KEYWORDS_INC';
	 EXECUTE IMMEDIATE 'DROP SEQUENCE  APP_LOG_INC';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
      END IF;
END;
/

--------------------------------------------------------
--  DDL for Sequence USERS_INC
--------------------------------------------------------
   CREATE SEQUENCE  "USERS_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
   
--------------------------------------------------------
--  DDL for Sequence PICTURES_INC
--------------------------------------------------------
   CREATE SEQUENCE  "PICTURES_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
   
--------------------------------------------------------
--  DDL for Sequence ALBUMS_INC
--------------------------------------------------------
   CREATE SEQUENCE  "ALBUMS_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--------------------------------------------------------
--  DDL for Sequence COMMENTS_INC
--------------------------------------------------------
   CREATE SEQUENCE  "COMMENTS_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
   
--------------------------------------------------------
--  DDL for Sequence CITIES_INC
--------------------------------------------------------
   CREATE SEQUENCE  "CITIES_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;  

--------------------------------------------------------
--  DDL for Sequence REGIONS_INC
--------------------------------------------------------
   CREATE SEQUENCE  "REGIONS_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--------------------------------------------------------
--  DDL for Sequence COUNTRIES_INC
--------------------------------------------------------
   CREATE SEQUENCE  "COUNTRIES_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--------------------------------------------------------
--  DDL for Sequence CATEGORIES_INC
--------------------------------------------------------
   CREATE SEQUENCE  "CATEGORIES_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--------------------------------------------------------
--  DDL for Sequence KEYWORDS_INC
--------------------------------------------------------
   CREATE SEQUENCE  "KEYWORDS_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
   
--------------------------------------------------------
--  DDL for Sequence APP_LOG_INC
--------------------------------------------------------
   CREATE SEQUENCE  "APP_LOG_INC"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
      
   
--------------------------------------------------------
--  DDL for Table USERS
--------------------------------------------------------
     
  CREATE TABLE "USERS" 
   (	
    "USER_ID" NUMBER(4,0), 
	"FIRST_NAME" VARCHAR2(20 BYTE), 
	"LAST_NAME" VARCHAR2(20 BYTE), 
	"PASSWORD" VARCHAR2(50 BYTE), 
	"EMAIL" VARCHAR2(30 BYTE), 
	"REGISTERED" DATE, 
	"PICTURE_SUM" NUMBER DEFAULT 0, 
	"PROFILE_PICTURE" ORDIMAGE,
	"PROFILE_PICTURE_THUMB" ORDIMAGE,
	"USERNAME" VARCHAR2(20 BYTE),
	"CITY_ID" NUMBER
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
  
--------------------------------------------------------
--  DDL for Table PICTURES
--------------------------------------------------------
  
  CREATE TABLE "PICTURES" 
   (	
    "PICTURE_ID" NUMBER, 
	"PICTURE_NAME" VARCHAR2(20 BYTE), 
	"UPLOAD_TIME" DATE, 
	"PICTURE" ORDIMAGE,
	"PICTURE_THUMBNAIL" ORDIMAGE,
	"CITY_ID" NUMBER, 
	"ALBUM_ID" NUMBER 
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);

--------------------------------------------------------
--  DDL for Table ALBUMS
--------------------------------------------------------  
  
  CREATE TABLE "ALBUMS" 
   (	
    "ALBUM_ID" NUMBER, 
	"USER_ID" NUMBER, 
	"NAME" VARCHAR2(20 BYTE), 
	"IS_PUBLIC" NUMBER
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);

--------------------------------------------------------
--  DDL for Table COMMENTS
--------------------------------------------------------

  CREATE TABLE "COMMENTS" 
   (	
    "COMMENT_ID" NUMBER, 
	"COMMENT_TEXT" VARCHAR2(200 BYTE), 
	"COMMENT_TIME" DATE, 
	"USER_ID" NUMBER,
	"PICTURE_ID" NUMBER
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
  
--------------------------------------------------------
--  DDL for Table CITIES
--------------------------------------------------------  

  CREATE TABLE "CITIES" 
   (	
    "CITY_ID" NUMBER, 
	"NAME" VARCHAR2(50 BYTE),
	"REGION_ID" NUMBER
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
  
--------------------------------------------------------
--  DDL for Table REGIONS
--------------------------------------------------------  
  
  CREATE TABLE "REGIONS" 
   (	
    "REGION_ID" NUMBER, 
	"NAME" VARCHAR2(60 BYTE),
	"COUNTRY_ID" NUMBER
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
  
--------------------------------------------------------
--  DDL for Table COUNTRIES
--------------------------------------------------------

  CREATE TABLE "COUNTRIES" 
   (	
    "COUNTRY_ID" NUMBER, 
	"NAME" VARCHAR2(60 BYTE)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
  
--------------------------------------------------------
--  DDL for Table CATEGORIES
--------------------------------------------------------
  
  CREATE TABLE "CATEGORIES" 
   (	
    "CATEGORY_ID" NUMBER, 
	"NAME" VARCHAR2(30 BYTE)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
  
--------------------------------------------------------
--  DDL for Table KEYWORDS
--------------------------------------------------------
  
  CREATE TABLE "KEYWORDS" 
   (	
    "KEYWORD_ID" NUMBER, 
	"NAME" VARCHAR2(20 BYTE)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
   
--------------------------------------------------------
--  DDL for Table RATINGS
--------------------------------------------------------  
  
  CREATE TABLE "RATINGS" 
   (	
	"PICTURE_ID" NUMBER, 
	"USER_ID" NUMBER,
	"VALUE" NUMBER
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
   
--------------------------------------------------------
--  DDL for Table PICTURE_TO_CATEGORY
--------------------------------------------------------

  CREATE TABLE "PICTURE_TO_CATEGORY" 
   (	
    "PICTURE_ID" NUMBER, 
	"CATEGORY_ID" NUMBER
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
  
--------------------------------------------------------
--  DDL for Table PICTURE_TO_KEYWORD
--------------------------------------------------------

  CREATE TABLE "PICTURE_TO_KEYWORD" 
   (	
    "PICTURE_ID" NUMBER, 
	"KEYWORD_ID" NUMBER
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
   
--------------------------------------------------------
--  DDL for Table APP_LOG
--------------------------------------------------------

  CREATE TABLE "APP_LOG" 
   (	
    "LOG_ID" NUMBER, 
	"TABLE_NAME" VARCHAR2(50 BYTE),
	"DATA_ID" NUMBER, 
	"USER_ID" NUMBER,
	"LOG_DATE" DATE, 
	"DESCRIPTION" VARCHAR2(200 BYTE)
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);


--------------------------------------------------------
--  Constraints for Table USERS
--------------------------------------------------------

  ALTER TABLE "USERS" ADD CONSTRAINT "USER_ID_PK" PRIMARY KEY ("USER_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE;

--------------------------------------------------------
--  Constraints for Table PICTURES
--------------------------------------------------------

  ALTER TABLE "PICTURES" ADD CONSTRAINT "PICTURE_ID_PK" PRIMARY KEY ("PICTURE_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE;

--------------------------------------------------------
--  Constraints for Table ALBUMS
--------------------------------------------------------

  ALTER TABLE "ALBUMS" ADD CONSTRAINT "ALBUM_ID_PK" PRIMARY KEY ("ALBUM_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE;

--------------------------------------------------------
--  Constraints for Table COMMENTS
--------------------------------------------------------

  ALTER TABLE "COMMENTS" ADD CONSTRAINT "COMMENT_ID_PK" PRIMARY KEY ("COMMENT_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE;

--------------------------------------------------------
--  Constraints for Table CITIES
--------------------------------------------------------

  ALTER TABLE "CITIES" ADD CONSTRAINT "CITY_ID_PK" PRIMARY KEY ("CITY_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE;
  
--------------------------------------------------------
--  Constraints for Table REGIONS
--------------------------------------------------------

  ALTER TABLE "REGIONS" ADD CONSTRAINT "REGION_ID_PK" PRIMARY KEY ("REGION_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE;


--------------------------------------------------------
--  Constraints for Table COUNTRIES
--------------------------------------------------------

  ALTER TABLE "COUNTRIES" ADD CONSTRAINT "COUNTRY_ID_PK" PRIMARY KEY ("COUNTRY_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE; 
 
--------------------------------------------------------
--  Constraints for Table CATEGORIES
--------------------------------------------------------

  ALTER TABLE "CATEGORIES" ADD CONSTRAINT "CATEGORY_ID_PK" PRIMARY KEY ("CATEGORY_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE; 
  
--------------------------------------------------------
--  Constraints for Table KEYWORDS
--------------------------------------------------------

  ALTER TABLE "KEYWORDS" ADD CONSTRAINT "KEYWORD_ID_PK" PRIMARY KEY ("KEYWORD_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE;
  
--------------------------------------------------------
--  Constraints for Table APP_LOG
--------------------------------------------------------

  ALTER TABLE "APP_LOG" ADD CONSTRAINT "LOG_ID_PK" PRIMARY KEY ("LOG_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  ENABLE;
  
--------------------------------------------------------
--  DDL for Index USERS
--------------------------------------------------------

  CREATE UNIQUE INDEX "USERS_USERNAME" ON "USERS" ("USERNAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);
  
  CREATE UNIQUE INDEX "USERS_EMAIL" ON "USERS" ("EMAIL") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);

  CREATE UNIQUE INDEX "USERS_FULLNAME" ON "USERS" ("FIRST_NAME", "LAST_NAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);

--------------------------------------------------------
--  DDL for Index PICTURES
--------------------------------------------------------

  CREATE UNIQUE INDEX "PICTURES_NAME" ON "PICTURES" ("PICTURE_NAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);  
  
--------------------------------------------------------
--  DDL for Index CITIES
--------------------------------------------------------

  CREATE UNIQUE INDEX "CITIES_NAME" ON "CITIES" ("NAME", "REGION_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);    

--------------------------------------------------------
--  DDL for Index REGIONS
--------------------------------------------------------

  CREATE UNIQUE INDEX "REGIONS_NAME" ON "REGIONS" ("NAME", "COUNTRY_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);    

--------------------------------------------------------
--  DDL for Index REGIONS
--------------------------------------------------------

  CREATE UNIQUE INDEX "COUNTRIES_NAME" ON "COUNTRIES" ("NAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);    
  
--------------------------------------------------------
--  DDL for Index CATEGORIES
--------------------------------------------------------

  CREATE UNIQUE INDEX "CATEGORIES_NAME" ON "CATEGORIES" ("NAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);    

--------------------------------------------------------
--  DDL for Index KEYWORDS
--------------------------------------------------------

  CREATE UNIQUE INDEX "KEYWORDS_NAME" ON "KEYWORDS" ("NAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);   

--------------------------------------------------------
--  DDL for Index RATINGS
--------------------------------------------------------

  CREATE UNIQUE INDEX "RATINGS_USER_ID" ON "RATINGS" ("PICTURE_ID", "USER_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);   
  
--------------------------------------------------------
--  DDL for Index PICTURE_TO_CATEGORY
--------------------------------------------------------

  CREATE UNIQUE INDEX "PICTURE_TO_CATEGORY" ON "PICTURE_TO_CATEGORY" ("PICTURE_ID", "CATEGORY_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);   
  
--------------------------------------------------------
--  DDL for Index PICTURE_TO_KEYWORD
--------------------------------------------------------

  CREATE UNIQUE INDEX "PICTURE_TO_KEYWORD" ON "PICTURE_TO_KEYWORD" ("PICTURE_ID", "KEYWORD_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT);   
  
  
--------------------------------------------------------
--  Ref Constraints for Table USERS
--------------------------------------------------------
 
  ALTER TABLE "USERS" ADD CONSTRAINT "USERS_CITY_ID_FK" FOREIGN KEY ("CITY_ID")
	  REFERENCES "CITIES" ("CITY_ID") ENABLE;
 
--------------------------------------------------------
--  Ref Constraints for Table PICTURES
--------------------------------------------------------

  ALTER TABLE "PICTURES" ADD CONSTRAINT "PICTURES_CITY_ID_FK" FOREIGN KEY ("CITY_ID")
	  REFERENCES "CITIES" ("CITY_ID") ON DELETE SET NULL ENABLE;
 
  ALTER TABLE "PICTURES" ADD CONSTRAINT "PICTURES_ALBUM_ID_FK" FOREIGN KEY ("ALBUM_ID")
	  REFERENCES "ALBUMS" ("ALBUM_ID") ON DELETE CASCADE ENABLE;
 
--------------------------------------------------------
--  Ref Constraints for Table ALBUMS
--------------------------------------------------------

  ALTER TABLE "ALBUMS" ADD CONSTRAINT "ALBUMS_USER_ID_FK" FOREIGN KEY ("USER_ID")
	  REFERENCES "USERS" ("USER_ID") ENABLE;
 
--------------------------------------------------------
--  Ref Constraints for Table COMMENTS
--------------------------------------------------------

  ALTER TABLE "COMMENTS" ADD CONSTRAINT "COMMENTS_USER_ID_FK" FOREIGN KEY ("USER_ID")
	  REFERENCES "USERS" ("USER_ID") ON DELETE SET NULL ENABLE; 
	  
  ALTER TABLE "COMMENTS" ADD CONSTRAINT "COMMENTS_PICTURE_ID_FK" FOREIGN KEY ("PICTURE_ID")
	  REFERENCES "PICTURES" ("PICTURE_ID") ON DELETE CASCADE ENABLE;
 
--------------------------------------------------------
--  Ref Constraints for Table CITIES
--------------------------------------------------------

  ALTER TABLE "CITIES" ADD CONSTRAINT "CITIES_REGION_ID_FK" FOREIGN KEY ("REGION_ID")
	  REFERENCES "REGIONS" ("REGION_ID") ENABLE;
 
--------------------------------------------------------
--  Ref Constraints for Table REGIONS
--------------------------------------------------------

  ALTER TABLE "REGIONS" ADD CONSTRAINT "REGIONS_COUNTRY_ID_FK" FOREIGN KEY ("COUNTRY_ID")
	  REFERENCES "COUNTRIES" ("COUNTRY_ID") ENABLE;
	  

--------------------------------------------------------
--  Ref Constraints for Table RATINGS
--------------------------------------------------------

  ALTER TABLE "RATINGS" ADD CONSTRAINT "RATINGS_PICTURE_ID_FK" FOREIGN KEY ("PICTURE_ID")
	  REFERENCES "PICTURES" ("PICTURE_ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "RATINGS" ADD CONSTRAINT "RATINGS_USER_ID_FK" FOREIGN KEY ("USER_ID")
	  REFERENCES "USERS" ("USER_ID") ON DELETE SET NULL ENABLE; 
 
--------------------------------------------------------
--  Ref Constraints for Table PICTURE_TO_CATEGORY 
--------------------------------------------------------

  ALTER TABLE "PICTURE_TO_CATEGORY" ADD CONSTRAINT "PTC_PICTURE_ID_FK" FOREIGN KEY ("PICTURE_ID")
	  REFERENCES "PICTURES" ("PICTURE_ID") ON DELETE CASCADE ENABLE;
	  
  ALTER TABLE "PICTURE_TO_CATEGORY" ADD CONSTRAINT "PTC_CATEGORY_ID_FK" FOREIGN KEY ("CATEGORY_ID")
	  REFERENCES "CATEGORIES" ("CATEGORY_ID") ON DELETE CASCADE ENABLE;
	 
--------------------------------------------------------
--  Ref Constraints for Table PICTURE_TO_KEYWORD
--------------------------------------------------------

  ALTER TABLE "PICTURE_TO_KEYWORD" ADD CONSTRAINT "PTK_PICTURE_ID_FK" FOREIGN KEY ("PICTURE_ID")
	  REFERENCES "PICTURES" ("PICTURE_ID") ON DELETE CASCADE ENABLE;
	  
  ALTER TABLE "PICTURE_TO_KEYWORD" ADD CONSTRAINT "PTK_KEYWORD_ID_FK" FOREIGN KEY ("KEYWORD_ID")
	  REFERENCES "KEYWORDS" ("KEYWORD_ID") ON DELETE CASCADE ENABLE;
	  
 

 
--------------------------------------------------------
--  DDL for Trigger AUTOINC_USERS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_USERS" 
	BEFORE INSERT ON USERS
	FOR EACH ROW
		BEGIN
			SELECT USERS_INC.NEXTVAL into :NEW.USER_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_USERS" ENABLE;
 
--------------------------------------------------------
--  DDL for Trigger AUTOINC_PICTURES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_PICTURES" 
	BEFORE INSERT ON PICTURES
	FOR EACH ROW
		BEGIN
			SELECT PICTURES_INC.NEXTVAL into :NEW.PICTURE_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_PICTURES" ENABLE;

--------------------------------------------------------
--  DDL for Trigger AUTOINC_ALBUMS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_ALBUMS" 
	BEFORE INSERT ON ALBUMS
	FOR EACH ROW
		BEGIN
			SELECT ALBUMS_INC.NEXTVAL into :NEW.ALBUM_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_ALBUMS" ENABLE;
 
--------------------------------------------------------
--  DDL for Trigger AUTOINC_COMMENTS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_COMMENTS" 
	BEFORE INSERT ON COMMENTS
	FOR EACH ROW
		BEGIN
			SELECT COMMENTS_INC.NEXTVAL into :NEW.COMMENT_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_COMMENTS" ENABLE;
  
--------------------------------------------------------
--  DDL for Trigger AUTOINC_CITIES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_CITIES" 
	BEFORE INSERT ON CITIES
	FOR EACH ROW
		BEGIN
			SELECT CITIES_INC.NEXTVAL into :NEW.CITY_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_CITIES" ENABLE;

--------------------------------------------------------
--  DDL for Trigger AUTOINC_REGIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_REGIONS" 
	BEFORE INSERT ON REGIONS
	FOR EACH ROW
		BEGIN
			SELECT REGIONS_INC.NEXTVAL into :NEW.REGION_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_REGIONS" ENABLE;

--------------------------------------------------------
--  DDL for Trigger AUTOINC_COUNTRIES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_COUNTRIES" 
	BEFORE INSERT ON COUNTRIES
	FOR EACH ROW
		BEGIN
			SELECT COUNTRIES_INC.NEXTVAL into :NEW.COUNTRY_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_COUNTRIES" ENABLE;  

--------------------------------------------------------
--  DDL for Trigger AUTOINC_CATEGORIES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_CATEGORIES" 
	BEFORE INSERT ON CATEGORIES
	FOR EACH ROW
		BEGIN
			SELECT CATEGORIES_INC.NEXTVAL into :NEW.CATEGORY_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_CATEGORIES" ENABLE;  

--------------------------------------------------------
--  DDL for Trigger AUTOINC_KEYWORDS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_KEYWORDS" 
	BEFORE INSERT ON KEYWORDS
	FOR EACH ROW
		BEGIN
			SELECT KEYWORDS_INC.NEXTVAL into :NEW.KEYWORD_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_KEYWORDS" ENABLE;  
 
--------------------------------------------------------
--  DDL for Trigger AUTOINC_APP_LOG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "AUTOINC_APP_LOG" 
	BEFORE INSERT ON APP_LOG
	FOR EACH ROW
		BEGIN
			SELECT APP_LOG_INC.NEXTVAL into :NEW.LOG_ID from DUAL;
		END;
  /
  ALTER TRIGGER "AUTOINC_APP_LOG" ENABLE;  
 
--------------------------------------------------------
--  DDL for Trigger INC_USER_PICTURE_SUM
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "INC_USER_PICTURE_SUM" 
	AFTER INSERT ON PICTURES
	FOR EACH ROW
		DECLARE CURRENT_USER_ID USERS.USER_ID%TYPE;
		BEGIN
			SELECT USER_ID INTO CURRENT_USER_ID FROM ALBUMS WHERE ALBUM_ID = :NEW.ALBUM_ID;
			UPDATE USERS SET PICTURE_SUM = PICTURE_SUM + 1 WHERE USER_ID = CURRENT_USER_ID;
		END;
  /
  ALTER TRIGGER "INC_USER_PICTURE_SUM" ENABLE;

--------------------------------------------------------
--  DDL for Trigger DEC_USER_PICTURE_SUM
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "DEC_USER_PICTURE_SUM" 
	AFTER DELETE ON PICTURES
	FOR EACH ROW
		DECLARE CURRENT_USER_ID USERS.USER_ID%TYPE;
		BEGIN
			SELECT USER_ID INTO CURRENT_USER_ID FROM ALBUMS WHERE ALBUM_ID = :OLD.ALBUM_ID;
			UPDATE USERS SET PICTURE_SUM = PICTURE_SUM - 1 WHERE USER_ID = CURRENT_USER_ID;
		END;
  /
  ALTER TRIGGER "DEC_USER_PICTURE_SUM" ENABLE;


--------------------------------------------------------
--  DDL for Trigger APP_LOG_PICTURE
--------------------------------------------------------

    CREATE OR REPLACE TRIGGER "APP_LOG_PICTURE" 
    AFTER INSERT OR DELETE OR UPDATE ON PICTURES
    FOR EACH ROW
		DECLARE
		descr app_log.description%TYPE;
		tablename app_log.table_name%TYPE;
		username users.username%TYPE;
		userid users.user_id%TYPE;
		pictureid pictures.picture_id%TYPE;
		albumid albums.album_id%TYPE;
	BEGIN
		tablename := 'pictures';
		IF INSERTING THEN
			pictureid := :NEW.picture_id;
		    albumid := :NEW.album_id;
		ELSIF UPDATING THEN
			pictureid := :NEW.picture_id;
		    albumid := :NEW.album_id;
		ELSIF DELETING THEN
			pictureid := :OLD.picture_id;
		    albumid := :OLD.album_id;
		END IF;
		
		SELECT users.username, users.user_id INTO username, userid FROM users, albums WHERE albums.album_id = albumid AND albums.user_id = users.user_id;
    
		IF INSERTING THEN
			descr := concat(concat(username, ' uploaded a new picture to album id:' ), albumid);
		ELSIF UPDATING THEN
			descr := concat(concat(username, ' edited a picture in album id:' ), albumid);
		ELSIF DELETING THEN
			descr := concat(concat(username, ' removed a picture from album id:' ), albumid);
		END IF;
    
		INSERT INTO app_log (table_name, data_id, log_date, user_id, description) VALUES (tablename, pictureid, SYSDATE, userid, descr);
	END;
  /
  ALTER TRIGGER "APP_LOG_PICTURE" ENABLE;

--------------------------------------------------------
--  DDL for Trigger APP_LOG_ALBUM
--------------------------------------------------------

    CREATE OR REPLACE TRIGGER "APP_LOG_ALBUM" 
    AFTER INSERT OR DELETE OR UPDATE ON ALBUMS
    FOR EACH ROW
		DECLARE
		descr app_log.description%TYPE;
		tablename app_log.table_name%TYPE;
		username users.username%TYPE;
		userid users.user_id%TYPE;
		albumid albums.album_id%TYPE;
		albumname albums.name%TYPE;
	BEGIN
		tablename := 'albums';
		IF INSERTING THEN
		    albumid := :NEW.album_id;
			userid := :NEW.user_id;
			albumname := :NEW.name;
		ELSIF UPDATING THEN
			albumid := :NEW.album_id;
			userid := :NEW.user_id;
			albumname := :NEW.name;
		ELSIF DELETING THEN
		    albumid := :OLD.album_id;
			userid := :OLD.user_id;
			albumname := :OLD.name;
		END IF;
		
		SELECT users.username INTO username FROM users WHERE users.user_id = userid;
    
		IF INSERTING THEN
			descr := concat(concat(username, ' created a new album named:' ), albumname);
		ELSIF UPDATING THEN
			descr := concat(concat(username, ' edited an album named:' ), albumname);
		ELSIF DELETING THEN
			descr := concat(concat(username, ' removed an album named:' ), albumname);
		END IF;
    
		INSERT INTO app_log (table_name, data_id, log_date, user_id, description) VALUES (tablename, albumid, SYSDATE, userid, descr);
	END;
  /
  ALTER TRIGGER "APP_LOG_ALBUM" ENABLE;


---------------------------------------------------
--   DATA FOR TABLE COUNTRIES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into COUNTRIES
Insert into COUNTRIES (COUNTRY_ID,NAME) values (1,'Magyarország');


---------------------------------------------------
--   DATA FOR TABLE REGIONS
--   FILTER = none used
---------------------------------------------------
REM INSERTING into REGIONS
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (1,'Bács-Kiskun megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (2,'Baranya megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (3,'Békés megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (4,'Borsod-Abaúj-Zemplén',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (5,'Csongrád megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (6,'Fejér megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (7,'Győr-Moson-Sopron megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (8,'Hajdú-Bihar megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (9,'Heves megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (10,'Jász-Nagykun-Szolnok megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (11,'Komárom-Esztergom megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (12,'Nógrád megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (13,'Pest megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (14,'Somogy megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (15,'Szabolcs-Szatmár-Bereg megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (16,'Tolna megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (17,'Vas megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (18,'Veszprém megye',1);
Insert into REGIONS (REGION_ID,NAME,COUNTRY_ID) values (19,'Zala megye',1);

---------------------------------------------------
--   DATA FOR TABLE CITIES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into CITIES
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (1,'Kecskemét',1);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (2,'Pécs',2);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (3,'Békéscsaba',3);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (4,'Miskolc',4);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (5,'Szeged',5);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (6,'Székesfehérvár',6);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (7,'Győr',7);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (8,'Debrecen',8);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (9,'Eger',9);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (10,'Szolnok',10);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (11,'Tatabánya',11);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (12,'Salgótarján',12);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (13,'Budapest',13);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (14,'Kaposvár',14);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (15,'Nyíregyháza',15);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (16,'Szekszárd',16);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (17,'Szombathely',17);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (18,'Veszprém',18);
Insert into CITIES (CITY_ID,NAME,REGION_ID) values (19,'Zalaegerszeg',19);

