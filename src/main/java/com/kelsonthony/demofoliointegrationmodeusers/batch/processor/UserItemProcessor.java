package com.kelsonthony.demofoliointegrationmodeusers.batch.processor;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.AddressDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.PersonalDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserExcelDTO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserItemProcessor implements ItemProcessor<UserExcelDTO, UserDTO> {

    @Override
    public UserDTO process(UserExcelDTO userExcelDTO) throws Exception {
        // Converter de UserExcelDTO para UserDTO
        UserDTO userDTO = new UserDTO();
        PersonalDTO personal = new PersonalDTO();
        AddressDTO address = new AddressDTO();

        // Preencher dados de PersonalDTO
        personal.setFirstName(userExcelDTO.getFirstName());
        personal.setMiddleName(userExcelDTO.getMiddleName());
        personal.setLastName(userExcelDTO.getLastName());
        personal.setPreferredFirstName(userExcelDTO.getPreferredFirstName());
        personal.setDateOfBirth(userExcelDTO.getDateOfBirth());
        personal.setEmail(userExcelDTO.getEmail());
        personal.setPhone(userExcelDTO.getPhone());
        personal.setMobilePhone(userExcelDTO.getMobilePhone());
        personal.setPreferredContactTypeId(userExcelDTO.getPreferredContactTypeId());

        // Preencher dados de AddressDTO
        address.setAddressLine1(userExcelDTO.getAddressLine1());
        address.setAddressLine2(userExcelDTO.getAddressLine2());
        address.setCity(userExcelDTO.getCity());
        address.setRegion(userExcelDTO.getRegion());
        address.setPostalCode(userExcelDTO.getPostalCode());
        address.setAddressTypeId(userExcelDTO.getAddressTypeId());
        address.setCountryId(userExcelDTO.getCountryId());

        personal.setAddresses(List.of(address));
        userDTO.setPersonal(personal);

        // Definir dados de UserDTO
        userDTO.setActive(userExcelDTO.isActive());
        userDTO.setUsername(userExcelDTO.getUsername());
        userDTO.setPatronGroup(userExcelDTO.getPatronGroup());
        userDTO.setExpirationDate(userExcelDTO.getExpirationDate());
        userDTO.setBarcode(userExcelDTO.getBarcode());
        userDTO.setEnrollmentDate(userExcelDTO.getEnrollmentDate());
        userDTO.setExternalSystemId(userExcelDTO.getExternalSystemId());

        // Verificar se departments está vazio ou nulo e definir uma lista vazia
        if (userExcelDTO.getDepartments() == null || userExcelDTO.getDepartments().isEmpty()) {
            userDTO.setDepartments(new ArrayList<>()); // Define uma lista vazia
        } else {
            userDTO.setDepartments(List.of(userExcelDTO.getDepartments().split(",")));
        }

        System.out.println("UserDTO Processor: " + userDTO);

        return userDTO;
    }

}
