package com.coherent.common.models;

import java.io.Serializable;

public enum OrderStatus implements Serializable
{
    //Status order: from ISSUED -> CONFIRMED -> COMPLETED
    ISSUED,
    CONFIRMED,
    COMPLETED
}
