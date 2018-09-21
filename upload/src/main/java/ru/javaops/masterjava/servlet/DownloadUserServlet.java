package ru.javaops.masterjava.servlet;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.javaops.masterjava.xml.schema.FlagType;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//@WebServlet(urlPatterns = "/result")
public class DownloadUserServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletFileUpload upload = new ServletFileUpload();
        List<User> list = new ArrayList<>();
        try {
            final FileItemIterator itemIterator = upload.getItemIterator(req);
            while (itemIterator.hasNext()){
                FileItemStream itemStream = itemIterator.next();
                InputStream is = itemStream.openStream();
                StaxStreamProcessor processor = new StaxStreamProcessor(is);
                while (processor.doUntil(XMLEvent.START_ELEMENT, "User")){
                    User user = new User();
                    user.setEmail(processor.getAttribute("email"));
                    user.setFlag(FlagType.fromValue(processor.getAttribute("flag")));
                    user.setValue(processor.getText());
                    list.add(user);
                }
            }
        } catch (FileUploadException | XMLStreamException e) {
            e.printStackTrace();
        }
        req.setAttribute("users", list);
        req.getRequestDispatcher("/WEB-INF/result.jsp").forward(req,resp);
    }
}
