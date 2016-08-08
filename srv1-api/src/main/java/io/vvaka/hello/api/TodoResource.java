package io.vvaka.hello.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.jaxrs.PATCH;
import io.vvaka.hello.api.model.ToDo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by vvaka on 7/28/16.
 */

@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/todos", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
public interface TodoResource {

    @ApiOperation(value = "Get all Todos")
    @GET
    List<ToDo> getAllToDo();

    @GET
    @Path("/{id}")
    ToDo getById(@PathParam("id") String id);


    @POST
    Response create(ToDo toDo);

    @PUT
    @Path("/{id}")
    Response createOrUpdate(@PathParam("id") String id, ToDo toDo);


    @PATCH
    @Path("/{id}")
    Response partialUpdate(@PathParam("id") String id, ToDo toDo);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") String id);

    @GET
    @Path("/hello/{name}")
    Map<String,String> hello(@PathParam("name") String name);


}
