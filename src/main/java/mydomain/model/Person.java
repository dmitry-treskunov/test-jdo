package mydomain.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Person
{
    @PrimaryKey
    String id;

    String name;

    Date birthday;

    List<String> groups;

    public Person(String id, String name, Date birthday, String... groups)
    {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.groups = Arrays.asList(groups);
    }
}
