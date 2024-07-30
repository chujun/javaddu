#FileChannel
只能工作在阻塞模式下

## 获取
* 由FileInputStream获取,FileChannel只能读
* 由FileOutputStream获取,FileChannel只能写
* 由RandomAccessFile根据读写模式决定FileChannel读写方式

## 读取数据
```
int readBytes=channel.read(byteBuffer);
```

## 写入数据
写入正确知识
```
ByteBuffer byte=...;
buffer.put(..);
buffer.flip();//切换读模式

while(buffer.hasRemaining()){
    channel.write(buffer);
}

```
注意:channel.write方法不能保证一次讲buffer数据全部写入channel
==>TODO:cj 如何证明这一点呢?

## 强制写入
force(true)立刻写入磁盘


#两个文件相互传输
```
channel.transferTo(position,count,
```
//传输效率高,jdk带有了transferTo关键字底层操作系统都使用了零拷贝，
//transferTo每次文件传输上限:2G数据