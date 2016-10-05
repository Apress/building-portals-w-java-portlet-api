
CREATE TABLE YazdFilter (
  filterObject   BLOB SUB_TYPE 0,
  forumID        INTEGER NOT NULL,
  filterIndex    INTEGER NOT NULL,
  PRIMARY KEY    (forumID, filterIndex)
);

CREATE INDEX filterIndexIdx ON YazdFilter (
	filterIndex
);

CREATE TABLE YazdForum (
  forumID      		INTEGER NOT NULL,
  name              VARCHAR(255),
  description       BLOB SUB_TYPE TEXT ,
  modifiedDate  	VARCHAR(15),
  creationDate 		VARCHAR(15),
  moderated         INTEGER NOT NULL,
  PRIMARY KEY       (forumID)
);

CREATE TABLE YazdForumProp (
  forumID     INTEGER NOT NULL,
  name        VARCHAR(30) NOT NULL,
  propValue   VARCHAR(255) NOT NULL,
  PRIMARY KEY (forumID,name)
);

CREATE TABLE YazdGroup (
  groupID      INTEGER NOT NULL,
  name         VARCHAR(50) NOT NULL,
  description  VARCHAR(255),
  PRIMARY KEY  (groupID)
);

CREATE TABLE YazdGroupPerm (
  forumID      INTEGER NOT NULL,
  groupID      INTEGER NOT NULL,
  permission   INTEGER NOT NULL,
  PRIMARY KEY  ( forumID, groupID, permission )
);

CREATE INDEX groupGroupIdx ON YazdGroupPerm (
  groupID
);

CREATE TABLE YazdGroupUser (
  groupID        INTEGER NOT NULL,
  userID         INTEGER NOT NULL,
  administrator  INTEGER NOT NULL,
  PRIMARY KEY   (groupID,userID)
);

CREATE INDEX groupIdx ON YazdGroupUser (
  userID
);

CREATE TABLE YazdMessage (
  messageID     INTEGER NOT NULL,
  threadID      INTEGER DEFAULT -1,
  subject       VARCHAR(255),
  userID        INTEGER NOT NULL,
  body          BLOB SUB_TYPE TEXT,
  modifiedDate  VARCHAR(15) NOT NULL,
  creationDate  VARCHAR(15) NOT NULL,
  approved      INTEGER NOT NULL,
  PRIMARY KEY   (messageID)
);

CREATE INDEX messageApprovedIdx ON YazdMessage (
  approved
);

CREATE INDEX messageThreadIDIdx ON YazdMessage (
  threadID
);

CREATE INDEX messageCreationDateIdx ON YazdMessage (
  creationDate
);

CREATE INDEX messageModifiedDateIdx ON YazdMessage (
  modifiedDate
);

CREATE INDEX messageUserIDIdx ON YazdMessage (
  userID
);

CREATE TABLE YazdMessageTree (
  parentID INTEGER NOT NULL,
  childID INTEGER NOT NULL,
  PRIMARY KEY (parentID, childID)
);

CREATE INDEX childIdx ON YazdMessageTree (
  childID
);

CREATE TABLE YazdMessageProp (
  messageID    INTEGER NOT NULL,
  name         VARCHAR(30) NOT NULL,
  propValue      VARCHAR(255) NOT NULL,
  PRIMARY KEY  (messageID,name)
);


CREATE TABLE YazdThread (
  threadID      INTEGER NOT NULL,
  forumID       INTEGER NOT NULL,
  rootMessageID INTEGER NOT NULL,
  approved      INTEGER NOT NULL,
  creationDate  VARCHAR(15) NOT NULL,
  modifiedDate  VARCHAR(15) NOT NULL,
  PRIMARY KEY   (threadID)
);

CREATE INDEX threadCreationDateIdx ON YazdThread (
  creationDate
);

CREATE INDEX threadModifiedDateIdx ON YazdThread (
  modifiedDate
);

CREATE INDEX threadForumIDIdx ON YazdThread (
  forumID
);

CREATE TABLE YazdUser (
  userID        INTEGER NOT NULL,
  name          VARCHAR(50),
  username      VARCHAR(30) NOT NULL,
  passwordHash  VARCHAR(32) NOT NULL,
  email         VARCHAR(30) NOT NULL,
  emailVisible  INTEGER NOT NULL,
  nameVisible   INTEGER NOT NULL,
  PRIMARY KEY   (userID)
);

CREATE TABLE YazdUserPerm (
  forumID      INTEGER NOT NULL,
  userID       INTEGER NOT NULL,
  permission   INTEGER NOT NULL,
  PRIMARY KEY  (forumID, userID, permission)
);

CREATE INDEX userUserIdx ON YazdUserPerm (
  userID
);

CREATE TABLE YazdUserProp (
  userID  		INTEGER NOT NULL,
  name    		VARCHAR(30) NOT NULL,
  propValue  	VARCHAR(255) NOT NULL,
  PRIMARY KEY   (userID,name)
);




