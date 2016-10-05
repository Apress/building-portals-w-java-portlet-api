/* ============================================================ */
/*   Table: YazdFilter                                          */
/* ============================================================ */
create table YazdFilter
(
    filterObject   text                   null    ,
    forumID        integer                not null,
    filterIndex    integer                not null,
    constraint PK_YazdFILTER primary key (forumID, filterIndex)
)
go

/* ============================================================ */
/*   Index: filterIndexIdx                                      */
/* ============================================================ */
create index filterIndexIdx on YazdFilter (filterIndex)
go

/* ============================================================ */
/*   Table: YazdForum                                           */
/* ============================================================ */
create table YazdForum
(
    forumID        integer                not null,
    name           varchar(255)           null    ,
    description    text                   null    ,
    modifiedDate   varchar(15)            null    ,
    creationDate   varchar(15)            null    ,
    moderated      integer                not null,
    constraint PK_YazdFORUM primary key (forumID)
)
go

/* ============================================================ */
/*   Table: YazdForumProp                                       */
/* ============================================================ */
create table YazdForumProp
(
    forumID        integer                not null,
    name           varchar(30)            not null,
    propValue      varchar(255)           not null
)
/////////////////////
go

/* ============================================================ */
/*   Table: YazdGroup                                           */
/* ============================================================ */
create table YazdGroup
(
    groupID        integer                not null,
    name           varchar(50)            not null,
    description    varchar(255)           null    ,
    constraint PK_YazdGROUP primary key (groupID)
)
go

/* ============================================================ */
/*   Table: YazdGroupPerm                                       */
/* ============================================================ */
create table YazdGroupPerm
(
    forumID        integer                not null,
    groupID        integer                not null,
    permission     integer                not null,
    constraint PK_YazdGROUPPERM primary key (forumID, groupID, permission)
)
go

/* ============================================================ */
/*   Index: groupGroupIdx                                       */
/* ============================================================ */
create index groupGroupIdx on YazdGroupPerm (groupID)
go

/* ============================================================ */
/*   Table: YazdGroupUser                                       */
/* ============================================================ */
create table YazdGroupUser
(
    groupID        integer                not null,
    userID         integer                not null,
    administrator  integer                not null,
    constraint PK_YazdGROUPUSER primary key (groupID, userID)
)
go

/* ============================================================ */
/*   Index: groupIdx                                            */
/* ============================================================ */
create index groupIdx on YazdGroupUser (userID)
go

/* ============================================================ */
/*   Table: YazdMessage                                         */
/* ============================================================ */
create table YazdMessage
(
    messageID      integer                not null,
    threadID       integer                default -1 null    ,
    subject        varchar(255)           null    ,
    userID         integer                not null,
    body           text                   null    ,
    modifiedDate   varchar(15)            not null    ,
    creationDate   varchar(15)            not null    ,
    approved       integer                not null,
    constraint PK_YazdMESSAGE primary key (messageID)
)
go

/* ============================================================ */
/*   Index: messageCreationDateIdx                              */
/* ============================================================ */
create index messageCreationDateIdx on YazdMessage (creationDate)
go

/* ============================================================ */
/*   Index: messageModifiedDateIdx                              */
/* ============================================================ */
create index messageModifiedDateIdx on YazdMessage (modifiedDate)
go

/* ============================================================ */
/*   Index: messageApprovedIdx                                  */
/* ============================================================ */
create index messageApprovedIdx on YazdMessage (approved)
go

/* ============================================================ */
/*   Index: messageThreadIDIdx                                  */
/* ============================================================ */
create index messageThreadIDIdx on YazdMessage (threadID)
go

/* ============================================================ */
/*   Index: messageUserIDIdx                                    */
/* ============================================================ */
create index messageUserIDIdx on YazdMessage (userID)
go

/* ============================================================ */
/*   Table: YazdMessageTree                                     */
/* ============================================================ */
create table YazdMessageTree
(
    parentID       integer                not null,
    childID        integer                not null,
    constraint PK_YazdMESSAGETREE primary key (parentID, childID)
)
go

/* ============================================================ */
/*   Index: childIdx                                            */
/* ============================================================ */
create index childIdx on YazdMessageTree (childID)
go

/* ============================================================ */
/*   Table: YazdMessageProp                                     */
/* ============================================================ */
create table YazdMessageProp
(
    messageID      integer                not null,
    name           varchar(50)            not null,
    propValue      varchar(255)           not null,
    constraint PK_YazdMESSAGEPROP primary key (messageID, name)
)
go


/* ============================================================ */
/*   Table: YazdThread                                          */
/* ============================================================ */
create table YazdThread
(
    threadID       integer                not null,
    forumID        integer                not null    ,
    rootMessageID  integer                not null,
    creationDate   varchar(15)            not null    ,
    modifiedDate   varchar(15)            not null    ,
    approved       integer                not null,
    constraint PK_YazdTHREAD primary key (threadID)
)
go

/* ============================================================ */
/*   Index: messageCreationDateIdx                              */
/* ============================================================ */
create index threadCreationDateIdx on YazdThread (creationDate)
go

/* ============================================================ */
/*   Index: messageModifiedDateIdx                              */
/* ============================================================ */
create index threadModifiedDateIdx on YazdThread (modifiedDate)
go

/* ============================================================ */
/*   Index: messageForumIDIdx                                   */
/* ============================================================ */
create index threadForumIDIdx on YazdThread (forumID)
go

/* ============================================================ */
/*   Table: YazdUser                                            */
/* ============================================================ */
create table YazdUser
(
    userID         integer                not null,
    name           varchar(50)            null    ,
    username       varchar(30)            not null,
    passwordHash   varchar(32)            not null,
    email          varchar(30)            not null,
    emailVisible   integer                not null,
    nameVisible    integer                not null,
    constraint PK_YazdUSER primary key (userID)
)
go

/* ============================================================ */
/*   Table: YazdUserPerm                                        */
/* ============================================================ */
create table YazdUserPerm
(
    forumID        integer                not null,
    userID         integer                not null,
    permission     integer                not null,
    constraint PK_YazdUSERPERM primary key (forumID, userID, permission)
)
go

/* ============================================================ */
/*   Index: userUserIdx                                         */
/* ============================================================ */
create index userUserIdx on YazdUserPerm (userID)
go

/* ============================================================ */
/*   Table: YazdUserProp                                        */
/* ============================================================ */
create table YazdUserProp
(
    userID         integer                not null,
    name           varchar(30)            not null,
    propValue      varchar(255)           not null,
    constraint PK_YazdUSERPROP primary key (userID, name)
)
go



