package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.mapper;

public interface CellMapper<T, U, K> {
    U map(T input, K key, String cellValue);
}