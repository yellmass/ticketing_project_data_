//package com.cydeo.mapper;
//
//import com.cydeo.dto.RoleDTO;
//import com.cydeo.entity.Role;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.TypeToken;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Type;
//
//@Component
//public class Mapper<Entity, Dto>{
//
//        private final ModelMapper modelMapper;
//
//        public Mapper(ModelMapper modelMapper) {
//            this.modelMapper = modelMapper;
//        }
//
//        public Entity convertToEntity(Dto dto) {
//            return modelMapper.map(dto, new TypeToken<Dto>(){}.getType());
//        }
//
//        public Dto convertToDto(Entity entity) {
//            return modelMapper.map(entity, new TypeToken<Dto>(){}.getType());
//        }
//
//
//}
