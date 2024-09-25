package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.mapper.impl;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.AddressDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.PersonalDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserExcelDTO;
import com.kelsonthony.demofoliointegrationmodeusers.infrastructure.mapper.CellMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class CellMapperImpl implements CellMapper<UserExcelDTO, Void, Integer> {

    private final Map<Integer, BiConsumer<UserExcelDTO, String>> mappers = new HashMap<>();

    public CellMapperImpl() {
        // Mapeando as colunas diretamente para UserExcelDTO
        mappers.put(0, (user, cellValue) -> user.setActive(Boolean.parseBoolean(cellValue)));  // Coluna 0: active
        mappers.put(1, UserExcelDTO::setFirstName);                                             // Coluna 1: firstName
        mappers.put(2, UserExcelDTO::setMiddleName);                                            // Coluna 2: middleName
        mappers.put(3, UserExcelDTO::setLastName);                                              // Coluna 3: lastName
        mappers.put(4, UserExcelDTO::setPreferredFirstName);                                    // Coluna 4: preferredFirstName
        mappers.put(5, UserExcelDTO::setDateOfBirth);                                           // Coluna 5: dateOfBirth
        mappers.put(6, UserExcelDTO::setEmail);                                                 // Coluna 6: email
        mappers.put(7, UserExcelDTO::setPhone);                                                 // Coluna 7: phone
        mappers.put(8, UserExcelDTO::setMobilePhone);                                           // Coluna 8: mobilePhone
        mappers.put(9, UserExcelDTO::setAddressLine1);                                          // Coluna 9: addressLine1
        mappers.put(10, UserExcelDTO::setAddressLine2);                                         // Coluna 10: addressLine2
        mappers.put(11, UserExcelDTO::setCity);                                                 // Coluna 11: city
        mappers.put(12, UserExcelDTO::setRegion);                                               // Coluna 12: region
        mappers.put(13, UserExcelDTO::setPostalCode);                                           // Coluna 13: postalCode
        mappers.put(14, UserExcelDTO::setAddressTypeId);                                        // Coluna 14: addressTypeId
        mappers.put(15, UserExcelDTO::setCountryId);                                            // Coluna 15: countryId
        mappers.put(16, UserExcelDTO::setPreferredContactTypeId);                               // Coluna 16: preferredContactTypeId
        mappers.put(17, UserExcelDTO::setUsername);                                             // Coluna 17: username
        mappers.put(18, UserExcelDTO::setPatronGroup);                                          // Coluna 18: patronGroup
        mappers.put(19, UserExcelDTO::setExpirationDate);                                       // Coluna 19: expirationDate
        mappers.put(20, UserExcelDTO::setBarcode);                                              // Coluna 20: barcode
        mappers.put(21, UserExcelDTO::setEnrollmentDate);                                       // Coluna 21: enrollmentDate
        mappers.put(22, UserExcelDTO::setExternalSystemId);                                     // Coluna 22: externalSystemId
        mappers.put(23, UserExcelDTO::setDepartments);                                          // Coluna 23: departments (pode ser ajustado conforme a necessidade)
    }

    @Override
    public Void map(UserExcelDTO obj, Integer columnIndex, String cellValue) {
        BiConsumer<UserExcelDTO, String> mapper = mappers.get(columnIndex);
        if (mapper != null) {
            mapper.accept(obj, cellValue);
        } else {
            System.out.println("Nenhum mapeamento encontrado para a coluna: " + columnIndex);
        }
        return null;
    }
}
