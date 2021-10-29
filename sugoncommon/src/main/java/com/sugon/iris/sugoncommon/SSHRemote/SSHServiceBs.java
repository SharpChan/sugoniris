package com.sugon.iris.sugoncommon.SSHRemote;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SSHServiceBs {

    private Session session;

    public SSHServiceBs(Session session){
        this.session = session;
    }
    /**
     * 执行相关的命令
     *
     */
    public List<String> execCommand(String command) throws IOException {
        InputStream in = null;// 输入流(读)
        Channel channel = null;// 定义channel变量
        List<String> processDataStream = null;
        try {
            // 如果命令command不等于null
            if (!StringUtils.isEmpty(command)) {
                // 打开channel
                //说明：exec用于执行命令;sftp用于文件处理
                channel = session.openChannel("exec");
                // 设置command
                ((ChannelExec) channel).setCommand(command);
                // channel进行连接
                channel.connect();
                // 获取到输入流
                in = channel.getInputStream();
                // 执行相关的命令
                processDataStream = processDataStream(in);
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (channel != null) {
                channel.disconnect();
            }
            //closeSession(session);
        }
        return processDataStream;
    }

    /**
     * 上传文件 可参考:https://www.cnblogs.com/longyg/archive/2012/06/25/2556576.html
     *
     * @param directory
     *            上传文件的目录
     * @param uploadFile
     *            将要上传的文件
     */
    public void uploadFile(String directory, String uploadFile) throws IOException {
        ChannelSftp channelSftp = null;
        FileInputStream in = null;
        try {
            // 打开channelSftp
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            // 远程连接
            channelSftp.connect();
            // 创建一个文件名称问uploadFile的文件
            File file = new File(uploadFile);
            // 将文件进行上传(sftp协议)
            // 将本地文件名为src的文件上传到目标服务器,目标文件名为dst,若dst为目录,则目标文件名将与src文件名相同.
            // 采用默认的传输模式:OVERWRITE
            in = new FileInputStream(file);
            channelSftp.put(in, directory, ChannelSftp.OVERWRITE);
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(null != in) {
                in.close();
            }
            // 切断远程连接
            if(null != channelSftp) {
                channelSftp.exit();
            }
            //closeSession(session);
        }
    }

    /**
     * 下载文件 采用默认的传输模式：OVERWRITE
     *
     * @param src
     *            linux服务器文件地址
     * @param dst
     *            本地存放地址
     * @throws JSchException
     * @throws SftpException
     */
    public void fileDownload(String src, String dst)  {
        // src 是linux服务器文件地址,dst 本地存放地址
        ChannelSftp channelSftp = null;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            // 远程连接
            channelSftp.connect();
            // 下载文件,多个重载方法
            channelSftp.get(src, dst);
        }catch (Exception e){
                e.printStackTrace();
        }finally {
            // 切断远程连接,quit()等同于exit(),都是调用disconnect()
            if(null != channelSftp) {
                channelSftp.quit();
            }
           // closeSession(session);
        }

    }

    /**
     * 删除文件
     *
     * @param
     * @throws SftpException
     * @throws JSchException
     */
    public void deleteFile(String directoryFile) {
        // 打开openChannel的sftp
        ChannelSftp channelSftp = null;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            // 远程连接
            channelSftp.connect();
            File targetFile = new File(directoryFile);
            // 删除文件
            if(targetFile.exists()) {
                channelSftp.rm(directoryFile);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 切断远程连接
            if(null != channelSftp){
                channelSftp.exit();
            }
           // closeSession(session);
        }

    }

    /**
     * 列出目录下的文件
     *
     * @param directory
     *            要列出的目录
     * @return
     * @throws SftpException
     * @throws JSchException
     */
    public Vector listFiles(String directory,boolean createFile) {
        ChannelSftp channelSftp = null;
        Vector ls = null;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            // 远程连接
            channelSftp.connect();
            // 显示目录信息
            ls = channelSftp.ls(directory);
        }catch (Exception e){
            //e.printStackTrace();
            try {
                if(createFile) {
                    //channelSftp.mkdir(directory);
                    execCommand("mkdir -p "+directory);
                }else{
                    e.printStackTrace();
                }
            }catch (Exception f){
                f.printStackTrace();
            }

        }finally {
            // 切断连接
            if(null != channelSftp){
                channelSftp.exit();
            }
           // closeSession(session);
        }

        return ls;
    }

    /**
     * 对将要执行的linux的命令进行遍历
     */
    private List<String> processDataStream(InputStream in) throws Exception {
        List<String> sb = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String result = "";
        try {
            while ((result = br.readLine()) != null) {
                sb.add(result);
            }
        } catch (Exception e) {
            throw new Exception("获取数据流失败: " + e);
        } finally {
            br.close();
        }
        return sb;
    }

    /**
     * 关闭连接
     */
    public void closeSession(Session session) {
        // 调用session的关闭连接的方法
        if (session != null) {
            // 如果session不为空,调用session的关闭连接的方法
            session.disconnect();
        }
    }
}
