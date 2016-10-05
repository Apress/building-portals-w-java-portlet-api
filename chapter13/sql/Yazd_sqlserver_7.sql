
/* ============================================================ */
/* Yazd Filter                                                  */
/* ============================================================ */
create table YazdFilter
(
  filterObject   image         null,
  forumID        int           not null,
  filterIndex    int           not null,
  constraint     filterPK primary key (forumID,filterIndex)
);

create index filterIndexIdx on YazdFilter (filterIndex);


/* ============================================================ */
/* Yazd Forum                                                   */
/* ============================================================ */
create table YazdForum
(
  forumID        int           not null,
  name           varchar(255)  null,
  description    varchar(2000) null,
  modifiedDate   varchar(15)   null,
  creationDate   varchar(15)   null,
  moderated      int           not null,
  constraint     forumPK primary key (forumID)
);


/* ============================================================ */
/* Yazd Forum Properties                                        */
/* ============================================================ */
create table YazdForumProp
(
  forumID        int           not null,
  name           varchar(30)   not null,
  propValue      varchar(255)  not null,
  constraint     forumPropPK primary key (forumID, name)
);


/* ============================================================ */
/* Yazd Group                                                   */
/* ============================================================ */
create table YazdGroup
(
  groupID       int            not null,
  name          varchar(50)    not null,
  description   varchar(255)   null,
  constraint    groupPK primary key (groupID)
);


/* ============================================================ */
/* Yazd GroupPerm                                               */
/* ============================================================ */
create table YazdGroupPerm
(
  forumID       int            not null,
  groupID       int            not null,
  permission    int            not null,
  constraint    groupPermPK primary key (forumID, groupID, permission)
);

create index groupGroupIdx on YazdGroupPerm (groupID);


/* ============================================================ */
/* Yazd GroupUser                                               */
/* ============================================================ */
create table YazdGroupUser
(
  groupID       int            not null,
  userID        int            not null,
  administrator int            not null,
  constraint    groupUserPK primary key (groupID, userID)
);

create index groupIdx on YazdGroupUser (userID);


/* ============================================================ */
/* Yazd Message                                                 */
/* ============================================================ */
create table YazdMessage
(
  messageID     int            not null,
  threadID      int            not null
                               default -1,
  subject       varchar(255)   null,
  userID        int            not null,
  body          text           null,
  modifiedDate  char(15)       not null,
  creationDate  char(15)       not null,
  approved      int            not null,
  constraint    messagePK primary key (messageID)
);

create index messageApprovedIdx on YazdMessage (approved);

create index messageThreadIDIdx on YazdMessage (threadID);

create index messageCreationDateIdx on YazdMessage (creationDate);

create index messageModifiedDateIdx on YazdMessage (modifiedDate);

create index messageUserIDIdx on YazdMessage (userID);


/* ============================================================ */
/* Yazd MessageTree                                             */
/* ============================================================ */
create table YazdMessageTree
(
  parentID      int            not null,
  childID       int            not null,
  constraint    messageTreePK primary key (parentID, childID)
);

create index childIdx on YazdMessageTree (childID);


/* ============================================================ */
/* Yazd MessageProp                                             */
/* ============================================================ */
create table YazdMessageProp
(
  messageID     int            not null,
  name          varchar(50)    not null,
  propValue     varchar(255)   not null,
  constraint    messagePropPK primary key (messageID, name)
);


/* ============================================================ */
/* Yazd Thread                                                  */
/* ============================================================ */
create table YazdThread
(
  threadID      int            not null,
  forumID       int            not null,
  rootMessageID int            not null,
  creationDate  char(15)       not null,
  modifiedDate  char(15)       not null,
  approved      int            not null,
  constraint    threadPK primary key (threadID)
);

create index threadCreationDateIdx on YazdThread (creationDate);

create index threadModifiedDateIdx on YazdThread (modifiedDate);

create index threadForumIDIdx on YazdThread (forumID);


/* ============================================================ */
/* Yazd User                                                    */
/* ============================================================ */
CREATE TABLE YazdUser (
  userID        int            not null,
  name          varchar(50)    null,
  username      varchar(30)    not null,
  passwordHash  varchar(32)    not null,
  email         varchar(30)    not null,
  emailVisible  int            not null,
  nameVisible   int            not null,
  constraint    userPK primary key (userID)
);


/* ============================================================ */
/* Yazd UserPerm                                                */
/* ============================================================ */
create table YazdUserPerm
(
  forumID       int            not null,
  userID        int            not null,
  permission    int            not null,
  constraint    userPermPK primary key (forumID, userID, permission)
);

create index userUserIdx on YazdUserPerm (userID);


/* ============================================================ */
/* Yazd UserProp                                                */
/* ============================================================ */
create table YazdUserProp
(
  userID        int            not null,
  name          varchar(30)    not null,
  propValue     varchar(255)   not null,
  constraint    userPropPK primary key (userID, name)
);


