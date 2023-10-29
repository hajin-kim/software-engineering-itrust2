package edu.ncsu.csc.itrust2.models;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class FoodDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "username", columnDefinition = "varchar(20)")
    private Patient patient;

    String date;
    String mealType;
    String foodName;
    String servingsNum;
    String caloriesPerServing;
    String fatGramsPerServing;
    String sodiumMilligramsPerServing;
    String carbsGramsPerServing;
    String sugarsGramsPerServing;
    String fiberGramsPerServing;
    String proteinGramsPerServing;
    String fatGramsTotal;
    String sodiumMilligramsTotal;
    String carbsGramsTotal;
    String sugarsGramsTotal;
    String fiberGramsTotal;
    String proteinGramsTotal;
}
