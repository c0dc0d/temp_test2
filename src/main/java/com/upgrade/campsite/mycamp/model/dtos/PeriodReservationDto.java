package com.upgrade.campsite.mycamp.model.dtos;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodReservationDto implements Serializable {

    private LocalDate beginDate;
    private LocalDate finalDate;

}
