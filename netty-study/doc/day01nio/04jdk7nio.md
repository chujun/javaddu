jdk7引入了一些API
# Path && Paths
Path:文件路径
Paths:工具类,用来获取Path


#Files
文件是否存在
```
Path path=Paths.get("/tmp/log");
Files.exists(path,StandardCopyOption.REPLACE_EXISTING);
```

拷贝文件
```
Path from=Paths.get("/tmp/log/a");
Path to=Paths.get("/tmp/log/a");
Files.copy(from,to);
```
