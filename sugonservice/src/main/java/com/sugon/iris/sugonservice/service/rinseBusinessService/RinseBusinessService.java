package com.sugon.iris.sugonservice.service.rinseBusinessService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.*;

import java.util.List;

public interface RinseBusinessService {

    int saveRinseBusinessRepeat(RinseBusinessRepeatDto rinseBusinessRepeatDto, List<Error> errorList);

    int saveRinseBusinessNull(RinseBusinessNullDto rinseBusinessNullDto, List<Error> errorList) throws IllegalAccessException;

    int saveRinseBusinessReplace(RinseBusinessReplaceDto rinseBusinessReplaceDto, List<Error> errorList) throws IllegalAccessException;

    int saveRinseBusinessSuffix(RinseBusinessSuffixDto rinseBusinessSuffixDto, List<Error> errorList) throws IllegalAccessException;

    int saveRinseBusinessPrefix(RinseBusinessPrefixDto rinseBusinessPrefixDto, List<Error> errorList) throws IllegalAccessException;

    int saveRinseBusinessIp(RinseBusinessIpDto rinseBusinessIpDto,List<Error> errorList) throws IllegalAccessException;

    int saveRinseBusinessPhone(RinseBusinessPhoneDto rinseBusinessPhoneDto,List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessRepeatDto> getRepetBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessNullDto> getNullBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessReplaceDto> getReplaceBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessSuffixDto> getSuffixBussList(Long fileTemplateId, List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessPrefixDto>  getPrefixBussList (Long fileTemplateId, List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessIpDto> getIpBussList(Long fileTemplateId,List<Error> errorList) throws IllegalAccessException;

    List<RinseBusinessPhoneDto> getPhoneBussList(Long fileTemplateId,List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessRepeatById(Long id, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessNullById(Long id, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessReplaceById(Long id, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessSuffixById(Long id, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessPrefixById(Long id, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessIpById(Long id, List<Error> errorList) throws IllegalAccessException;

    int deleteRinseBusinessPhoneById(Long id, List<Error> errorList) throws IllegalAccessException;
}
