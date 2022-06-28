package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Required;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class Review {

    private Integer id;

    @NotNull(message = "Content may not be null")
    private String content;

    @NotNull
    private Boolean isPositive;
    private Integer userId;
    private Integer filmId;
    private int useful;

    public Integer getId() {
        return id;
    }

    @JsonProperty("isPositive")
    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }
}
