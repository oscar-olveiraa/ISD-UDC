namespace java es.udc.ws.app.thrift

struct ThriftPartidoDto {
    1: i64 idPartido
    2: string nombre
    3: string fechaPartido
    4: double precio
    5: i32 maxEntradas
    6: i32 entradasVendidas
}

struct ThriftCompraDto{
    1: i64 idCompra
    2: string email
    3: string tarjeta
    4: i32 cantidad
    5: string fechaCompra
    6: i64 idPartido
    7: bool recogida
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftInvalidMatchDateException{
    1: i64 idPartido
    2: string fechaPartido
}

exception ThriftNotEnoughTicketsException{
    1: i64 idPartido
    2: i32 cantidad
}

exception ThriftTicketAlreadyGivenException{
    1: i64 idCompra
}

exception ThriftWrongCreditCardException{
    1: i64 idCompra
}


service ThriftPartidoService {
    ThriftPartidoDto addPartido(1: ThriftPartidoDto partidoDto) throws (1: ThriftInputValidationException e)
    list<ThriftPartidoDto> findByDate(1: string fecha) throws (1: ThriftInputValidationException e)
    ThriftPartidoDto findById(1: i64 idPartido) throws (1: ThriftInstanceNotFoundException e)
    ThriftCompraDto buyTicket( 1: i64 idCompra, 2: string email, 3: string tarjeta, 4: i32 cantidad) throws (1: ThriftInvalidMatchDateException e1,
                                                                                          2:ThriftInstanceNotFoundException e2, 3:ThriftInputValidationException e3, 4:ThriftNotEnoughTicketsException e4)
    list<ThriftCompraDto> findPurchase(1: string email) throws (1: ThriftInputValidationException e)
    void pickupTicket(1: i64 idCompra, 2: string tarjeta) throws (1: ThriftTicketAlreadyGivenException e1,
                                                                             2: ThriftWrongCreditCardException e2,
                                                                             3: ThriftInputValidationException e3,
                                                                             4: ThriftInstanceNotFoundException e4)
}