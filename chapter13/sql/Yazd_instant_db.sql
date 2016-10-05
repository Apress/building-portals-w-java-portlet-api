

; Load the JDBC driver and open a database. Make sure to adjust the path
; to your own instant db properties file.
d org.enhydra.instantdb.jdbc.idbDriver;
o jdbc:idb=/path_to_/Yazd_instant_db.properties;


;  //////////////////////
;  //  YazdFilter
;  //////////////////////

e CREATE TABLE YazdFilter (
  filterObject   BINARY,
  forumID        INT  NOT NULL,
  filterIndex    INT  NOT NULL,
  PRIMARY KEY (forumID,filterIndex) 
);

e CREATE INDEX filterIndexIdx ON YazdFilter (
  filterIndex  ASC
 );


;  //////////////////////
;  //  YazdForum
;  //////////////////////

e CREATE TABLE YazdForum (
  forumID        INT  NOT NULL,
  name           VARCHAR(255),
  description    VARCHAR(2000),
  modifiedDate   VARCHAR(15),
  creationDate   VARCHAR(15),
  moderated      INT  NOT NULL,
  PRIMARY KEY (forumID) 
);


;  //////////////////////
;  //  YazdForumProp
;  //////////////////////

e CREATE TABLE YazdForumProp (
  forumID     INT  NOT NULL,
  name        VARCHAR(30) NOT NULL,
  propValue   VARCHAR(255) NOT NULL,
  PRIMARY KEY (forumID,name)
);


;  //////////////////////
;  //  YazdGroup
;  //////////////////////

e CREATE TABLE YazdGroup (
  groupID       INT  NOT NULL,
  name          VARCHAR(50) NOT NULL,
  description   VARCHAR(255),
  PRIMARY KEY (groupID) 
);


;  //////////////////////
;  //  YazdGroupPerm
;  //////////////////////

e CREATE TABLE YazdGroupPerm (
  forumID       INT  NOT NULL,
  groupID       INT  NOT NULL,
  permission    INT  NOT NULL,
  PRIMARY KEY (forumID,groupID,permission) 
);

e CREATE INDEX groupGroupIdx ON YazdGroupPerm (
  groupID   ASC
);


;  //////////////////////
;  //  YazdGroupUser
;  //////////////////////

e CREATE TABLE YazdGroupUser (
  groupID          INT  NOT NULL,
  userID           INT  NOT NULL,
  administrator    INT  NOT NULL,
  PRIMARY KEY (groupID,userID)
);

e CREATE INDEX groupIdx ON YazdGroupUser (
  userID     ASC
);


;  //////////////////////
;  //  YazdMessage
;  //////////////////////
  
e CREATE TABLE YazdMessage (
  messageID          INT NOT NULL,
  threadID      	 INT DEFAULT 0,
  subject            VARCHAR(255),
  userID             INT  NOT NULL,
  body               LONGCHAR,
  modifiedDate       VARCHAR(15) NOT NULL,
  creationDate       VARCHAR(15) NOT NULL,
  approved           INT  NOT NULL,
  PRIMARY KEY 		(messageID)
);

e CREATE INDEX messageApprovedIdx ON YazdMessage (
  approved    ASC
);

e CREATE INDEX messageThreadIDIdx ON YazdMessage (
  threadID    ASC
);

e CREATE INDEX messageCreationDateIdx ON YazdMessage (
  creationDate    ASC
);

e CREATE INDEX messageModifiedDateIdx ON YazdMessage (
  modifiedDate    ASC
);

e CREATE INDEX messageUserIDIdx ON YazdMessage (
  userID    ASC
);


;  //////////////////////
;  //  YazdMessageTree
;  //////////////////////
    
e CREATE TABLE YazdMessageTree (
  parentID     INT  NOT NULL,
  childID      INT  NOT NULL,
  PRIMARY KEY (parentID,childID) 
);

e CREATE INDEX childIdx ON YazdMessageTree (
  childID      ASC
);


;  //////////////////////
;  //  YazdMessageProp
;  //////////////////////
    
e CREATE TABLE YazdMessageProp (
  messageID     INT  NOT NULL,
  name          VARCHAR(50) NOT NULL,
  propValue     VARCHAR(255) NOT NULL,
  PRIMARY KEY (messageID,name) 
);


;  //////////////////////
;  //  YazdThread
;  //////////////////////
    
e CREATE TABLE YazdThread (
  threadID            INT  NOT NULL,
  forumID             INT NOT NULL,
  rootMessageID       INT  NOT NULL,
  creationDate        VARCHAR(15) NOT NULL,
  modifiedDate        VARCHAR(15) NOT NULL,
  approved            INT  NOT NULL,
  PRIMARY KEY (threadID) 
);

e CREATE INDEX threadCreationDateIdx ON YazdThread (
  creationDate    ASC
);

e CREATE INDEX threadModifiedDateIdx ON YazdThread (
  modifiedDate    ASC
);

e CREATE INDEX threadForumIDIdx ON YazdThread (
  forumID    ASC
);


;  //////////////////////
;  //  YazdUser
;  //////////////////////
    
e CREATE TABLE YazdUser (
  userID               INT  NOT NULL,
  name                 VARCHAR(50),
  username             VARCHAR(30) NOT NULL,
  passwordHash         VARCHAR(32) NOT NULL,
  email                VARCHAR(30) NOT NULL,
  emailVisible         INT  NOT NULL,
  nameVisible          INT  NOT NULL,
  PRIMARY KEY (userID) 
);


;  //////////////////////
;  //  YazdUserPerm
;  //////////////////////
    
e CREATE TABLE YazdUserPerm (
  forumID          INT  NOT NULL,
  userID           INT  NOT NULL,
  permission       INT  NOT NULL,
  PRIMARY KEY (forumID,userID,permission)
);

e CREATE INDEX userUserIdx ON YazdUserPerm (
  userID    ASC
);


;  //////////////////////
;  //  YazdUserProp
;  //////////////////////
    
e CREATE TABLE YazdUserProp (
  userID      INT  NOT NULL,
  name        VARCHAR(30) NOT NULL,
  propValue   VARCHAR(255) NOT NULL,
  PRIMARY KEY (userID,name) 
);



c close;

