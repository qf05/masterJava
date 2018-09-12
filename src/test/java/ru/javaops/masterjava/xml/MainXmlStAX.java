package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.*;


public class MainXmlStAX {

    public static void main(String[] args) throws Exception {
        String projectName = "top";//args[0];
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            Map<String, List<String>> users = new TreeMap<>(String::compareTo);
            List<String> groups = new ArrayList<>();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    if ("User".equals(reader.getLocalName())) {
                        String email = reader.getAttributeValue(2);
                        List<String> groupsUser = Arrays.asList(reader.getAttributeValue(3).split(" "));
                        reader.next();
                        reader.next();
                        users.put(reader.getElementText() + " - " + email, groupsUser);
                    }
                    if ("Project".equals(reader.getLocalName())) {
                        if (projectName.equals(reader.getAttributeValue(0))) {
                            int event1 = reader.next();
                            while (true) {
                                if (event1 == XMLEvent.END_ELEMENT){
                                    if ("Project".equals(reader.getLocalName())){
                                        break;
                                    }
                                }
                                if (event1 == XMLEvent.START_ELEMENT) {
                                    if ("Group".equals(reader.getLocalName())) {
                                        groups.add(reader.getAttributeValue(0));
                                    }
                                }
                                event1 = reader.next();
                            }
                        }
                    }
                }
            }
            for (Map.Entry<String, List<String>> entry : users.entrySet()) {
                if (!Collections.disjoint(entry.getValue(), groups)) {
                    System.out.println(entry.getKey());
                }
            }
        }
    }
}
