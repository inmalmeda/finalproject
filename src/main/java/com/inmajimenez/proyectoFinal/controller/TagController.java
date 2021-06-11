package com.inmajimenez.proyectoFinal.controller;

import com.inmajimenez.proyectoFinal.model.response.Response;
import com.inmajimenez.proyectoFinal.model.response.TagResponseGetAll;
import com.inmajimenez.proyectoFinal.model.response.TagResponseGetOne;
import com.inmajimenez.proyectoFinal.model.entities.Tag;
import com.inmajimenez.proyectoFinal.model.request.TagFilters;
import com.inmajimenez.proyectoFinal.service.TagService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.net.URISyntaxException;


/**
 * Rest controller of tags
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
//@CrossOrigin(origins = "https://ijimenezfinal.vercel.app", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    /**
     * It returns all tags
     * @return Response with list of tags
     */
    @GetMapping("/etiquetas")
    @ApiOperation("Encuentra todas las etiquetas con filtro y paginación")
    public TagResponseGetAll findAll(@QueryParam("name") String name, @QueryParam("page") String page,
                                             @QueryParam("limit") String limit){

        TagResponseGetAll response = tagService.findAllTags(new TagFilters(name, page, limit));

        if(!response.getTags().isEmpty()){
            response.setResponse(new Response("Etiquetas encontradas",
                                        new ResponseEntity(HttpStatus.OK).getStatusCode()));
        }else{
            response.setResponse(new Response("No hubo resultados en la búsqueda",
                                        new ResponseEntity(HttpStatus.NOT_FOUND).getStatusCode()));
        }
        return response;
    }

    /**
     * It returns a tag by id
     * @param id Long id of tag
     * @return Response with tag from database
     */
    @GetMapping("/etiquetas/{id}")
    @ApiOperation("Encuentra una etiqueta por su id")
    public TagResponseGetOne findOne(@ApiParam("Clave primaria de la etiqueta")
                                       @PathVariable Long id) {


        TagResponseGetOne response = new TagResponseGetOne();

        response.setTag(tagService.findOneTagById(id));

        if(response.getTag() != null){
            response.setResponse(new Response("Etiqueta encontrada con id: " + id,
                    new ResponseEntity(HttpStatus.OK).getStatusCode()));
        }else{
            response.setResponse(new Response("No hubo resultados en la búsqueda",
                    new ResponseEntity(HttpStatus.NOT_FOUND).getStatusCode()));
        }

        return response;
    }

    /**
     * It saves a tag and returns the tag created with id
     * @param tag New tag
     * @return Tag created
     */
    @PostMapping("/etiquetas")
    @ApiOperation("Guarda en base de datos una etiqueta nueva")
    public Response createTag(@ApiParam("Objeto tag nueva")
                                         @RequestBody Tag tag) throws URISyntaxException {

        Response response;

        if(tagService.createTag(tag) != null){
            response = new Response("La etiqueta se ha creado correctamente",
                    new ResponseEntity(HttpStatus.OK).getStatusCode());
        }else{
            response = new Response("Error al crear la etiqueta",
                    new ResponseEntity(HttpStatus.BAD_REQUEST).getStatusCode());
        }
        return response;
    }

    /**
     * It updates a tag
     * @param tag Tag to update
     * @return Response of update
     */
    @PutMapping("/etiquetas")
    @ApiOperation("Actualiza en base de datos una etiqueta")
    public Response updateTag(@ApiParam("Etiqueta con datos actualizados")
                                             @RequestBody Tag tag) {
        Response response;

        if(tagService.updateTag(tag)!=null){
            response = new Response("La etiqueta se ha actualizado correctamente",
                    new ResponseEntity(HttpStatus.OK).getStatusCode());
        }else{
            response = new Response("Error al actualizar la etiqueta",
                    new ResponseEntity(HttpStatus.BAD_REQUEST).getStatusCode());
        }

        return response;
    }

    /**
     * It deletes one tag by id
     * @param id id of tag
     * @return Response of delete
     */
    @DeleteMapping("/etiquetas/{id}")
    @ApiOperation("Borra de base de datos una etiqueta según su id")
    public Response deleteTag(@ApiParam("Id de la etiqueta")
                                               @PathVariable Long id) {

        Response response;

        if(id!=null){

            if(tagService.deleteTagById(id)){
                response = new Response("La etiqueta se ha borrado correctamente",
                                                    new ResponseEntity(HttpStatus.OK).getStatusCode());
            }else{
                response = new Response("Error al eliminar la etiqueta",
                                                    new ResponseEntity(HttpStatus.NOT_FOUND).getStatusCode());
            }

        }else{
            response = new Response("El id enviado es nulo",
                                                    new ResponseEntity(HttpStatus.BAD_REQUEST).getStatusCode());
        }

        return response;
    }
}
