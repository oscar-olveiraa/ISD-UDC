# -----------Explicacion Iteración 2 ISD de ws-examples-----------
-------------------------------------------------------------------------------
 >## CLASES A TER EN CONTA:
 >
 > 1) RestClientMovieService implementanse cada Caso de uso.
 > 2) Interfaz do cliente que implementa cada caso de uso -> ClientMovieService.
 > 3) MovieServlet/SaleServlet son clases que cando o servidor recibe unha petición sobre unha URL, invoca ao servlet asociado. Ambas heredan da clase RestHttpServletTemplate que está en a ws.util e esta misma hereda da clase HttpServlet de Java que implementa os doGet, doPost...
 > 4) ServletUtil é unha clase de ws.util con métodos utilidad para implementar Servlets (writeServiceResponse,  normalizePath, getMandatoryParameter...)

## Cliente lanza un comando que está na clase MovieServiceClient (-a,-u,-f,-b,-g):

- Antes dos if-else para os flags:
     - Comprobamos que tamaño dos argumentos non sea 0.
     - Creamos unha instancia da interfaz ClientMovieService que é a que implementa a RestMovieService.

- En cada flag, validanse os argumentos -> **validateArgs(args, 6, new int[] {2, 3, 5});**(neste caso mirase que haba no comando 6 argumentos e que os argumentos 2,3,5 sean numéricos) 


### - Caso de uso 'añadir película' (-a):
- 1)Chamamos ao metodo *addMovie*  que está na interfaz do cliente e implementada en RestClientMovieService. A addMovie pasaselle por parámetro:
    + **new ClientMovieDto** e este ten como parámetro os args que se lle pasa ao o comando a executar menos o id que está o seu campo como null xa que generao a capa modelo.

- 2)En RestClientMovieService, miramos o método *addMovie(ClientMovieDto movie)*:
   
  - 2.1) Creamos unha resposta http (ClassicHttpResponse response), que genera unha petición POST -> **ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "movies").bodyStream(toInputStream(movie), ContentType.create("application/json")).execute().returnResponse();**
   
    Parámetros da creación da resposta:
    - **getEndpointAddress**(metodo propio que obtén a parte fixa da URL desde o ficheiro de configuración usando a clase ConfigurationParametersManager(clase que está na carpeta ws.util)) e aplicaselle o método *bodyStream* que ten como parámetros:
      
      - **toInputStream**(clase propia que convirte un obxeto ClientMovieDto á súa representación JSON e devolve un InputStream do que poder leer o JSON).
       
      - **un ContentType**(para indicar o tipo de MIME, no noso caso o tipo de medio de comunicación sería "application/json").
   
      - Ao bodyStream aplícaselle o método *execute* que envía a petición e devolve un obxeto response.

  - 2.2) Unha vez que temos a resposta que devolve o servlet, valídase que o codigo da resposta fora 201 (creado correctamente) cun método da clase RestClientMovieService -> **validateStatusCode(HttpStatus.SC_CREATED, response);**
 
  - 2.3) Devolvemos un JsonToClientMovieDtoConversor (fai conversións de ClientMovieDto a JSON (toObjectNode) ou coversións de ClientMovieDto desde JSON (toClientMovieDto)). ->**return JsonToClientMovieDtoConversor.toClientMovieDto(response. getEntity().getContent()).getMovieId();**

       + O **getEntity().getContent()** -> obtén o corpo da resposta

