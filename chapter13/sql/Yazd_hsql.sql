
;  //////////////////////
;  //  YazdFilter
;  //////////////////////

CREATE TABLE YazdFilter (
  filterObject	 BINARY,
  forumID		 INT  NOT NULL,
  filterIndex	 INT  NOT NULL,
  PRIMARY KEY (forumID,filterIndex) 
);

CREATE INDEX filterIndexIdx ON YazdFilter (
  filterIndex
 );


;  //////////////////////
;  //  YazdForum
;  //////////////////////

CREATE TABLE YazdForum (
  forumID		 INT  NOT NULL,
  name			 VARCHAR(255),
  description	 VARCHAR(2000),
  modifiedDate	 VARCHAR(15),
  creationDate	 VARCHAR(15),
  moderated		 INT  NOT NULL,
  PRIMARY KEY (forumID) 
);


;  //////////////////////
;  //  YazdForumProp
;  //////////////////////

CREATE TABLE YazdForumProp (
  forumID	  INT  NOT NULL,
  name		  VARCHAR(30) NOT NULL,
  propValue	  VARCHAR(255) NOT NULL,
  PRIMARY KEY (forumID,name)
);


;  //////////////////////
;  //  YazdGroup
;  //////////////////////

CREATE TABLE YazdGroup (
  groupID		INT	 NOT NULL,
  name			VARCHAR(50)	NOT NULL,
  description   VARCHAR(255),
  PRIMARY KEY (groupID) 
);


;  //////////////////////
;  //  YazdGroupPerm
;  //////////////////////

CREATE TABLE YazdGroupPerm (
  forumID		INT	 NOT NULL,
  groupID		INT	 NOT NULL,
  permission    INT	 NOT NULL,
  PRIMARY KEY (forumID,groupID,permission) 
);

CREATE INDEX groupGroupIdx ON YazdGroupPerm (
  groupID
);


;  //////////////////////
;  //  YazdGroupUser
;  //////////////////////

CREATE TABLE YazdGroupUser (
  groupID		   INT	NOT	NULL,
  userID		   INT	NOT	NULL,
  administrator	   INT  NOT NULL,
  PRIMARY KEY (groupID,userID)
);

CREATE INDEX groupIdx ON YazdGroupUser (
  userID
);


;  //////////////////////
;  //  YazdMessage
;  //////////////////////
  
CREATE TABLE YazdMessage (
  messageID			 INT NOT NULL,
  threadID			 INT,
  subject			 VARCHAR(255),
  userID			 INT  NOT NULL,
  body				 LONGVARCHAR,
  modifiedDate		 VARCHAR(15) NOT NULL,
  creationDate		 VARCHAR(15) NOT NULL,
  approved			 INT  NOT NULL,
  PRIMARY KEY		(messageID)
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


;  //////////////////////
;  //  YazdMessageTree
;  //////////////////////
    
CREATE TABLE YazdMessageTree (
  parentID	   INT	NOT	NULL,
  childID	   INT	NOT	NULL,
  PRIMARY KEY (parentID,childID) 
);

CREATE INDEX childIdx ON YazdMessageTree (
  childID
);


;  //////////////////////
;  //  YazdMessageProp
;  //////////////////////
    
CREATE TABLE YazdMessageProp (
  messageID	    INT	 NOT NULL,
  name			VARCHAR(50)	NOT NULL,
  propValue	    VARCHAR(255) NOT NULL,
  PRIMARY KEY (messageID,name) 
);


;  //////////////////////
;  //  YazdThread
;  //////////////////////
    
CREATE TABLE YazdThread (
  threadID			  INT  NOT NULL,
  forumID			  INT  NOT NULL,
  rootMessageID		  INT  NOT NULL,
  creationDate		  VARCHAR(15) NOT NULL,
  modifiedDate		  VARCHAR(15) NOT NULL,
  approved			  INT  NOT NULL,
  PRIMARY KEY (threadID) 
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


;  //////////////////////
;  //  YazdUser
;  //////////////////////
    
CREATE TABLE YazdUser (
  userID			   INT	NOT	NULL,
  name				   VARCHAR(50),
  username			   VARCHAR(30) NOT NULL,
  passwordHash		   VARCHAR(32) NOT NULL,
  email				   VARCHAR(30) NOT NULL,
  emailVisible		   INT	NOT	NULL,
  nameVisible		   INT	NOT	NULL,
  PRIMARY KEY (userID) 
);


;  //////////////////////
;  //  YazdUserPerm
;  //////////////////////
    
CREATE TABLE YazdUserPerm (
  forumID		   INT	NOT	NULL,
  userID		   INT	NOT	NULL,
  permission	   INT	NOT	NULL,
  PRIMARY KEY (forumID,userID,permission)
);

CREATE INDEX userUserIdx ON YazdUserPerm (
  userID
);


;  //////////////////////
;  //  YazdUserProp
;  //////////////////////
    
CREATE TABLE YazdUserProp (
  userID	  INT  NOT NULL,
  name		  VARCHAR(30) NOT NULL,
  propValue	  VARCHAR(255) NOT NULL,
  PRIMARY KEY (userID,name) 
);



