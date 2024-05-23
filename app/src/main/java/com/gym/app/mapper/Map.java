package com.gym.app.mapper;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.dto.ContactInfoDto;

import java.util.List;
import java.util.stream.Collectors;

public class Map {

    public static ContactInfo toContactInfo(ContactInfoDto dto) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setMobilePhone(dto.getMobilePhone());
        contactInfo.setEmail(dto.getEmail());
        return contactInfo;
    }

    public static List<ContactInfo> toContactInfoList(List<ContactInfoDto> dtoList) {
        return dtoList.stream()
                .map(Map::toContactInfo)
                .collect(Collectors.toList());
    }
}
