package com.sugon.iris.sugonservice.service.rinseBusinessService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessNullDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessRepeatDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessReplaceDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessSuffixDto;

import java.util.List;

public interface RinseBusinessService {

    int saveRinseBusinessRepeat(RinseBusinessRepeatDto rinseBusinessRepeatDto, List<Error> errorList);

    int saveRinseBusinessNull(RinseBusinessNullDto rinseBusinessNullDto, List<Error> errorList) throws IllegalAccessException;

    int saveRinseBusinessReplace(RinseBusinessReplaceDto rinseBusinessReplaceDto, List<Error> errorList) throws IllegalAccessException;

    int saveRinseBusinessSuffix(RinseBusinessSuffixDto rinseBusinessSuffixDto, List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessRepeatDto> getRepetBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessNullDto> getNullBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessReplaceDto> getReplaceBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessSuffixDto> getSuffixBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessRepeatById(Long id, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessNullById(Long id, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessReplaceById(Long id, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessSuffixById(Long id, List<Error> errorList) throws IllegalAccessException;
}
