package com.sugon.iris.sugoncommon.fileUtils;

import com.sugon.iris.sugondomain.dtos.fileDtos.FieldDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.MppTableDto;
import org.apache.commons.collections.CollectionUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Desc 导出csv
 * @Date
 */
public class CsvExportUtil {
    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = "&&&&";

    /**
     * CSV文件行分隔符
     */
    private static final String CSV_ROW_SEPARATOR = System.lineSeparator();

    /**
     * @param os       输出流
     */
    public static void doExport(MppTableDto mppTableDto, OutputStream os) throws Exception {

        // 保证线程安全
        StringBuffer buf = new StringBuffer();

        // 组装表头
        for (FieldDto fieldDto : mppTableDto.getCols()) {
            buf.append(fieldDto.getFieldKey()).append(CSV_COLUMN_SEPARATOR);
        }
        buf.append(CSV_ROW_SEPARATOR);

        // 组装数据
        if (CollectionUtils.isNotEmpty(mppTableDto.getRows())) {
            for (Map<String, Object> data : mppTableDto.getRows()) {
                for (FieldDto fieldDto : mppTableDto.getCols()) {
                    buf.append(data.get(fieldDto.getFieldName())).append(CSV_COLUMN_SEPARATOR);
                }
                buf.append(CSV_ROW_SEPARATOR);
            }
        }

        // 写出响应
        os.write(buf.toString().getBytes("GBK"));
        os.flush();
    }

    /**
     * 设置Header
     *
     * @param fileName
     * @param response
     * @throws UnsupportedEncodingException
     */
    public static void responseSetProperties(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置文件后缀
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = fileName + sdf.format(new Date()) + ".csv";
        // 读取字符编码
        String utf = "UTF-8";

        // 设置响应
        response.setContentType("application/ms-txt.numberformat:@");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=120");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }

}