# JQnapAPI
A Simple wrapper for QNAP API.


#### Getting Started
To get started using JQnap API just initialize the core class of the Project
 
```java QNAPCore core = new QNAPCore(*ip*, *port*);```

#### Login
```java core.login(*username*,*password*);```

You can handle the return value of login in order to check if you are logged in and then remove unwanted error in your program

```java
if(core.login(*username*,*password*)){
   ...
}```

#### Logout
Be sure to logout when you finished all the operations
```java core.logout();```

#### Filesystem
####### Folder List
```java List<QNAPFolder> folderList = core.getFolderList(*path*);```


#[Under Develop]
