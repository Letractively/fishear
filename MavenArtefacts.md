## FishEar's Maven Artefacts ##
<div>Fishear release (currently 1.0.2) is available in Maven central repo. For development version, you need to add <a href='MavenRepositories.md'>FishEar's Snapshot Repository</a> to your project.<br>
<font size='1'></font>
<hr />
<b>fishear-data</b>: <a href='modules_Data.md'>Common Data Module</a>
<font size='1'>
<pre><code>&lt;dependency&gt;<br>
    &lt;groupId&gt;net.fishear&lt;/groupId&gt;<br>
    &lt;artifactId&gt;fishear-data&lt;/artifactId&gt;<br>
    &lt;version&gt;1.0.2&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
</code></pre>
</font>
<hr />
<b>fishear-data-hibernate</b>: <a href='modules_Hibernate.md'>Hibernate Dao implementation</a>
<font size='1'>
<pre><code>&lt;dependency&gt;<br>
    &lt;groupId&gt;net.fishear&lt;/groupId&gt;<br>
    &lt;artifactId&gt;fishear-data-t5hibernate&lt;/artifactId&gt;<br>
    &lt;version&gt;1.0.2&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
</code></pre>
</font>
<hr />
<b>fishear-t5</b>: <a href='modules_T5.md'>Tapestry5 Abstract Component Module</a>
<font size='1'>
<pre><code>&lt;dependency&gt;<br>
    &lt;groupId&gt;net.fishear&lt;/groupId&gt;<br>
    &lt;artifactId&gt;fishear-t5-rights&lt;/artifactId&gt;<br>
    &lt;version&gt;1.0.2&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
</code></pre>
</font>
<hr />
<b>fishear-t5-rights</b>: <a href='modules_T5Rights.md'>Tapestry5 Access Control Module</a>
<font size='1'>
<pre><code>&lt;dependency&gt;<br>
    &lt;groupId&gt;net.fishear&lt;/groupId&gt;<br>
    &lt;artifactId&gt;fishear-t5-rights&lt;/artifactId&gt;<br>
    &lt;version&gt;1.0.2&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
</code></pre>
</font>
<hr />
<b>fishear-data-t5hibernate</b>: <a href='modules_T5Hibername.md'>Hibernate Dao integrated with Tapestry5</a>
<font size='1'>
<pre><code>&lt;dependency&gt;<br>
    &lt;groupId&gt;net.fishear&lt;/groupId&gt;<br>
    &lt;artifactId&gt;fishear-data-t5hibernate&lt;/artifactId&gt;<br>
    &lt;version&gt;1.0.2&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
</code></pre>
</font>
<hr />
<b>fishear-data-audit</b>: <a href='modules_Audit#data.md'>Data Audit (underlying functions)</a> - database operation auditting subsystem (independent to concrete UI)<br>
<font size='1'>
<pre><code>&lt;dependency&gt;<br>
    &lt;groupId&gt;net.fishear&lt;/groupId&gt;<br>
    &lt;artifactId&gt;fishear-data-audit&lt;/artifactId&gt;<br>
    &lt;version&gt;1.0.2&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
</code></pre>
</font>
<hr />
<b>fishear-t5-audit</b>: <a href='modules_Audit#t5.md'>Data Audit (presentation)</a> - database auditting subsystem (screens for view auditing data)<br>
<font size='1'>
<pre><code>&lt;dependency&gt;<br>
    &lt;groupId&gt;net.fishear&lt;/groupId&gt;<br>
    &lt;artifactId&gt;fishear-data-audit&lt;/artifactId&gt;<br>
    &lt;version&gt;1.0.2&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
</code></pre>
</font>
<hr />
<div><b>fishear-core: <a href='modules_Core.md'>Core Module</a></b></div>
<font size='1'>The most common module. All other modules depend on the <b><a href='modules_Core.md'>fishear-core</a></b> module, thus adding any of project modules causes to add this (common) module to your project.</font>
<font size='1'>
<pre><code>&lt;dependency&gt;<br>
    &lt;groupId&gt;net.fishear&lt;/groupId&gt;<br>
    &lt;artifactId&gt;fishear-core&lt;/artifactId&gt;<br>
    &lt;version&gt;1.0.2&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
</code></pre>
</font>