## External libraries references ##

TODO

Some project modules depend on thrid-party modules. Those modules are not referenced firmly from our project <font size='1'>(<a href='WhyExternalRefs.md'>why?</a>)</font>, you can add those sectins to your POM to solve those dependencies:


TODO: add T5 and Hibernate references
<font size='1'>
<pre><code>        &lt;!-- Hibernate core. --&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;org.hibernate&lt;/groupId&gt;<br>
            &lt;artifactId&gt;hibernate-core&lt;/artifactId&gt;<br>
            &lt;version&gt;3.6.0.Final&lt;/version&gt;<br>
            &lt;exclusions&gt;<br>
            		    &lt;!-- prevention from SLFJ version conflict --&gt;<br>
                &lt;exclusion&gt;<br>
                    &lt;groupId&gt;org.slf4j&lt;/groupId&gt;<br>
                    &lt;artifactId&gt;slf4j-api&lt;/artifactId&gt;<br>
                &lt;/exclusion&gt;<br>
            &lt;/exclusions&gt;<br>
        &lt;/dependency&gt;<br>
<br>
        &lt;!-- optional cache --&gt;<br>
        &lt;dependency&gt;<br>
            &lt;groupId&gt;org.hibernate&lt;/groupId&gt;<br>
            &lt;artifactId&gt;hibernate-c3p0&lt;/artifactId&gt;<br>
            &lt;version&gt;3.6.0.Final&lt;/version&gt;<br>
        &lt;/dependency&gt;<br>
</code></pre>
</font>

Tapestry 5 (TODO: add repo)
```
				<dependency>
				<groupId>org.apache.tapestry</groupId>
				<artifactId>tapestry-hibernate</artifactId>
				<version>5.2.6</version>
				</dependency>
```
