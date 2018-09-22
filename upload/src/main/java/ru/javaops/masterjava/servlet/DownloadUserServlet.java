package ru.javaops.masterjava.servlet;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//@WebServlet(urlPatterns = "/result")
@MultipartConfig
public class DownloadUserServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletFileUpload upload = new ServletFileUpload();
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        JaxbUnmarshaller unmarshaller = parser.createUnmarshaller();
        List<User> list = new ArrayList<>();
        try {
            Part part = req.getPart("file");
            try (InputStream is = part.getInputStream()) {
                StaxStreamProcessor processor = new StaxStreamProcessor(is);
                while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                    User user = unmarshaller.unmarshal(processor.getReader(), User.class);
                    list.add(user);
                }
            }
        } catch (XMLStreamException | JAXBException e) {
            e.printStackTrace();
        }
        req.setAttribute("users", list);
        req.getRequestDispatcher("/WEB-INF/result.jsp").forward(req, resp);
    }
}
