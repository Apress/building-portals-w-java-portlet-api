

REM  //////////////////////
REM  //  YazdFilter
REM  //////////////////////

CREATE TABLE YazdFilter (
  filterObject   BLOB NULL,
  forumID        INTEGER NOT NULL,
  filterIndex    INTEGER NOT NULL
);

CREATE INDEX filterIndexIdx ON YazdFilter (
  filterIndex  ASC
);

ALTER TABLE YazdFilter
  ADD ( 
    PRIMARY KEY (forumID,filterIndex) 
  );



REM  //////////////////////
REM  //  YazdForum
REM  //////////////////////

CREATE TABLE YazdForum (
  forumID        INTEGER NOT NULL,
  name           VARCHAR2(255) NULL,
  description    VARCHAR2(2000) NULL,
  modifiedDate   VARCHAR2(15) NULL,
  creationDate   VARCHAR2(15) NULL,
  moderated      INTEGER NOT NULL
);

ALTER TABLE YazdForum
  ADD ( 
    PRIMARY KEY (forumID) 
  );



REM  //////////////////////
REM  //  YazdForumProp
REM  //////////////////////
CREATE TABLE YazdForumProp (
  forumID     INTEGER NOT NULL,
  name        VARCHAR2(30) NOT NULL,
  propValue   VARCHAR2(255) NOT NULL
);

ALTER TABLE YazdForumProp
  ADD (
    PRIMARY KEY (forumID,name)
  );

  

REM  //////////////////////
REM  //  YazdGroup
REM  //////////////////////

CREATE TABLE YazdGroup (
  groupID       INTEGER NOT NULL,
  name          VARCHAR2(50) NOT NULL,
  description   VARCHAR2(255) NULL
);

ALTER TABLE YazdGroup
  ADD ( 
    PRIMARY KEY (groupID) 
  );



REM  //////////////////////
REM  //  YazdGroupPerm
REM  //////////////////////

CREATE TABLE YazdGroupPerm (
  forumID       INTEGER NOT NULL,
  groupID       INTEGER NOT NULL,
  permission    INTEGER NOT NULL
);

CREATE INDEX groupGroupIdx ON YazdGroupPerm (
  groupID   ASC
);

ALTER TABLE YazdGroupPerm
  ADD ( 
    PRIMARY KEY (forumID, groupID, permission) 
  );



REM  //////////////////////
REM  //  YazdGroupUser
REM  //////////////////////

CREATE TABLE YazdGroupUser (
  groupID          INTEGER NOT NULL,
  userID           INTEGER NOT NULL,
  administrator    INTEGER NOT NULL
);

CREATE INDEX groupIdx ON YazdGroupUser (
  userID     ASC
);

ALTER TABLE YazdGroupUser
  ADD ( 
    PRIMARY KEY (groupID, userID)
  );



REM  //////////////////////
REM  //  YazdMessage
REM  //////////////////////
  
CREATE TABLE YazdMessage (
  messageID          INTEGER NOT NULL,
  threadID           INTEGER DEFAULT -1,
  subject            VARCHAR2(255) NULL,
  userID             INTEGER NOT NULL,
  body               LONG VARCHAR NULL,
  modifiedDate       VARCHAR2(15) NOT NULL,
  creationDate       VARCHAR2(15) NOT NULL,
  approved           INTEGER NOT NULL
);

CREATE INDEX messageApprovedIdx ON YazdMessage (
  approved    ASC
);

CREATE INDEX messageThreadIDIdx ON YazdMessage (
  threadID    ASC
);

CREATE INDEX messageCreationDateIdx ON YazdMessage (
  creationDate    ASC
);

CREATE INDEX messageModifiedDateIdx ON YazdMessage (
  modifiedDate    ASC
);

CREATE INDEX messageUserIDIdx ON YazdMessage (
  userID    ASC
);

ALTER TABLE YazdMessage
  ADD ( 
    PRIMARY KEY (messageID)
  );



REM  //////////////////////
REM  //  YazdMessageTree
REM  //////////////////////
    
CREATE TABLE YazdMessageTree (
  parentID     INTEGER NOT NULL,
  childID      INTEGER NOT NULL
);

CREATE INDEX childIdx ON YazdMessageTree (
  childID      ASC
);

ALTER TABLE YazdMessageTree
  ADD ( 
    PRIMARY KEY (parentID, childID) 
  );



REM  //////////////////////
REM  //  YazdMessageProp
REM  //////////////////////
    
CREATE TABLE YazdMessageProp (
  messageID     INTEGER NOT NULL,
  name          VARCHAR2(50) NOT NULL,
  propValue     VARCHAR2(255) NOT NULL
);

ALTER TABLE YazdMessageProp
  ADD ( 
    PRIMARY KEY (messageID, name) 
  );


REM  //////////////////////
REM  //  YazdThread
REM  //////////////////////
    
CREATE TABLE YazdThread (
  threadID            INTEGER NOT NULL,
  forumID             INTEGER NOT NULL,
  rootMessageID       INTEGER NOT NULL,
  creationDate        VARCHAR2(15) NOT NULL,
  modifiedDate        VARCHAR2(15) NOT NULL,
  approved            INTEGER NOT NULL
);

CREATE INDEX threadCreationDateIdx ON YazdThread (
  creationDate    ASC
);

CREATE INDEX threadModifiedDateIdx ON YazdThread (
  modifiedDate    ASC
);

CREATE INDEX threadForumIDIdx ON YazdThread (
  forumID    ASC
);

ALTER TABLE YazdThread
  ADD ( 
    PRIMARY KEY (threadID) 
  );   



REM  //////////////////////
REM  //  YazdUser
REM  //////////////////////
    
CREATE TABLE YazdUser (
  userID               INTEGER NOT NULL,
  name                 VARCHAR2(50) NULL,
  username             VARCHAR2(30) NOT NULL,
  passwordHash         VARCHAR2(32) NOT NULL,
  email                VARCHAR2(30) NOT NULL,
  emailVisible         INTEGER NOT NULL,
  nameVisible          INTEGER NOT NULL
);

ALTER TABLE YazdUser
  ADD ( 
    PRIMARY KEY (userID) 
  );



REM  //////////////////////
REM  //  YazdUserPerm
REM  //////////////////////
    
CREATE TABLE YazdUserPerm (
  forumID          INTEGER NOT NULL,
  userID           INTEGER NOT NULL,
  permission       INTEGER NOT NULL
);

CREATE INDEX userUserIdx ON YazdUserPerm (
  userID    ASC
);

ALTER TABLE YazdUserPerm
  ADD ( 
    PRIMARY KEY (forumID, userID, permission)
  );



REM  //////////////////////
REM  //  YazdUserProp
REM  //////////////////////
    
CREATE TABLE YazdUserProp (
  userID      INTEGER NOT NULL,
  name        VARCHAR2(30) NOT NULL,
  propValue   VARCHAR2(255) NOT NULL
);

ALTER TABLE YazdUserProp
  ADD ( 
    PRIMARY KEY (userID, name) 
  );



 




