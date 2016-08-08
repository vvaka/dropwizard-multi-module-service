package io.vvaka.hello.resources;

import com.github.kristofa.brave.Brave;
import io.vvaka.hello.api.TodoResource;
import realdoc.api.model.ImmutableToDo;
import io.vvaka.hello.api.model.ToDo;
import io.vvaka.hello.client.ToDoResourceClient;
import io.vvaka.hello.core.Person;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by vvaka on 7/28/16.
 */
public class ToDoResourceImpl implements TodoResource {

    List<ToDo> todos = new ArrayList<>();

    Brave brave;
    final Random random = new Random();


    public ToDoResourceImpl() {
    }

    @Inject
    public ToDoResourceImpl(Brave brave) {
        this.brave = brave;

        IntStream.range(1, 10).forEach(i -> {

                    todos.add(ImmutableToDo.builder().name(
                            "Created Task " + i).isCompleted(false)
                            .id(UUID.randomUUID().toString())
                            .build());
                }
        );

    }


    @Override
    public List<ToDo> getAllToDo() {

        TodoResource toDoResourceClient = ToDoResourceClient.connect(brave, "http://localhost:19000");

        final List<ToDo> list = new ArrayList<>();
        todos.forEach(toDo -> {
                    list.add(toDoResourceClient.getById(toDo.id().get()));
                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        return list;
    }

    @Override
    public ToDo getById(String id) {

        Optional<ToDo> toDoOptional = todos.stream().filter(t -> id.equalsIgnoreCase(t.id().get())).findFirst();
        TodoResource toDoResourceClient = ToDoResourceClient.connect(brave, "http://localhost:19000");

        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, String> response = toDoResourceClient.hello(new Person().givenName);

        ToDo t = toDoOptional.orElseThrow(() -> new RuntimeException("not found"));

        return ImmutableToDo.copyOf(t).withName(String.join(", ", t.name(), response.get("name")));
    }

    @Override
    public Response create(ToDo toDo) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ToDo todoCreated = ImmutableToDo.copyOf(toDo).withId(UUID.randomUUID().toString());
        todos.add(todoCreated);
        return Response.created(UriBuilder.fromResource(TodoResource.class).path("/{id}").build(todoCreated.id().get())).build();
    }

    @Override
    public Response createOrUpdate(String id, ToDo toDo) {

        Optional<ToDo> toDoOptional = todos.stream().filter(f -> f.id().equals(id)).findFirst();

        ToDo todoCreated = null;
        if (toDoOptional.isPresent()) {

            todoCreated = ImmutableToDo.copyOf(toDoOptional.get()).withName(toDo.name())
                    .withIsCompleted(toDo.isCompleted());
            todos.remove(toDoOptional.get());

        } else {
            todoCreated = ImmutableToDo.copyOf(toDo).withId(String.valueOf(new Random().nextInt()));
        }

        todos.add(todoCreated);
        return Response.created(UriBuilder.fromResource(TodoResource.class).path("/{id}").build(todoCreated.id().get())).build();
    }

    @Override
    public Response partialUpdate(String id, ToDo toDo) {

        ToDo toDo1 = getById(id);

        ToDo updated = ImmutableToDo.copyOf(toDo1).withName(toDo.name());

        todos.remove(toDo1);
        todos.add(updated);
        return Response.ok().build();
    }

    @Override
    public Response delete(String id) {

        boolean removed = todos.removeIf(f -> f.id().equals(id));

        return Response.ok().build();
    }

    @Override
    public Map<String, String> hello(String name) {

        return new HashMap<String, String>() {{
            put("name", String.format("Hello %s !!!!", name));
        }};
    }
}
