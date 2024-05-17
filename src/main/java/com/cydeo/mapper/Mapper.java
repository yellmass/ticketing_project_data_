package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

//@Component
//public class Mapper<Entity, Dto> {
//
//        private final ModelMapper modelMapper;
//
//        public Mapper(ModelMapper modelMapper) {
//            this.modelMapper = modelMapper;
//        }
//
//        public Entity convertToEntity(Dto dto) {
//            return (Entity) modelMapper.map(dto, new TypeToken<Entity>(){}.getClass());
//        }
//
//        public Dto convertToDto(Entity entity) {
//            return (Dto) modelMapper.map(entity, new TypeToken<Dto>(){}.getClass());
//        }
//
//
//}
