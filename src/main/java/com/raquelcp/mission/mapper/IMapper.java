package com.raquelcp.mission.mapper;

public interface IMapper<I, O> {
    public O map(I in);
}
