### The "login-sample" sample application ###

<div>After you have built and start the application (how it is described on <a href='SampleProjects.md'>sample projects page</a> - the application subdirectory is</div>
**`fishear-samples/login-sample`**), it listen on URL http://localhost:8080/login-sample/

#### Sample page ####
The application consists of only one page, which contains two blocks. User with role "admin" has access to one block, user with any of roles "user" or "admin" has access to second one.

For login, dummy login service is implemented which verifies any combination of username and password as successfully logged in. It sets roles as follow:

  * for username "admin", sets role "admin" and "user"
  * for any username containing word "user" anywhere it sets role "user" (for example "myUser", "user1", "sampleuser2" etc...)
  * in any other case sets role "public"

#### Login variants ####
There are three variants of login in this app:
  1. login inside layout (using javascript - the "jquery" library)
  1. login in dedicated page wit application's layout
  1. very simple login page from within fishear's module

The 1st possibility (javascript) needs to have javascript libraries "jquery" and "jquery-ui" included in you application and to layout
It is possible to show login dialog automatically whenever it is need and required (but this is not in this sample now).

The 3th option is really very simple and almost empty, it is usable onlůy for quick tests.