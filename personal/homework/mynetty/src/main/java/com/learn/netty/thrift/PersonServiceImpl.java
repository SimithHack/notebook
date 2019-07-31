package com.learn.netty.thrift;

import org.apache.thrift.TException;

public class PersonServiceImpl implements PersonService.Iface{
    @Override
    public Person getPersonByUsername(String username) throws DataException, TException {
        Person person = new Person();
        person.setAge(12)
                .setMarried(true)
                .setUsername(username);
        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {
        System.out.println(person.toString());
    }
}
