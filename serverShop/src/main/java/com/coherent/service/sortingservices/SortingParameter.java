package com.coherent.service.sortingservices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SortingParameter
{
    private String parameterField;
    private String strategy;
}
