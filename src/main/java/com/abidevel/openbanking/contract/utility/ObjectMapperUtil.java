package com.abidevel.openbanking.contract.utility;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ObjectMapperUtil {
    private static ModelMapper modelMapper;
    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }


    public  <T, T1> T1  mapEntity (T entity, Class<T1> targetClass ) {
        return modelMapper.map(entity, targetClass);
    }

    public  <T, T1> List<T1>  mapAllEntity (Collection<T> entities, Class<T1> targetClass ) {
        return entities.stream()
            .map(e -> mapEntity(e, targetClass))
            .collect(Collectors.toList());
    }
}
