--  //////////////////////
--  //  YazdFilter
--  //////////////////////

CREATE TABLE YazdFilter (
  filterObject   BLOB(32768) ,
  forumID        INTEGER NOT NULL,
  filterIndex    INTEGER NOT NULL
);

CREATE INDEX filterIndexIdx ON YazdFilter (
  filterIndex  ASC
 );

ALTER TABLE YazdFilter
  ADD PRIMARY KEY (forumID,filterIndex) 
  ;

--  //////////////////////
--  //  YazdForum
--  //////////////////////

CREATE TABLE YazdForum (
  forumID        INTEGER NOT NULL,
  name           VARCHAR(255) ,
  description    VARCHAR(2000) ,
  modifiedDate   VARCHAR(15) ,
  creationDate   VARCHAR(15) ,
  moderated      INTEGER NOT NULL
);

ALTER TABLE YazdForum
  ADD PRIMARY KEY (forumID) 
  ;

--  //////////////////////
--  //  YazdForumProp
--  //////////////////////
CREATE TABLE YazdForumProp (
  forumID     INTEGER NOT NULL,
  name        VARCHAR(30) NOT NULL,
  propValue   VARCHAR(255) NOT NULL
);

ALTER TABLE YazdForumProp
  ADD PRIMARY KEY (forumID,name)
  ;

--  //////////////////////
--  //  YazdGroup
--  //////////////////////

CREATE TABLE YazdGroup (
  groupID       INTEGER NOT NULL,
  name          VARCHAR(50) NOT NULL,
  description   VARCHAR(255) 
);

ALTER TABLE YazdGroup
  ADD PRIMARY KEY (groupID) 
  ;

--  //////////////////////
--  //  YazdGroupPerm
--  //////////////////////

CREATE TABLE YazdGroupPerm (
  forumID       INTEGER NOT NULL,
  groupID       INTEGER NOT NULL,
  permission    INTEGER NOT NULL
);

CREATE INDEX groupGroupIdx ON YazdGroupPerm (
  groupID   ASC
);

ALTER TABLE YazdGroupPerm
  ADD PRIMARY KEY (forumID, groupID, permission) 
  ;

--  //////////////////////
--  //  YazdGroupUser
--  //////////////////////

CREATE TABLE YazdGroupUser (
  groupID          INTEGER NOT NULL,
  userID           INTEGER NOT NULL,
  administrator    INTEGER NOT NULL
);

CREATE INDEX groupIdx ON YazdGroupUser (
  userID     ASC
);

ALTER TABLE YazdGroupUser
  ADD PRIMARY KEY (groupID, userID)
  ;

--  //////////////////////
--  //  YazdMessage
--  //////////////////////
  
CREATE TABLE YazdMessage (
  messageID          INTEGER NOT NULL,
  threadID           INTEGER DEFAULT -1,
  subject            VARCHAR(255) ,
  userID             INTEGER NOT NULL,
  body               LONG VARCHAR ,
  modifiedDate       VARCHAR(15) NOT NULL,
  creationDate       VARCHAR(15) NOT NULL,
  approved           INTEGER NOT NULL
);


--  *************************************
--  Change the name of the 4 index bellow
--  *************************************

CREATE INDEX msgApprovedIdx ON YazdMessage (
  approved        ASC
);

CREATE INDEX msgThreadIDIdx ON YazdMessage (
  threadID        ASC
);

CREATE INDEX msgModifiedDateIdx ON YazdMessage (
  modifiedDate    ASC
);

CREATE INDEX msgCreationDateIdx ON YazdMessage (
  creationDate    ASC
);

CREATE INDEX msgUserIDIdx ON YazdMessage (
  userID    ASC
);

ALTER TABLE YazdMessage
  ADD PRIMARY KEY (messageID)
  ;

--  //////////////////////
--  //  YazdMessageTree
--  //////////////////////
    
CREATE TABLE YazdMessageTree (
  parentID     INTEGER NOT NULL,
  childID      INTEGER NOT NULL
);

CREATE INDEX childIdx ON YazdMessageTree (
  childID      ASC
);

ALTER TABLE YazdMessageTree
  ADD PRIMARY KEY (parentID, childID) 
  ;

--  //////////////////////
--  //  YazdMessageProp
--  //////////////////////
    
CREATE TABLE YazdMessageProp (
  messageID     INTEGER NOT NULL,
  name          VARCHAR(50) NOT NULL,
  propValue     VARCHAR(255) NOT NULL
);

ALTER TABLE YazdMessageProp
  ADD PRIMARY KEY (messageID, name) 
  ;

--  //////////////////////
--  //  YazdThread
--  //////////////////////
    
CREATE TABLE YazdThread (
  threadID            INTEGER NOT NULL,
  forumID             INTEGER NOT NULL,
  rootMessageID       INTEGER NOT NULL,
  creationDate        VARCHAR(15) NOT NULL,
  modifiedDate        VARCHAR(15) NOT NULL,
  approved            INTEGER NOT NULL
);

--  *************************************
--  Change the name of the 3 index bellow
--  *************************************
CREATE INDEX thdModifiedDateIdx ON YazdThread (
  modifiedDate    ASC
);

CREATE INDEX thdCreationDateIdx ON YazdThread (
  creationDate    ASC
);

CREATE INDEX thdForumIDIdx ON YazdThread (
  forumID    ASC
);

ALTER TABLE YazdThread
  ADD PRIMARY KEY (threadID) 
  ;   

--  //////////////////////
--  //  YazdUser
--  //////////////////////
    
CREATE TABLE YazdUser (
  userID               INTEGER NOT NULL,
  name                 VARCHAR(50) ,
  username             VARCHAR(30) NOT NULL,
  passwordHash         VARCHAR(32) NOT NULL,
  email                VARCHAR(30) NOT NULL,
  emailVisible         INTEGER NOT NULL,
  nameVisible          INTEGER NOT NULL
);

ALTER TABLE YazdUser
  ADD PRIMARY KEY (userID) 
  ;

--  //////////////////////
--  //  YazdUserPerm
--  //////////////////////
    
CREATE TABLE YazdUserPerm (
  forumID          INTEGER NOT NULL,
  userID           INTEGER NOT NULL,
  permission       INTEGER NOT NULL
);

CREATE INDEX userUserIdx ON YazdUserPerm (
  userID    ASC
);

ALTER TABLE YazdUserPerm
  ADD PRIMARY KEY (forumID, userID, permission)
  ;


--  //////////////////////
--  //  YazdUserProp
--  //////////////////////
    
CREATE TABLE YazdUserProp (
  userID      INTEGER NOT NULL,
  name        VARCHAR(30) NOT NULL,
  propValue   VARCHAR(255) NOT NULL
);

ALTER TABLE YazdUserProp
  ADD PRIMARY KEY (userID, name) 
  ;
