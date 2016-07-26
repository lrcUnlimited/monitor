package com.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;

@Controller
@RequestMapping("/file")
public class FileAction {
    /*
     * 上传文件的两种方法、 1、一种用参数接收 2、解析request
     */
    /**
     * 参数接收形式上传文件
     */
    @RequestMapping("/upload")
    public String uploadFile(@RequestParam("file") CommonsMultipartFile file,
                             HttpServletRequest request) throws IOException {
        System.out.println("fileName : " + file.getOriginalFilename());

        if (!file.isEmpty()) {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream("D:/" + file.getOriginalFilename()));
            InputStream in = file.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            int n = 0;
            byte[] b = new byte[1024];
            while ((n = bis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bos.flush();
            bos.close();
            bis.close();
        }
        return "/success";
    }

    /**
     * springMVC封装的解析request上传文件（快捷、与springMVC很好结合，首选）
     */
    @RequestMapping("/upload2")
    public String uploadFile2(@RequestParam("file") CommonsMultipartFile file,
                              HttpServletRequest request) throws IOException {
        // 定义解析器去解析request
        CommonsMultipartResolver mutilpartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //request如果是Multipart类型、
        if (mutilpartResolver.isMultipart(request)) {
            //强转成 MultipartHttpServletRequest
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取文件名
            Iterator<String> it = multiRequest.getFileNames();
            while (it.hasNext()) {
                //获取MultipartFile类型文件
                MultipartFile fileDetail = multiRequest.getFile(it.next());
                if (fileDetail != null) {
                    String fileName = "demoUpload" + fileDetail.getOriginalFilename();
                    String path = "D:/" + fileName;
                    File localFile = new File(path);
                    //将上传文件写入到指定文件出、核心！
                    fileDetail.transferTo(localFile);
                    //非常重要、有了这个想做什么处理都可以
                    //fileDetail.getInputStream();
                }
            }
        }
        return "/success";
    }

    @RequestMapping("toUpload")
    public String tpUpload() {
        return "/upload";
    }

    @RequestMapping("/download")
    public String download(String fileName, HttpServletRequest request,
                           HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        try {
            //String path = Thread.currentThread().getContextClassLoader().getResource("").getPath()
            String path = "D:"
                    + "download";//这个download目录为啥建立在classes下的
            InputStream inputStream = new FileInputStream(new File(path
                    + File.separator + fileName));

            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }

            // 这里主要关闭。
            os.close();

            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  返回值要注意，要不然就出现下面这句错误！
        //java+getOutputStream() has already been called for this response
        return null;
    }
}
