
CREATE TABLE yazdFilter (
  filterObject   BLOB,
  forumID        INT NOT NULL,
  filterIndex    INT NOT NULL,
  KEY            (forumID),
  KEY            (filterIndex)
);

CREATE TABLE yazdForum (
  forumID           INT NOT NULL,
  name              VARCHAR(255),
  description       TEXT,
  modifiedDate      VARCHAR(15),
  creationDate      VARCHAR(15),
  moderated         INT NOT NULL,
  PRIMARY KEY       (forumID)
);

CREATE TABLE yazdForumProp (
  forumID     INT NOT NULL,
  name        VARCHAR(30) NOT NULL,
  propValue   VARCHAR(255) NOT NULL,
  KEY         (forumID,name)
);

CREATE TABLE yazdGroup (
  groupID      INT NOT NULL,
  name         VARCHAR(50) NOT NULL,
  description  VARCHAR(255),
  PRIMARY KEY  (groupID)
);

CREATE TABLE yazdGroupPerm (
  forumID      INT NOT NULL,
  groupID      INT NOT NULL,
  permission   INT NOT NULL,
  KEY          (forumID),
  KEY          (groupID)
);

CREATE TABLE yazdGroupUser (
  groupID        INT NOT NULL,
  userID         INT NOT NULL,
  administrator  INT NOT NULL,
  PRIMARY KEY   (groupID,userID)
);

CREATE TABLE yazdMessage (
  messageID     INT NOT NULL,
  threadID      INT NOT NULL DEFAULT -1,
  subject       VARCHAR(255),
  userID        INT NOT NULL,
  body          TEXT,
  modifiedDate  VARCHAR(15) NOT NULL,
  creationDate  VARCHAR(15) NOT NULL,
  approved      INT NOT NULL,
  PRIMARY KEY   (messageID),
  KEY           (userID),
  KEY           (threadID),
  KEY           (approved),
  KEY           (creationDate),
  KEY           (modifiedDate)
);

CREATE TABLE yazdMessageTree (
  parentID   INT NOT NULL,
  childID    INT NOT NULL,
  KEY        (parentID),
  KEY        (childID)
);

CREATE TABLE yazdMessageProp (
  messageID    INT NOT NULL,
  name         VARCHAR(30) NOT NULL,
  propValue    VARCHAR(255) NOT NULL,
  PRIMARY KEY  (messageID,name)
);

CREATE TABLE yazdThread (
  threadID      INT NOT NULL,
  forumID       INT NOT NULL,
  rootMessageID INT NOT NULL,
  approved      INT NOT NULL,
  creationDate  VARCHAR(15) NOT NULL,
  modifiedDate  VARCHAR(15) NOT NULL,
  PRIMARY KEY   (threadID),
  KEY           (forumID),
  KEY           (rootMessageID),
  KEY           (creationDate),
  KEY           (modifiedDate)
);

CREATE TABLE yazdUser (
  userID        INT NOT NULL,
  name          VARCHAR(50),
  username      VARCHAR(30) NOT NULL,
  passwordHash  VARCHAR(32) NOT NULL,   
  email         VARCHAR(30) NOT NULL,
  emailVisible  INT NOT NULL,
  nameVisible   INT NOT NULL,
  PRIMARY KEY   (userID)
);

CREATE TABLE yazdUserPerm (
  forumID      INT NOT NULL,
  userID       INT NOT NULL,
  permission   INT NOT NULL,
  KEY          (forumID),
  KEY          (userID)
);

CREATE TABLE yazdUserProp (
  userID     INT NOT NULL,
  name       VARCHAR(30) NOT NULL,
  propValue  VARCHAR(255) NOT NULL,
  KEY        (userID,name)
);


