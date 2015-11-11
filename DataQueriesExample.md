# Data Queries Examples #

Suppose these 2 entities:

```
public Class Department extands AbstractEntity 
{
    private String name;

    private String streetAddress;

    private String city;
}
```

```
public class Person extends AbstractEntity 
{

    private String name;

    private String personId;

    private Department department;

}
```




```

    QueryConstraints qc = QueryFactory.create();

    qc.join("");

```