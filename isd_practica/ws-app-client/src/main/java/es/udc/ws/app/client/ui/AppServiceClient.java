package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.ClientPartidoServiceFactory;
import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientInvalidMatchDateException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughTicketsException;
import es.udc.ws.app.client.service.exceptions.ClientTicketAlreadyGivenException;
import es.udc.ws.app.client.service.exceptions.ClientWrongCreditCardException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {
        // TODO

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientPartidoService clientPartidoService =
                ClientPartidoServiceFactory.getService();

        if("-add".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[] {3, 4});

            // [add] PartidaServiceClient -add <nombre> <fechaPartido> <precio> <maxEntradas>

            try {
                LocalDateTime strLocalDateTime = LocalDateTime.parse(args[2]);
                Long idPartido = clientPartidoService.addPartido(new ClientPartidoDto(null,
                        args[1], strLocalDateTime, Float.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[4])));

                System.out.println("Partido " + idPartido + " creado correctamente");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-buy".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[] {4});

            // [buy] PartidoServiceClient -buy <idPartido> <email> <tarjeta> <cantidad>

            Long idCompra;
            try {
                idCompra = clientPartidoService.buyTicket(Long.parseLong(args[1]),
                        args[2], args[3], Integer.valueOf(args[4]));

               /* System.out.println("En el partido " + args[1] +
                        " ha comprado correctamente las entradas con id " +
                        idCompra);*/
                System.out.println("Ha realizado correctamente la compra de entradas con id " + idCompra +
                        " para el partido con id " + args[1]);

            } catch (NumberFormatException | InstanceNotFoundException |
                    InputValidationException | ClientInvalidMatchDateException | ClientNotEnoughTicketsException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-findPurchase".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [findPurchase]   PartidoServiceClient -findPurchase <email>

            try {
                List<ClientCompraDto> compras = clientPartidoService.findPurchase(args[1]);
                System.out.println("Encontradas " + compras.size() +
                        " compra(s) con email '" + args[1] + "'");
                for (int i = 0; i < compras.size(); i++) {
                    ClientCompraDto compraDto = compras.get(i);
                    System.out.println("Id: " + compraDto.getIdCompra() +
                            ", Email: " + compraDto.getEmail() +
                            ", Tarjeta: " + compraDto.getTarjeta() +
                            ", Cantidad: " + compraDto.getCantidad() +
                            ", Fecha: " + compraDto.getFechaCompra() +
                            ", Recogidas: " + compraDto.isRecogida() +
                            ", Partido: " + compraDto.getIdPartido());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }else if("-findByDate".equalsIgnoreCase(args[0])) {
            validateArgs(args,2,new int[]{});

            // [findByDate]   PartidoServiceClient -findByDate <fecha>

            try {
                LocalDateTime strLocalDateTime = LocalDateTime.parse(args[1]);

                List<ClientPartidoDto> partidos = clientPartidoService.findByDate(strLocalDateTime);
                System.out.println("Encontrados " + partidos.size() +
                        " compra(s) con fecha anterior a '" + args[1] + "'");
                for (int i = 0; i < partidos.size(); i++) {
                    ClientPartidoDto partidoDto = partidos.get(i);
                    System.out.println("Id: " + partidoDto.getIdPartido() +
                            ", Nombre: " + partidoDto.getNombre() +
                            ", Fecha: " + partidoDto.getFechaPartido().toString() +
                            ", Precio: " + partidoDto.getPrecio() +
                            ", Entradas Totales: " + partidoDto.getMaxEntradas() +
                            ", Entradas Disponibles: " + partidoDto.getEntradasDisponibles());
                }
            }catch (InputValidationException ex){
                ex.printStackTrace(System.err);
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }

        }  else if("-findById".equalsIgnoreCase(args[0])){
            validateArgs(args,2,new int[]{1});

            // [findById]   PartidoServiceClient -findById <idPartido>

            try{
                ClientPartidoDto partido = clientPartidoService.findById(Long.parseLong(args[1]));
                clientPartidoService.findById(Long.parseLong(args[1]));

                System.out.println("Partido econtrado con id " + args[1] + ":");

                System.out.println("Id: " + partido.getIdPartido() +
                        ", Nombre: " + partido.getNombre() +
                        ", Fecha: " + partido.getFechaPartido().toString() +
                        ", Precio: " + partido.getPrecio() +
                        ", Entradas Totales: " + partido.getMaxEntradas() +
                        ", Entradas Disponibles: " + partido.getEntradasDisponibles());
            }catch (InstanceNotFoundException ex){
                ex.printStackTrace(System.err);
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }

        } else if("-pickup".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {});

            // [pickup]   PartidoServiceClient -pickup <idPartido> <tarjeta>

            try {
                clientPartidoService.pickupTicket(Long.parseLong(args[1]),args[2]);

                System.out.println("Entradas para la compra " + args[1] + " recogidas sin problemas");

            } catch (NumberFormatException | InputValidationException |
                     InstanceNotFoundException | ClientTicketAlreadyGivenException | ClientWrongCreditCardException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [add]    PartidoServiceClient -add <nombre> <fechaPartido> <precio> <maxEntradas>\n" +
                "    [buy]    PartidoServiceClient -buy <idPartido> <email> <tarjeta> <cantidad>\n" +
                "    [findPurchase]   PartidoServiceClient -findPurchase <email>\n" +
                "    [pickup]   PartidoServiceClient -pickup <idCompra> <tarjeta>\n" +
                "    [findByDate]   PartidoServiceClient -findByDate <fecha>\n" +
                "    [findById]   PartidoServiceClient -findById <idPartido>\n");

    }
}