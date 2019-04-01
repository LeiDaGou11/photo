package com.kf.photo;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Controller
public class photoController {

    @Value("${pic_real_path}")
    private String pic_real_path;

    @RequestMapping("")
    public String index() {
        return "index";
    }

    private static final int limitSize = 1024*1024*10;

    @RequestMapping("upload")
    @ResponseBody
    public AjaxResult upload(HttpServletRequest request) {
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("utf-8");

            List<FileItem> fileList = upload.parseRequest(request);
            if(fileList!=null && fileList.size()>0) {
                FileItem fileItem = fileList.get(0);

                //大小限制
                if(fileItem.getSize() > limitSize) {
                    throw new BusinessException("上传照片大小限制为10MB");
                }

                String savePath=pic_real_path+"/"+fileItem.getName();
                System.out.println("savePath"+savePath);
                File saveFile = new File(savePath);
                if(!saveFile.getParentFile().exists()) {
                    saveFile.mkdirs();
                }
                fileItem.write(saveFile);

                //改变长宽
                Thumbnails.of(savePath).size(295, 413).keepAspectRatio(false).toFile("D:/home/share2/" + fileItem.getName());
                //改变大小
                File tempFile = new File("D:/home/share2/" + fileItem.getName());
                double scale = 1.0d ;
                if(tempFile.length() > 1024*200) {
                    scale = (200*1024f) / fileItem.getSize() ;
                    Thumbnails.of("D:/home/share2/" + fileItem.getName()).scale(1f).outputQuality(scale).outputFormat("jpg").toFile("D:/home/share2/"+fileItem.getName());
                }


                String accessPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
                        + "/file/"+fileItem.getName();
                String finalPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
                        + "/file2/"+fileItem.getName();
                System.out.println("accessPath:"+accessPath);
                System.out.println("finalPath:"+finalPath);

                return AjaxResult.newInstance().doSuccess("图片处理成功",accessPath+","+finalPath);

            }else{
                return AjaxResult.newInstance().doFail("服务器未接收照片文件");
            }
        } catch (BusinessException e) {
            return AjaxResult.newInstance().doFail(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.newInstance().doFail("请刷新页面后重试");
        }
    }

    @RequestMapping(value = "downLoad")
    @ResponseBody
    public void downLoad(String path, HttpServletResponse response) {
        try {
            if(path == null || path.length() == 0) {
                throw new BusinessException("请重新上传照片");
        }
            //path = "http://localhost:8084/file2/1.jpg";
            int index = path.lastIndexOf("/");
            String name = path.substring(index+1);

            String url="D:/home/share2/"+name;

            File file = new File(url);

            FileImageInputStream  fs = new FileImageInputStream (file);
            int streamLength = (int)fs.length();
            byte[] image = new byte[streamLength ];
            fs.read(image,0,streamLength );
            fs.close();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","attachment;filename="+name);
            response.getOutputStream().write(image );
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //return response;
    }





}
