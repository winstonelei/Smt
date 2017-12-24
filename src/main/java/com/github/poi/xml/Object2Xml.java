package com.github.poi.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Created by winstone on 2017/12/24 0024.
 */
public class Object2Xml {
    public static String toXml(Object obj){
        XStream xstream=new XStream();
        xstream.processAnnotations(obj.getClass());

        return xstream.toXML(obj);
    }

    public static <T> T toBean(String xmlStr,Class<T> cls){
        XStream xstream=new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        @SuppressWarnings("unchecked")
        T obj=(T)xstream.fromXML(xmlStr);
        return obj;
    }
}
