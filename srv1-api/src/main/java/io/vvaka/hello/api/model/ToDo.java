package io.vvaka.hello.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.immutables.value.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;


/**
 * Created by vvaka on 7/28/16.
 */


@Value.Immutable
@Value.Style(passAnnotations = {NotNull.class,Valid.class,JsonIgnoreProperties.class} , strictBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(as = ImmutableToDo.class)
@JsonDeserialize(as = ImmutableToDo.class)
@Value.Modifiable
public abstract class ToDo {

    public abstract Optional<String> id();

    @NotNull
    @Valid
    @NotEmpty
    @Email
    public abstract String name();


    public abstract Optional<Boolean> isCompleted();

}
