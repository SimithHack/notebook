package com.learn.netty.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClient {
    public static void main(String[] args) throws Exception {
        TTransport transport = new TFramedTransport(new TSocket("localhost", 8800), 600);
        TProtocol protocol = new TCompactProtocol(transport);
        PersonService.Client client = new PersonService.Client(protocol);
        try{
            transport.open();
            Person person = client.getPersonByUsername("王二小");
            System.out.println(person.toString());

            Person pp = new Person()
                    .setAge(56)
                    .setMarried(false)
                    .setUsername("李晓春");
            client.savePerson(pp);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage(), ex);
        }finally {
            transport.close() ;
        }
    }
}
