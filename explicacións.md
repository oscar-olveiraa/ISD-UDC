# -----------Explicacion Iteración 2 ISD de ws-examples-----------
-------------------------------------------------------------------------------
 >## CLASES A TER EN CONTA:
 >
 > 1) RestClientMovieService implementanse cada Caso de uso.
 > 2) Interfaz do cliente que implementa cada caso de uso -> ClientMovieService.

## Cliente lanza un comando que está na clase MovieServiceClient (-a,-u,-f,-b,-g)

### - Caso de uso 'añadir película' (-a):
- 1)Chamamos ao metodo *addMovie*  que está na interfaz do cliente e implementada en RestClientMovieService. A addMovie pasaselle por parámetro:
    + **new ClientMovieDto** e este ten como parámetro os args que se lle pasa ao executar o comando menos o id que está o seu campo como null xa que generao a capa modelo.

- 2)En RestClientMovieService, miramos o método *addMovie(ClientMovieDto movie)*:
   
  - 2.1) **ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "movies").bodyStream(toInputStream(movie), ContentType.create("application/json")).execute().returnResponse();**
   
       Créase unha resposta http (ClassicHttpResponse response), que genera unha petición post, pasandolle a este por parámetro:
    - **getEndpointAddress**(metodo propio que obtén a parte fixa da URL desde o ficheiro de configuración usando a clase ConfigurationParametersManager(clase que está na carpeta ws.util)) e aplicaselle o método *bodyStream* que ten como parámetros:
      
      - **toInputStream**(clase propia que convirte un obxeto ClientMovieDto á súa representación JSON e devolve un InputStream do que poderleer o JSON).
       
      - **un ContentType**(para indicar 0 tipo de MIME, no noso caso o tipo de medio de comunicación sería "application/json").
   
      - Ao bodyStream aplícaselle o método *execute* que envía a petición e devolve un obxeto response.

  - 2.2) Valídase que o codigo da peticion fora 201 (creado correctamente) cun método da clase RestClientMovieService -> **validateStatusCode(HttpStatus.SC_CREATED, response);**
 
  - 2.3) Devolvemos un JsonToClientMovieDtoConversor (fai conversións de ClientMovieDto a JSON (toObjectNode) ou coversións de ClientMovieDto desde JSON (toClientMovieDto)).

- 3)Java ve que o cliente fai unha petición tipo ost entonces chama ao método **ProcessPost** da clase MovieServlet da da capa de Servicios:

    - 3.1) Miramos que a petición non estea vacía -> **ServletUtils.checkEmptyPath(req).**
  
