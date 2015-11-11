## FishEar's sample applications ##

There are several sample applications that show using of fishear's parts. The application's projects are wrapped up to one maven parent project "`fishear-samples`" (this project is a parent of all samples).
  * <b><a href='SearchGridEditSample.md'>search-grid-edit-sample</a></b> is simple application consists of four main pages, which use two entities (business objects). Two pages are editable forms. This application shows using FishEar's components and how to set up your environment. It is based on Tapestry demo app.
  * <b><a href='LoginSample.md'>login-sample</a></b> is very simple - single page - application demonstrates using of access control component and login (in independent page and as javascript form in layout component)
What the sample applications do is described on its pages.

### How to get the sample projects ###

Source codes of the sample projects you can get by any of those manners:
  * download sample projects sources as [ZIP file](http://fishear.googlecode.com/files/fishear-samples.zip) from downloads page
  * or clone mercurial repository like this:
```
hg clone https://code.google.com/p/fishear.samples/ fishear-samples
```

### Building and running samples ###

<div>To <b>build sample projects</b>, you have to have <a href='http://maven.apache.org/download.html'>Maven</a> installed in your environment (at least version 3, the good choice is version 3.0.3 or above). <font size='1'>You can use your favorite IDE of course, but describing it is out of scope of this document.</font></div>

We assume you have some directory where you place your source codes. Let us call it <i><code>fe_samples</code></i>**. In this directory all samples will be placed. Furthemore, we assume we will build and run the 'login-sample' sample application.**

  * **navigate to** the source's directory '<i><b><code>fe_samples</code></b></i>'
  * <div><b>get project's sources</b> by any of method above</div><font size='1'>from mercurial repository: type command <b><code>hg clone https://code.google.com/p/fishear.samples/ fishear-samples</code></b>, or download <a href='http://fishear.googlecode.com/files/fishear-samples.zip'>ZIP file</a> and <b>extract it to the '</b><i><code>fe_samples</code></i>'<b>directory, so the '<code>fishear-samples</code>' subdirectory apears inside the '</b><i><code>fe_samples</code></i>' directory</font>
  * **build sample applications** (all together)
    * navigate to sample's rot directory: **'`cd fe_samples/fishear-samples`'**
    * <div>type <b><code>mvn clean install</code></b> to build all sample application</div><font size='1'>If you have started maven command first time, it take some time (maybe couple of minutes). Please be patient.</font>
  * **run sample application**
    * navigate to application's subdirectory (**'`fe_samples/fishear-samples/login-sample`'**)
    * <div>type <b><code>mvn jetty:run </code></b> to build and start application</div><font size='1'>First start of this maven command will take some time again, due maven will download all necessary libraries to run</font>
    * if everything is OK, you should have web server started on port 8080
  * **test application is running correctly**
    * open web browser, navigate to page "http://localhost:8080/login-sample/", you should see the sampe application's main page.
