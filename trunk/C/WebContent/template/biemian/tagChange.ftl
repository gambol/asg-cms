<div class="content-box">
<header>
	<h3>最新发布</h3>
</header>
<section>
  	<#if latestArticles?? >
  		<dl>
		   <#list latestArticles as a>
	           <dt>
	           	${a.releaseDate!""} <a href="Article/${a.id?c}.html" 	title="${a.title}" target="_blank">${a.title!""}</a>  
	           </dt>
	           
           </#list>
        </dl> 
     </#if>	
</section>
</div>

<div class="content-box">
<header>
	<h3>最多浏览</h3>
</header>
<section>
<#if mostVisitArticles?? >
  	<dl>
		 <#list mostVisitArticles as a>
	           <dt>
	           	［${a.channel.name!""}］<a href="Article/${a.id?c}.html" 	title="${a.title}" target="_blank">${a.title!""}</a>
	           </dt>
           </#list>
     </dl>
     </#if> 
</section>
</div>
