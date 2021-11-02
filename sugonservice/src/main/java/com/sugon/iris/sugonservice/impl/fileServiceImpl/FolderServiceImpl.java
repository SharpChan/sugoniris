package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.sugon.iris.sugoncommon.SSHRemote.SSHConfig;
import com.sugon.iris.sugoncommon.SSHRemote.SSHServiceBs;
import com.sugon.iris.sugoncommon.publicUtils.PublicRuleUtils;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.DeclarMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileAttachmentMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.fileBeans.FileInfoBean;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.declarDtos.DeclarationDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileAttachmentDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.DeclarationDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileAttachmentEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileDetailEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugondomain.enums.FileType_Enum;
import com.sugon.iris.sugondomain.enums.Peripheral_Enum;
import com.sugon.iris.sugonservice.service.fileService.FolderService;
import com.sugon.iris.sugonservice.service.declarService.DeclarService;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import com.jcraft.jsch.Session;

@Service
public class FolderServiceImpl implements FolderService {

    private static final String RAR  = ".rar";

    private static final String ZIP  = ".zip";

    private static final String XLSX  = ".xlsx";

    private static final String XLS  = ".xls";

    private static final String CSV = ".csv";

    private final static String DETAIL = "文件删除:";

    @Resource
    private FileAttachmentMapper fileAttachmentMapper;

    @Resource
    private FileDetailMapper fileDetailMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DeclarService declarServiceImpl;

    @Resource
    private DeclarMapper declarMapper;


    //进行数据同步
    @Override
    public void dataSync(User user,String[] selectedArr,List<Error> errorList) throws IOException {

    }

