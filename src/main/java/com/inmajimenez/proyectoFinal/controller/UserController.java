package com.inmajimenez.proyectoFinal.controller;

import com.inmajimenez.proyectoFinal.model.Response;
import com.inmajimenez.proyectoFinal.model.ResponseLoggin;
import com.inmajimenez.proyectoFinal.model.entities.User;
import com.inmajimenez.proyectoFinal.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class UserController {

    UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/user")
    @CrossOrigin(origins = "http://localhost:4200")
    @ApiOperation("Comprueba el usuario en bbdd")
    public ResponseLoggin checkUser(@ApiParam("Objeto del usuario")
                              @RequestBody User user) throws URISyntaxException {


        ResponseLoggin response = new ResponseLoggin();

        User userBBDD = userService.findOneUserLoggin(user);

        if(userBBDD !=null){
           response.setNameUser(userBBDD.getName());
           response.setEmailUser(userBBDD.getEmail());
           response.setResponse(new Response("El usuario se ha logueado correctamente",
                   new ResponseEntity(HttpStatus.OK).getStatusCode()));
        }else{
            response.setResponse(new Response("Error al loguear el usuario",
                    new ResponseEntity(HttpStatus.BAD_REQUEST).getStatusCode()));
        }

        return response;
    }

}
