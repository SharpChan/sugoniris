package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.*;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.fileBeans.ExcelRow;
import com.sugon.iris.sugondomain.dtos.fileDtos.*;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.*;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.excelService.ExcelService;
import com.sugon.iris.sugonservice.service.fileService.FileCaseService;
import com.sugon.iris.sugonservice.service.fileService.FileImportCountService;
import com.sugon.iris.sugonservice.service.fileService.FolderService;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class FileImportCountServiceImpl implements FileImportCountService {

    @Resource
    private FileCaseService fileCaseServiceImpl;

    @Resource
    private FolderService folderServiceImpl;

    @Resource
    private FileDetailMapper fileDetailMapper;

    @Resource
    private FileParsingFailedMapper fileParsingFailedMapper;

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private ExcelService excelServiceImpl;

    @Resource
    private FileRinseGroupMapper fileRinseGroupMapper;

    @Resource
    private FileRinseDetailMapper fileRinseDetailMapper;

    @Resource
    private FileRinseRegularMapper fileRinseRegularMapper;

    @Resource
    private RegularDetailMapper regularDetailMapper;


    @Override
    public List<FileCaseDto> getImportCount(FileCaseDto fileCaseDto,List<Error> errorList) throws IllegalAccessException, InterruptedException, ExecutionException {
        List<FileCaseDto> fileCaseDtoList = null;
        List<FileAttachmentDto> fileAttachmentDtoList = null;
        List<FileDetailDto>  fileDetailDtoList = new ArrayList<>();


        FileAttachmentDto fileAttachmentDto = new FileAttachmentDto();
        fileAttachmentDto.setUserId(fileCaseDto.getUserId());

        FileDetailEntity fileDetailEntity = new FileDetailEntity();
        fileDetailEntity.setUserId(fileCaseDto.getUserId());
        try {
            fileCaseDtoList = fileCaseServiceImpl.selectCaseList(fileCaseDto, errorList);
            fileAttachmentDtoList = folderServiceImpl.findFileAttachmentList(fileAttachmentDto,errorList);
            List<FileDetailEntity> fileDetailEntityList = fileDetailMapper.selectFileDetailList(fileDetailEntity);
            for(FileDetailEntity fileDetailEntityBean : fileDetailEntityList){
                FileDetailDto fileDetailDto = new FileDetailDto();
                PublicUtils.trans(fileDetailEntityBean ,fileDetailDto );
                fileDetailDtoList.add(fileDetailDto);
                //???mpp??????????????????????????????????????????
                //1.??????????????????


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //?????????????????????
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Callable<Boolean>> cList = new ArrayList<>();  //???????????????????????????
        Callable<Boolean> task = null;  //??????????????????
        List<FileAttachmentDto> fileAttachmentDtoListFinal = fileAttachmentDtoList;
        for(FileCaseDto fileCaseDtoBean :  fileCaseDtoList){

            task = new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {

                    for (FileAttachmentDto fileAttachmentDtoBean : fileAttachmentDtoListFinal) {
                        if (fileCaseDtoBean.getId().equals(fileAttachmentDtoBean.getCaseId())) {
                            fileCaseDtoBean.getFileAttachmentDtoList().add(fileAttachmentDtoBean);

                            for (FileDetailDto fileDetailDtoBean : fileDetailDtoList) {
                                //???????????????
                                if (fileDetailDtoBean.getFileAttachmentId().equals(fileAttachmentDtoBean.getId()) && fileDetailDtoBean.getHasImport()) {
                                    fileAttachmentDtoBean.getFileDetailDtoList().add(fileDetailDtoBean);
                                    //??????????????????????????????
                                    int cou1 = fileParsingFailedMapper.countRecordByFileDetail(fileDetailDtoBean.getId());
                                    fileDetailDtoBean.setErrorItemCount(cou1);
                                    //???????????????????????????????????????
                                    String SQL = "select count(*) from " + fileDetailDtoBean.getTableName() + " where mppid2errorid = 0 and file_detail_id = " + fileDetailDtoBean.getId();
                                    int cou2 = mppMapper.mppSqlExecForSearchCount(SQL);
                                    fileDetailDtoBean.setImportRowCount(cou2);
                                }
                                //???????????????
                                else if (fileDetailDtoBean.getFileAttachmentId().equals(fileAttachmentDtoBean.getId()) && !fileDetailDtoBean.getHasImport()) {
                                    fileAttachmentDtoBean.getFileDetailDtoFailedList().add(fileDetailDtoBean);
                                }
                            }
                        }
                    }
                    return true;
                }
            };
            cList.add(task);
        }
        List<Future<Boolean>> results = executorService.invokeAll(cList,30, TimeUnit.SECONDS); //?????????????????????????????????????????????????????????????????????????????????????????????
        for(Future<Boolean> recordPer:results){  //???????????????
            try {
                log.info(String.valueOf(recordPer.get()));
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                continue;
            }
        }
        executorService.shutdown();
        for(FileCaseDto fileCaseDtoBean :  fileCaseDtoList){
            fileCaseDtoBean.rowCount();
        }
        return fileCaseDtoList;
    }

    @Override
    public List<FileParsingFailedDto> getFileParsingFailed(Long userId, Long fileDetailId,List<Error> errorList) throws IllegalAccessException {
        List<FileParsingFailedDto> fileParsingFailedDtoList = new ArrayList<>();
        FileParsingFailedEntity fileParsingFailedEntity = new FileParsingFailedEntity();
        fileParsingFailedEntity.setUserId(userId);
        fileParsingFailedEntity.setFileDetailId(fileDetailId);
        fileParsingFailedEntity.setMark(false);
        List<FileParsingFailedEntity> fileParsingFailedEntityList = fileParsingFailedMapper.selectFileParsingFailedList(fileParsingFailedEntity);
        for(FileParsingFailedEntity fileParsingFailedEntityBean : fileParsingFailedEntityList){
            FileParsingFailedDto fileParsingFailedDto = new FileParsingFailedDto();
            PublicUtils.trans(fileParsingFailedEntityBean,fileParsingFailedDto);
            fileParsingFailedDtoList.add(fileParsingFailedDto);
        }
        return fileParsingFailedDtoList;
    }

    @Override
    public Boolean downloadErrorsExcelZip(HttpServletResponse response, HttpServletRequest request,Long fileDetailId) {

        //1.????????????id????????????????????????
        FileDetailEntity fileDetailEntity = fileDetailMapper.selectByPrimaryKey(fileDetailId);
        FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
        fileTemplateDetailEntity4Sql.setTemplateId(fileDetailEntity.getFileTemplateId());
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
        FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileDetailEntity.getFileTemplateId());
        PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);
        List<ExcelRow> excelRowList = new ArrayList<>();
        //????????????,???????????????sql
        ExcelRow excelRowHead = new ExcelRow();
        excelRowHead.getFields().add("id");
        excelRowHead.getFields().add("mppid2errorid");
        excelRowList.add(excelRowHead);
        String sql = "select id,mppid2errorid,";
        List<String> fieldNameList = new ArrayList<>();
        fieldNameList.add("id");
        fieldNameList.add("mppid2errorid");
        int i = 0;
        for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
            //????????????
            excelRowHead.getFields().add(fileTemplateDetailEntity.getFieldKey());
            //??????????????????
            fieldNameList.add(fileTemplateDetailEntity.getFieldName());
            //??????sql
            if(i < fileTemplateDetailEntityList.size()-1) {
                sql += fileTemplateDetailEntity.getFieldName() + ", ";
            }else{
                sql += fileTemplateDetailEntity.getFieldName();
            }
            i++;
        }
        sql += " from "+fileDetailEntity.getTableName() + " where mppid2errorid != '0'";
        //2.????????????id??????????????????
        List<Map<String,Object>>  errors = mppMapper.mppSqlExecForSearchRtMapList(sql);
        for(Map map : errors){
            ExcelRow excelRow = new ExcelRow();
            for(String str : fieldNameList){
                excelRow.getFields().add(map.get(str)+"");
            }
            excelRowList.add(excelRow);
        }

        //3.??????excel
        HSSFWorkbook workbook = excelServiceImpl.getNewExcel(fileDetailEntity.getCaseId()+"_"+fileDetailEntity.getId()+"_"+fileDetailEntity.getFileTemplateId(),excelRowList);
        try {
            response.setContentType("application/zip; charset=UTF-8");
            //?????????????????????????????????????????????
            String agent = request.getHeader("USER-AGENT");
            String downloadName = "??????????????????.zip";
            //??????IE?????????IE????????????????????????
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                downloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("Content-disposition", "attachment;filename=" + downloadName);

            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            //??????????????????????????????
            // --start
            ZipEntry entry = new ZipEntry(fileTemplateEntity.getTemplateName()+"_"+fileDetailEntity.getFileName().substring(0,fileDetailEntity.getFileName().lastIndexOf("."))+"_"+fileDetailEntity.getFileType()+"_????????????.xls");
            zipOutputStream.putNextEntry(entry);
            ByteOutputStream byteOutputStream = new ByteOutputStream();
            workbook.write(byteOutputStream);
            byteOutputStream.writeTo(zipOutputStream);
            byteOutputStream.close();
            zipOutputStream.closeEntry();
            // --end
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void downloadErrorsExcel(HttpServletResponse response, HttpServletRequest request, Long fileDetailId) throws IOException {
        //1.????????????id????????????????????????
        FileDetailEntity fileDetailEntity = fileDetailMapper.selectByPrimaryKey(fileDetailId);
        FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
        fileTemplateDetailEntity4Sql.setTemplateId(fileDetailEntity.getFileTemplateId());
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
        FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(fileDetailEntity.getFileTemplateId());
        PublicUtils.fileTemplateDetailEntityListSort(fileTemplateDetailEntityList);
        List<ExcelRow> excelRowList = new ArrayList<>();
        //????????????,???????????????sql
        ExcelRow excelRowHead = new ExcelRow();
        excelRowHead.getFields().add("id");
        excelRowHead.getFields().add("mppid2errorid");
        excelRowList.add(excelRowHead);
        String sql = "select id,mppid2errorid,";
        List<String> fieldNameList = new ArrayList<>();
        fieldNameList.add("id");
        fieldNameList.add("mppid2errorid");
        int i = 0;
        for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
            //????????????
            excelRowHead.getFields().add(fileTemplateDetailEntity.getFieldKey());
            //??????????????????
            fieldNameList.add(fileTemplateDetailEntity.getFieldName());
            //??????sql
            if(i < fileTemplateDetailEntityList.size()-1) {
                sql += fileTemplateDetailEntity.getFieldName() + ", ";
            }else{
                sql += fileTemplateDetailEntity.getFieldName();
            }
            i++;
        }
        sql += " from "+fileDetailEntity.getTableName() + " where mppid2errorid != '0' and file_detail_id ='"+fileDetailId+"'";
        //2.????????????id??????????????????
        List<Map<String,Object>>  errors = mppMapper.mppSqlExecForSearchRtMapList(sql);
        for(Map map : errors){
            ExcelRow excelRow = new ExcelRow();
            for(String str : fieldNameList){
                excelRow.getFields().add(map.get(str)+"");
            }
            excelRowList.add(excelRow);
        }
        //3.??????excel
        HSSFWorkbook workbook = excelServiceImpl.getNewExcel(fileDetailEntity.getCaseId()+"_"+fileDetailEntity.getId()+"_"+fileDetailEntity.getFileTemplateId(),excelRowList);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileTemplateEntity.getTemplateName()+"_"+fileDetailEntity.getFileName().substring(0,fileDetailEntity.getFileName().lastIndexOf("."))+"_"+fileDetailEntity.getFileType()+"_????????????.xls");
        response.setCharacterEncoding("UTF-8");
        response.flushBuffer();
        workbook.write(response.getOutputStream());

    }

    @Override
    public Integer dataAmendment(List<MultipartFile> files, Long  fileDetailId,List<Error> errorList) throws IOException {
        int c = 0;
        for(MultipartFile file : files){
            InputStream inputStream = file.getInputStream();
            Workbook workbook = null;
            try {
                workbook = WorkbookFactory.create(inputStream);
            }catch (Exception e){
                e.printStackTrace();
            }
            inputStream.close();
            //???????????????
            Sheet sheet = workbook.getSheetAt(0);
            if(StringUtils.isEmpty(sheet.getSheetName())){
                errorList.add(new Error(ErrorCode_Enum.FILE_01_004.getCode(), ErrorCode_Enum.FILE_01_004.getMessage()));
                return 0;
            }
            String[] ids = sheet.getSheetName().replaceAll("\\s*","").split("_");
            if(null == ids || ids.length<3){
                errorList.add(new Error(ErrorCode_Enum.FILE_01_005.getCode(), ErrorCode_Enum.FILE_01_005.getMessage()));
                return 0;
            }
            String caseId = ids[0];
            String fileDetailIdExcel = ids[1];
            String fileTemplateId = ids[2];

            if(!String.valueOf(fileDetailId).equals(fileDetailIdExcel)){
                errorList.add(new Error(ErrorCode_Enum.FILE_01_006.getCode(), ErrorCode_Enum.FILE_01_006.getMessage()));
                return 0;
            }

            Row rowHead = sheet.getRow(0);
            //?????????????????????
            int firstRowIndex = sheet.getFirstRowNum()+1;
            int lastRowIndex = sheet.getLastRowNum()+1;
            int firstCellIndex = rowHead.getFirstCellNum();
            int lastCellIndex = rowHead.getLastCellNum();
            if(lastRowIndex < 2){
                errorList.add(new Error(ErrorCode_Enum.FILE_01_006.getCode(), ErrorCode_Enum.FILE_01_006.getMessage()));
                return 0;
            }
            //??????????????????,????????????????????????????????????
            FileTemplateDto fileTemplateDto = assembleFileTemplate(fileTemplateId,errorList);

            //??????mpp??????
            FileDetailEntity fileDetailEntity = fileDetailMapper.selectByPrimaryKey(fileDetailId);

            //??????????????????
            String insertSql = getInsertSql(Long.parseLong(caseId),fileDetailEntity.getFileAttachmentId(),fileDetailId,fileTemplateDto.getFileTemplateDetailDtoList(),fileDetailEntity.getTableName(),fileTemplateDto.getId());

            //??????excel??????
            List<String> headList = new ArrayList<>();
            int mppid2errorid_index = 0;
            for(int i = firstCellIndex;i<lastCellIndex;i++){
                Cell cell = rowHead.getCell(i);

                if("mppid2errorid".equals(cell.getStringCellValue().replaceAll("\\s*", ""))){
                    mppid2errorid_index = i;
                }
                //??????id???mppid2errorid
                if("id".equals(cell.getStringCellValue().replaceAll("\\s*", ""))||"mppid2errorid".equals(cell.getStringCellValue().replaceAll("\\s*", ""))){
                         continue;
                }
                headList.add(cell.getStringCellValue().replaceAll("\\s*", ""));
            }


            List<String> filedList = new ArrayList<>();
            for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDto.getFileTemplateDetailDtoList()){
                filedList.add(fileTemplateDetailDto.getFieldKey());
            }
            //??????excel????????????????????????
            for(String field : headList){
                if(!filedList.contains(field)){
                    errorList.add(new Error(ErrorCode_Enum.FILE_01_009.getCode(), ErrorCode_Enum.FILE_01_009.getMessage()));
                    return 0;
                }
            }

            //?????????????????????excel?????????????????????
            //key:excel?????????;value:????????????
            Map<Integer,FileTemplateDetailDto>  excelColum2TemplateDetailMap = new HashMap<>();
            //key:????????????id???value:excel?????????
            Map<Long, Integer>  feildIdRefIndexMap = new HashMap<>();
            for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDto.getFileTemplateDetailDtoList()){
                for(int i = firstCellIndex;i<lastCellIndex;i++){
                    Cell cell = rowHead.getCell(i);
                    if(fileTemplateDetailDto.getFieldKey().replaceAll("\\s*", "").equals(cell.getStringCellValue().replaceAll("\\s*", ""))){
                        excelColum2TemplateDetailMap.put(i,fileTemplateDetailDto);
                        feildIdRefIndexMap.put(fileTemplateDetailDto.getId(),i);
                    }
                }
            }

            //???excel???????????????
            int k = 0;
            Set<String> mppid2errorid_index_set = new HashSet<>();
             for(int i = firstRowIndex;i<lastRowIndex;i++){
                Row row = sheet.getRow(i);
                 retry:  for(int j = firstCellIndex;j<lastCellIndex;j++){
                    Cell cell = row.getCell(j);
                    Cell cellHead = rowHead.getCell(j);
                     //??????id???mppid2errorid
                    if("id".equals(cellHead.getStringCellValue().replaceAll("\\s*", ""))||"mppid2errorid".equals(cellHead.getStringCellValue().replaceAll("\\s*", ""))){
                        continue;
                    }
                    //??????????????????
                    List<RegularDetailDto> regularDetailDtoListY = null;
                    List<RegularDetailDto> regularDetailDtoListN = null;
                    if(null != excelColum2TemplateDetailMap.get(j).getFileRinseDetailDto()){
                        regularDetailDtoListY = excelColum2TemplateDetailMap.get(j).getFileRinseDetailDto().getRegularDetailDtoListY();
                        regularDetailDtoListN = excelColum2TemplateDetailMap.get(j).getFileRinseDetailDto().getRegularDetailDtoListN();
                    }

                    //boolean checkRegular = false;
                    //????????????????????????????????????
                    if(CollectionUtils.isEmpty(regularDetailDtoListY) && CollectionUtils.isEmpty(regularDetailDtoListN)){
                       continue;
                    }

                    //???????????????????????????string??????
                    cell.setCellType(Cell.CELL_TYPE_STRING);

                    //????????????????????????????????????
                    boolean checkRegularY = false;
                    if(!CollectionUtils.isEmpty(regularDetailDtoListY)){
                        for(RegularDetailDto regularDetailDto : regularDetailDtoListY){
                            if (cell.getStringCellValue().replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularY = true;
                                break;//???????????????????????????
                            }
                        }
                    }else{
                        checkRegularY = true;
                    }

                    //???????????????????????????????????????
                    boolean checkRegularN = true;
                    if(!CollectionUtils.isEmpty(regularDetailDtoListN)){
                        for (RegularDetailDto regularDetailDto : regularDetailDtoListN) {
                            if (cell.getStringCellValue().replaceAll("\\s*", "").matches(regularDetailDto.getRegularValue().trim())) {
                                checkRegularN = false;
                                break;//???????????????????????????
                            }
                        }
                    }

                    //??????????????????????????????????????????
                    if(!(checkRegularY && checkRegularN)){
                        break retry;
                    }
                }
                for(int h = firstCellIndex;h<lastCellIndex;h++){
                    if(h == mppid2errorid_index){
                        mppid2errorid_index_set.add(row.getCell(h).getStringCellValue().replaceAll("\\s*", ""));
                    }

                }
                //????????????????????????
                String insertSqlExec = insertSql;
                for(Map.Entry<Long, Integer> entry : feildIdRefIndexMap.entrySet()){
                    //???????????????????????????string??????
                    row.getCell(entry.getValue()).setCellType(Cell.CELL_TYPE_STRING);
                    //insertSqlExec = insertSqlExec.replace("&&"+entry.getKey()+"&&",row.getCell(entry.getValue()).getStringCellValue().replaceAll("\\s*", ""));
                    insertSqlExec = insertSqlExec.replace("&&"+entry.getKey()+"&&",row.getCell(entry.getValue()).getStringCellValue().trim());

                }
                mppMapper.mppSqlExec(insertSqlExec);
                k++;
                c++ ;
            }

            if(k != 0) {
                fileDetailEntity.setImportRowCount(fileDetailEntity.getImportRowCount() + k);
                fileDetailMapper.updateByPrimaryKey(fileDetailEntity);
            }

            //????????????
            if(!CollectionUtils.isEmpty(mppid2errorid_index_set)){
                for(String str : mppid2errorid_index_set){
                    String deleteMppRecord = "delete from  " + fileDetailEntity.getTableName() + " where mppid2errorid ='"+str+ "'";
                    mppMapper.mppSqlExec(deleteMppRecord);
                    //??????mysql  file_parsing_failed???
                    fileParsingFailedMapper.deleteFileParsingFailedByMppid2errorid(Long.parseLong(str));
                    String errorInfoRecord = "delete from  error_info where mppid2errorid ='"+str+ "'";
                    //??????mpp  error_info???
                    mppMapper.mppSqlExec(errorInfoRecord);
                }
            }

        }
        return c;
    }


    //????????????????????????
    private String getInsertSql(Long caseId,Long fileAttachmentId, Long fileDetailId,List<FileTemplateDetailDto> fileTemplateDetailDtoList, String tableName,Long fileTemplateId) {
        //??????insertSql??????
        String sqlInsert = "insert into "+tableName+"(";
        String  sqlValues = "";
        for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDetailDtoList){
            sqlInsert += fileTemplateDetailDto.getFieldName()+",";
            sqlValues += "'&&"+fileTemplateDetailDto.getId()+"&&',";
        }
        sqlInsert += "file_template_id,case_id,file_attachment_id,file_detail_id,mppId2ErrorId";
        sqlValues += "'"+fileTemplateId+"','"+caseId+"','"+fileAttachmentId+"','"+fileDetailId+"',"+"'0'";
        sqlInsert +=") values(" +sqlValues+");";
        return sqlInsert;
    }

    private FileTemplateDto assembleFileTemplate(String fileTemplateId,List<Error> errorList){
        FileTemplateDto fileTemplateDto = new FileTemplateDto();
        FileTemplateEntity fileTemplateEntity = fileTemplateMapper.selectFileTemplateByPrimaryKey(Long.parseLong(fileTemplateId));
        try {
            PublicUtils.trans(fileTemplateEntity,fileTemplateDto);
        }catch (Exception e){
            e.printStackTrace();
        }


        //?????????????????????
        FileRinseGroupEntity fileRinseGroupEntity = fileRinseGroupMapper.selectByPrimaryKey(fileTemplateDto.getFileRinseGroupId());
        FileRinseGroupDto fileRinseGroupDto = new FileRinseGroupDto();
        try{
            PublicUtils.trans(fileRinseGroupEntity,fileRinseGroupDto);
        }catch (Exception e){
            e.printStackTrace();
        }

        fileTemplateDto.setFileRinseGroupDto(fileRinseGroupDto);

        //???????????????????????????
        List<FileTemplateDetailDto> fileTemplateDetailDtoList = new ArrayList<>();
        fileTemplateDto.setFileTemplateDetailDtoList(fileTemplateDetailDtoList);
        FileTemplateDetailEntity fileTemplateDetailEntity4Sql = new FileTemplateDetailEntity();
        fileTemplateDetailEntity4Sql.setTemplateId(Long.parseLong(fileTemplateId));
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList =  fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity4Sql);
        if(CollectionUtils.isEmpty(fileTemplateDetailEntityList)){
            errorList.add(new Error(ErrorCode_Enum.FILE_01_007.getCode(), ErrorCode_Enum.FILE_01_007.getMessage()));
            return fileTemplateDto;
        }
        for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
            FileTemplateDetailDto fileTemplateDetailDto = new FileTemplateDetailDto();
            try {
                PublicUtils.trans(fileTemplateDetailEntity, fileTemplateDetailDto);
            }catch (Exception e){
                e.printStackTrace();
            }
            fileTemplateDetailDtoList.add(fileTemplateDetailDto);

            //???????????????????????????????????????
            if(null == fileTemplateDetailDto.getFileRinseDetailId()){
                continue;
            }

            //??????????????????
            FileRinseDetailEntity fileRinseDetailEntity = fileRinseDetailMapper.selectByPrimaryKey(fileTemplateDetailDto.getFileRinseDetailId());
            FileRinseDetailDto fileRinseDetailDto = new FileRinseDetailDto();
            try {
                PublicUtils.trans(fileRinseDetailEntity,fileRinseDetailDto);
            }catch (Exception e){
                e.printStackTrace();
            }
            fileTemplateDetailDto.setFileRinseDetailDto(fileRinseDetailDto);
            //?????????????????????
            List<RegularDetailDto>  regularDetailDtoListY = new ArrayList<>();
            List<RegularDetailDto>  regularDetailDtoListN = new ArrayList<>();
            //?????????????????????
            //???????????????????????????????????????????????????
            List<FileRinseRegularEntity> fileRinseRegularEntityList = fileRinseRegularMapper.selectByFileRinseDetailId(fileRinseDetailEntity.getId());

            if(!CollectionUtils.isEmpty(fileRinseRegularEntityList)) {
                for (FileRinseRegularEntity fileRinseRegularEntity : fileRinseRegularEntityList) {
                    if ("1".equals(fileRinseRegularEntity.getType())) {
                        RegularDetailEntity regularDetailEntityY = regularDetailMapper.selectByPrimaryKey(fileRinseRegularEntity.getRegularDetailId());
                        RegularDetailDto regularDetailDtoY = new RegularDetailDto();
                        try{
                            PublicUtils.trans(regularDetailEntityY, regularDetailDtoY);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        regularDetailDtoListY.add(regularDetailDtoY);
                    }
                    if ("2".equals(fileRinseRegularEntity.getType())) {
                        RegularDetailEntity regularDetailEntityN = regularDetailMapper.selectByPrimaryKey(fileRinseRegularEntity.getRegularDetailId());
                        RegularDetailDto regularDetailDtoN = new RegularDetailDto();
                        try{
                            PublicUtils.trans(regularDetailEntityN, regularDetailDtoN);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        regularDetailDtoListY.add(regularDetailDtoN);
                    }
                }
                fileRinseDetailDto.setRegularDetailDtoListY(regularDetailDtoListY);
                fileRinseDetailDto.setRegularDetailDtoListN(regularDetailDtoListN);
            }
        }
        return fileTemplateDto;
    }
}