- 3)Java ve que o cliente fai unha petición tipo POST entonces chama ao método **processPost** da clase MovieServlet da da capa de Servicios:

    - 3.1) Miramos que a petición non estea vacía -> **ServletUtils.checkEmptyPath(req);**
 
    - 3.2) Creamos un 'movieDto' facendo un JsonToRestMovieDtoConversor (fai conversion de RestMovieDto a JSON (toObjectNode) ou conversións de RestMovieDto desde JSON (toRestMovieDto) -> **JsonToRestMovieDtoConversor.toRestMovieDto(req. getInputStream());**

      Parámetro da conversión:

         - **getInputStream** -> para leer o corpo da petición (faise nas peticións que teñen corpo com POST e PUT).

    - 3.3) Creamos un 'movie' facendo un MovieToRestMovieDtoConversor (fai conversións entre Movie e RestMovieDto) -> **MovieToRestMovieDtoConversor.toMovie(movieDto);**
 
    - 3.4) A ese 'movie' creámoslle unha instancia do método addMovie da capa Modelo para asignarlle un id -> **MovieServiceFactory.getService().addMovie(movie);**
 
    - 3.5) Collemos o 'movieDto' e convertimolo a RestMovieDto con ese 'movie' instanciado -> **MovieToRestMovieDtoConversor.toRestMovieDto(movie);**
 
    - 3.6) Creamos un string 'movieURL' que genera unha URL con normalizePath(recibe un String e devolve outro String igual ao recibido quitándolle o carácter ‘/’ en caso de que finalice con él) -> **ServletUtils.normalizePath (req.getRequestURL().toString()) + "/" + movie.getMovieId();**
 
    - 3.7) Creamos en un Map clave Location e asignamoslle o 'movieURL'. Unha ves o temos creamos a resposta HTTP -> **ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,	JsonToRestMovieDtoConversor.toObjectNode(movieDto), headers);**
 
        Parámetros:

         - **resp** -> objeto de tipo HttpServletResponse que se pasa por parámetro a **processPost**.
         - **HttpServletResponse.SC_CREATED** -> código a enviar na resposta.
         - **JsonToRestMovieDtoConversor.toObjectNode(movieDto)** ->  corpo a enviar na resposta, obxeto JsonNode de Jackson que debe ser o nodo raíz do árbol do JSON que se qere enviar
         - **headers** -> Un mapa cos nomes e valores de cabeceiras a añadir na resposta
     
     - 3.8) Creo que asi o servlet 'avisaría' na capa de acceso a servicios que ten unha resposta e chegaríalle ao cliente.

### - Caso de uso 'eliminar película' (-r):

- 1)Chamamos ao metodo *removeMovie*  que está na interfaz do cliente e implementada en RestClientMovieService. A removeMovie pasaselle por parámetro:
    + **Long.parseLong(args[1])** -> pasaselle a posición 1 da cadena do comando (movieId). A posición 0 é o '-r'

- 2)En RestClientMovieService, miramos o método *removeMovie(Long movieId)*:

    - 2.1) Creamos unha resposta http (ClassicHttpResponse response), que genera unha petición DELETE -> **ClassicHttpResponse response = (ClassicHttpResponse) Request.delete(getEndpointAddress() + "movies/" + movieId).execute().returnResponse();**
 
         Parámetros da creación da resposta:
         - **getEndpointAddress**(metodo propio que obtén a parte fixa da URL desde o ficheiro de configuración usando a clase ConfigurationParametersManager(clase que está na carpeta ws.util)). En esta petición non hai bodyStream porque non ten corpo esta petición, pasase todos os parámetros pola URL.
     
         - Aplicamos o método *execute* que envía a petición e devolve un obxeto response.

    - 2.2) Unha vez que temos a resposta que devolve o servlet, valídase que o codigo da resposta fora 204 (servidor procesou con éxito a solicitude, pero non devolve contido) cun método da clase RestClientMovieService -> **validateStatusCode(HttpStatus.SC_NO_CONTENT, response);**
 
    - 2.3) Non se convirte de JSON a ClientMovieDto xa que non xe generou ningún JSON no corpo da resposta


- 3)Java ve que o cliente fai unha petición tipo DELETE entonces chama ao método **processDelete** da clase MovieServlet da capa de Servicios:

   - 3.1) Obtemos o id do path -> **ServletUtils.getIdFromPath(req, "movie");**(pasaselle un string movie para construir mensajes de error, mirar en código)
 
   - 3.2) Creamos unha instancia do método removeMovie da capa modelo para eliminar ese id -> **MovieServiceFactory.getService(). removeMovie(movieId);**
 
   - 3.3) Si non existe ninguha película con ese id, creamos unha resposta con código 403 (recibeu a petición pero rechaza enviar unha resposta) ca excepción de non poder eliminar esa película -> **ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,MoviesExceptionToJsonConversor.toMovieNotRemovableException(ex),null);**

       Si existe unha película con ese id, creamos unha resposta con código 402, onde os parámetros de rootNode(para JSON) e headers(para añadir parámetros ao corpo ou URL) son null -> **ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);**














      
  
