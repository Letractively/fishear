## The Tapestry5 Access Control Module ##
this module allows you to contol access to parts of your application in very simple way. After adding module to your project, components in namespace "feac" will be added:

  * <b><a href='feac_requireLogin.md'>feac.requireLogin</a></b>: (class net.fishear.web.rights.t5.components.RequireLogin) controls access to parts of page
  * <b><a href='feac_loginFormDialog.md'>feac.loginFormDialog</a></b> (class net.fishear.web.rights.t5.components.LoginForm) prezents simple login dialog with username and password
  * <b><a href='feac_accountInfo.md'>feac.accountInfo</a></b> (class net.fishear.web.rights.t5.components.AccountInfo) keeps informations about user currently logged in (firstname, lastname)

### To be able to use access control ###
you have to follow next steps:
  * add [dependency](MavenArtefacts.md) to the `fishear-t5-rights` module to your POM
  * implement LoginLogoutService (see bellow) to verify user's account (or use any existing implementation).
  * <div><b>register</b> your implementation <b>of LoginLogoutService service</b> (or any other of your choice) <b>in your AppModule</b></div>The service has to be registered (at least some dummy version), even it is not used.
  * in page templates, at desired places, enclose controlled block into access control component like this:

```
<t:feac.requireLogin roleCode="${RoleCodes}">
	... block with controlled access ...
</t:feac.requireLogin>
```
${RoleCodes} in this snippet is string with comma separated, case insensitive list of roles having accs to cobtrolled block. For example `<t:feac.requireLogin roleCode="admin,sysuser">...`
### Using other module' components ###
As described above, the module brings some other components: `feac.loginFormDialog` and `feac.accountInfo`.
It is useful to add those componens to your layout (to "layout" component common for all pages) like this:

In your **Layout.java**
```
package your_package.components.layout;

import net.fishear.web.rights.t5.components.LoginForm;
import net.fishear.web.rights.t5.components.AccountInfo;
... your imports ...

public class Layout extends AbstractComponent {

    @Component
    private LoginForm loginForm;

    @Component
    private AccountInfo accountInfo;
    
    ... your code ...
}
```
And in your **Layout.tml** file
```
...
<body>
    ...
    <div id="loginFormDialog" title="Login form" style="display:none">
        <t:component t:id="loginForm" />
    </div>

    <!-- The Rught Place For Informations About User Logged in-->
    <div>
        <t:component t:id="accountInfo" onclickAction="literal:showLogin()"/>
    </div>
    ...
</body>
```

### Implementing the LoginLogoutService ###
To make access control functional, you have to implement [LoginLogoutService](http://code.google.com/p/fishear/source/browse/fishear-t5-rights/src/main/java/net/fishear/web/rights/services/LoginLogoutService.java), or use any existing implementation.

The simplest way how to implement the service is copy our [DummyLoginLogoutService](http://code.google.com/p/fishear/source/browse/fishear-t5-rights/src/main/java/net/fishear/web/rights/services/impl/DummyLoginLogoutService.java) and modify **doLogin** method. Alternatively you can implement **doLogout** if you require to be informed about users logout.