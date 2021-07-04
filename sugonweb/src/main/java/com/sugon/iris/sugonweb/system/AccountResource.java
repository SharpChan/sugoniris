package com.sugon.iris.sugonweb.system;


import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugoncommon.redisUtils.RedisUtil;
import com.sugon.iris.sugoncommon.session.MySessionContext;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.emailService.EmailService;
import com.sugon.iris.sugonservice.service.systemService.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.session.Session;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountResource {
    private static final String FAILED = "FAILED";
    @Autowired
    private AccountService accountServiceImpl;

    @Autowired
    private EmailService emailServiceImpl;

    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/account/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RestResult<Integer> registerAccount(@Valid @RequestBody UserDto userDto) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(accountServiceImpl.saveAccount(userDto, errorList));
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("注册失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("注册成功");
        }
        return restResult;
    }

    @PostMapping("/account/login")
    @ResponseStatus(HttpStatus.CREATED)
    public RestResult<Integer> login(@Valid @RequestBody UserDto userDto, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        RestResult<Integer> restResult = new RestResult<Integer>();
        List<Error> errorList = new ArrayList<>();
        User user = null;
        try {
             user = accountServiceImpl.getUserInfo(userDto, errorList);
        }catch (Exception e){
            e.printStackTrace();
        }
        session .setAttribute("user",user);
        Cookie cookie = new Cookie("jsessionid", session.getId());
        response.addCookie(cookie);
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("登录失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("登录成功");
        }
        return restResult;
    }

    @PostMapping("/account/logOut")
    public void logOut( HttpServletRequest request){
        MySessionContext myc= MySessionContext.getInstance();
        Session sess = myc.getSession(request.getSession().getId());
        request.getSession().invalidate();
    }

    /*
    @PostMapping("/account/restPassword")
    @LogInCheck(doLock = true,doProcess = true)
    public void restPassword(@RequestParam(value = "email") String email, HttpServletResponse response) throws IOException, IllegalAccessException {

        Random rd = new Random();
        String password = "";
        int getNum;
        int getNum1;
        do {
            getNum = Math.abs(rd.nextInt()) % 10 + 48;// 产生数字0-9的随机数
            getNum1 = Math.abs(rd.nextInt())%26 + 97;//产生字母a-z的随机数
            char num1 = (char) getNum;
            char num2 = (char) getNum1;
            String dn = Character.toString(num1);
            String dn1 = Character.toString(num2);
            if(Math.random()>0.5){
                password += dn;
            }else{
                password += dn1;
            }
        } while (password.length() < 8 );
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        List<Error> errorList = new ArrayList<>();
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(re_md5);
        accountServiceImpl.updateUser(userDto,errorList);

        emailServiceImpl.sendMail(email,"密码",password);
        String[] strs = email.split("@");
        response.sendRedirect("http://"+strs[1]);
    }*/

    @PostMapping("/account/lock")
    @LogInCheck(doLock = true,doProcess = true)
    @ResponseStatus(HttpStatus.CREATED)
    public RestResult<Void> lock(@CurrentUser User user, HttpSession session){
        RestResult<Void> restResult = new RestResult<Void>();
        user.setLocked(true);
        session.setAttribute("user",user);
        return restResult;
    }

    @PostMapping("/account/unlock")
    @LogInCheck(doLock = false,doProcess = true)
    @ResponseStatus(HttpStatus.CREATED)
    public RestResult<Integer> unlock(@RequestParam(value = "password") String password, @CurrentUser User user, HttpSession session) throws IllegalAccessException {
        RestResult<Integer> restResult = new RestResult<Integer>();
        List<Error> errorList = new ArrayList<>();
        UserDto userDto = new UserDto();
        PublicUtils.trans(user,userDto);
        User userentity  = accountServiceImpl.getUserInfo(userDto, errorList);
        if(null != userentity && userentity.getPassword().equals(password)){
            user.setLocked(false);
            session.setAttribute("user",user);
        }else{
            errorList.add(new Error(ErrorCode_Enum.IRIS_00_003.getCode(),ErrorCode_Enum.IRIS_00_003.getMessage()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setObj(1);
            restResult.setFlag(FAILED);
            restResult.setMessage("解锁失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setObj(0);
            restResult.setMessage("解锁成功");
        }
        return restResult;
    }

    @PostMapping("/account/userCheck")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> userCheck(@RequestParam(value = "id") long  id,@RequestParam(value = "flag") int flag){
        RestResult<Integer> restResult = new RestResult<Integer>();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(accountServiceImpl.alterUserStatus(id,flag,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("审核失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("审核成功");
        }
        return restResult;
    }

    @PostMapping("/account/getUsers")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<User>> getUsers(@CurrentUser User user,@RequestParam(value = "flag") int flag,@RequestParam(value = "keyWord") String keyWord){
        RestResult<List<User>> restResult = new RestResult<List<User>>();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(accountServiceImpl.getUserInfoCheck(keyWord,flag,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("获取用户列表失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("获取用户列表成功");
        }
        return restResult;
    }

    @PostMapping("/account/deleteUser")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> getUsers(@RequestParam(value = "id") long id){
        RestResult<Integer> restResult = new RestResult<Integer>();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(accountServiceImpl.deleteUser(id,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("删除用户失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("删除用户成功");
        }
        return restResult;
    }
}
