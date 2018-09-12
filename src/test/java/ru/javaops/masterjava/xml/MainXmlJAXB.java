package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import com.hp.gagawa.java.elements.*;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;
import ru.javaops.masterjava.xml.util.XsltProcessor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class MainXmlJAXB {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws Exception {
        String projectName = "top";//args[0];
        URL payloadUrl = Resources.getResource("payload.xml");
        Payload payload = JAXB_PARSER.unmarshal(payloadUrl.openStream());
        Project project = payload.getProjects().getProject().stream().filter(i->projectName.equals(i.getName())).findFirst().get();
        Set<Group> groups = new HashSet<>(project.getGroups().getGroup());
        List<User> users = payload.getUsers().getUser().stream()
                .filter(i->!Collections.disjoint(i.getGroups(),groups))
//                .filter(i-> {
//                    boolean b = false;
//                    for (Object g :i.getGroups()){
//                        if (groups.contains((Group)g)) b = true;
//                    }
//                    return b;
//                })
                .collect(Collectors.toList());
        users = users.stream().sorted(Comparator.comparing(User::getFullName)).collect(Collectors.toList());
        users.forEach(i-> System.out.println(i.getFullName()+" - " + i.getEmail()));
        outHtml(users);

        String html = xslt(projectName,payloadUrl);
        try(Writer writer = new FileWriter("src\\test\\resources\\outXSLT.html")){
            writer.write(html);
        }catch (Exception e){}
    }

    private static String xslt(String projectName, URL payloadUrl){
        URL xsl = Resources.getResource("groups.xsl");
        String result = "";
        try (InputStream xslInputStream = xsl.openStream();
             InputStream xmlInputStream = payloadUrl.openStream()) {
            XsltProcessor processor = new XsltProcessor(xslInputStream);
            processor.addParametr("projectName", projectName);
            result = processor.transform(xmlInputStream);
        } catch (Exception e) { }
        return result;
    }

    private static void outHtml(List<User> users){
        Html html = new Html();
        Head head = new Head();
        Body body = new Body();
        H1 h1 = new H1();
        h1.appendText("Users");
        Div div = new Div();
        Table table = new Table();
        table.setBorder("1");
        table.setStyle("font-size:21px;");
        table.appendChild(new Tr().appendChild(new Th().appendText("name"), new Th().appendText("email")));
        users.forEach(i->
            table.appendChild(new Tr()
                    .appendChild(new Td().appendText(i.getFullName()))
                    .appendChild(new Td().appendText(i.getEmail()))));
        div.appendChild(table);
        body.appendChild(h1,div);
        html.appendChild(head,body);
        try(Writer writer = new FileWriter("src\\test\\resources\\out.html")){
            writer.write(html.write());
        }catch (Exception e){}
        System.out.println(html.write());
    }
}
