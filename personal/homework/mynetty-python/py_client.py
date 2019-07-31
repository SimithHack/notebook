# -*- coding:utf-8 -*-
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import  TTransport
from thrift.protocol import TCompactProtocol
from py.thrift import PersonService
from py.thrift import ttypes


try:
    socket = TSocket.TSocket("localhost", 8800)
    socket.setTimeout(600)

    transport = TTransport.TFramedTransport(socket)
    protocol = TCompactProtocol.TCompactProtocol(transport)
    client = PersonService.Client(protocol)

    transport.open()
    person = client.getPersonByUsername("王国强")
    print(person)

    lisi = ttypes.Person()
    lisi.username = "李四"
    lisi.age = 32
    lisi.married = True

    client.savePerson(lisi)

    transport.close()

except Thrift.TException as e:
    pass