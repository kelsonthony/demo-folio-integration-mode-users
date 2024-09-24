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
        mappers.put(0, (user, cellValue) -> user.setActive(Boolean.parseBoolean(cellValue)));  // Coluna 0: active
        mappers.put(1, (user, cellValue) -> ensurePersonal(user).setFirstName(cellValue));      // Coluna 1: firstName
        mappers.put(2, (user, cellValue) -> ensurePersonal(user).setMiddleName(cellValue));     // Coluna 2: middleName
        mappers.put(3, (user, cellValue) -> ensurePersonal(user).setLastName(cellValue));       // Coluna 3: lastName
        mappers.put(4, (user, cellValue) -> ensurePersonal(user).setPreferredFirstName(cellValue)); // Coluna 4: preferredFirstName
        mappers.put(5, (user, cellValue) -> ensurePersonal(user).setDateOfBirth(cellValue));    // Coluna 5: dateOfBirth
        mappers.put(6, (user, cellValue) -> ensurePersonal(user).setEmail(cellValue));          // Coluna 6: email
        mappers.put(7, (user, cellValue) -> ensurePersonal(user).setPhone(cellValue));          // Coluna 7: phone
        mappers.put(8, (user, cellValue) -> ensurePersonal(user).setMobilePhone(cellValue));    // Coluna 8: mobilePhone
        mappers.put(9, (user, cellValue) -> ensureAddress(user).setAddressLine1(cellValue));    // Coluna 9: addressLine1
        mappers.put(10, (user, cellValue) -> ensureAddress(user).setAddressLine2(cellValue));   // Coluna 10: addressLine2
        mappers.put(11, (user, cellValue) -> ensureAddress(user).setCity(cellValue));           // Coluna 11: city
        mappers.put(12, (user, cellValue) -> ensureAddress(user).setRegion(cellValue));         // Coluna 12: region
        mappers.put(13, (user, cellValue) -> ensureAddress(user).setPostalCode(cellValue));     // Coluna 13: postalCode
        mappers.put(14, (user, cellValue) -> ensureAddress(user).setAddressTypeId(cellValue));  // Coluna 14: addressTypeId
        mappers.put(15, (user, cellValue) -> ensureAddress(user).setCountryId(cellValue));      // Coluna 15: countryId
        mappers.put(16, (user, cellValue) -> ensureAddress(user).setAddressLine1(cellValue)); // Coluna 16: preferredContactTypeId
        mappers.put(17, UserDTO::setUsername);                                                  // Coluna 17: username
        mappers.put(18, UserDTO::setPatronGroup);                                               // Coluna 18: patronGroup
        mappers.put(19, UserDTO::setExpirationDate);                                            // Coluna 19: expirationDate
        mappers.put(20, UserDTO::setBarcode);                                                   // Coluna 20: barcode
        mappers.put(21, UserDTO::setEnrollmentDate);                                            // Coluna 21: enrollmentDate
        mappers.put(22, UserDTO::setExternalSystemId);                                          // Coluna 22: externalSystemId
        mappers.put(23, (user, cellValue) -> {
            // Assumindo que departments está em uma célula como uma lista separada por vírgulas
            List<String> departments = List.of(cellValue.split(","));
            user.setDepartments(departments);
        });                                                                                     // Coluna 23: departments
    }

    /**
     * Garante que o objeto PersonalDTO dentro do UserDTO esteja inicializado.
     * @param user O objeto UserDTO que contém os dados pessoais.
     * @return O objeto PersonalDTO.
     */
    private PersonalDTO ensurePersonal(UserDTO user) {
        if (user.getPersonal() == null) {
            user.setPersonal(new PersonalDTO());
        }
        return user.getPersonal();
    }

    /**
     * Garante que o AddressDTO dentro do PersonalDTO esteja inicializado.
     * @param user O objeto UserDTO que contém os dados do endereço.
     * @return O objeto AddressDTO.
     */
    private AddressDTO ensureAddress(UserDTO user) {
        PersonalDTO personal = ensurePersonal(user);
        if (personal.getAddresses() == null || personal.getAddresses().isEmpty()) {
            AddressDTO address = new AddressDTO();
            personal.setAddresses(List.of(address));
        }
        return personal.getAddresses().get(0);  // Retorna o primeiro endereço, assumindo um único endereço.
    }

    /**
     * Método que faz o mapeamento da célula para o DTO correspondente.
     * @param obj O objeto UserDTO onde o valor será inserido.
     * @param columnIndex O índice da coluna no Excel.
     * @param cellValue O valor da célula a ser mapeado.
     * @return Sempre retorna null (não necessário para este caso de uso).
     */
    @Override
    public Void map(UserDTO obj, Integer columnIndex, String cellValue) {
        BiConsumer<UserDTO, String> mapper = mappers.get(columnIndex);
        if (mapper != null) {
            mapper.accept(obj, cellValue);
        }
        return null;
    }
}
