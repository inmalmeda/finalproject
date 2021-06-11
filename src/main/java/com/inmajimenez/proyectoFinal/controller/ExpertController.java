package com.inmajimenez.proyectoFinal.controller;

import com.inmajimenez.proyectoFinal.model.request.ExpertFilters;
import com.inmajimenez.proyectoFinal.model.response.ExpertResponseGetAll;
import com.inmajimenez.proyectoFinal.model.response.ExpertResponseGetOne;
import com.inmajimenez.proyectoFinal.model.response.Response;
import com.inmajimenez.proyectoFinal.model.entities.Expert;
import com.inmajimenez.proyectoFinal.service.ExpertService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.net.URISyntaxException;

/**
 * Rest controller of experts
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
//@CrossOrigin(origins = "https://ijimenezfinal.vercel.app", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class ExpertController {

    private final ExpertService expertService;

    public ExpertController(ExpertService expertService){
        this.expertService = expertService;
    }

    /**
     * It returns all experts
     * @return Response with list of experts
     */
    @GetMapping("/expertos")
    @ApiOperation("Encuentra todos los expertos con filtro y paginación")
    public ExpertResponseGetAll findAll(@QueryParam("name") String name, @QueryParam("mode") String mode,
                                                @QueryParam("state") String state,
                                                @QueryParam("score") Integer score,
                                                @QueryParam("tag") String tag,
                                                @QueryParam("page") String page, @QueryParam("limit") String limit){


        ExpertResponseGetAll response = expertService.findAllExperts(new ExpertFilters(name, mode, state,score, tag, page, limit));

        if(!response.getExperts().isEmpty()){
            response.setResponse(new Response("Expertos encontrados",
                    new ResponseEntity(HttpStatus.OK).getStatusCode()));
        }else{
            response.setResponse(new Response("No hubo resultados en la búsqueda",
                    new ResponseEntity(HttpStatus.NOT_FOUND).getStatusCode()));
        }

        return response;
    }

    /**
     * It returns an expert by id
     * @param id Long id of expert
     * @return Response with an expert from database
     */
    @GetMapping("/expertos/{id}")
    @ApiOperation("Encuentra un experto por su id")
    public ExpertResponseGetOne findOne(@ApiParam("Clave primaria del experto")
                                        @PathVariable Long id) {

        ExpertResponseGetOne response = new ExpertResponseGetOne();

        response.setExpert(expertService.findOneExpertById(id));

        if(response.getExpert() != null){
            response.setResponse(new Response("Experto encontrado con id: " + id,
                    new ResponseEntity(HttpStatus.OK).getStatusCode()));
        }else{
            response.setResponse(new Response("No hubo resultados en la búsqueda",
                    new ResponseEntity(HttpStatus.NOT_FOUND).getStatusCode()));
        }

        return response;
    }

    /**
     * It saves an expert and returns the expert created with id
     * @param expert New expert
     * @return Expert created
     */
    @PostMapping("/expertos")
    @ApiOperation("Guarda en base de datos un experto nuevo")
    public Response createExpert(@ApiParam("Objeto experto nuevo")
                                           @RequestBody Expert expert) throws URISyntaxException {

        Response response;

        if(expertService.createExpert(expert) != null){
            response = new Response("El experto se ha creado correctamente",
                    new ResponseEntity(HttpStatus.OK).getStatusCode());
        }else{
            response = new Response("Error al crear el experto",
                    new ResponseEntity(HttpStatus.BAD_REQUEST).getStatusCode());
        }

        return response;
    }

    /**
     * It updates an expert
     * @param expert Expert to update
     * @return Response of update expert
     */
    @PutMapping("/expertos")
    @ApiOperation("Actualiza en base de datos un experto existente")
    public Response updateExpert(@ApiParam("Información del experto")
                                           @RequestBody Expert expert) {

        Response response;

        if(expertService.updateExpert(expert) != null){
            response = new Response("El experto se ha actualizado correctamente",
                    new ResponseEntity(HttpStatus.OK).getStatusCode());
        }else{
            response = new Response("Error al actualizar el experto",
                    new ResponseEntity(HttpStatus.BAD_REQUEST).getStatusCode());
        }

        return response;
    }

    /**
     * It deletes one expert by id
     * @param id id of expert
     * @return Response of delete
     */
    @DeleteMapping("/expertos/{id}")
    @ApiOperation("Borra de base de datos un experto según su id")
    public Response deleteExpert(@ApiParam("Id del experto")
                                               @PathVariable Long id) {
        Response response;

        if(id!=null) {
            if (expertService.deleteExpertById(id)) {
                response = new Response("El experto se ha borrado correctamente",
                        new ResponseEntity(HttpStatus.OK).getStatusCode());
            } else {
                response = new Response("Error al eliminar al experto",
                        new ResponseEntity(HttpStatus.NOT_FOUND).getStatusCode());
            }
        }else{
            response = new Response("El id enviado es nulo",
                    new ResponseEntity(HttpStatus.BAD_REQUEST).getStatusCode());
        }

        return response;

    }

}
