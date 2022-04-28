package com.sugon.iris.sugonservice.impl.statisticsServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db4.CasePersonnelInfoMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.CasePersonnelInfoDto;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.CentrostigmaDto;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.InOutDto;
import com.sugon.iris.sugondomain.dtos.abnormalTradingDtos.TradingDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.abnormalTrading.CasePersonnelInfoEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.abnormalTrading.TradingEntity;
import com.sugon.iris.sugonservice.service.statisticsService.StatisticsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    DateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

    @Resource
    private MppMapper mppMapper;

    @Resource
    private CasePersonnelInfoMapper casePersonnelInfoMapper;
    /**
     * 从0时开始，以一个小时为步长统计转入交易次数
     *
     * @param userId
     * @param caseId
     */
    @Override
    public List<CentrostigmaDto> centrostigmaInAnalyse(Long userId, Long caseId,List<Error> errorList) {
            List<CentrostigmaDto> centrostigmaDtoList = new ArrayList<>();
            String sql = "select " +
                   "distinct  tradingHour ,count(1) OVER(PARTITION BY tradingHour ORDER BY tradingHour) AS count " +
                   "from (select  to_char(to_date(jysj,'yyyy-MM-dd HH24:mi:ss'),'HH24') tradingHour  from base_bank_jymx_"+caseId+"_"+userId+"_localfile"+" where sfbz = '进' ) order by tradingHour";
            List<Map<String,Object>> list =  mppMapper.mppSqlExecForSearchRtMapList(sql);

            for(Map map : list){
                CentrostigmaDto centrostigmaDto = new CentrostigmaDto();
                centrostigmaDto.setTimeQuantum((String) map.get("tradinghour"));
                centrostigmaDto.setTradingQuantity( map.get("count").toString());
                centrostigmaDtoList.add(centrostigmaDto);
            }
            return centrostigmaDtoList;
    }

    /**
     * 从0时开始，以一个小时为步长统计转出交易次数
     *
     * @param userId
     * @param caseId
     */
    @Override
    public List<CentrostigmaDto> centrostigmaOutAnalyse(Long userId, Long caseId,List<Error> errorList) {
        List<CentrostigmaDto> centrostigmaDtoList = new ArrayList<>();
        String sql = "select " +
                "distinct  tradingHour ,count(1) OVER(PARTITION BY tradingHour ORDER BY tradingHour) AS count " +
                "from (select  to_char(to_date(jysj,'yyyy-MM-dd HH24:mi:ss'),'HH24') tradingHour  from base_bank_jymx_"+caseId+"_"+userId+"_localfile"+" where sfbz = '出' ) order by tradingHour";
        List<Map<String,Object>> list =  mppMapper.mppSqlExecForSearchRtMapList(sql);

        for(Map map : list){
            CentrostigmaDto centrostigmaDto = new CentrostigmaDto();
            centrostigmaDto.setTimeQuantum((String) map.get("tradinghour"));
            centrostigmaDto.setTradingQuantity( map.get("count").toString());
            centrostigmaDtoList.add(centrostigmaDto);
        }
        return centrostigmaDtoList;
    }

    /**
     * 快进快出交易点分析
     *
     * @param userId
     * @param caseId
     * @param errorList
     */
    @Override
    public List<CasePersonnelInfoDto> getInOutTrading(Long userId, Long caseId, List<Error> errorList) {
        String  tableName_zhxx = "base_bank_zhxx_"+caseId+"_"+userId+"_localfile";
        String  tableName_jymx = "base_bank_jymx_"+caseId+"_"+userId+"_localfile";
        //通过案件编号获取案件下的人员信息
        List<CasePersonnelInfoEntity> casePersonnelInfoEntityList  = casePersonnelInfoMapper.getCasePersonnelInfo4InOut(tableName_zhxx);
        List<CasePersonnelInfoDto> casePersonnelInfoDtoList = new ArrayList<>();
        //对每一个人员进行遍历，获取快进快出交易点
        casePersonnelInfoEntityList.forEach(bean ->
              {
                CasePersonnelInfoDto casePersonnelInfoDto = new CasePersonnelInfoDto();
                PublicUtils.trans(bean,casePersonnelInfoDto);
                //获取该人员  收入 交易记录，找出对应的支出记录
                //1.通过证件号，获取所有的交易记录
                  List<TradingEntity> tradingEntityList =  casePersonnelInfoMapper.getTradingDetailByIdNo(tableName_jymx,bean.getIdNo());



                  //时间间隔，配置的单位为分钟
                  long interval = 30*60*1000;//默认30分钟
                  if(null != PublicUtils.getConfigMap().get("interval_inOut")) {
                      interval = Integer.parseInt(PublicUtils.getConfigMap().get("interval_inOut")) * 60 * 1000;//间隔单位为毫秒数
                  }

                  //获取偏差范围
                  BigDecimal offset = new BigDecimal(20);
                  if(null != PublicUtils.getConfigMap().get("offset_inOut")) {
                      offset =new BigDecimal(PublicUtils.getConfigMap().get("interval_inOut"));//间隔单位为毫秒数
                  }

                  List<InOutDto> inOutList = new ArrayList<>();
                  try {
                      if(!CollectionUtils.isEmpty(tradingEntityList)) {
                          this.inOutPoint(tradingEntityList, inOutList, interval, offset);
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
                  if(!CollectionUtils.isEmpty(inOutList)){
                      casePersonnelInfoDto.setInOutList(inOutList);
                      casePersonnelInfoDtoList.add(casePersonnelInfoDto);
                  }
             }
        );

        return casePersonnelInfoDtoList;
    }

    /**
     * 只进只出账户分析
     *
     * @param userId
     * @param caseId
     * @param errorList
     */
    @Override
    public List<CasePersonnelInfoDto> getInOrOutOnly(Long userId, Long caseId, List<Error> errorList) {
        List<CasePersonnelInfoDto> casePersonnelInfoDtoList = new ArrayList<>();
        String  tableName_zhxx = "base_bank_zhxx_"+caseId+"_"+userId+"_localfile";
        String  tableName_jymx = "base_bank_jymx_"+caseId+"_"+userId+"_localfile";
        //通过案件编号获取案件下的人员信息
        List<CasePersonnelInfoEntity> casePersonnelInfoEntityList  = casePersonnelInfoMapper.getCasePersonnelInfo4InOut(tableName_zhxx);

        if(CollectionUtils.isEmpty(casePersonnelInfoEntityList)){
           return casePersonnelInfoDtoList;
        }
        //key:账号accountNo，value：人员信息
        Map<String,CasePersonnelInfoEntity> map = new HashMap<>();
        for(CasePersonnelInfoEntity bean : casePersonnelInfoEntityList){
            map.put(bean.getAccountNo(),bean);
        }

        //获取该案件的  按jyzh,sfbz,jydszh分类后的其中一条数据,并且把满足要求的数据全部加载到内存
        List<TradingEntity> tradingEntityList = casePersonnelInfoMapper.selectTradingEntityClassifyBySfbz(tableName_jymx);

        List<TradingEntity> tradingEntityList4OnlyIn = new ArrayList<>();
        List<TradingEntity> tradingEntityList4OnlyOut = new ArrayList<>();
        //进行分类
        for(CasePersonnelInfoEntity casePersonnelInfoEntity : casePersonnelInfoEntityList){

            //1.1交易明细分成两类，一类相对于选定账户为只出不进，另一类相对于选定账户为只进不出
            for(TradingEntity TradingEntity : tradingEntityList){
               if(casePersonnelInfoEntity.getAccountNo().equals(TradingEntity.getReciprocalAccount())){
                   if("出".equals(TradingEntity.getReceiptsOrPaid())){
                       tradingEntityList4OnlyIn.add(TradingEntity);
                   }else if("进".equals(TradingEntity.getReceiptsOrPaid())){
                       tradingEntityList4OnlyOut.add(TradingEntity);
                   }
               }
            }
        }

        List<TradingEntity> tradingEntityList4OnlyInCopy = PublicUtils.deepCopy(tradingEntityList4OnlyIn);
        tradingEntityList4OnlyIn.removeAll(tradingEntityList4OnlyOut);

        tradingEntityList4OnlyOut.removeAll(tradingEntityList4OnlyInCopy);

        for(CasePersonnelInfoEntity casePersonnelInfoEntity : casePersonnelInfoEntityList){

            List<CasePersonnelInfoDto> casePersonnelInfoInList = new ArrayList<>();
            List<CasePersonnelInfoDto> casePersonnelInfoOutList = new ArrayList<>();

            for(TradingEntity in :  tradingEntityList4OnlyIn){
                if(casePersonnelInfoEntity.getAccountNo().equals(in.getReciprocalAccount())){

                    if(PublicUtils.trans(map.get(in.getAccountNo()),new CasePersonnelInfoDto()).getAccountNo() == null){
                        CasePersonnelInfoDto casePersonnelInfoDto = new CasePersonnelInfoDto();
                        casePersonnelInfoDto.setAccountNo(in.getAccountNo());
                        casePersonnelInfoInList.add(casePersonnelInfoDto);
                    }else{
                        casePersonnelInfoInList.add(PublicUtils.trans(map.get(in.getAccountNo()),new CasePersonnelInfoDto()));
                    }
                }
            }

            for(TradingEntity out :  tradingEntityList4OnlyOut){
                if(casePersonnelInfoEntity.getAccountNo().equals(out.getReciprocalAccount())){
                    if(PublicUtils.trans(map.get(out.getAccountNo()),new CasePersonnelInfoDto()).getAccountNo() == null){
                        CasePersonnelInfoDto casePersonnelInfoDto = new CasePersonnelInfoDto();
                        casePersonnelInfoDto.setAccountNo(out.getAccountNo());
                        casePersonnelInfoOutList.add(casePersonnelInfoDto);
                    }else {
                        casePersonnelInfoOutList.add(PublicUtils.trans(map.get(out.getAccountNo()),new CasePersonnelInfoDto()));
                    }
                }
            }

            if(!CollectionUtils.isEmpty(casePersonnelInfoInList) || !CollectionUtils.isEmpty(casePersonnelInfoOutList)){
                CasePersonnelInfoDto casePersonnelInfoDto = new CasePersonnelInfoDto();
                PublicUtils.trans(casePersonnelInfoEntity,casePersonnelInfoDto);
                casePersonnelInfoDto.setCasePersonnelInfoInList(casePersonnelInfoInList);
                casePersonnelInfoDto.setCasePersonnelInfoOutList(casePersonnelInfoOutList);
                casePersonnelInfoDtoList.add(casePersonnelInfoDto);
            }

        }

        return casePersonnelInfoDtoList;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        List<TradingEntity> tradingEntityList4OnlyIn = new ArrayList<>();
        List<TradingEntity> tradingEntityList4OnlyOut = new ArrayList<>();

        TradingEntity tradingEntity_01 = new TradingEntity();
        tradingEntity_01.setAccountNo("111");
        tradingEntity_01.setReciprocalAccount("222");
        tradingEntity_01.setReceiptsOrPaid("进");

        TradingEntity tradingEntity_03 = new TradingEntity();
        tradingEntity_03.setAccountNo("111a");
        tradingEntity_03.setReciprocalAccount("222");
        tradingEntity_03.setReceiptsOrPaid("进");

        TradingEntity tradingEntity_02 = new TradingEntity();
        tradingEntity_02.setAccountNo("111");
        tradingEntity_02.setReciprocalAccount("222");
        tradingEntity_02.setReceiptsOrPaid("出");

        TradingEntity tradingEntity_04 = new TradingEntity();
        tradingEntity_04.setAccountNo("111b");
        tradingEntity_04.setReciprocalAccount("222");
        tradingEntity_04.setReceiptsOrPaid("出");

        tradingEntityList4OnlyIn.add(tradingEntity_01);
        tradingEntityList4OnlyIn.add(tradingEntity_03);
        tradingEntityList4OnlyOut.add(tradingEntity_02);
        tradingEntityList4OnlyOut.add(tradingEntity_04);

        List<TradingEntity> tradingEntityList4OnlyInBak = PublicUtils.deepCopy(tradingEntityList4OnlyIn);

        tradingEntityList4OnlyIn.removeAll(tradingEntityList4OnlyOut);

        System.out.println("--差集--" +tradingEntityList4OnlyIn);

        tradingEntityList4OnlyOut.removeAll(tradingEntityList4OnlyInBak);
        System.out.println("--差集--" +tradingEntityList4OnlyOut);
    }

    private void inOutPoint(List<TradingEntity> tradingEntityList, List<InOutDto> inOutList, long interval, BigDecimal offset) throws Exception{

        if(CollectionUtils.isEmpty(tradingEntityList)){
            return;
        }

        //把按照时间排序的，头部为“出”的交易删除
        for(Iterator<TradingEntity> it = tradingEntityList.iterator(); it.hasNext();){
            TradingEntity tradingEntity = it.next();

            if(!"进".equals(tradingEntity.getReceiptsOrPaid())){
                it.remove();
            }else {
                break;
            }
        }

        if(CollectionUtils.isEmpty(tradingEntityList)){
            return;
        }

        //进账交易
        List<TradingEntity> tradingEntityInList = new ArrayList<>();

        //出账交易
        List<TradingEntity> tradingEntityOutList = new ArrayList<>();

        for(TradingEntity tradingEntity : tradingEntityList){
            if("进".equals(tradingEntity.getReceiptsOrPaid())){
                tradingEntityInList.add(tradingEntity);
            }
            if("出".equals(tradingEntity.getReceiptsOrPaid())){
                if(StringUtils.isNotBlank(tradingEntity.getTradingAmount())){
                    //把交易金额是0的去除
                    if(new BigDecimal(tradingEntity.getTradingAmount()).compareTo(new BigDecimal("0"))==0){
                      continue;
                    }
                    //把负号去掉
                   if(tradingEntity.getTradingAmount().contains("-")){
                       tradingEntity.setTradingAmount(tradingEntity.getTradingAmount().replace("-",""));
                   }

                }else{
                    continue;
                }
                tradingEntityOutList.add(tradingEntity);
            }
        }

        //key:进账交易；value:出账交易
        Map<TradingDto,List<TradingDto>>  map = new LinkedHashMap<>();
        for_1 : for(TradingEntity tradingEntityIn : tradingEntityInList){
            BigDecimal inAmount = null;
            if(StringUtils.isNotBlank(tradingEntityIn.getTradingAmount())){
                inAmount = new BigDecimal(tradingEntityIn.getTradingAmount());
            }else{
                continue;
            }
            TradingDto tradingDtoIn = PublicUtils.trans(tradingEntityIn,new TradingDto());
            List<TradingDto> listOut4Map = new ArrayList<>();


            //找出金额正好匹配的
            for(Iterator<TradingEntity> it = tradingEntityOutList.iterator(); it.hasNext();){
                TradingEntity  tradingEntityOut = it.next();
                if(sdf.parse(tradingEntityIn.getTradingDate()).getTime()+interval >= sdf.parse(tradingEntityOut.getTradingDate()).getTime()){
                    //如果出账金额和进账金额匹配则直接进行对应
                    if(new BigDecimal(tradingEntityIn.getTradingAmount()).compareTo(new BigDecimal(tradingEntityOut.getTradingAmount()))==0){
                        listOut4Map.add(PublicUtils.trans(tradingEntityOut,new TradingDto()) );
                        map.put(tradingDtoIn,listOut4Map);
                        it.remove();
                        break for_1;
                    }
                }else{
                    break;
                }

            }

            //找出金额在偏差值范围内匹配的
            BigDecimal outAmount = new BigDecimal(0);
            for(Iterator<TradingEntity> it = tradingEntityOutList.iterator(); it.hasNext();){
                TradingEntity  tradingEntityOut = it.next();
                if(sdf.parse(tradingEntityIn.getTradingDate()).getTime()+interval >= sdf.parse(tradingEntityOut.getTradingDate()).getTime()){

                    outAmount = outAmount.add(new BigDecimal(tradingEntityOut.getTradingAmount()));

                    //进账金额和出账金额的比值。四舍五入取2位小数,再乘以100
                    BigDecimal specific = inAmount.divide(outAmount,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));

                    //如果出账金额和进账金额 偏差20%以内 进行对应
                    if(specific.compareTo(new BigDecimal(100).add(offset)) <= 0 && specific.compareTo(new BigDecimal(100).subtract(offset)) >= 0){
                        listOut4Map.add(PublicUtils.trans(tradingEntityOut,new TradingDto()) );
                        map.put(tradingDtoIn,listOut4Map);
                        it.remove();
                        break for_1;
                    }
                    //如果超过20%以上没有匹配则直接退出
                    else if(specific.compareTo(new BigDecimal(100).add(offset))> 0){
                        break for_1;
                    }
                }else{
                    break for_1;
                }

            }
        }

        Set set = map.entrySet();
        Iterator<Map.Entry<TradingDto,List<TradingDto>>> iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry<TradingDto,List<TradingDto>> entry = iterator.next();
            TradingDto key = (TradingDto) entry.getKey();
            List<TradingDto> value = (List<TradingDto>) entry.getValue();

            InOutDto inOutDto = new InOutDto();
            inOutDto.setTradingIn(PublicUtils.trans(key,new TradingDto()));
            inOutDto.getTradingOut().addAll(value);

            inOutList.add(inOutDto);
        }
    }
}
