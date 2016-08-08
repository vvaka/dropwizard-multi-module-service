package io.vvaka.hello.client;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.EmptySpanCollectorMetricsHandler;
import com.github.kristofa.brave.http.HttpSpanCollector;
import io.vvaka.hello.api.TodoResource;
import io.vvaka.hello.api.model.ToDo;

import java.util.List;

/**
 * Created by vvaka on 8/1/16.
 */
public class Srv1Client {

    public static void main(String[] args) {
        final Brave brave = new Brave.Builder("namaste")
                .spanCollector(HttpSpanCollector.create("http://localhost:9411", new EmptySpanCollectorMetricsHandler()))
                .build();

        TodoResource toDoResourceClient = ToDoResourceClient.connect(brave, "http://localhost:19000");


        /*
        ToDo toDo = toDoResourceClient.getById(toDoList.get(0).id().get());
        System.out.println(toDo);

        Response r2 = toDoResourceClient.partialUpdate(toDoList.get(0).id().get(), ImmutableToDo.builder()
                .name("patched Name").build());

        ToDo toDo2 = toDoResourceClient.getById(toDoList.get(0).id().get());
        System.out.println(toDo2);*/


        List<ToDo> toDoList = toDoResourceClient.getAllToDo();

        toDoList.stream().forEach(System.out::println);

        //System.exit(1);

    }

}
