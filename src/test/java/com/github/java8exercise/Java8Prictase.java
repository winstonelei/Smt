package com.github.java8exercise;


import com.github.multiQuery.Person;
import com.github.serializer.KryoSerializer;
import com.github.serializer.ObjectSerializer;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author wanglei
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class Java8Prictase {
    //查找集合中的第一个对象
    @Test
    public void testFindOne(){
        List<Person> listPersons = new ArrayList<>();
        Person p1 = new Person();
        p1.setAge(20);
        p1.setName("zs");
        listPersons.add(p1);

        Person p3 = new Person();
        p3.setAge(21);
        p3.setName("lisi");
        listPersons.add(p3);

        Optional<Person> personOptional =listPersons.stream().filter(a->"lisi".equalsIgnoreCase(a.getName())).findFirst();
        System.out.println(personOptional.get().getName());
    }

    //查找集合中的所有对象
    @Test
    public void testFindAll(){
        List<Person> listPersons = new ArrayList<>();
        Person p1 = new Person();
        p1.setAge(20);
        p1.setName("zs");
        listPersons.add(p1);

        Person p3 = new Person();
        p3.setAge(21);
        p3.setName("lisi");
        listPersons.add(p3);

        List<Person> personList=listPersons.stream().filter(a->"lisi".equalsIgnoreCase(a.getName())).collect(Collectors.toList());
        System.out.println(personList.size());

        List<String> nameList = listPersons.stream().map(Person::getName).collect(Collectors.toList());
        System.out.println(nameList);
    }


    //实现根据某个属性数据分组
    @Test
    public void testGroupBy(){
        List<Person> listPersons = new ArrayList<>();
        Person p1 = new Person();
        p1.setAge(20);
        p1.setName("zs");
        listPersons.add(p1);

        Person p2 = new Person();
        p2.setAge(21);
        p2.setName("zs");
        listPersons.add(p2);

        Person p3 = new Person();
        p3.setAge(21);
        p3.setName("lisi");
        listPersons.add(p3);

        final Map<String, List<Person>> senderPersons =
                listPersons.stream().collect(Collectors.groupingBy(Person::getName));

        senderPersons.forEach((k,v)->{
            System.out.println(k+"="+v.size());
        });
    }



}
