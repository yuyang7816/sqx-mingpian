package com.sqx.modules.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.freewayso.image.combiner.ImageCombiner;
import com.freewayso.image.combiner.enums.OutputFormat;
import com.freewayso.image.combiner.enums.ZoomMode;
import com.sqx.common.exception.CommonException;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.file.utils.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
public class ImageWaterUtils {

    private static CommonInfoService commonInfoService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setImageWaterUtils(CommonInfoService commonInfoService) {
        ImageWaterUtils.commonInfoService = commonInfoService;
    }

    public static String toImageWater(String bgImageUrl, String waterMarkUrl) throws Exception {

        BufferedImage waterMark = ImageIO.read(new URL(waterMarkUrl)); //水印图
        String suffix = bgImageUrl.substring(Objects.requireNonNull(bgImageUrl).lastIndexOf("."));
        //创建合成器（指定背景图和输出格式，整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
        ImageCombiner combiner = new ImageCombiner(bgImageUrl, OutputFormat.JPG);  //v1.1.4之后可以指定背景图新宽高了（不指定则默认用图片原宽高）



        int x = (combiner.getCanvasWidth() - (waterMark.getWidth()/2)-50);
        int y = (combiner.getCanvasHeight() - (waterMark.getHeight()/2)-10);
        combiner.addImageElement(waterMark, x, y,  (waterMark.getWidth()/2)-50,  (waterMark.getHeight()/2)-10, ZoomMode.Height)
                .setAlpha(0.2F);      //透明度，0.0~1.0
        //执行图片合并
        combiner.combine();
        //可以获取流（并上传oss等）
        InputStream is = combiner.getCombinedImageStream();
        String value = commonInfoService.findOne(234).getValue();
        if ("1".equals(value)) {
            String completePath = getPath(suffix);
            OSS ossClient = new OSSClientBuilder().build(commonInfoService.findOne(68).getValue(), commonInfoService.findOne(69).getValue(), commonInfoService.findOne(70).getValue());
            ossClient.putObject(commonInfoService.findOne(71).getValue(), completePath, is);
            // 关闭OSSClient。
            ossClient.shutdown();
            //        String src = commonRepository.findOne(72).getValue()+"/"+completePath;
            return commonInfoService.findOne(19).getValue() + "/img/" + completePath;
        } else {
            try {
                String http = commonInfoService.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath = "/www/wwwroot/" + split[1] + "/file/uploadPath";
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                // 将BufferedImage对象的数据写入ByteArrayOutputStream对象
                ImageIO.write(combiner.getCombinedImage(), "jpg", outputStream);

                // 创建MultipartFile对象
                MultipartFile multipartFile = new MockMultipartFile(bgImageUrl.substring(bgImageUrl.lastIndexOf('/') + 1), bgImageUrl.substring(bgImageUrl.lastIndexOf('/') + 1), "jpg", is);
                String fileName = FileUploadUtils.upload(filePath, multipartFile);
                return http + fileName;
            } catch (Exception e) {
                log.error("文件上传失败:", e);
                throw new CommonException("文件上传失败");
            }
        }
    }

    private static String getPath(String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = format(new Date()) + "/" + uuid;
        return path + suffix;
    }

    private static String format(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            return df.format(date);
        }
        return null;
    }
}
