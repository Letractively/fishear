# Entity and Service Usage Example #

Usual way is to create entity, service interface and implementation of service interface.

Also it is neccessary to tell underlying ORM tool (not Hibernate) about the Entity, and optionally register services in case dependency injection is used (for example Spring bens, or Tapestry services). It is also possible to manage service instances "manually" of course.

It is also possible to ommit interface and implement service class directly, especially in case no extra business logic is required (anly basic CRUD operations are needed).

Suppose that "`my.application.package`" is the base package, that is mappd to Tapestry5 as root package (also packages "pages" and "components" are in it).

## Entity ##

```
package my.application.package.entities;

... imports ...

@Entity
public class Person extends AnstractEntity 
{
    private String name;

    private String departmentCode;

    @Column(name="USER_NAME", lenght=64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="DEPARTMENT_CODE", lenght=16)
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
}
```



## Service Interface ##

It is places in "services" package

```
package my.application.package.services;

... imports ...

public interface PersonService extends ServiceI<Person> 
{
    List<Person> listByDepartments(String dptCode);
}

```

## Service Implementation ##

Usually places in "services.impl" package

```
package my.application.package.services.impl;

... imports ...

public class PersonServiceImpl implements PersonService 
{
    public List<Person> listByDepartments(String dptCode) {
        return super.list(QueryFactory.equals("departmentCode", dptCode));
    }
}
```

## Registrer service to the Tapestry ##

In case Tapestry 5 application, you can register service to be injected using `javax.inject.Inject`

```
package my.application.package.services;

... imports ...

public class AppModule
{
    public static void bind(ServiceBinder binder) {

        binder.bind(UserService.class, UserServiceImpl.class);
    }

... Another AppModule's parts ...

}
```

In case another framework (Spring, EJB, ...) you need to use proper method how to register services (for example to define required services as beans in ApplicationConext.xml file for spring, or use Spring annotations to auto register beans, ... etc).