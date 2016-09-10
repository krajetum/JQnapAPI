# JQnapAPI
A Simple wrapper for QNAP API.


### Getting Started
To get started using JQnap API just initialize the core class of the Project
 
```java QNAPCore core = new QNAPCore(String ip, int port);```

### Login
```java core.login(String user, String pass);```

You can handle the return value of login in order to check if you are logged in and then remove unwanted error in your program

```java if(core.login(String user, String pass)){
   ...
}```

### Logout
Be sure to logout when you finished all the operations
```java core.logout();```

### Filesystem
###### **NOTE: The QNAPFile and QNAPFolder size parameter is String**

###### Folder List
```java List<QNAPFolder> folderList = core.getFolderList(String path);```
###### File and Folder list
This is a little bit tricky, it return a QNAPFile and inside this class is defined a parameter isFolder that identifies if is a folder or not
```java List<QNAPFile> fileList = getFileList(String path)```

###### File and Folder size
```java String getFileSize(String path, String filename)```




#### The wrapper still under develop. 
