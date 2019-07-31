package com.learn.netty.protobuf.singletype;

public class TestProtobuf {
    public static void main(String[] args) throws Exception {
        DataInfo.Student stu = DataInfo.Student.newBuilder()
                .setName("张三")
                .setAddress("成都")
                .setAge(12)
                .build();
        byte[] studentByte = stu.toByteArray();
        DataInfo.Student stuCopy = DataInfo.Student.parseFrom(studentByte);
        System.out.println(String.format("name = %s, age = %s, address = %s",
                stuCopy.getName(),
                stuCopy.getAge(),
                stuCopy.getAddress()));
    }
}
