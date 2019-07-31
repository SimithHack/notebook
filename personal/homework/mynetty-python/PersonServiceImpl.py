from py.thrift import ttypes
from py.thrift import PersonService


class PsersonServiceImpl(PersonService.Iface):

    def getPersonByUsername(self, username):
        p = ttypes.Person()
        p.married = True
        p.username = "王连成"
        p.age = 30
        return p

    def savePerson(self, p):
        print(p)


