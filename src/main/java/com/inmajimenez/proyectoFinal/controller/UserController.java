package com.inmajimenez.proyectoFinal.controller;

import com.inmajimenez.proyectoFinal.model.request.LoginRequest;
import com.inmajimenez.proyectoFinal.model.request.SignupRequest;
import com.inmajimenez.proyectoFinal.model.response.Response;
import com.inmajimenez.proyectoFinal.model.response.LoginResponse;
import com.inmajimenez.proyectoFinal.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping()
//@CrossOrigin(origins = "http://localhost:4200", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@CrossOrigin(origins = "https://ijimenezfinal.vercel.app", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class UserController {

    UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @ApiOperation("Comprueba el usuario en bbdd")
    public ResponseEntity<LoginResponse> checkUser(@ApiParam("Objeto del usuario")
                              @RequestBody LoginRequest login) throws URISyntaxException {

        LoginResponse response = userService.checkLoginUser(login);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/register")
    @ApiOperation("Comprueba el usuario en bbdd")
    public Response createUser(@ApiParam("Objeto del usuario")
                                    @RequestBody SignupRequest signUpuser) throws URISyntaxException {

        Response response;

        if(!userService.checkEmailUser(signUpuser.getEmail())){

            if(userService.createUser(signUpuser)){
                response = new Response("El usuario se ha creado correctamente",
                        new ResponseEntity(HttpStatus.OK).getStatusCode());
            }else{
                response = new Response("Error al crear el usuario",
                        new ResponseEntity(HttpStatus.BAD_REQUEST).getStatusCode());
            }

        }else{
            response = new Response("Error: El email ya está registrado",
                    new ResponseEntity(HttpStatus.BAD_REQUEST).getStatusCode());
        }
        return response;
    }
}
