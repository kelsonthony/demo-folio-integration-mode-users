package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.mapper.impl;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.AddressDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.PersonalDTO;
import com.kelsonthony.demofoliointegrationmodeusers.infrastructure.mapper.CellMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class CellMapperImpl implements CellMapper<UserDTO, Void, Integer> {

    private final Map<Integer, BiConsumer<UserDTO, String>> mappers = new HashMap<>();

    public CellMapperImpl() {
        mappers.put(0, (user, cellValue) -> user.setActive(Boolean.parseBoolean(cellValue)));
        mappers.put(1, (user, cellValue) -> ensurePersonal(user).setFirstName(cellValue));
        mappers.put(2, (user, cellValue) -> ensurePersonal(user).setLastName(cellValue));
        mappers.put(3, UserDTO::setUsername);
        mappers.put(4, UserDTO::setPatronGroup);
        mappers.put(5, UserDTO::setExpirationDate);
        mappers.put(6, UserDTO::setBarcode);
        mappers.put(7, UserDTO::setEnrollmentDate);
        mappers.put(8, UserDTO::setExternalSystemId);
        mappers.put(9, (user, cellValue) -> ensurePersonal(user).setEmail(cellValue));
        mappers.put(10, (user, cellValue) -> ensurePersonal(user).setPhone(cellValue));
        mappers.put(11, (user, cellValue) -> ensurePersonal(user).setMobilePhone(cellValue));
        mappers.put(12, (user, cellValue) -> ensureAddress(user).setAddressLine1(cellValue));
        mappers.put(13, (user, cellValue) -> ensureAddress(user).setCity(cellValue));

    }


    private PersonalDTO ensurePersonal(UserDTO user) {
        if (user.getPersonal() == null) {
            user.setPersonal(new PersonalDTO());
        }
        return user.getPersonal();
    }


    private AddressDTO ensureAddress(UserDTO user) {
        PersonalDTO personal = ensurePersonal(user);
        if (personal.getAddresses() == null || personal.getAddresses().isEmpty()) {
            AddressDTO address = new AddressDTO();
            personal.setAddresses(List.of(address));
        }
        return personal.getAddresses().getFirst();
    }

    @Override
    public Void map(UserDTO obj, Integer columnIndex, String cellValue) {
        BiConsumer<UserDTO, String> mapper = mappers.get(columnIndex);
        if (mapper != null) {
            mapper.accept(obj, cellValue);
        }
        return null;
    }
}
