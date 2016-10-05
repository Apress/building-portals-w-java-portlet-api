
     /* ============================================================ */
     /* Table: YazdFilter */
     /* ============================================================ */
     create table YazdFilter
     (
     filterObject oid ,
     forumID integer not null,
     filterIndex integer not null,
     constraint PK_YazdFILTER primary key (forumID, filterIndex)
     )
     ;

     /* ============================================================ */
     /* Index: filterIndexIdx */
     /* ============================================================ */
     create index filterIndexIdx on YazdFilter (filterIndex)
     ;

     /* ============================================================ */
     /* Table: YazdForum */
     /* ============================================================ */
     create table YazdForum
     (
     forumID integer not null,
     name varchar(255) ,
     description text ,
     modifiedDate varchar(15) ,
     creationDate varchar(15) ,
     moderated integer not null,
     constraint PK_YazdFORUM primary key (forumID)
     )
     ;

     /* ============================================================ */
     /* Table: YazdForumProp */
     /* ============================================================ */
     create table YazdForumProp
     (
     forumID integer not null,
     name varchar(30) not null,
     propValue varchar(255) not null
     )
     ;

     /* ============================================================ */
     /* Table: YazdGroup */
     /* ============================================================ */
     create table YazdGroup
     (
     groupID integer not null,
     name varchar(50) not null,
     description varchar(255) ,
     constraint PK_YazdGROUP primary key (groupID)
     )
     ;

     /* ============================================================ */
     /* Table: YazdGroupPerm */
     /* ============================================================ */
     create table YazdGroupPerm
     (
     forumID integer not null,
     groupID integer not null,
     permission integer not null,
     constraint PK_YazdGROUPPERM primary key (forumID, groupID, permission)
     )
     ;

     /* ============================================================ */
     /* Index: groupGroupIdx */
     /* ============================================================ */
     create index groupGroupIdx on YazdGroupPerm (groupID)
     ;

     /* ============================================================ */
     /* Table: YazdGroupUser */
     /* ============================================================ */
     create table YazdGroupUser
     (
     groupID integer not null,
     userID integer not null,
     administrator integer not null,
     constraint PK_YazdGROUPUSER primary key (groupID, userID)
     )
     ;

     /* ============================================================ */
     /* Index: groupIdx */
     /* ============================================================ */
     create index groupIdx on YazdGroupUser (userID)
     ;

     /* ============================================================ */
     /* Table: YazdMessage */
     /* ============================================================ */
     create table YazdMessage
     (
     messageID integer not null,
     threadID integer default -1 ,
     subject varchar(255) ,
     userID integer not null,
     body text ,
     modifiedDate varchar(15) not null,
     creationDate varchar(15) not null,
     approved integer not null,
     constraint PK_YazdMESSAGE primary key (messageID)
     )
     ;

     /* ============================================================ */
     /* Index: messageApprovedIdx */
     /* ============================================================ */
     create index messageApprovedIdx on YazdMessage (approved)
     ;

     /* ============================================================ */
     /* Index: messageCreationDateIdx */
     /* ============================================================ */
     create index messageCreationDateIdx on YazdMessage (creationDate)
     ;

     /* ============================================================ */
     /* Index: messageModifiedDateIdx */
     /* ============================================================ */
     create index messageModifiedDateIdx on YazdMessage (modifiedDate)
     ;

     /* ============================================================ */
     /* Index: messageThreadIDIdx */
     /* ============================================================ */
     create index messageThreadIDIdx on YazdMessage (threadID)
     ;

     /* ============================================================ */
     /* Index: messageUserIDIdx */
     /* ============================================================ */
     create index messageUserIDIdx on YazdMessage (userID)
     ;

     /* ============================================================ */
     /* Table: YazdMessageTree */
     /* ============================================================ */
     create table YazdMessageTree
     (
     parentID integer not null,
     childID integer not null,
     constraint PK_YazdMESSAGETREE primary key (parentID, childID)
     )
     ;

     /* ============================================================ */
     /* Index: childIdx */
     /* ============================================================ */
     create index childIdx on YazdMessageTree (childID)
     ;

     /* ============================================================ */
     /* Table: YazdMessageProp */
     /* ============================================================ */
     create table YazdMessageProp
     (
     messageID integer not null,
     name varchar(50) not null,
     propValue varchar(255) not null,
     constraint PK_YazdMESSAGEPROP primary key (messageID, name)
     )
     ;

     /* ============================================================ */
     /* Table: YazdThread */
     /* ============================================================ */
     create table YazdThread
     (
     threadID integer not null,
     forumID integer not null,
     rootMessageID integer not null,
     creationDate varchar(15) not null,
     modifiedDate varchar(15) not null,
     approved integer not null,
     constraint PK_YazdTHREAD primary key (threadID)
     )
     ;

     /* ============================================================ */
     /* Index: threadCreationDateIdx */
     /* ============================================================ */
     create index threadCreationDateIdx on YazdThread (creationDate)
     ;

     /* ============================================================ */
     /* Index: threadModifiedDateIdx */
     /* ============================================================ */
     create index threadModifiedDateIdx on YazdThread (modifiedDate)
     ;

     /* ============================================================ */
     /* Index: threadForumIDIdx */
     /* ============================================================ */
     create index threadForumIDIdx on YazdThread (forumID)
     ;

     /* ============================================================ */
     /* Table: YazdUser */
     /* ============================================================ */
     create table YazdUser
     (
     userID integer not null,
     name varchar(50) ,
     username varchar(30) not null,
     passwordHash varchar(32) not null,
     email varchar(30) not null,
     emailVisible integer not null,
     nameVisible integer not null,
     constraint PK_YazdUSER primary key (userID)
     )
     ;

     /* ============================================================ */
     /* Table: YazdUserPerm */
     /* ============================================================ */
     create table YazdUserPerm
     (
     forumID integer not null,
     userID integer not null,
     permission integer not null,
     constraint PK_YazdUSERPERM primary key (forumID, userID, permission)
     )
     ;

     /* ============================================================ */
     /* Index: userUserIdx */
     /* ============================================================ */
     create index userUserIdx on YazdUserPerm (userID)
     ;

     /* ============================================================ */
     /* Table: YazdUserProp */
     /* ============================================================ */
     create table YazdUserProp
     (
     userID integer not null,
     name varchar(30) not null,
     propValue varchar(255) not null,
     constraint PK_YazdUSERPROP primary key (userID, name)
     )
     ;