    //如果是压缩文件则进行解压
    @Override
    public void decompress(User user,String[] selectedArr,List<Error> errorList)  {
        List<FileAttachmentEntity> fileAttachmentEntityListAll = new ArrayList<>();
        for(String id : selectedArr) {
            FileAttachmentEntity fleAttachmentEntity = new FileAttachmentEntity();
            fleAttachmentEntity.setId(Long.parseLong(id));
            fleAttachmentEntity.setUserId(user.getId());
            //通过用户编号和文件信息id获取路径
            List<FileAttachmentEntity> fileAttachmentEntityList = fileAttachmentMapper.selectFileAttachmentList(fleAttachmentEntity);
            if(!CollectionUtils.isEmpty(fileAttachmentEntityList)) {
                //剔除已经解压，和剔除没有配置模板组的
                for(FileAttachmentEntity fileAttachmentEntityBean : fileAttachmentEntityList){
                     if(fileAttachmentEntityBean.getHasImport() == false && null != fileAttachmentEntityBean.getTemplateGroupId()){
                         fileAttachmentEntityListAll.add(fileAttachmentEntityBean);
                     }
                     if(fileAttachmentEntityBean.getHasImport() == true){
                         errorList.add(new Error(ErrorCode_Enum.SUGON_01_003.getCode(),fileAttachmentEntityBean.getFileName()+ErrorCode_Enum.SUGON_01_003.getMessage()));
                     }
                     if( fileAttachmentEntityBean.getTemplateGroupId()== null){
                         errorList.add(new Error(ErrorCode_Enum.SUGON_01_004.getCode(),fileAttachmentEntityBean.getFileName()+ErrorCode_Enum.SUGON_01_004.getMessage()));
                     }

                }
            }
        }
        if(CollectionUtils.isEmpty(fileAttachmentEntityListAll)){
             return;
        }
        Session session = null;
        try {
            session = new SSHConfig().getSession();
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SUGON_SSH_001.getCode(),ErrorCode_Enum.SUGON_SSH_001.getMessage()));
        }
        SSHServiceBs sSHServiceBs = new SSHServiceBs(session);
        try {
            if (!CollectionUtils.isEmpty(fileAttachmentEntityListAll)) {
                for (FileAttachmentEntity fileAttachmentEntity : fileAttachmentEntityListAll) {
                    String command = null;
                    //已经存在则删除
                    command = "if [-d " + fileAttachmentEntity.getAttachment() + "]; then rm -rf  " + fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "") + " fi";
                    sSHServiceBs.execCommand(command);

                    command = "unar  -o " + fileAttachmentEntity.getAttachment().replace(fileAttachmentEntity.getFileType(), "") + " " + fileAttachmentEntity.getAttachment();
                    sSHServiceBs.execCommand(command);

                    fileAttachmentEntity.setHasDecompress(true);
                    //修改解压状态
                    fileAttachmentMapper.updateFileAttachment(fileAttachmentEntity);

                    //远程调用进行数据同步
                    //获取远程调用路径
                    String url = PublicUtils.getConfigMap().get("fileServer")+ Peripheral_Enum.sugonfilerest_data2Mpp_uploadFile.getCode();
                    MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
                    paramMap.add("userId", user.getId());
                    paramMap.add("fileAttachmentId", fileAttachmentEntity.getId());
                    HttpHeaders headers = new HttpHeaders();
                    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap,headers);
                    RestResult<Void> response = restTemplate.postForObject(url, httpEntity, RestResult.class);
                    if(!CollectionUtils.isEmpty(response.getErrorList())){
                        errorList.addAll(response.getErrorList());
                        return;
                    }
                    //修改数据同步状态
                    fileAttachmentEntity.setHasImport(true);
                    fileAttachmentMapper.updateFileAttachment(fileAttachmentEntity);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SUGON_SSH_001.getCode(),ErrorCode_Enum.SUGON_SSH_001.getMessage(),e.toString()));
        }finally {
            if(null != session){
                sSHServiceBs.closeSession(session);
            }
        }
    }

    public int uploadFile(User user, List<MultipartFile> files, Long caseId, List<Error> errorList) throws Exception {
        //在配置页面配置文件的上传路径
        String uploadPath = PublicUtils.getConfigMap().get("fileUploadPath");

        File folder = new File(uploadPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Calendar calendar = Calendar.getInstance();
        String year =String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day = String.valueOf(calendar.get(Calendar.DATE));
        String fileServerBasePath = PublicUtils.getConfigMap().get("fileServerBasePath");
        String fileServerPAth = fileServerBasePath + "/"+year+"/"+month+"/"+day+"/"+caseId+"/";
        Session session = new SSHConfig().getSession();
        SSHServiceBs sSHServiceBs = new SSHServiceBs(session);
        sSHServiceBs.listFiles(fileServerPAth,true);

        //文件上传组件
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        int i = 0;
        List<FileAttachmentEntity> fileAttachmentEntityList = new ArrayList<>();
        //把文件临时存入服务器，并且把文件信息记录进表。并且上传到
        for (MultipartFile fileItem : files) {
            String fileName = fileItem.getOriginalFilename();
            String md5FileName = DigestUtils.md5DigestAsHex((fileName+System.currentTimeMillis()+new Random().nextInt(99)).getBytes());
            String size =String.valueOf(fileItem.getSize());
            String type= "";

            if(fileName.contains(".")){
                String str1=fileName.substring(0, fileName.indexOf("."));
                type=fileName.substring(str1.length(), fileName.length());
                if(!PublicUtils.getConfigMap().get("fileType").contains(type)){
                    errorList.add(new Error(ErrorCode_Enum.IRIS_00_002.getCode(),ErrorCode_Enum.IRIS_00_002.getMessage(),fileName));
                    continue;
                }
            }else {
                errorList.add(new Error(ErrorCode_Enum.IRIS_00_002.getCode(),ErrorCode_Enum.IRIS_00_002.getMessage(),fileName));
                continue;
            }


            String attachment = fileServerPAth+md5FileName+type;
            FileAttachmentEntity fileAttachmentEntity = new FileAttachmentEntity();
            fileAttachmentEntity.setAttachment(attachment);
            fileAttachmentEntity.setCaseId(caseId);
            fileAttachmentEntity.setFileName(fileName);
            fileAttachmentEntity.setFileType(type);
            fileAttachmentEntity.setFileSize(size);
            fileAttachmentEntity.setUserId(user.getId());
            fileAttachmentEntity.setHasDecompress(false);
            fileAttachmentEntity.setHasImport(false);
            //如果是经侦角色，则默认模板组
            if(user.isEconomicUser()){
                fileAttachmentEntity.setTemplateGroupId(1L);
            }
            fileAttachmentEntityList.add(fileAttachmentEntity);

            FileOutputStream fout = null;
            InputStream is = null;
            try {
                //io 输入流读文件
                is = fileItem.getInputStream();
                //利用输出流写入对应的文件夹
                fout = new FileOutputStream(new File(uploadPath + "/" + md5FileName+type));

                //写入数据
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = (is.read(buffer))) > -1) {
                    fout.write(buffer, 0, len);
                }
            }catch (Exception e){
                   e.printStackTrace();
                   errorList.add(new Error(ErrorCode_Enum.SYS_STORE_001.getCode(),ErrorCode_Enum.SYS_STORE_001.getMessage(),fileName));
            }finally {
                if(null!= fout){
                    try{
                        fout.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(null!= fout){
                    try{
                        is.close();
                    }catch (Exception e){
                    e.printStackTrace();
                }
                }
            }
            //文件上传到文件服务器，使用一次SSHServiceBs对象创建一次，可能有文件不被释放的情况
            //sSHServiceBs = new SSHServiceBs(new SSHConfig().getSession());
            sSHServiceBs.uploadFile(fileServerPAth+md5FileName+type,uploadPath + "/" + md5FileName+type);
            sSHServiceBs.closeSession(session);
            //删除本地文件
            File file = new File(uploadPath + "/" + md5FileName+type);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            i++;
        }
        //把文件信息入库
        fileAttachmentMapper.batchFileAttachmentInsert(fileAttachmentEntityList);
        return i;
    }

    @Override
    public int deleteFile(User user, String[] arr,boolean flag ,List<Error> errorList) throws Exception {
        List<String> arrList = new ArrayList<>(Arrays.asList(arr));
        int j = 0;
        if(flag && StringUtils.isNotEmpty(PublicUtils.getConfigMap().get("file_delete_time"))) {
            String time = PublicUtils.getConfigMap().get("file_delete_time").trim().replaceAll("^\\s*$", "");
            Pattern pattern = Pattern.compile("^-?[0-9]+");
            if (StringUtils.isNotEmpty(time) && pattern.matcher(time).matches()) {
                List<DeclarationDetailDto> declarationDetailDtoList = new ArrayList<>();
                for (Iterator<String> it = arrList.iterator(); it.hasNext();) {
                    String   id = it.next();
                    FileAttachmentEntity fleAttachmentEntity = new FileAttachmentEntity();
                    fleAttachmentEntity.setId(Long.parseLong(id));
                    fleAttachmentEntity.setUserId(user.getId());
                    //查询到文件服务器的文件路径进行删除
                    FileAttachmentEntity fileAttachmentEntity = fileAttachmentMapper.selectFileAttachmentList(fleAttachmentEntity).get(0);
                    Long createTime = fileAttachmentEntity.getCreateTime().getTime();
                    Long nowTime = new Date().getTime();

                    if ((nowTime - createTime) / 1000 / 60 / 60 > Long.parseLong(time)) {
                        DeclarationDetailDto declarationDetailDto = new DeclarationDetailDto();
                        declarationDetailDto.setDetail(DETAIL + fileAttachmentEntity.getFileName());
                        declarationDetailDto.setOwnerUserId(user.getId());
                        declarationDetailDto.setStatus(PublicRuleUtils.ZERO);
                        declarationDetailDto.setBusinessType(PublicRuleUtils.TWO);
                        declarationDetailDto.setBusinessId(fileAttachmentEntity.getId());
                        declarationDetailDtoList.add(declarationDetailDto);
                        //删除数组中对应的值
                        it.remove();
                        j++;
                    }
                }
                if(!CollectionUtils.isEmpty(declarationDetailDtoList)){
                    declarServiceImpl.saveDeclaration(user, declarationDetailDtoList, errorList);
                }
                if (arrList.size() == 0) {
                    return j;
                }
            }
        }
        arr  = (String[])arrList.toArray(new String[arrList.size()]);

        int i = 0;
        for(String id :arr ){
            //查询到文件服务器的文件路径进行删除
            FileAttachmentEntity fileAttachmentEntity = fileAttachmentMapper.selectFileAttachmentByPrimaryKey(Long.parseLong(id));
            if(null == fileAttachmentEntity){
              return  j;
            }
            Session session = new SSHConfig().getSession();
            SSHServiceBs sSHServiceBs = new SSHServiceBs(session);
            sSHServiceBs = new SSHServiceBs(new SSHConfig().getSession());
            //删除压缩文件
            sSHServiceBs.deleteFile(fileAttachmentEntity.getAttachment());

            //删除解压文件
            String path = fileAttachmentEntity.getAttachment().substring(0,fileAttachmentEntity.getAttachment().lastIndexOf("."));
            String command = "rm -rf  " + path;
            sSHServiceBs.execCommand(command);
            //关闭session
            sSHServiceBs.closeSession(session);
            //删除mpp数据库的数据
            //通过fleAttachmentEntity的id编号获取文件信息
            FileDetailEntity fileDetailEntitySql = new FileDetailEntity();
            fileDetailEntitySql.setFileAttachmentId(fileAttachmentEntity.getId());
            List<FileDetailEntity> fileDetailEntityList = fileDetailMapper.selectFileDetailList(fileDetailEntitySql);
            //删除mpp数据
            for(FileDetailEntity fileDetailEntityBean : fileDetailEntityList){
                if(StringUtils.isEmpty(fileDetailEntityBean.getTableName())){
                    continue;
                }
                String sql = "delete from  "+fileDetailEntityBean.getTableName()+" where file_attachment_id ='"+fileAttachmentEntity.getId()+"'";
                mppMapper.mppSqlExec(sql);
            }
            //删除文件信息
            fileDetailMapper.deleteFileDetailByFileAttachmentId(fileAttachmentEntity.getId());
            fileAttachmentMapper.deleteFileAttachmentById(fileAttachmentEntity);
        }
        return i+j;
    }

    @Override
    public List<FileAttachmentDto> findFileAttachmentList(FileAttachmentDto fileAttachmentDto , List<Error> errorList) throws IllegalAccessException {
        List<FileAttachmentDto> FileAttachmentDtoList = new ArrayList<>();
        FileAttachmentEntity fleAttachmentEntity = new FileAttachmentEntity();
        PublicUtils.trans(fileAttachmentDto,fleAttachmentEntity);
        List<FileAttachmentEntity> fileAttachmentEntityList = fileAttachmentMapper.selectFileAttachmentList(fleAttachmentEntity);
        for(FileAttachmentEntity fileAttachmentEntity: fileAttachmentEntityList){
            FileAttachmentDto fileAttachmentDtoBean = new FileAttachmentDto();
            //查询申报状态
            DeclarationDetailEntity declarationDetailEntitySql = new DeclarationDetailEntity();
            declarationDetailEntitySql.setBusinessType(PublicRuleUtils.TWO);
            declarationDetailEntitySql.setBusinessId(fileAttachmentEntity.getId());
            List<DeclarationDetailEntity> declarationDetailEntityList = declarMapper.findDeclarationDetail(declarationDetailEntitySql);
            if(CollectionUtils.isEmpty(declarationDetailEntityList)){
                fileAttachmentDtoBean.setDeclarationStatus(PublicRuleUtils.ZERO);
            }else if(PublicRuleUtils.ZERO.equals(declarationDetailEntityList.get(0).getStatus())){
                fileAttachmentDtoBean.setDeclarationStatus(PublicRuleUtils.ONE);
            }else if(PublicRuleUtils.TWO.equals(declarationDetailEntityList.get(0).getStatus())){
                fileAttachmentDtoBean.setDeclarationStatus(PublicRuleUtils.TWO);
            }

            PublicUtils.trans(fileAttachmentEntity,fileAttachmentDtoBean);
            FileAttachmentDtoList.add(fileAttachmentDtoBean);
        }
        return FileAttachmentDtoList;
    }

    @Override
    public Integer csvReadOperation(String userId, Long caseId, String filePath) {


        return null;
    }

    /**
     * 修改模板组
     *
     * @param user
     * @param fileAttachmentDto
     * @param errorList
     */
    @Override
    public Integer updateFileAttachmentTemplateGroup(User user, FileAttachmentDto fileAttachmentDto, List<Error> errorList) throws IllegalAccessException {

        FileAttachmentEntity fileAttachmentEntity = new FileAttachmentEntity();
        PublicUtils.trans(fileAttachmentDto,fileAttachmentEntity);
        fileAttachmentEntity.setUserId(user.getId());
        fileAttachmentMapper.updateFileAttachmentTemplateGroup(fileAttachmentEntity);
        return null;
    }

    @Override
    public String getFileServerIp() {
        if("1".equals(PublicUtils.getConfigMap().get("environment"))){
            return "localhost:8092";
        }
        return PublicUtils.getConfigMap().get("webSocket_fileRest");
    }

    /**
     *
     * @param file:指定文件夹
     * @param fileType：是否只取压缩文件 0：除压缩包；1：除普通csv、Excel、Excels；2：两者都有
     */
    private List<FileInfoBean> getFileNameAndPath(File file , String fileType) {
        //存放文件名和它的绝对路径
        List<FileInfoBean> fileList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File tmpFile :files){
            if (tmpFile.isDirectory()){//当前是文件夹 继续递归
                getFileNameAndPath(tmpFile,fileType);
            }else {//当前是文件
                //获取最后一个.的位置
                int lastIndexOf = tmpFile.getAbsolutePath().lastIndexOf(".");
                //获取文件的后缀名
                String suffix = tmpFile.getAbsolutePath().substring(lastIndexOf);

                if(PublicRuleUtils.ZERO.equals(fileType)){
                    if(suffix.equals(CSV) || suffix.equals(XLSX) || suffix.equals(XLS)){
                        getFileList(fileList, tmpFile, suffix);
                    }
                }else if(PublicRuleUtils.ONE.equals(fileType)){
                    if (suffix.equals(RAR) || suffix.equals(ZIP)){
                        getFileList(fileList, tmpFile, suffix);
                    }
                }else if(PublicRuleUtils.TWO.equals(fileType)){
                    if(suffix.equals(CSV) || suffix.equals(XLSX) || suffix.equals(XLS) || suffix.equals(RAR) || suffix.equals(ZIP)){
                        getFileList(fileList, tmpFile, suffix);
                    }
                }
            }
        }
        return fileList;

    }

    private void getFileList(List<FileInfoBean> fileList, File tmpFile, String suffix) {
        FileInfoBean fileInfoBean = new FileInfoBean();
        fileInfoBean.setFileName(tmpFile.getName());
        fileInfoBean.setAbsolutePath(tmpFile.getAbsolutePath());
        fileInfoBean.setFileTypeEnum(FileType_Enum.getEnumByType(suffix));
        fileList.add(fileInfoBean);
    }

    private  boolean isWindows(){
        if (System.getProperties().getProperty("os.name").toLowerCase().contains("windows")) {
            return true;
        }
        return false;
    }

    private void deleteFolder(String path) {

    }


    private void createFolder(String path) {

    }
}
