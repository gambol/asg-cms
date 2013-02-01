
<nav id="menu">
	<ul class="sf-menu">
		<li><a href="/">首页</a></li>
		<#if channelsDisplay??>
          <#list channelsDisplay as channel>
         	 	<li><a href="channel?id=${channel.id}">${channel.name}</a></li>
          </#list>
        </#if>
	</ul>
</nav>



