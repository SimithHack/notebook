# -*- coding:utf-8 -*-
from thrift import Thrift
from py.thrift import PersonService
from thrift.transport import TTransport
from thrift.transport import TSocket
from thrift.protocol import TCompactProtocol
from thrift.server import TServer
from PersonServiceImpl import PsersonServiceImpl

try:
    personHandler = PsersonServiceImpl()
    processor = PersonService.Processor(personHandler)
    ss = TSocket.TServerSocket(port=8800, host="localhost")
    transFactory = TTransport.TFramedTransportFactory()
    protocolFactory = TCompactProtocol.TCompactProtocolFactory()

    server = TServer.TSimpleServer(processor, ss, transFactory, protocolFactory)
    server.serve()
except Thrift.TException as e:
    pass