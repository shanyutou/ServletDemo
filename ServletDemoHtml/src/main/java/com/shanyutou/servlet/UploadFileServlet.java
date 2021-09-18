package com.shanyutou.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(urlPatterns = {"/UploadFileServlet"})
public class UploadFileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /**
         * 检测是否是多媒体上传
         */

        if (!ServletFileUpload.isMultipartContent(req)){
            PrintWriter writer = resp.getWriter();
            writer.println("error：表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }

        /**
         * 配置参数
         */
        //配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置内存临界值
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        //设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        //设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        //设置最大请求值
        upload.setSizeMax(MAX_REQUEST_SIZE);

        //中文处理
        upload.setHeaderEncoding("UTF-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = req.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;

        /**
         * 检查目录
         */

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }

        /**
         * 处理文件
         */

        try {

            List<FileItem> formItms = upload.parseRequest(req);

            if (formItms != null && formItms.size() > 0){
                for (FileItem item: formItms){
                    if (!item.isFormField()){
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        System.out.println(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                        req.setAttribute("message",
                                "文件上传成功!");
                    }
                }
            }

        } catch (Exception e){
            req.setAttribute("message", "错误信息" + e.getMessage());

        } finally {
            req.getServletContext().getRequestDispatcher("/message.jsp").forward(req,resp);
        }

        /**
         * 结果处理
         */

    }
}
